package com.github.aayman93.recipes.data.models

import com.google.gson.annotations.SerializedName

data class Ingredient(
    @SerializedName("idIngredient")
    val id: String,
    @SerializedName("strIngredient")
    val name: String,
    @SerializedName("strDescription")
    val description: String?,
    @SerializedName("strType")
    val type: String?
)