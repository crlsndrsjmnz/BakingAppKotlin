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

package co.carlosandresjimenez.android.bakingappkotlin.ui.ingredientslist.widget

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import co.carlosandresjimenez.android.bakingappkotlin.R
import co.carlosandresjimenez.android.bakingappkotlin.data.model.BakingIngredient
import co.carlosandresjimenez.android.bakingappkotlin.util.Utility

/**
 * Created by carlosjimenez on 1/5/18.
 */

class IngredientsWidgetRemoteViewsService : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory {
        return IngredientsRemoteViewsFactory(applicationContext)
    }

}

class IngredientsRemoteViewsFactory(val context: Context) : RemoteViewsService.RemoteViewsFactory {

    var data = emptyList<BakingIngredient>()

    override fun onCreate() {
    }

    override fun getLoadingView(): RemoteViews {
        return RemoteViews(context.packageName, R.layout.widget_ingredients_list_item)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onDataSetChanged() {
        val recipe = Utility.readRecipePreference(context)
        data = recipe?.ingredients ?: emptyList()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getViewAt(position: Int): RemoteViews? {

        if (data.isEmpty()) {
            return null
        }

        val views = RemoteViews(context.packageName, R.layout.widget_ingredients_list_item)

        val formattedIngredient = "${data[position].quantity} ${data[position].measure} of ${data[position].ingredient}"
        views.setTextViewText(R.id.ingredient_item, formattedIngredient)

        return views
    }

    override fun getCount(): Int {
        return data.size
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun onDestroy() {
        data = emptyList()
    }
}