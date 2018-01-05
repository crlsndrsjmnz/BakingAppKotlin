package co.carlosandresjimenez.android.bakingappkotlin.ui.ingredientslist

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import co.carlosandresjimenez.android.bakingappkotlin.R
import co.carlosandresjimenez.android.bakingappkotlin.data.model.BakingIngredient


/**
 * Created by carlosjimenez on 1/3/18.
 */

class IngredientsFragment : DialogFragment() {

    val LOG_TAG = IngredientsFragment::class.simpleName

    companion object {
        private val INGREDIENT_PARAMS = "INGREDIENT_PARAMS"

        fun newInstance(ingredients: List<BakingIngredient>): IngredientsFragment {
            val args = Bundle()
            args.putParcelableArrayList(INGREDIENT_PARAMS, ArrayList(ingredients))
            val fragment = IngredientsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    var ingredients: TextView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        if (showsDialog) {
            return null
        }

        val view = inflater.inflate(R.layout.fragment_ingredients, container, false)
        setHasOptionsMenu(true)

        ingredients = view.findViewById(R.id.ingredients)
        ingredients?.text = getIngredients()

        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(context!!)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(getString(R.string.ingredients_title))
                .setMessage(getIngredients())
                .setPositiveButton(R.string.alert_dialog_ok,
                        { dialog, whichButton -> Toast.makeText(context, "Ok", Toast.LENGTH_SHORT).show() }
                )
                .setNegativeButton(R.string.alert_dialog_cancel,
                        { dialog, whichButton -> Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show() }
                )
                .create()
    }

    fun getIngredients(): String {
        var ingredientsStr = ""
        val ingredients: List<BakingIngredient> = arguments!!.getParcelableArrayList(INGREDIENT_PARAMS)
        for (ingredient in ingredients) {
            ingredientsStr += "${ingredient.quantity} ${ingredient.measure} of ${ingredient.ingredient} \n"
        }

        return ingredientsStr
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