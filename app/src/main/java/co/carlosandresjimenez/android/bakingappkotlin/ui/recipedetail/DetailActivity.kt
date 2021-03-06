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

package co.carlosandresjimenez.android.bakingappkotlin.ui.recipedetail

import android.os.Bundle
import android.os.Parcelable
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.FrameLayout
import android.widget.LinearLayout
import co.carlosandresjimenez.android.bakingappkotlin.R
import co.carlosandresjimenez.android.bakingappkotlin.data.model.BakingRecipe
import co.carlosandresjimenez.android.bakingappkotlin.ui.ingredientslist.IngredientsFragment
import co.carlosandresjimenez.android.bakingappkotlin.ui.recipelist.MainActivity
import co.carlosandresjimenez.android.bakingappkotlin.ui.stepdetail.StepDetailFragment
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity(), RecipeDetailsAdapter.OnItemClickListener {

    companion object {
        val RECYCLERVIEW_STATE: String = "RECYCLERVIEW_STATE"

        const val RECIPE_EXTRA: String = "bakingappkotlin.ui.recipedetail.RECIPE_EXTRA"

        const val DETAIL_FRAGMENT: String = "bakingappkotlin.ui.recipedetail.DETAIL_FRAGMENT"
    }

    var recyclerView: RecyclerView? = null
    var frameLayout: FrameLayout? = null

    private var twoPane: Boolean = false

    lateinit var bakingRecipe: BakingRecipe

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        bakingRecipe = intent!!.getParcelableExtra(DetailActivity.RECIPE_EXTRA)

        recyclerView = findViewById(R.id.recipe_detail_list)
        frameLayout = findViewById(R.id.fragment)

        recyclerView?.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        recyclerView?.adapter = RecipeDetailsAdapter(this, bakingRecipe.steps)

        if (savedInstanceState != null) {
            val recyclerState: Parcelable = savedInstanceState.getParcelable(MainActivity.RECYCLERVIEW_STATE)
            recyclerView?.layoutManager?.onRestoreInstanceState(recyclerState)
        }

        twoPane = resources.getBoolean(R.bool.show_master_detail_flow)

    }

    override fun onSaveInstanceState(outState: Bundle?) {
        val recyclerState = recyclerView?.layoutManager?.onSaveInstanceState()
        outState?.putParcelable(RECYCLERVIEW_STATE, recyclerState)

        super.onSaveInstanceState(outState)
    }

    override fun onItemClick(holder: RecyclerView.ViewHolder) {
        when (holder) {
            is RecipeDetailsAdapter.ViewHolderIngredients -> {
                var ingredientsFragment = IngredientsFragment.newInstance(bakingRecipe)

                if (twoPane) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment, ingredientsFragment, DetailActivity.DETAIL_FRAGMENT)
                            .addToBackStack(null)
                            .commit()
                } else {
                    ingredientsFragment.show(supportFragmentManager, "Dialog")
                }

            }
            is RecipeDetailsAdapter.ViewHolderSteps -> {
                var stepDetailFragment = StepDetailFragment.newInstance(holder.step)
                supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment, stepDetailFragment, DetailActivity.DETAIL_FRAGMENT)
                        .addToBackStack(null)
                        .commit()

            }
        }
    }

    override fun onBackPressed() {
        if (supportActionBar?.isShowing != true) {
            supportActionBar?.show()
        }

        super.onBackPressed()
    }

    fun hideToolbar() {
        supportActionBar?.hide()
    }

    fun showToolbar() {
        supportActionBar?.show()
    }
}
