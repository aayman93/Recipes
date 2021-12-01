package com.github.aayman93.recipes.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.aayman93.recipes.data.models.Meal
import com.github.aayman93.recipes.data.models.MealsResponse
import com.github.aayman93.recipes.repositories.MainRepository
import com.github.aayman93.recipes.util.Event
import com.github.aayman93.recipes.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailsViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _mealsResponse = MutableLiveData<Event<Resource<MealsResponse>>>()
    val mealsResponse: LiveData<Event<Resource<MealsResponse>>> = _mealsResponse

    fun getRecipeById(id: String) {
        val content = _mealsResponse.value?.peekContent()
        if (content is Resource.Success) {
            return
        }
        _mealsResponse.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main) {
            val result = repository.getMealById(id)
            _mealsResponse.postValue(Event(result))
        }
    }

    fun addMealToFavourites(meal: Meal) {
        viewModelScope.launch {
            repository.addMealToFavourites(meal)
        }
    }
}