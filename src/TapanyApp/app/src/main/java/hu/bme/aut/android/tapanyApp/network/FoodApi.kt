package hu.bme.aut.android.tapanyApp.network

import hu.bme.aut.android.tapanyApp.Model.Reply
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FoodApi {
    @GET("/api/food-database/v2/parser")
    fun getFood(
        @Query("app_id") appId: String,
        @Query("ingr") ingr: String,
        @Query("nutrition-type") nutritionType: String,
        @Query("app_key") appKey: String
    ): Call<Reply?>?
}