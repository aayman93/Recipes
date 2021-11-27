package com.github.aayman93.recipes.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.aayman93.recipes.databinding.FragmentFavouritesBinding
import com.github.aayman93.recipes.ui.adapters.MealAdapter
import com.github.aayman93.recipes.ui.viewmodels.FavouritesViewModel
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