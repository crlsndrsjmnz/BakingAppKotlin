package co.carlosandresjimenez.android.bakingappkotlin.ui.recipedetail

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import co.carlosandresjimenez.android.bakingappkotlin.R
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    companion object {
        const val RECIPE_EXTRA: String = "RECIPE_EXTRA"
        const val STEP_EXTRA: String = "STEP_EXTRA"

        const val STEP_LIST_FRAGMENT: String = "STEP_LIST_FRAGMENT"
        const val INGREDIENTS_LIST_FRAGMENT: String = "INGREDIENTS_LIST_FRAGMENT"
        const val STEP_DETAIL_FRAGMENT: String = "STEP_DETAIL_FRAGMENT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            var detailFragment = DetailActivityFragment.newInstance()
            supportFragmentManager.beginTransaction()
                    .add(R.id.fragment, detailFragment, STEP_LIST_FRAGMENT)
                    .addToBackStack(null)
                    .commit()
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
