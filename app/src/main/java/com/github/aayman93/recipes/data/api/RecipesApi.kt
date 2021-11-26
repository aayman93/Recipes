package com.github.aayman93.recipes.data.api

import com.github.aayman93.recipes.data.models.CategoriesResponse
import com.github.aayman93.recipes.data.models.IngredientsResponse
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

    @GET("list.php")
    suspend fun getIngredientList(
        @Query("i") query: String = "list"
    ): Response<IngredientsResponse>

    @GET("search.php")
    suspend fun findRecipeByName(
        @Query("s") recipeName: String
    ): Response<MealsResponse>

    @GET("filter.php")
    suspend fun findRecipeByCategory(
        @Query("c") category: String
    ): Response<MealsResponse>

    @GET("filter.php")
    suspend fun findRecipeByCountry(
        @Query("a") country: String
    ): Response<MealsResponse>

    @GET("filter.php")
    suspend fun findRecipeByMainIngredient(
        @Query("i") ingredient: String
    ): Response<MealsResponse>
}