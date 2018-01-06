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