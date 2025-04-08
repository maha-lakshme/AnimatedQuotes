package com.maha.animatedquotes.data.repository

import com.maha.animatedquotes.data.model.Quote
import com.maha.animatedquotes.data.model.Video
import com.maha.animatedquotes.data.model.VideoFile
import com.maha.animatedquotes.data.remote.PexelsResponse
import com.maha.animatedquotes.data.remote.PexelsService
import com.maha.animatedquotes.data.remote.QuotesService
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import retrofit2.HttpException
import retrofit2.Response



@RunWith(RobolectricTestRunner::class)
class ContentRepositoryTest {
    private lateinit var pexelsService: PexelsService
    private lateinit var quotesServices:QuotesService
    private lateinit var contentRepository: ContentRepository

    @Before
    fun setUp(){
       pexelsService = Mockito.mock(PexelsService::class.java)
        quotesServices = Mockito.mock(QuotesService::class.java)
        contentRepository = ContentRepository(pexelsService, quotesServices)
    }
  @Test
  fun `fetchvideos only returns sd videos`() = runBlocking {
      // Prepare a fake video response that contains both HD and SD versions.
      val VideoFileSD = VideoFile(quality = "sd", fileType = "video", link = "sd_link1")
      val VideoFileSD2 = VideoFile(quality = "sd", fileType = "video", link = "sd_link2")
      val VideoFileHD = VideoFile(quality = "hd", fileType = "video", link = "hd_link")

      val videoResponse1 = Video(id = "1", title = "videohd", video_files = listOf(VideoFileSD,VideoFileHD), url = "pexelurl1")
      val videoResponse2 = Video(id = "2", title = "videohd", video_files = listOf(VideoFileSD2,VideoFileHD), url = "pexelurl2")
      val videoResponse3 = Video(id = "2", title = "videohd", video_files = listOf(VideoFileHD,VideoFileHD), url = "pexelurl2")


      // Create a fake response
      val fakeResponse =PexelsResponse(videos= listOf(videoResponse1,videoResponse2,videoResponse3) )
      println(fakeResponse)

      // Configure the mock to return the fake response.
      Mockito.`when`(pexelsService.searchVideos("motivation",10)).thenReturn(fakeResponse)

      // Act: Call the repository method that filters for SD videos.
      val sdVideos:List<Video> = contentRepository.fetchVideos("motivation")
println(sdVideos)
      // Assert: We expect only one video with the SD link.
      assertEquals(2, sdVideos.size)
      assertEquals("sd_link1", sdVideos[0].url)
      assertEquals("sd_link2", sdVideos[1].url)

  }
    @Test
    fun `fetch quotes form api`()= runBlocking {
        val quotes1 = Quote(id = "1", quote = "Sun is up", author = "hanuman", length = 150)
        val quotes2 =Quote(id = "2", quote = "shivam", author = "shiva",length = 120)
        val fakeResponse = listOf(quotes1,quotes2)
       // println(fakeResponse)

        Mockito.`when`(quotesServices.getMultipleRandomQuotes(20)).thenReturn(fakeResponse)

        val quoteList:List<Quote> =contentRepository.fetchQuotes()
        println(quoteList)

        assertEquals(2,quoteList.size)

    }


    @Test
    fun `fetch QuotesbyRange`() = runBlocking {
        val quotes1 = Quote(id = "1", quote = "Sun is up", author = "hanuman", length = 20)
        val quotes2 =Quote(id = "2", quote = "shivam", author = "shiva",length = 150)
        val quotes3 =Quote(id = "3", quote = "shivam", author = "shiva",length = 130)
        val fakeResponse = listOf(quotes1,quotes2,quotes3)

        Mockito.`when`(quotesServices.getQuoteByLength(20,130,30)).thenReturn(fakeResponse)

        val quoteList:List<Quote> = contentRepository.fetQuotesByRange()
        println("Expected output: 3 quotes; Actual output: ${quoteList.size}")

        assertNotNull("quoteList should not be null",quoteList)
        assertEquals("Expected output: 3 quotes; but found ${quoteList.size}",3,quoteList.size)
    }

    @Test
    fun `fetch quotes handles emptyLists`() = runBlocking {
        Mockito.`when`(quotesServices.getQuoteByLength(20,130,30)).thenReturn(emptyList())
        val quoteList:List<Quote> = contentRepository.fetQuotesByRange()
        assertTrue(quoteList.isEmpty())
    }

    @Test
    fun `verify the quotesRange are correctly passed`() {

        runBlocking {
            // Arrange: Mock API response
            val fakeResponse = listOf<Quote>()
            println(fakeResponse)

            Mockito.`when`(quotesServices.getQuoteByLength(20, 130, 30)).thenReturn(fakeResponse)

            // Act: Call the repository method
            val quoteList: List<Quote> = contentRepository.fetQuotesByRange()
            println(quoteList)

            // Assert: Verify the returned list is not null and check its size.
            // (If fakeResponse is an empty list, size should be 0.)
            assertNotNull(quoteList)
            assertEquals(fakeResponse.size, quoteList.size)

            // Assert: Verify the API call parameters
            Mockito.verify(quotesServices).getQuoteByLength(20, 130, 30)
        }
    }

    /**
       *  Test case : when api returns the quotes by the expected author
       * Expected result: The repository returns the same list.**/
    @Test
    fun `fetch quotes by author returns expected result `() {

        runBlocking {
        // Arrange: Create a fake response with a couple of quotes.
        val expectedResult = listOf(
            Quote(id = "1", quote = "sun rises", author = "john doe", length = 20),
            Quote(id = "1", quote = "Motivation is the key", author = "john doe", length = 20)
        )

        // Stub the API call to return the expected quotes when the author is "John Doe"
        Mockito.`when`(quotesServices.getQuoteByAuthor("john doe")).thenReturn(expectedResult)

        // Act: Invoke the repository method.
        val quoteResult: List<Quote> = contentRepository.fecthQuotesByAuthor("john doe")
        println("Expected Result: " + expectedResult)
        println("Actual Result: " + quoteResult)

        // Assert: Verify the returned list matches the expected list.
        assertNotNull("The returned list should not be null", quoteResult)
        assertEquals("Expected 2 quotes to be returned", expectedResult.size, quoteResult.size)
        assertEquals(
            "The returned quotes should match the expected quotes",
            expectedResult,
            quoteResult
        )

        // Verify that the service method was called exactly once with "John Doe".
        Mockito.verify(quotesServices).getQuoteByAuthor("john doe")
    }

    }
    /**
     *   Test case : when api returns empty list when the autor is not found
     *   Expected Result: The emptylist is returned by the repository
     */
    @Test
    fun `fetch quotes by author return empty list if author not found` () {
        runBlocking {
            val expectedResult: List<Quote> = emptyList()
            Mockito.`when`(quotesServices.getQuoteByAuthor("unknown author"))
                .thenReturn(emptyList())

            val quoteResult: List<Quote> = contentRepository.fecthQuotesByAuthor("Unknown Author")
            println("Expected Result: " + expectedResult)
            println("Actual Result: " + quoteResult)

            // Assert: Validate that the repository returns an empty list.
            assertNotNull("The result should not be null", quoteResult)
            assertTrue("Expected an empty list", quoteResult.isEmpty())

            Mockito.verify(quotesServices).getQuoteByAuthor("Unknown Author")
        }
    }
        /**
         * Test case : the result should not be null when an exception is thrown
         * Expected result: empty list is passed in repository
         */

        @Test
        fun `fetch quotes by author returns empty list when exception is thrown`(){
           runBlocking {
               Mockito.`when`(quotesServices.getQuoteByAuthor("Error")).thenThrow(RuntimeException("Network Error"))

               // Act: Invoke the repository method (which handles exceptions internally)
               val result: List<Quote> = contentRepository.fecthQuotesByAuthor("Error Author")
               println("Expected: [] because of exception")
               println("Actual  : $result")

               assertNotNull("The result should not be null",result)
               assertTrue("Expected an empty list",result.isEmpty())
               Mockito.verify(quotesServices).getQuoteByAuthor("Error Author")
           }

        }
    /**
     * Test Case:
     * When getQuoteByAuthor is called and a network error occurs (e.g., 404),
     * the repository should catch the HttpException and return an empty list.
     * It also should record the error code in lastErrorCode.
     */
    @Test
    fun `fetch quote by author returns empty list on network error and verifies error code`() {
        runBlocking {
            // Arrange:
            // Create an error response with an HTTP 404 status code.
            val errorJson = "{\"message\":\"Not Found\"}"
            val errorResponseBody = errorJson.toResponseBody("application/json".toMediaType())
            val errorResponse: Response<List<Quote>> = Response.error(404, errorResponseBody)
            val httpException = HttpException(errorResponse)

            // Stub the API method to throw the HttpException for a given author.
            Mockito.`when`(quotesServices.getQuoteByAuthor("Error Author"))
                .thenThrow(httpException)

            // Act: Call the repository method.
            val result: List<Quote> = contentRepository.fecthQuotesByAuthor("Error Author")

            // Assert:
            // Verify that the returned list is empty and that lastErrorCode is correctly recorded.
            assertNotNull("The result should not be null", result)
            assertTrue("Expected an empty list on HTTP error", result.isEmpty())
            assertEquals("Expected error code 404", 404, contentRepository.lastErrorCode)

            // Optionally, verify that the service method was called with the correct author.
            Mockito.verify(quotesServices).getQuoteByAuthor("Error Author")
        }
    }

}