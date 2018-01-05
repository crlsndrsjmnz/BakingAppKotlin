package co.carlosandresjimenez.android.bakingappkotlin.data.remote

import co.carlosandresjimenez.android.bakingappkotlin.data.model.BakingRecipe
import retrofit2.Call
import retrofit2.http.GET

/**
 * Created by carlosjimenez on 1/1/18.
 */
interface BakingApi {

    @GET("/topher/2017/May/59121517_baking/baking.json")
    fun getRecipes(): Call<List<BakingRecipe>>
}