package com.github.aayman93.recipes.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import coil.load
import com.github.aayman93.recipes.R
import com.github.aayman93.recipes.data.models.Meal
import com.github.aayman93.recipes.databinding.FragmentRecipeDetailsBinding
import com.github.aayman93.recipes.ui.viewmodels.RecipeDetailsViewModel
import com.github.aayman93.recipes.util.EventObserver
import com.github.aayman93.recipes.util.snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.min

@AndroidEntryPoint
class RecipeDetailsFragment : Fragment() {

    private var _binding: FragmentRecipeDetailsBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<RecipeDetailsFragmentArgs>()

    private val viewModel: RecipeDetailsViewModel by viewModels()

    private var recipe: Meal? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recipeId = args.id
        viewModel.getRecipeById(recipeId)
        subscribeToObservables()
        setListeners()
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun setListeners() {
        with(binding) {
            fabFavourite.setOnClickListener {
                recipe?.let {
                    viewModel.addMealToFavourites(it)
                    snackbar(getString(R.string.recipe_added_to_favourites))
                }
            }
            buttonWatchVideo.setOnClickListener {
                recipe?.let {
                    val packageManager = requireContext().packageManager ?: return@setOnClickListener
                    var intent = Intent(Intent.ACTION_VIEW, getLaunchUri(it.strYoutube))
                    if (intent.resolveActivity(packageManager) == null) {
                        intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.strYoutube))
                    }
                    startActivity(intent)
                }
            }
        }
    }

    private fun subscribeToObservables() {
        viewModel.mealsResponse.observe(viewLifecycleOwner, EventObserver(
            onError = {
                binding.progressBar.isVisible = false
                binding.fabFavourite.isVisible = false
                binding.mainLayout.isVisible = false
                snackbar(it)
            },
            onLoading = {
                binding.progressBar.isVisible = true
                binding.fabFavourite.isVisible = false
                binding.mainLayout.isVisible = false
            },
        ) { response ->
            binding.progressBar.isVisible = false
            if (!response.meals.isNullOrEmpty()) {
                binding.fabFavourite.isVisible = true
                binding.mainLayout.isVisible = true
                recipe = response.meals[0]
                updateUi(recipe!!)
            }
        })
    }

    private fun updateUi(recipe: Meal) {
        with(binding) {
            ivRecipeImage.load(recipe.thumbnail) {
                placeholder(R.drawable.ic_placeholder)
                error(R.drawable.ic_broken_image)
            }
            tvRecipeTitle.text = recipe.name
            tvCategory.text = recipe.strCategory
            tvArea.text = recipe.strArea
            tvIngredients.text = getIngredients(recipe)
            tvInstructions.text = recipe.instructions
            layoutWatchVideo.isVisible = recipe.strYoutube.isNotBlank()
        }
    }

    private fun getIngredients(recipe: Meal): String {
        val ingredientsList = listOf(
            recipe.strIngredient1, recipe.strIngredient2, recipe.strIngredient3,
            recipe.strIngredient4, recipe.strIngredient5, recipe.strIngredient6,
            recipe.strIngredient7, recipe.strIngredient8, recipe.strIngredient9,
            recipe.strIngredient10, recipe.strIngredient11, recipe.strIngredient12,
            recipe.strIngredient13, recipe.strIngredient14, recipe.strIngredient15,
            recipe.strIngredient16, recipe.strIngredient17, recipe.strIngredient18,
            recipe.strIngredient19, recipe.strIngredient20
        ).filter { !it.isNullOrBlank() }

        val measuresList = listOf(
            recipe.strMeasure1, recipe.strMeasure2, recipe.strMeasure3, recipe.strMeasure4,
            recipe.strMeasure5, recipe.strMeasure6, recipe.strMeasure7, recipe.strMeasure8,
            recipe.strMeasure9, recipe.strMeasure10, recipe.strMeasure11, recipe.strMeasure12,
            recipe.strMeasure13, recipe.strMeasure14, recipe.strMeasure15, recipe.strMeasure16,
            recipe.strMeasure17, recipe.strMeasure18, recipe.strMeasure19, recipe.strMeasure20
        ).filter { !it.isNullOrBlank() }

        var ingredientString = ""
        val minSize = min(ingredientsList.size, measuresList.size) - 1
        for (i in 0..minSize) {
            ingredientString += if (i == 0) {
                "${measuresList[i]} ${ingredientsList[i]}"
            } else {
                "\n${measuresList[i]} ${ingredientsList[i]}"
            }
        }
        return ingredientString
    }

    private fun getLaunchUri(url: String): Uri {
        val uri = Uri.parse(url)
        return Uri.parse("vnd.youtube:" + uri.getQueryParameter("v"))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}