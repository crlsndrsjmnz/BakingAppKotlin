package co.carlosandresjimenez.android.bakingappkotlin.ui.recipelist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import co.carlosandresjimenez.android.bakingappkotlin.R
import co.carlosandresjimenez.android.bakingappkotlin.data.model.BakingRecipe

/**
 * Created by carlosjimenez on 1/1/18.
 */

class RecipeListAdapter(val listener: OnItemClickListener, var recipes: List<BakingRecipe> = emptyList()) : RecyclerView.Adapter<RecipeListAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(holder: ViewHolder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recipe_item_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(listener, recipes[position])
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var recipe: BakingRecipe

        val tvRecipeName = itemView.findViewById<TextView>(R.id.recipe_name)
        val llListItem = itemView.findViewById<LinearLayout>(R.id.list_item)

        fun bindItems(listener: OnItemClickListener, recipe: BakingRecipe) {
            this.recipe = recipe

            tvRecipeName.text = recipe.name
            llListItem.setOnClickListener { listener.onItemClick(this) }
        }

    }

}