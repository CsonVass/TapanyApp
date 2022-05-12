package hu.bme.aut.android.tapanyApp.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import hu.bme.aut.android.tapanyApp.R
import hu.bme.aut.android.tapanyApp.data.TapanyAppDatabase
import hu.bme.aut.android.tapanyApp.data.food.FoodItem
import hu.bme.aut.android.tapanyApp.data.user.User
import hu.bme.aut.android.tapanyApp.databinding.ActivityLoginBinding
import hu.bme.aut.android.tapanyApp.fragments.NewFoodDialogFragment
import hu.bme.aut.android.tapanyApp.fragments.RegistrationDialogFragment
import java.time.temporal.ValueRange
import kotlin.concurrent.thread

class LoginActivity : AppCompatActivity(), RegistrationDialogFragment.RegistrationDialogListener{
    private lateinit var binding: ActivityLoginBinding

    private lateinit var database: TapanyAppDatabase
    private lateinit var users: ArrayList<User>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(1000)
        setTheme(R.style.Theme_FoodList)

        if(checkIfLoggedIn())
            switchToCalculator()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        database = TapanyAppDatabase.getDatabase(applicationContext)

        loadUsersInBackground()

        binding.btnLogin.setOnClickListener {
            when {
                binding.etUserName.text.toString().isEmpty() -> {
                    binding.etUserName.requestFocus()
                    binding.etUserName.error = getString(R.string.error_username)
                }
                binding.etPassword.text.toString().isEmpty() -> {
                    binding.etPassword.requestFocus()
                    binding.etPassword.error = getString(R.string.error_password)
                }
                else -> {
                    val userFromDb = validate(User(
                        name = binding.etUserName.text.toString(),
                        password = binding.etPassword.text.toString(),
                        calorie_goal = 0
                    ))
                    if(userFromDb != null){

                        val sharedPref = getSharedPreferences(getString(R.string.shp), Context.MODE_PRIVATE)
                        with (sharedPref.edit()){
                            putString(getString(R.string.loggedInUser), userFromDb.id.toString())
                            apply()
                        }
                        switchToCalculator()
                    }else{
                        Toast.makeText(this, getString(R.string.invalid_credentials), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.btnRegistration.setOnClickListener {
            RegistrationDialogFragment().show(
                supportFragmentManager,
                RegistrationDialogFragment.TAG
            )
        }


    }

    private fun checkIfLoggedIn(): Boolean{
        val sharedPref = getSharedPreferences(getString(R.string.shp), Context.MODE_PRIVATE)
        return !sharedPref.getString(getString(R.string.loggedInUser), null).isNullOrEmpty()
    }

    private fun switchToCalculator(){
        val calculatorIntent = Intent(this, CalculatorActivity::class.java)
        calculatorIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(calculatorIntent)
    }

    override fun onUserCreated(newUser: User) {
        val userRecord = users.firstOrNull {it.name == newUser.name}
        if(userRecord != null){
            Toast.makeText(this, getString(R.string.username_taken), Toast.LENGTH_SHORT).show()
            return
        }
        addNewUser(newUser)

    }

    private fun loadUsersInBackground() {
        thread {
            val usersLoaded = database.userDao().getAll()
            runOnUiThread {
                users = ArrayList(usersLoaded)
            }
        }
    }

    private fun addNewUser(newUser:User){
        thread {
            database.userDao().insert(newUser)
            runOnUiThread{
                Toast.makeText(this, getString(R.string.registration_success), Toast.LENGTH_SHORT).show()
                loadUsersInBackground()
            }
        }
    }

    private fun validate(user: User): User?{
        return users.firstOrNull() {it.name == user.name}
    }

}
