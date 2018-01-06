package co.carlosandresjimenez.android.bakingappkotlin.ui.ingredientslist

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import co.carlosandresjimenez.android.bakingappkotlin.R
import co.carlosandresjimenez.android.bakingappkotlin.data.model.BakingRecipe
import co.carlosandresjimenez.android.bakingappkotlin.ui.ingredientslist.widget.IngredientWidgetProvider
import co.carlosandresjimenez.android.bakingappkotlin.util.Utility


/**
 * Created by carlosjimenez on 1/3/18.
 */

class IngredientsFragment : DialogFragment() {

    val LOG_TAG = IngredientsFragment::class.simpleName

    companion object {
        private val RECIPE_PARAMS = "RECIPE_PARAMS"

        fun newInstance(recipe: BakingRecipe): IngredientsFragment {
            val args = Bundle()
            args.putParcelable(RECIPE_PARAMS, recipe)
            val fragment = IngredientsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var ingredients: TextView? = null

    private var recipe: BakingRecipe? = null

    private var twoPane: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        recipe = arguments?.getParcelable(RECIPE_PARAMS)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        if (showsDialog) {
            return null
        }

        val view = inflater.inflate(R.layout.fragment_ingredients, container, false)

        twoPane = resources.getBoolean(R.bool.show_master_detail_flow)
        if (!twoPane) {
            setHasOptionsMenu(true)
        }

        ingredients = view.findViewById(R.id.ingredients)
        ingredients?.text = getIngredients()

        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return AlertDialog.Builder(context!!)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(getString(R.string.ingredients_title))
                .setMessage(getIngredients())
                .setPositiveButton(R.string.alert_dialog_send, { _, _ ->
                    Utility.saveRecipePreference(context!!, recipe)
                    val dataUpdatedIntent = Intent(IngredientWidgetProvider.ACTION_DATA_UPDATED)
                            .setPackage(context!!.packageName)
                            .putExtra(IngredientWidgetProvider.EXTRA_DATA_RECIPE, recipe)
                    context?.sendBroadcast(dataUpdatedIntent)
                })
                .setNegativeButton(R.string.alert_dialog_cancel, { _, _ -> dismiss() })
                .create()
    }

    private fun getIngredients(): String {
        var formattedIngredients = ""

        for (ingredient in recipe?.ingredients ?: emptyList()) {
            formattedIngredients += "${ingredient.quantity} ${ingredient.measure} of ${ingredient.ingredient} \n"
        }

        return formattedIngredients
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            android.R.id.home -> {
                fragmentManager!!.popBackStack()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}