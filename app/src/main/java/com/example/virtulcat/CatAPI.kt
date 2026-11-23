package com.example.virtulcat

import retrofit2.http.GET

interface CatApi {

    @GET("fact")
    suspend fun getRandomCat(): CatFactDto
}