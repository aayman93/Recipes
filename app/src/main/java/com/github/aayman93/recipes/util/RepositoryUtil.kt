package com.github.aayman93.recipes.util

inline fun <T> safeCall(action: () -> Resource<T>): Resource<T> {
    return try {
        action()
    } catch (e: Exception) {
        Resource.Error(e.localizedMessage ?: "An unknown error occurred")
    }
}