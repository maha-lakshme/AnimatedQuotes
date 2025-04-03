package com.maha.animatedquotes.data.remote
import com.maha.animatedquotes.data.model.Video
import retrofit2.http.GET
import retrofit2.http.Query

data class PexelsResponse(
    val videos: List<Video>
)

interface PexelsService {
    @GET("videos/search")
    suspend fun searchVideos(
        @Query("query") query: String,
        @Query("per_page") perPage: Int = 10
    ): PexelsResponse
}