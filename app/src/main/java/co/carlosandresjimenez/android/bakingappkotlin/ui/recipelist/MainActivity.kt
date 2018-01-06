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

package co.carlosandresjimenez.android.bakingappkotlin.ui.recipelist

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.design.widget.Snackbar
import android.support.test.espresso.idling.CountingIdlingResource
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import co.carlosandresjimenez.android.bakingappkotlin.R
import co.carlosandresjimenez.android.bakingappkotlin.data.model.BakingRecipe
import co.carlosandresjimenez.android.bakingappkotlin.data.remote.ApiCallback
import co.carlosandresjimenez.android.bakingappkotlin.data.remote.ConnectionManager
import co.carlosandresjimenez.android.bakingappkotlin.ui.recipedetail.DetailActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ApiCallback, RecipeListAdapter.OnItemClickListener {

    val LOG_TAG = MainActivity::class.simpleName

    companion object {
        val RECIPE_LIST: String = "RECIPE_LIST"
        val RECYCLERVIEW_STATE: String = "RECYCLERVIEW_STATE"
    }

    private var recyclerView: RecyclerView? = null
    private var progressBar: ProgressBar? = null

    private val request = ConnectionManager()
    private val adapter = RecipeListAdapter(this)

    var countingIdlingResource = CountingIdlingResource("Network_Call")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        progressBar = findViewById(R.id.progress_bar)

        recyclerView = findViewById(R.id.recipe_list)
        recyclerView?.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        recyclerView?.adapter = adapter

        if (savedInstanceState == null) {
            progressBar?.visibility = View.VISIBLE
            countingIdlingResource.increment()
            request.getRecipes(this)
        } else {
            adapter.recipes = savedInstanceState.getParcelableArrayList(RECIPE_LIST)
            adapter.notifyDataSetChanged()

            val recyclerState: Parcelable = savedInstanceState.getParcelable(RECYCLERVIEW_STATE)
            recyclerView?.layoutManager?.onRestoreInstanceState(recyclerState)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putParcelableArrayList(RECIPE_LIST, ArrayList(adapter.recipes))

        val recyclerState = recyclerView?.layoutManager?.onSaveInstanceState()
        outState?.putParcelable(RECYCLERVIEW_STATE, recyclerState)

        super.onSaveInstanceState(outState)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onRequestSuccess(recipes: List<BakingRecipe>?) {
        progressBar?.visibility = View.GONE
        if (recipes != null) {
            adapter.recipes = recipes
            adapter.notifyDataSetChanged()
            countingIdlingResource.decrement()
        }
    }

    override fun onRequestFailed(message: String?) {
        progressBar?.visibility = View.GONE

        Snackbar.make((progressBar as View), getString(R.string.network_error), Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.action_retry), {
                    progressBar?.visibility = View.VISIBLE
                    request.getRecipes(this)
                }).show()
    }

    override fun onItemClick(holder: RecipeListAdapter.ViewHolder) {
        val intent = Intent(this, DetailActivity::class.java )
        intent.putExtra(DetailActivity.RECIPE_EXTRA, holder.recipe)
        startActivity(intent)
    }

    fun getEspressoIdlingResource(): CountingIdlingResource {
        return countingIdlingResource
    }
}
