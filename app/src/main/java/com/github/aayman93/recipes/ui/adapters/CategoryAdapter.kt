package com.github.aayman93.recipes.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.github.aayman93.recipes.data.models.Category
import com.github.aayman93.recipes.databinding.ItemCategoryBinding
import javax.inject.Inject

class CategoryAdapter @Inject constructor() :
    ListAdapter<Category, CategoryAdapter.CategoryViewHolder>(Differ) {

    var selectedPosition: Int = 0

    private var onCategoryClickListener: ((Category) -> Unit)? = null

    fun setOnCategoryClickListener(listener: (Category) -> Unit) {
        onCategoryClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    companion object Differ : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }
    }

    inner class CategoryViewHolder(
        private val binding: ItemCategoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(category: Category) {
            with(binding) {
                ivCategoryImage.load(category.thumbnail)
                tvCategoryName.text = category.name
                constraintLayout.isSelected = adapterPosition == selectedPosition

                root.setOnClickListener {
                    notifyItemChanged(selectedPosition)
                    selectedPosition = adapterPosition
                    notifyItemChanged(selectedPosition)
                    onCategoryClickListener?.invoke(category)
                }
            }
        }
    }
}