package hu.bme.aut.android.tapanyApp.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.tapanyApp.R
import hu.bme.aut.android.tapanyApp.data.food.FoodItem
import hu.bme.aut.android.tapanyApp.databinding.ItemFoodsBinding

class FoodAdapter(private val listener: FoodClickListener, private val context: Context) :
    RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    private val items = mutableListOf<FoodItem>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = FoodViewHolder(
        ItemFoodsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val foodItem = items[position]


        holder.binding.tvName.text = context.getString(R.string.tvName, foodItem.name)
        holder.binding.tvAmount.text = context.getString(R.string.tvAmount, foodItem.amount)
        holder.binding.tvCalory.text = context.getString(R.string.tvCalorie, foodItem.calorie)
        holder.binding.tvProtein.text = context.getString(R.string.tvProtein, foodItem.protein)
        holder.binding.tvCarb.text = context.getString(R.string.tvCarbs, foodItem.carbs)
        holder.binding.tvFat.text = context.getString(R.string.tvFat, foodItem.fat)

        holder.binding.ibRemove.setOnClickListener {
            listener.onItemRemoved(foodItem)
        }


    }


    override fun getItemCount(): Int = items.size

    interface FoodClickListener {
        fun onItemChanged(item: FoodItem)
        fun onItemRemoved(item: FoodItem)
    }

    inner class FoodViewHolder(val binding: ItemFoodsBinding) : RecyclerView.ViewHolder(binding.root)

    fun addItem(item: FoodItem) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun update(foodItems: List<FoodItem>) {
        items.clear()
        items.addAll(foodItems)
        notifyDataSetChanged()
    }

    fun removeItem(item: FoodItem){
        val idx : Int = items.indexOf(item)
        items.remove(item)
        notifyItemRemoved(idx)
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


