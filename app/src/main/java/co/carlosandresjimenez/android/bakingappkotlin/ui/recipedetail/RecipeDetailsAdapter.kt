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

package co.carlosandresjimenez.android.bakingappkotlin.ui.recipedetail

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import co.carlosandresjimenez.android.bakingappkotlin.R
import co.carlosandresjimenez.android.bakingappkotlin.data.model.BakingStep

/**
 * Created by carlosjimenez on 1/1/18.
 */

class RecipeDetailsAdapter(private val listener: OnItemClickListener, private var steps: List<BakingStep> = emptyList()) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private val VIEW_TYPE_INGREDIENTS = 0
        private val VIEW_TYPE_STEPS = 1
    }

    interface OnItemClickListener {
        fun onItemClick(holder: RecyclerView.ViewHolder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v: View

        return when (viewType) {
            VIEW_TYPE_INGREDIENTS -> {
                v = LayoutInflater.from(parent.context).inflate(R.layout.recipe_ingredient_layout, parent, false)
                ViewHolderIngredients(v)
            }
            else -> {
                v = LayoutInflater.from(parent.context).inflate(R.layout.recipe_step_layout, parent, false)
                ViewHolderSteps(v)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolderIngredients -> holder.bindItems(listener)
            is ViewHolderSteps -> holder.bindItems(listener, steps[position-1])
            else -> TODO("Not implemented")
        }
    }

    override fun getItemCount(): Int {
        return steps.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> VIEW_TYPE_INGREDIENTS
            else -> VIEW_TYPE_STEPS
        }
    }

    class ViewHolderIngredients(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val llListItem = itemView.findViewById<LinearLayout>(R.id.list_item)

        fun bindItems(listener: OnItemClickListener) {
            llListItem.setOnClickListener { listener.onItemClick(this) }
        }

    }

    class ViewHolderSteps(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var step: BakingStep

        private val tvStepDescription = itemView.findViewById<TextView>(R.id.step_description)
        private val llListItem = itemView.findViewById<LinearLayout>(R.id.list_item)

        fun bindItems(listener: OnItemClickListener, step: BakingStep) {
            this.step = step

            tvStepDescription.text = step.shortDescription
            llListItem.setOnClickListener { listener.onItemClick(this) }
        }

    }
}