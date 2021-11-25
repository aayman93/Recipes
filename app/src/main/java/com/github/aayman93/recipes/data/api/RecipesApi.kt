package com.github.aayman93.recipes.data.api

import com.github.aayman93.recipes.data.models.CategoriesResponse
import com.github.aayman93.recipes.data.models.MealsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RecipesApi {

    @GET("categories.php")
    suspend fun getAllCategories(): Response<CategoriesResponse>

    @GET("filter.php")
    suspend fun getMealsByCategory(
        @Query("c") categoryName: String
    ): Response<MealsResponse>
}