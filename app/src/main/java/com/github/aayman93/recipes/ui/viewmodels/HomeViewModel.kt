package com.github.aayman93.recipes.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.aayman93.recipes.data.models.CategoriesResponse
import com.github.aayman93.recipes.data.models.MealsResponse
import com.github.aayman93.recipes.repositories.MainRepository
import com.github.aayman93.recipes.util.Event
import com.github.aayman93.recipes.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _categoriesResponse = MutableLiveData<Event<Resource<CategoriesResponse>>>()
    val categoriesResponse: LiveData<Event<Resource<CategoriesResponse>>> = _categoriesResponse

    private val _mealsResponse = MutableLiveData<Event<Resource<MealsResponse>>>()
    val mealsResponse: LiveData<Event<Resource<MealsResponse>>> = _mealsResponse

    init {
        getAllCategories()
    }

    private fun getAllCategories() {
        _categoriesResponse.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main) {
            val result = repository.getAllCategories()
            _categoriesResponse.postValue(Event(result))
        }
    }

    fun getMealsByCategory(categoryName: String) {
        _mealsResponse.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main) {
            val result = repository.getMealsByCategory(categoryName)
            _mealsResponse.postValue(Event(result))
        }
    }
}