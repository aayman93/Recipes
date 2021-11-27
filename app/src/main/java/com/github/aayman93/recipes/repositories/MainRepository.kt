package com.github.aayman93.recipes.repositories

import androidx.lifecycle.LiveData
import com.github.aayman93.recipes.data.api.RecipesApi
import com.github.aayman93.recipes.data.database.RecipeDao
import com.github.aayman93.recipes.data.models.CategoriesResponse
import com.github.aayman93.recipes.data.models.IngredientsResponse
import com.github.aayman93.recipes.data.models.Meal
import com.github.aayman93.recipes.data.models.MealsResponse
import com.github.aayman93.recipes.util.Resource
import com.github.aayman93.recipes.util.safeCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val api: RecipesApi,
    private val recipeDao: RecipeDao
) {

    suspend fun getAllCategories(): Resource<CategoriesResponse> {
        return withContext(Dispatchers.IO) {
            safeCall {
                val response = api.getAllCategories()
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Resource.Success(result)
                } else {
                    Resource.Error(response.message())
                }
            }
        }
    }

    suspend fun getMealsByCategory(categoryName: String): Resource<MealsResponse> {
        return withContext(Dispatchers.IO) {
            safeCall {
                val response = api.getMealsByCategory(categoryName)
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Resource.Success(result)
                } else {
                    Resource.Error(response.message())
                }
            }
        }
    }

    suspend fun getIngredientList(): Resource<IngredientsResponse> {
        return withContext(Dispatchers.IO) {
            safeCall {
                val response = api.getIngredientList()
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Resource.Success(result)
                } else {
                    Resource.Error(response.message())
                }
            }
        }
    }

    suspend fun findRecipeByName(recipeName: String): Resource<MealsResponse> {
        return withContext(Dispatchers.IO) {
            safeCall {
                val response = api.findRecipeByName(recipeName)
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Resource.Success(result)
                } else {
                    Resource.Error(response.message())
                }
            }
        }
    }

    suspend fun findRecipeByCategory(category: String): Resource<MealsResponse> {
        return withContext(Dispatchers.IO) {
            safeCall {
                val response = api.findRecipeByCategory(category)
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Resource.Success(result)
                } else {
                    Resource.Error(response.message())
                }
            }
        }
    }

    suspend fun findRecipeByCountry(country: String): Resource<MealsResponse> {
        return withContext(Dispatchers.IO) {
            safeCall {
                val response = api.findRecipeByCountry(country)
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Resource.Success(result)
                } else {
                    Resource.Error(response.message())
                }
            }
        }
    }

    suspend fun findRecipeByMainIngredient(ingredient: String): Resource<MealsResponse> {
        return withContext(Dispatchers.IO) {
            safeCall {
                val response = api.findRecipeByMainIngredient(ingredient)
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Resource.Success(result)
                } else {
                    Resource.Error(response.message())
                }
            }
        }
    }

    fun getAllFavouriteMeals(): LiveData<List<Meal>> = recipeDao.getAllMeals()

    suspend fun addMealToFavourites(meal: Meal) {
        withContext(Dispatchers.IO) {
            recipeDao.addMeal(meal)
        }
    }
}