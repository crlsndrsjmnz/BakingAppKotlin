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

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import co.carlosandresjimenez.android.bakingappkotlin.R
import co.carlosandresjimenez.android.bakingappkotlin.ui.recipelist.MainActivity
import co.carlosandresjimenez.android.bakingappkotlin.util.Utility

/**
 * Created by carlosjimenez on 1/5/18.
 */
class IngredientWidgetProvider: AppWidgetProvider() {

    val LOG_TAG = IngredientWidgetProvider::class.simpleName

    companion object {
        val ACTION_DATA_UPDATED: String = "bakingappkotlin.ui.ingredientslist.widget.ACTION_DATA_UPDATED"

        val EXTRA_DATA_RECIPE: String = "bakingappkotlin.ui.ingredientslist.widget.EXTRA_DATA_RECIPE"

        fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager,
                            appWidgetId: Int) {
            val views = RemoteViews(context.packageName, R.layout.widget_ingredients)

            // Create an Intent to launch MainActivity
            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(context.applicationContext, 0, intent, 0)
            views.setOnClickPendingIntent(R.id.widgetName, pendingIntent)

            val recipe = Utility.readRecipePreference(context)
            views.setTextViewText(R.id.widgetRecipeName, recipe?.name)

            // Set up the collection
            views.setRemoteAdapter(R.id.widget_list,
                    Intent(context.applicationContext, IngredientsWidgetRemoteViewsService::class.java))

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onUpdate(context: Context?, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray?) {
        for (appWidgetId in appWidgetIds ?: IntArray(0)) {
            updateAppWidget(context!!, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        if (ACTION_DATA_UPDATED == intent?.action) {

            val views = RemoteViews(context?.packageName, R.layout.widget_ingredients)

            val recipe = Utility.readRecipePreference(context!!)
            views.setTextViewText(R.id.widgetRecipeName, recipe?.name)

            val appWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(
                    ComponentName(context, javaClass))

            appWidgetManager.updateAppWidget(appWidgetIds, views)
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list)
        }
    }
}