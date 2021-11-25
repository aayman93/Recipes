package com.github.aayman93.recipes.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.github.aayman93.recipes.data.models.Meal
import com.github.aayman93.recipes.databinding.ItemMealBinding
import com.github.aayman93.recipes.util.Constants.THUMBNAIL_POSTFIX
import javax.inject.Inject

class MealAdapter @Inject constructor() : ListAdapter<Meal, MealAdapter.MealViewHolder>(Differ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val binding = ItemMealBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MealViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    companion object Differ : DiffUtil.ItemCallback<Meal>() {
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem == newItem
        }
    }

    inner class MealViewHolder(
        private val binding: ItemMealBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(meal: Meal) {
            with(binding) {
                ivMealImage.load("${meal.thumbnail}$THUMBNAIL_POSTFIX")
                tvMealName.text = meal.name
            }
        }
    }
}