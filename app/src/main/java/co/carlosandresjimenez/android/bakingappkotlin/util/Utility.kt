/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 Carlos Andres Jimenez <apps@carlosandresjimenez.co>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

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


