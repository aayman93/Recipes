package com.github.aayman93.recipes.data.models

import com.google.gson.annotations.SerializedName

data class IngredientsResponse(
    @SerializedName("meals")
    val ingredients: List<Ingredient>
)