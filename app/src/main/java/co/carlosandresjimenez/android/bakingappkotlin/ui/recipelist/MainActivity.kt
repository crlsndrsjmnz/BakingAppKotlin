package co.carlosandresjimenez.android.bakingappkotlin.ui.recipelist

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
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
    val RECIPE_LIST: String = "RECIPE_LIST"

    private var recyclerView: RecyclerView? = null
    private var progressBar: ProgressBar? = null

    private val request = ConnectionManager()
    private val adapter = RecipeListAdapter(this)

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
            request.getRecipes(this)
        } else {
            adapter.recipes = savedInstanceState.getParcelableArrayList(RECIPE_LIST)
            adapter.notifyDataSetChanged()
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putParcelableArrayList(RECIPE_LIST, ArrayList(adapter.recipes))

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
        }
    }

    override fun onRequestFailed(message: String?) {
        progressBar?.visibility = View.GONE
        Log.d(LOG_TAG, "ERROR: " + message)
    }

    override fun onItemClick(holder: RecipeListAdapter.ViewHolder) {
        val intent = Intent(this, DetailActivity::class.java )
        intent.putExtra(DetailActivity.RECIPE_EXTRA, holder.recipe)
        startActivity(intent)
    }
}
