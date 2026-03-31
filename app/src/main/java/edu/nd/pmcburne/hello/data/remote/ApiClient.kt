package edu.nd.pmcburne.hello.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    val service: PlacemarkApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.cs.virginia.edu/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PlacemarkApiService::class.java)
    }
}