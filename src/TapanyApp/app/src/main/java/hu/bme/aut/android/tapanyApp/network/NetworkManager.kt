package hu.bme.aut.android.tapanyApp.network

import android.util.Log
import hu.bme.aut.android.tapanyApp.Model.Reply
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkManager {
    private val retrofit: Retrofit
    private val foodApi: FoodApi

    private const val SERVICE_URL = "https://api.edamam.com"
    private const val APP_ID = "c4821570"
    private const val APP_KEY = "53ad4f46f19a0fd96fdb640faf1ed87c"

    init {
        retrofit = Retrofit.Builder()
            .baseUrl(SERVICE_URL)
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        foodApi = retrofit.create(FoodApi::class.java)
    }

    fun getFood(food: String): Call<Reply?>? {
        return foodApi.getFood(APP_ID, food, "logging", APP_KEY)
    }
}