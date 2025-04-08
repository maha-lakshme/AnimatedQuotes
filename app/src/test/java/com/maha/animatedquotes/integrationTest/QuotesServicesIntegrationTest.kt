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
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.test.assertFailsWith

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
    @Test
    fun `getMultipleRandomQuotes returns the expected number of quotes`() = runBlocking {
        // Arrange
        val fakeResponseQuotes = listOf(Quote("1","Everyday is oppurtinuty","Shiva",150),
            Quote("2","work harder","ganesh",150),
            Quote("3","believe in yourself","murugan",150))
        val jsonResponse = Gson().toJson(fakeResponseQuotes)

        // Enqueue a MockResponse with status 200 and the JSON body
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(jsonResponse)
        )

        // Act: Call the Retrofit service method with a count parameter of 3
        val response = quotesService.getMultipleRandomQuotes(3)

        // Assert
        assertNotNull(response)
        assertEquals("The response should contain 3 quotes", 3, response.size)
        assertEquals("Everyday is oppurtinuty", response[0].quote)
        assertEquals("Shiva", response[0].author)
        // Optionally add additional assertions for other items:
        assertEquals("work harder", response[1].quote)
        assertEquals("ganesh", response[1].author)
    }
    @Test
    fun `getMultipleRandomQuotes throws exception when retrofit receives network error` ()= runBlocking {
        mockWebServer.enqueue(
            MockResponse().setResponseCode(500).setBody("Internal server error"))
            // Act & Assert: Verify that calling the service method throws an HttpException.
            val exception = assertFailsWith<HttpException> {
                quotesService.getMultipleRandomQuotes(3)
            }

        // Optionally, verify that the HTTP status code in the exception is 500
        assertEquals(500, exception.code())

    }
}
