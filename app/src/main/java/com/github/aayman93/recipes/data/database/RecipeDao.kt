package com.github.aayman93.recipes.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.github.aayman93.recipes.data.models.Meal

@Dao
interface RecipeDao {

    @Query("SELECT * FROM meals ORDER BY name")
    fun getAllMeals(): LiveData<List<Meal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMeal(meal: Meal)

    @Delete
    suspend fun deleteMeal(meal: Meal)
}