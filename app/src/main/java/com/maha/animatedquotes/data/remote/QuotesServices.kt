package com.maha.animatedquotes.data.remote

import com.maha.animatedquotes.data.model.Quote
import retrofit2.http.GET
import retrofit2.http.Query

interface QuotesService {
    //Get random quotes
    @GET("api/quotes/random")
    suspend fun getQuotes(): List<Quote>

    //Get Multiple Random Quotes
    @GET("/api/quotes/random")
    suspend fun getMultipleRandomQuotes(@Query("count")count: Int):List<Quote>

    //Get QuoteBy LengthRange
    @GET("/api/quotes/random")
    suspend fun getQuoteByLength(@Query("minLength")minLength:Int,@Query("maxLength")maxLength:Int,@Query("count")count:Int):List<Quote>

    //Get QuoteBy Author
    @GET("/api/quotes/random")
    suspend fun getQuoteByAuthor(@Query("author")author:String):List<Quote>

    //Get QuoteBy Tags
    @GET("/api/quotes/random")
    suspend fun getQuoteByTag(@Query("tags")tags:String):List<Quote>
}