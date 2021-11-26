package com.github.aayman93.recipes.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.aayman93.recipes.databinding.FragmentHomeBinding
import com.github.aayman93.recipes.ui.adapters.CategoryAdapter
import com.github.aayman93.recipes.ui.adapters.MealAdapter
import com.github.aayman93.recipes.ui.viewmodels.HomeViewModel
import com.github.aayman93.recipes.util.EventObserver
import com.github.aayman93.recipes.util.snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    @Inject lateinit var categoryAdapter: CategoryAdapter
    @Inject lateinit var mealAdapter: MealAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclers()
        subscribeToObservables()
    }

    private fun setupRecyclers() {
        binding.categoriesRecycler.adapter = categoryAdapter
        categoryAdapter.setOnCategoryClickListener { category ->
            viewModel.getMealsByCategory(category.name)
        }

        binding.mealsRecycler.adapter = mealAdapter
    }

    private fun subscribeToObservables() {
        viewModel.categoriesResponse.observe(viewLifecycleOwner, EventObserver(
            onError = {
                binding.mainProgressBar.isVisible = false
                binding.mainLayout.isVisible = false
                snackbar(it)
            },
            onLoading = {
                binding.mainProgressBar.isVisible = true
                binding.mainLayout.isVisible = false
            }
        ) { categoriesResponse ->
            binding.mainProgressBar.isVisible = false
            binding.mainLayout.isVisible = true
            val categories = categoriesResponse.categories
            if (!categories.isNullOrEmpty()) {
                categoryAdapter.submitList(categories)
                viewModel.getMealsByCategory(categories[0].name)
            }
        })

        viewModel.mealsResponse.observe(viewLifecycleOwner, EventObserver(
            onError = {
                binding.mealsProgressBar.isVisible = false
                binding.mealsRecycler.isVisible = false
                snackbar(it)
            },
            onLoading = {
                binding.mealsProgressBar.isVisible = true
                binding.mealsRecycler.isVisible = false
            }
        ) { mealsResponse ->
            binding.mealsProgressBar.isVisible = false
            binding.mealsRecycler.isVisible = true
            val meals = mealsResponse.meals
            if (!meals.isNullOrEmpty()) {
                mealAdapter.submitList(meals)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}