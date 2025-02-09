package com.example.photogalleryapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface UnsplashApiService {
    @GET("photos/random")
    fun getPhotos(
        @Query("count") count: Int = 30,
        @Query("client_id") clientId: String = "G4HUM_jYrinhcMsW7nnTcbA2wlRLLx75fmw37s4SrVQ"
    ): Call<List<Photo>>
}
