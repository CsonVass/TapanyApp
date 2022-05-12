import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.tapanyApp.R
import hu.bme.aut.android.tapanyApp.data.food.FoodItem
import hu.bme.aut.android.tapanyApp.databinding.ItemFoodsBinding
import hu.bme.aut.android.tapanyApp.databinding.LogitemFoodsBinding

class LogFoodAdapter(private val context: Context) :
    RecyclerView.Adapter<LogFoodAdapter.FoodViewHolder>() {

    private val items = mutableListOf<FoodItem>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = FoodViewHolder(
        LogitemFoodsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val foodItem = items[position]


        holder.binding.tvName.text = context.getString(R.string.tvName, foodItem.name)
        holder.binding.tvAmount.text = context.getString(R.string.tvAmount, foodItem.amount)
        holder.binding.tvCalory.text = context.getString(R.string.tvCalorie, foodItem.calorie)
        holder.binding.tvProtein.text = context.getString(R.string.tvProtein, foodItem.protein)
        holder.binding.tvCarb.text = context.getString(R.string.tvCarbs, foodItem.carbs)
        holder.binding.tvFat.text = context.getString(R.string.tvFat, foodItem.fat)



    }


    override fun getItemCount(): Int = items.size

    inner class FoodViewHolder(val binding: LogitemFoodsBinding) : RecyclerView.ViewHolder(binding.root)


    fun update(foodItems: List<FoodItem>) {
        items.clear()
        items.addAll(foodItems)
        notifyDataSetChanged()
    }


    fun getSumOfNutr() : List<Double>{
        var sumCal = 0.0
        var sumProt = 0.0
        var sumCarb = 0.0
        var sumFat = 0.0
        for (f in items){
            sumCal += f.calorie
            sumProt += f.protein
            sumCarb += f.carbs
            sumFat += f.fat
        }
        return listOf( sumCal, sumProt, sumCarb, sumFat )
    }


}


