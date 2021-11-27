package com.github.aayman93.recipes.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.github.aayman93.recipes.data.models.Meal
import com.github.aayman93.recipes.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    val meals: LiveData<List<Meal>> = repository.getAllFavouriteMeals()

}