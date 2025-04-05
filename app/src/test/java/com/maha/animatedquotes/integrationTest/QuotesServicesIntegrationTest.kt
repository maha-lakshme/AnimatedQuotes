package com.maha.animatedquotes.integrationTest

import com.google.gson.Gson
import com.maha.animatedquotes.data.model.Quote
import com.maha.animatedquotes.data.remote.QuotesService
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class QuotesServicesIntegrationTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var quotesService: QuotesService

    @Before
    fun setUP(){
        mockWebServer = MockWebServer()
        mockWebServer.start()

        quotesService = Retrofit.Builder().baseUrl(mockWebServer.url("/")).
                addConverterFactory(GsonConverterFactory.create()).
                client(OkHttpClient.Builder().build()).
                build().create(QuotesService::class.java)
    }
    @After
    fun complete(){
        mockWebServer.shutdown()
    }
    @Test
    fun `getQuotes returns a random quotes`()= runBlocking {
        //Arrange
        val fakeResponseQuotes = listOf(Quote("1","Everyday is oppurtinuty","Shiva",150),
            Quote("2","work harder","ganesh",150),
            Quote("3","believe in yourself","murugan",150))
        val jsonResponse = Gson().toJson(fakeResponseQuotes)

        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(jsonResponse))

        //Act
        val quoteRsponse = quotesService.getQuotes()
        //Assert
        assertNotNull("Expected response is not null",quoteRsponse)
        assertTrue("Expected response is having a quotes list",quoteRsponse.isNotEmpty())
        assertEquals("Everyday is oppurtinuty",quoteRsponse[0].quote)
        assertEquals("Shiva",quoteRsponse[0].author)

    }
}