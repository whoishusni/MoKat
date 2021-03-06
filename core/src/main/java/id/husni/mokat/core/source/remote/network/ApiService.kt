package id.husni.mokat.core.source.remote.network

import id.husni.mokat.core.source.remote.response.MovieResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("3/discover/movie")
    suspend fun getAllMovies(
        @Query("api_key") api_key: String,
        @Query("language") language: String
    ): MovieResponse
}