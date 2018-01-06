package co.carlosandresjimenez.android.bakingappkotlin.util

import android.content.Context
import android.preference.PreferenceManager
import co.carlosandresjimenez.android.bakingappkotlin.R
import co.carlosandresjimenez.android.bakingappkotlin.data.model.BakingRecipe
import com.squareup.moshi.Moshi

/**
 * Created by carlosjimenez on 1/6/18.
 */
class Utility {

    companion object {
        fun saveRecipePreference(context: Context, recipe: BakingRecipe?) {

            if (recipe == null) {
                return
            }

            val moshi = Moshi.Builder().build()
            val jsonAdapter = moshi.adapter(BakingRecipe::class.java)

            val jsonRecipe = jsonAdapter.toJson(recipe)

            val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
            val editor = sharedPref.edit()

            editor.putString(context.getString(R.string.recipe_preference_key), jsonRecipe)
            editor.apply()
        }

        fun readRecipePreference(context: Context): BakingRecipe? {

            val moshi = Moshi.Builder().build()
            val jsonAdapter = moshi.adapter(BakingRecipe::class.java)

            val sp = PreferenceManager.getDefaultSharedPreferences(context)
            val recipeJson = sp.getString(context.getString(R.string.recipe_preference_key), "")

            if (recipeJson == "") {
                return null
            }

            return jsonAdapter.fromJson(recipeJson)
        }

    }
}


