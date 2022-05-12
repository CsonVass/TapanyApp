package hu.bme.aut.android.tapanyApp.activities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.SubMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.android.tapanyApp.R
import hu.bme.aut.android.tapanyApp.adapter.FoodAdapter
import hu.bme.aut.android.tapanyApp.data.TapanyAppDatabase
import hu.bme.aut.android.tapanyApp.data.food.FoodItem
import hu.bme.aut.android.tapanyApp.databinding.ActivityMainBinding
import hu.bme.aut.android.tapanyApp.fragments.NewFoodDialogFragment
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread


class CalculatorActivity : AppCompatActivity(), FoodAdapter.FoodClickListener,
    NewFoodDialogFragment.NewShoppingItemDialogListener {
    private lateinit var binding: ActivityMainBinding

    private lateinit var database: TapanyAppDatabase
    private lateinit var adapter: FoodAdapter

    private lateinit var today: String

    private lateinit var userId: String
    private var calGoal: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        userId = getSharedPreferences(getString(R.string.shp), Context.MODE_PRIVATE).getString(getString(R.string.loggedInUser), null) ?: ""

        today = SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()).toString()

        database = TapanyAppDatabase.getDatabase(applicationContext)

        binding.fab.setOnClickListener {
            NewFoodDialogFragment().show(
                supportFragmentManager,
                NewFoodDialogFragment.TAG
            )
        }
        initRecyclerView()
        setNutrientSummary()
    }


    private fun initRecyclerView() {
        adapter = FoodAdapter(this, this)
        binding.rvMain.layoutManager = LinearLayoutManager(this)
        binding.rvMain.adapter = adapter
        loadItemsInBackground()
    }

    private fun loadItemsInBackground() {
        thread {
            val items = database.foodItemDao().getItemsByDateAndUser(today, userId.toInt())
            val user = database.userDao().getUser(userId.toLong())
            runOnUiThread {
                binding.toolbar.title = user.name
                calGoal = user.calorie_goal
                adapter.update(items)
                setNutrientSummary()
            }
        }
    }

    override fun onItemChanged(item: FoodItem) {
        thread {
            database.foodItemDao().update(item)
            Log.d("MainActivity", "FoodItem update was successful")
        }
    }

    override fun onItemRemoved(item: FoodItem) {
        thread {
            database.foodItemDao().deleteItem(item)
            Log.d("MainActivity", "FoodItem deletion was successful")

            runOnUiThread {
                adapter.removeItem(item)
                setNutrientSummary()
            }
        }
    }

    override fun onFoodItemCreated(newItem: FoodItem) {
        newItem.userId = userId.toLong()
        thread {
            database.foodItemDao().insert(newItem)

            runOnUiThread {
                adapter.addItem(newItem)
                setNutrientSummary()
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val toolbarMenu: Menu = binding.toolbar.menu
        menuInflater.inflate(R.menu.menu_toolbar, toolbarMenu)
        for (i in 0 until toolbarMenu.size()) {
            val menuItem: MenuItem = toolbarMenu.getItem(i)
            menuItem.setOnMenuItemClickListener { item -> onOptionsItemSelected(item) }
            if (menuItem.hasSubMenu()) {
                val subMenu: SubMenu = menuItem.subMenu
                for (j in 0 until subMenu.size()) {
                    subMenu.getItem(j)
                        .setOnMenuItemClickListener { item -> onOptionsItemSelected(item) }
                }
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_logout -> {

                val sharedPref = getSharedPreferences(getString(R.string.shp), Context.MODE_PRIVATE)
                with (sharedPref.edit()){
                    putString(getString(R.string.loggedInUser), null)
                    apply()
                }

                val loginIntent = Intent(this, LoginActivity::class.java)
                loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(loginIntent)
                true
            }
            R.id.menu_diary -> {
                val diaryIntent = Intent(this, FoodLogActivity::class.java)
                startActivity(diaryIntent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    fun setNutrientSummary(){
        var nutrients = adapter.getSumOfNutr()
        binding.tvSumCalorie.tvCalory.text = getString(R.string.tvSumCal, nutrients[0])
        binding.tvSumCalorie.tvProtein.text = getString(R.string.tvSumProt, nutrients[1])
        binding.tvSumCalorie.tvCarb.text = getString(R.string.tvSumCarbs, nutrients[2])
        binding.tvSumCalorie.tvFat.text = getString(R.string.tvSumFat, nutrients[3])

        createNotification()
    }

    fun createNotification(){
        var builder = NotificationCompat.Builder(this, "0")
            .setSmallIcon(R.drawable.apple)
            .setContentTitle(getString(R.string.calorieNotif))
            .setContentText(binding.tvSumCalorie.tvCalory.text.toString() + " / " + calGoal.toString())
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(binding.tvSumCalorie.tvCalory.text.toString() + " / " + calGoal.toString()))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        createNotificationChannel()

        with(NotificationManagerCompat.from(this)) {
            notify(0, builder.build())
        }

    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("0", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
