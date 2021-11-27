package com.github.aayman93.recipes.di

import android.content.Context
import androidx.room.Room
import com.github.aayman93.recipes.data.api.RecipesApi
import com.github.aayman93.recipes.data.database.RecipeDao
import com.github.aayman93.recipes.data.database.RecipeDatabase
import com.github.aayman93.recipes.util.Constants.BASE_URL
import com.github.aayman93.recipes.util.Constants.RECIPE_DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRecipesApi(): RecipesApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(RecipesApi::class.java)

    @Singleton
    @Provides
    fun provideRecipeDatabase(
        @ApplicationContext appContext: Context
    ): RecipeDatabase = Room.databaseBuilder(
        appContext,
        RecipeDatabase::class.java,
        RECIPE_DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideRecipeDao(
        database: RecipeDatabase
    ): RecipeDao = database.getRecipeDao()
}