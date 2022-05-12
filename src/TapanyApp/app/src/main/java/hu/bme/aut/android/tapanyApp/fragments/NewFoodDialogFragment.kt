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
import hu.bme.aut.android.tapanyApp.databinding.DialogNewFoodItemBinding
import hu.bme.aut.android.tapanyApp.network.NetworkManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class NewFoodDialogFragment : DialogFragment() {

    interface NewShoppingItemDialogListener {
        fun onFoodItemCreated(newItem: FoodItem)
    }

    private lateinit var listener: NewShoppingItemDialogListener

    private lateinit var binding: DialogNewFoodItemBinding

    private lateinit var context_ : Context

    private lateinit var food: Food

    companion object {
        const val TAG = "NewFoodDialogFragment"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? NewShoppingItemDialogListener
            ?: throw RuntimeException("Activity must implement the NewFoodDialogListener interface!")
        context_ = this.requireContext()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogNewFoodItemBinding.inflate(LayoutInflater.from(context))


        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.new_food)
            .setView(binding.root)
            .setNegativeButton(R.string.button_cancel, null)
            .setPositiveButton(R.string.button_ok) { dialogInterface, i ->
                if (isValid()) {
                    loadFood(binding.etName.text.toString())
                }
            }
            .create()


    }



    private fun isValid() = binding.etName.text.isNotEmpty()

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getFoodItem() = FoodItem(
        name = food.label,
        calorie = (food.nutrients.ENERC_KCAL * binding.etAmount.text.toString().toDouble()/100),
        protein = (food.nutrients.PROCNT * binding.etAmount.text.toString().toDouble()/100),
        carbs = (food.nutrients.CHOCDF * binding.etAmount.text.toString().toDouble()/100),
        fat = (food.nutrients.FAT * binding.etAmount.text.toString().toDouble()/100),
        amount = binding.etAmount.text.toString().toDouble(),
        date = SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()).toString(),
        other = food.categoryLabel
    )

    private fun loadFood(foodName : String) {
        NetworkManager.getFood(foodName)?.enqueue(object : Callback<Reply?> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(
                call: Call<Reply?>,
                response: Response<Reply?>
            ) {
                Log.d(TAG, "onResponse: " + response.code())
                if (response.isSuccessful) {
                    try {
                        displayFoodData(response.body())
                    }
                    catch (e : Exception){
                        Toast.makeText(context_, e.message, Toast.LENGTH_LONG).show()
                    }

                } else {
                    Toast.makeText(context_, context_.getString(R.string.error) + response.message(), Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(
                call: Call<Reply?>,
                throwable: Throwable
            ) {
                throwable.printStackTrace()
                Toast.makeText(context_, context_.getString(R.string.error_network), Toast.LENGTH_LONG).show()
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun displayFoodData(recievedFoodData: Reply?) {
        if(recievedFoodData?.parsed == null)
            throw Exception(context_.getString(R.string.food_not_found))
        if(recievedFoodData.parsed.isEmpty())
            throw Exception(context_.getString(R.string.food_not_found))
         food = recievedFoodData.parsed[0].food
        listener.onFoodItemCreated(getFoodItem())

    }


}
