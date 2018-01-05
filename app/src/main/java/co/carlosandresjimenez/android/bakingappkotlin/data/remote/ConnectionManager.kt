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

//        val client = OkHttpClient().newBuilder()
//                .addInterceptor(HttpLoggingInterceptor().apply {
//                    level = HttpLoggingInterceptor.Level.BODY
//                })
//                .build()


        val retrofit = Retrofit.Builder()
                .baseUrl("https://d17h27t6h515a5.cloudfront.net/")
//                .client(client)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

        bakingApi = retrofit.create(BakingApi::class.java)
    }

    fun createApiBuilder(): Call<List<BakingRecipe>> {
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
