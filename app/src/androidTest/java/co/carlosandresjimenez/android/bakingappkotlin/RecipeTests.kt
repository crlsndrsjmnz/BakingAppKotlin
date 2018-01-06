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