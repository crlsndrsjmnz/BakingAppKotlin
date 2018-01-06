package co.carlosandresjimenez.android.bakingappkotlin

import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.IdlingResource
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.RootMatchers.isDialog
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import co.carlosandresjimenez.android.bakingappkotlin.ui.recipelist.MainActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith



/**
 * Created by carlosjimenez on 1/6/18.
 */
@RunWith(AndroidJUnit4::class)
class RecipeTests {

    val LOG_TAG = RecipeTests::class.simpleName

    @Rule
    @JvmField
    var activityRule = ActivityTestRule(MainActivity::class.java)

    var idlingResource: IdlingResource? = null

    @Before
    fun registerIdlingResource() {
        idlingResource = activityRule.activity.getEspressoIdlingResource()
        // To prove that the test fails, omit this call:
        Espresso.registerIdlingResources(idlingResource)
    }

    @Test
    fun testBrowniesIntro() {
        onView(withId(R.id.recipe_list))
                .perform(RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                        hasDescendant(withText("Brownies")), click()))

        onView(withId(R.id.recipe_detail_list))
                .perform(RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                        hasDescendant(withText("Recipe Introduction")), click()))

        onView(withId(R.id.shortDescription)).check(matches(withText("Recipe Introduction")))
    }

    @Test
    fun testCheesecakeIngredients() {
        onView(withId(R.id.recipe_list))
                .perform(RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                        hasDescendant(withText("Cheesecake")), click()))

        onView(withId(R.id.recipe_detail_list))
                .perform(RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                        hasDescendant(withText("Ingredients")), click()))

        onView(withText("Ingredients"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))

        onView(withId(android.R.id.button2))
                .perform(click())
    }

    @After
    fun unregisterIdlingResource() {
        if (idlingResource != null) {
            Espresso.unregisterIdlingResources(idlingResource)
        }
    }
}