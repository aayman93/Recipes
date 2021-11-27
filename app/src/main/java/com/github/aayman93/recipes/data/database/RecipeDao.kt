package com.github.aayman93.recipes.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.aayman93.recipes.data.models.Meal

@Dao
interface RecipeDao {

    @Query("SELECT * FROM meals ORDER BY name")
    fun getAllMeals(): LiveData<List<Meal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMeal(meal: Meal)
}