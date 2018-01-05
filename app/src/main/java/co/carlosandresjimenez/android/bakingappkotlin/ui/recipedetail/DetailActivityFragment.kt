package co.carlosandresjimenez.android.bakingappkotlin.ui.recipedetail

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import co.carlosandresjimenez.android.bakingappkotlin.R
import co.carlosandresjimenez.android.bakingappkotlin.data.model.BakingRecipe
import co.carlosandresjimenez.android.bakingappkotlin.ui.ingredientslist.IngredientsFragment
import co.carlosandresjimenez.android.bakingappkotlin.ui.stepdetail.StepDetailFragment

/**
 * A placeholder fragment containing a simple view.
 */
class DetailActivityFragment : Fragment(), RecipeDetailsAdapter.OnItemClickListener {

    companion object {
        fun newInstance(): DetailActivityFragment {
            return DetailActivityFragment()
        }
    }

    var recyclerView: RecyclerView? = null

    lateinit var bakingRecipe: BakingRecipe

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_detail, container, false)

        bakingRecipe = activity!!.intent!!.getParcelableExtra(DetailActivity.RECIPE_EXTRA)

        recyclerView = view.findViewById(R.id.recipe_detail_list)
        recyclerView?.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        recyclerView?.adapter = RecipeDetailsAdapter(this, bakingRecipe.steps)

        return view
    }

    override fun onItemClick(holder: RecyclerView.ViewHolder) {
        when (holder) {
            is RecipeDetailsAdapter.ViewHolderIngredients -> {
                var ingredientsFragment = IngredientsFragment.newInstance(bakingRecipe.ingredients)
                ingredientsFragment.show(fragmentManager, "dialog")

//                fragmentManager!!.beginTransaction()
//                        .add(R.id.fragment, ingredientsFragment, DetailActivity.INGREDIENTS_LIST_FRAGMENT)
//                        .addToBackStack(null)
//                        .commit()
            }
            is RecipeDetailsAdapter.ViewHolderSteps -> {

                var stepDetailFragment = StepDetailFragment.newInstance(holder.step)
                fragmentManager!!.beginTransaction()
                        .add(R.id.fragment, stepDetailFragment, DetailActivity.INGREDIENTS_LIST_FRAGMENT)
                        .addToBackStack(null)
                        .commit()

            }
        }
    }

}
