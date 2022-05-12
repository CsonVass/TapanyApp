package hu.bme.aut.android.tapanyApp.fragments

import hu.bme.aut.android.tapanyApp.R
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import hu.bme.aut.android.tapanyApp.Model.Food
import hu.bme.aut.android.tapanyApp.Model.Reply
import hu.bme.aut.android.tapanyApp.data.food.FoodItem
import hu.bme.aut.android.tapanyApp.data.user.User
import hu.bme.aut.android.tapanyApp.databinding.DialogNewFoodItemBinding
import hu.bme.aut.android.tapanyApp.databinding.RegistrationBinding
import hu.bme.aut.android.tapanyApp.network.NetworkManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class RegistrationDialogFragment : DialogFragment() {

    interface RegistrationDialogListener {
        fun onUserCreated(newUser: User)
    }

    private lateinit var listener: RegistrationDialogFragment.RegistrationDialogListener

    private lateinit var binding: RegistrationBinding

    private lateinit var context_ : Context

    companion object {
        const val TAG = "Registration Fragment"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? RegistrationDialogListener
            ?: throw RuntimeException("Activity must implement the NewFoodDialogListener interface!")
        context_ = this.requireContext()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = RegistrationBinding.inflate(LayoutInflater.from(context))


        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.btn_registration)
            .setView(binding.root)
            .setNegativeButton(R.string.button_cancel, null)
            .setPositiveButton(R.string.button_ok) { dialogInterface, i ->
                if(isValid()){
                    listener.onUserCreated(getUser())
                }else{
                    Toast.makeText(requireContext(), getString(R.string.invalid_registration), Toast.LENGTH_SHORT).show()
                }
            }
            .create()


    }

    private fun getUser() = User(
        name = binding.etUserName.text.toString(),
        password = binding.etPassword.text.toString(),
        calorie_goal = if(!binding.etAmount.text.isEmpty()) binding.etAmount.text.toString().toInt() else 0
    )

    fun isValid() : Boolean{
        return !(binding.etUserName.text.toString().isEmpty() || binding.etPassword.text.toString().isEmpty())
    }


}
