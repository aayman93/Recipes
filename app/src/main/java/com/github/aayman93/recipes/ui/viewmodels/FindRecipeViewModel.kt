package com.github.aayman93.recipes.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.aayman93.recipes.data.models.IngredientsResponse
import com.github.aayman93.recipes.data.models.MealsResponse
import com.github.aayman93.recipes.repositories.MainRepository
import com.github.aayman93.recipes.util.Event
import com.github.aayman93.recipes.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FindRecipeViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _ingredientsResponse = MutableLiveData<Event<Resource<IngredientsResponse>>>()
    val ingredientsResponse: LiveData<Event<Resource<IngredientsResponse>>> = _ingredientsResponse

    private val _mealsResponse = MutableLiveData<Event<Resource<MealsResponse>>>()
    val mealsResponse: LiveData<Event<Resource<MealsResponse>>> = _mealsResponse

    init {
        getIngredientList()
    }

    private fun getIngredientList() {
        _ingredientsResponse.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main) {
            val result = repository.getIngredientList()
            _ingredientsResponse.postValue(Event(result))
        }
    }

    fun findRecipeByName(recipeName: String) {
        if (recipeName.isBlank()) {
            _mealsResponse.postValue(Event(Resource.Error("Please enter recipe name")))
            return
        }
        _mealsResponse.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main) {
            val result = repository.findRecipeByName(recipeName)
            _mealsResponse.postValue(Event(result))
        }
    }

    fun findRecipeByCategory(category: String) {
        _mealsResponse.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main) {
            val result = repository.getMealsByCategory(category)
            _mealsResponse.postValue(Event(result))
        }
    }

    fun findRecipeByArea(area: String) {
        _mealsResponse.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main) {
            val result = repository.findRecipeByArea(area)
            _mealsResponse.postValue(Event(result))
        }
    }

    fun findRecipeByMainIngredient(ingredient: String) {
        _mealsResponse.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main) {
            val result = repository.findRecipeByMainIngredient(ingredient)
            _mealsResponse.postValue(Event(result))
        }
    }
}