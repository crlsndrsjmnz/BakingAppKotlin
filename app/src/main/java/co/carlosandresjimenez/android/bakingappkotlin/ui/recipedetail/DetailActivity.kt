package co.carlosandresjimenez.android.bakingappkotlin.ui.recipedetail

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.FrameLayout
import android.widget.LinearLayout
import co.carlosandresjimenez.android.bakingappkotlin.R
import co.carlosandresjimenez.android.bakingappkotlin.data.model.BakingRecipe
import co.carlosandresjimenez.android.bakingappkotlin.ui.ingredientslist.IngredientsFragment
import co.carlosandresjimenez.android.bakingappkotlin.ui.stepdetail.StepDetailFragment
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity(), RecipeDetailsAdapter.OnItemClickListener {

    companion object {
        const val RECIPE_EXTRA: String = "bakingappkotlin.ui.recipedetail.RECIPE_EXTRA"
        const val STEP_EXTRA: String = "bakingappkotlin.ui.recipedetail.STEP_EXTRA"

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

        twoPane = resources.getBoolean(R.bool.show_master_detail_flow)

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
