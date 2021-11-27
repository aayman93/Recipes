package com.github.aayman93.recipes.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.aayman93.recipes.data.models.Meal

@Database(entities = [Meal::class], version = 1, exportSchema = false)
abstract class RecipeDatabase : RoomDatabase() {

    abstract fun getRecipeDao(): RecipeDao
}