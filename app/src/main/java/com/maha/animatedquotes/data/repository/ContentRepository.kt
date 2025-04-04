package com.maha.animatedquotes.data.repository


import com.maha.animatedquotes.data.model.Quote
import com.maha.animatedquotes.data.model.Video
import com.maha.animatedquotes.data.remote.PexelsService
import com.maha.animatedquotes.data.remote.QuotesService
import com.maha.animatedquotes.ui.view.OpenForTesting

@OpenForTesting
open class ContentRepository(
    private val pexelsService: PexelsService,
    private val quotesService: QuotesService
) {
    var lastErrorCode: Int? = null

    suspend fun fetchVideos(query: String): List<Video> {
        return try {
            val response = pexelsService.searchVideos(query)
            response.videos.mapNotNull { videoResponse ->
                val sdFile = videoResponse.video_files.firstOrNull() { it.quality == "sd" }
                sdFile?.let {
                    Video(
                        id = videoResponse.id.toString(),
                        title = videoResponse.url,
                        video_files = videoResponse.video_files,// You can map to title as needed
                        url = sdFile.link // Use the SD-quality video file's link
                    )
                }
            }
        } catch (e: Exception) {
            emptyList() // Return an empty list in case of errors
        }
    }

    suspend fun fetchQuotes(): List<Quote> {
        return try {
            quotesService.getMultipleRandomQuotes(20)

        } catch (e: Exception) {
            emptyList()
        }
    }
    suspend fun fetQuotesByRange():List<Quote>{
        return try {
            quotesService.getQuoteByLength(20,130,30)
        }catch (e:Exception){
            emptyList()
        }
    }

    suspend fun fecthQuotesByAuthor(author:String):List<Quote>{
        return try {
            lastErrorCode = null
            quotesService.getQuoteByAuthor(author) ?: emptyList()
        }catch (e: retrofit2.HttpException) {
            // Capture the HTTP status code and return an empty list.
            lastErrorCode = e.code()
            emptyList()
        }catch (e:Exception){
            emptyList()
        }
    }
}