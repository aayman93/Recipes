package com.github.aayman93.recipes.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.aayman93.recipes.data.models.Meal
import com.github.aayman93.recipes.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    val meals: LiveData<List<Meal>> = repository.getAllFavouriteMeals()

    fun deleteMeal(meal: Meal) {
        viewModelScope.launch {
            repository.removeMealFromFavourites(meal)
        }
    }

    fun addMeal(meal: Meal) {
        viewModelScope.launch {
            repository.addMealToFavourites(meal)
        }
    }
}