package co.carlosandresjimenez.android.bakingappkotlin.data.remote

import co.carlosandresjimenez.android.bakingappkotlin.data.model.BakingRecipe

/**
 * Created by carlosjimenez on 1/1/18.
 */
interface ApiCallback {
    fun onRequestSuccess(recipes: List<BakingRecipe>?)
    fun onRequestFailed(message: String?)
}