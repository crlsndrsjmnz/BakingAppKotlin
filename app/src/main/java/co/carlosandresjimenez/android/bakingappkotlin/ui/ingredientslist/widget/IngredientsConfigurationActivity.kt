package co.carlosandresjimenez.android.bakingappkotlin.ui.ingredientslist.widget

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import co.carlosandresjimenez.android.bakingappkotlin.R
import co.carlosandresjimenez.android.bakingappkotlin.data.model.BakingRecipe
import co.carlosandresjimenez.android.bakingappkotlin.data.remote.ApiCallback
import co.carlosandresjimenez.android.bakingappkotlin.data.remote.ConnectionManager
import co.carlosandresjimenez.android.bakingappkotlin.ui.recipelist.MainActivity
import co.carlosandresjimenez.android.bakingappkotlin.ui.recipelist.RecipeListAdapter
import co.carlosandresjimenez.android.bakingappkotlin.util.Utility

/**
 * Created by carlosjimenez on 1/6/18.
 */
class IngredientsConfigurationActivity : AppCompatActivity(), ApiCallback, RecipeListAdapter.OnItemClickListener {

    val LOG_TAG = IngredientsConfigurationActivity::class.simpleName

    companion object {
        val RECIPE_LIST: String = "RECIPE_LIST"
        val RECYCLERVIEW_STATE: String = "RECYCLERVIEW_STATE"
    }

    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID

    private var recyclerView: RecyclerView? = null
    private var progressBar: ProgressBar? = null

    private val request = ConnectionManager()
    private val adapter = RecipeListAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if they press the back button.
        setResult(RESULT_CANCELED)

        // Set the view layout resource to use.
        setContentView(R.layout.widget_configure)

        // Find the widget id from the intent.
        appWidgetId = intent.extras.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)

        // If they gave us an intent without the widget id, just bail.
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
        }

        progressBar = findViewById(R.id.progress_bar)

        recyclerView = findViewById(R.id.recipe_list)
        recyclerView?.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        recyclerView?.adapter = adapter

        if (savedInstanceState == null) {
            progressBar?.visibility = View.VISIBLE
            request.getRecipes(this)
        } else {
            adapter.recipes = savedInstanceState.getParcelableArrayList(MainActivity.RECIPE_LIST)
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

    override fun onRequestSuccess(recipes: List<BakingRecipe>?) {
        progressBar?.visibility = View.GONE
        if (recipes != null) {
            adapter.recipes = recipes
            adapter.notifyDataSetChanged()
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
        Utility.saveRecipePreference(this, holder.recipe)

        // Push widget update to surface with newly set prefix
        val appWidgetManager = AppWidgetManager.getInstance(this)
        IngredientWidgetProvider.updateAppWidget(this, appWidgetManager, appWidgetId)

        // Make sure we pass back the original appWidgetId
        val resultValue = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_OK, resultValue)
        finish()
    }
}