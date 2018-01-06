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

package co.carlosandresjimenez.android.bakingappkotlin.data.remote

import co.carlosandresjimenez.android.bakingappkotlin.data.model.BakingRecipe
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Created by carlosjimenez on 1/1/18.
 */
class ConnectionManager {

    val LOG_TAG = ConnectionManager::class.java.simpleName

    private val bakingApi: BakingApi

    init {

        val retrofit = Retrofit.Builder()
                .baseUrl("https://d17h27t6h515a5.cloudfront.net/")
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

        bakingApi = retrofit.create(BakingApi::class.java)
    }

    private fun createApiBuilder(): Call<List<BakingRecipe>> {
        return bakingApi.getRecipes()
    }

    fun getRecipes(callback: ApiCallback) {

        createApiBuilder().enqueue(object : Callback<List<BakingRecipe>> {
            override fun onFailure(call: Call<List<BakingRecipe>>?, t: Throwable?) {
                callback.onRequestFailed(t?.message)
            }

            override fun onResponse(call: Call<List<BakingRecipe>>?, response: Response<List<BakingRecipe>>?) {
                callback.onRequestSuccess(response?.body())
            }
        })

    }
}
