package com.github.aayman93.recipes.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.github.aayman93.recipes.R
import com.github.aayman93.recipes.databinding.FragmentFavouritesBinding
import com.github.aayman93.recipes.ui.adapters.MealAdapter
import com.github.aayman93.recipes.ui.viewmodels.FavouritesViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FavouritesFragment : Fragment() {

    private var _binding: FragmentFavouritesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavouritesViewModel by viewModels()

    @Inject lateinit var mealAdapter: MealAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
        subscribeToObservables()
    }

    private fun setupRecycler() {
        binding.mealsRecycler.adapter = mealAdapter
        mealAdapter.setOnMealClickListener { meal ->
            val action = FavouritesFragmentDirections.actionGlobalRecipeDetailsFragment(meal.id)
            findNavController().navigate(action)
        }

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val currentMeal = mealAdapter.currentList[position]
                viewModel.deleteMeal(currentMeal)
                Snackbar.make(
                    requireView(),
                    getString(R.string.recipe_removed),
                    Snackbar.LENGTH_LONG
                ).setAction("Undo") {
                    viewModel.addMeal(currentMeal)
                }.show()
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.mealsRecycler)
    }

    private fun subscribeToObservables() {
        viewModel.meals.observe(viewLifecycleOwner) { meals ->
            binding.mealsProgressBar.isVisible = false
            if (meals.isNullOrEmpty()) {
                binding.tvEmptyState.isVisible = true
                binding.mealsRecycler.isVisible = false
            } else {
                binding.tvEmptyState.isVisible = false
                binding.mealsRecycler.isVisible = true
                mealAdapter.submitList(meals)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}