package com.github.aayman93.recipes.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.aayman93.recipes.R
import com.github.aayman93.recipes.databinding.FragmentFindRecipeBinding
import com.github.aayman93.recipes.ui.adapters.MealAdapter
import com.github.aayman93.recipes.ui.viewmodels.FindRecipeViewModel
import com.github.aayman93.recipes.util.EventObserver
import com.github.aayman93.recipes.util.snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FindRecipeFragment : Fragment() {

    private var _binding: FragmentFindRecipeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FindRecipeViewModel by viewModels()

    @Inject lateinit var mealAdapter: MealAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFindRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclers()
        subscribeToObservables()
        setListeners()
    }

    private fun setListeners() {
        with(binding) {
            buttonSearch.setOnClickListener { findRecipeByName() }

            chipGroup.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.chip_by_name -> {
                        showAndHideLayouts(isByName = true)
                        buttonSearch.setOnClickListener { findRecipeByName() }
                    }
                    R.id.chip_by_category -> {
                        showAndHideLayouts(isByCategory = true)
                        buttonSearch.setOnClickListener { findRecipeByCategory() }
                    }
                    R.id.chip_by_country -> {
                        showAndHideLayouts(isByCountry = true)
                        buttonSearch.setOnClickListener { findRecipeByCountry() }
                    }
                    R.id.chip_by_ingredient -> {
                        showAndHideLayouts(isByIngredient = true)
                        buttonSearch.setOnClickListener { findRecipeByMainIngredient() }
                    }
                }
            }
        }
    }

    private fun setupRecyclers() {
        binding.mealsRecycler.adapter = mealAdapter
    }

    private fun subscribeToObservables() {
        viewModel.ingredientsResponse.observe(viewLifecycleOwner, EventObserver(
            onError = { snackbar(it) }
        ) { response ->
            val ingredients = response.ingredients
            if (ingredients.isNotEmpty()) {
                val ingredientList = mutableListOf<String>()
                for (ingredient in ingredients) {
                    ingredientList.add(ingredient.name)
                }
                val adapter = ArrayAdapter(
                    requireContext(), android.R.layout.simple_spinner_item, ingredientList
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerIngredients.adapter = adapter
            }
        })

        viewModel.mealsResponse.observe(viewLifecycleOwner, EventObserver(
            onError = {
                binding.mealsProgressBar.isVisible = false
                binding.mealsRecycler.isVisible = false
                binding.tvEmptyState.isVisible = true
                binding.tvEmptyState.setText(R.string.find_recipes_message)
                snackbar(it)
            },
            onLoading = {
                binding.mealsProgressBar.isVisible = true
                binding.mealsRecycler.isVisible = false
                binding.tvEmptyState.isVisible = false
            }
        ) { response ->
            binding.mealsProgressBar.isVisible = false
            val meals = response.meals
            if (meals.isNullOrEmpty()) {
                binding.mealsRecycler.isVisible = false
                binding.tvEmptyState.isVisible = true
                binding.tvEmptyState.setText(R.string.find_recipes_empty_state)
            } else {
                binding.mealsRecycler.isVisible = true
                binding.tvEmptyState.isVisible = false
                mealAdapter.submitList(meals)
            }
        })
    }

    private fun findRecipeByName() {
        viewModel.findRecipeByName(
            binding.inputRecipeName.text.toString()
        )
    }

    private fun findRecipeByCategory() {
        viewModel.findRecipeByCategory(
            binding.spinnerCategories.selectedItem.toString()
        )
    }

    private fun findRecipeByCountry() {
        viewModel.findRecipeByCountry(
            binding.spinnerCountries.selectedItem.toString()
        )
    }

    private fun findRecipeByMainIngredient() {
        viewModel.findRecipeByMainIngredient(
            binding.spinnerIngredients.selectedItem.toString()
        )
    }

    private fun showAndHideLayouts(
        isByName: Boolean = false,
        isByCategory: Boolean = false,
        isByCountry: Boolean = false,
        isByIngredient: Boolean = false
    ) {
        with(binding) {
            layoutFindByName.isVisible = isByName
            layoutFindByCategory.isVisible = isByCategory
            layoutFindByCountry.isVisible = isByCountry
            layoutFindByMainIngredient.isVisible = isByIngredient
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}