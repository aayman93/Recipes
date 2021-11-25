package com.github.aayman93.recipes.data.models

import com.google.gson.annotations.SerializedName

data class CategoriesResponse(
    @SerializedName("categories")
    val categories: List<Category>
)