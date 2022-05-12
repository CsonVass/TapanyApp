package hu.bme.aut.android.tapanyApp.activities

import LogFoodAdapter
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.SubMenu
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.android.tapanyApp.R
import hu.bme.aut.android.tapanyApp.data.TapanyAppDatabase
import hu.bme.aut.android.tapanyApp.databinding.ActivityFoodLogBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread

class FoodLogActivity : AppCompatActivity(){
    private lateinit var binding: ActivityFoodLogBinding

    private lateinit var database: TapanyAppDatabase
    private lateinit var adapter: LogFoodAdapter

    private lateinit var date: String

    private var dateList: ArrayList<String> = ArrayList()

    private lateinit var userId: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userId = getSharedPreferences(getString(R.string.shp), Context.MODE_PRIVATE).getString(getString(R.string.loggedInUser), null) ?: ""

        binding = ActivityFoodLogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        database = TapanyAppDatabase.getDatabase(applicationContext)

        date = SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()).toString()
        loadDatesInBackground()

        initRecyclerView()
        setNutrientSummary()

        binding.toolbar.title = date
    }

    private fun initRecyclerView() {
        adapter = LogFoodAdapter(this)
        binding.rvLog.layoutManager = LinearLayoutManager(this)
        binding.rvLog.adapter = adapter
        loadItemsInBackground()
    }

    private fun loadItemsInBackground() {
        thread {
            val items = database.foodItemDao().getItemsByDateAndUser(date, userId.toInt())
            runOnUiThread {
                adapter.update(items)
                setNutrientSummary()
            }
        }
    }

    private fun loadDatesInBackground() {
        thread {
            val datesLoaded = database.foodItemDao().getDatesByUserId(userId.toInt())
            runOnUiThread {
                dateList = ArrayList(datesLoaded)
                if(!dateList.contains(date)){
                    dateList.add(date)
                }
            }
        }

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val toolbarMenu: Menu = binding.toolbar.menu
        menuInflater.inflate(R.menu.log_menu_toolbar, toolbarMenu)
        for (i in 0 until toolbarMenu.size()) {
            val menuItem: MenuItem = toolbarMenu.getItem(i)
            if(dateList.count() == 1){
                menuItem.setEnabled(false)
            }
            if(i == 1){
                menuItem.setEnabled(false)
            }
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
            R.id.menu_left -> {
                date = dateList.get(dateList.indexOf(date)-1)
                if(dateList.indexOf(date) == 0){
                    item.setEnabled(false)
                }
                binding.toolbar.menu.getItem(1).setEnabled(true)
                loadItemsInBackground()
                binding.toolbar.title = date
                true
            }
            R.id.menu_right -> {
                date = dateList.get(dateList.indexOf(date) + 1)
                if(dateList.indexOf(date) == dateList.count() - 1){
                    item.setEnabled(false)
                }
                binding.toolbar.menu.getItem(0).setEnabled(true)
                loadItemsInBackground()
                binding.toolbar.title = date
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
    }

}
