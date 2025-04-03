package com.maha.animatedquotes.data.viewmodel

import android.os.Looper
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.maha.animatedquotes.data.model.Quote
import com.maha.animatedquotes.data.model.Video
import com.maha.animatedquotes.data.model.VideoFile
import com.maha.animatedquotes.data.repository.ContentRepository
import com.maha.animatedquotes.ui.viewmodel.ContentViewModel
import com.maha.animatedquotes.util.getOrAwaitValue
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class ContentViewModelTest {

    // This rule makes LiveData execute each task synchronously.
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    //You can optionally create a test dispatcher rule for coroutines if needed.
    // For simplicity, we'll use runTest which supports coroutines.

    // Create a mock of your repository.
    private lateinit var contentRepository: ContentRepository
    private lateinit var contentViewModel: ContentViewModel


    // Dispatchers.Main relies on the Android main thread,
    // but in a unit test environment (outside of an Android runtime), it doesn't exist. For testing purposes, you need to use the kotlinx-coroutines-test library to provide a test dispatcher for Dispatchers.Main.


    @Before
    fun setUp(){
        // Override Dispatchers.Main with the test dispatcher

        contentRepository = Mockito.mock(ContentRepository::class.java)
        contentViewModel = ContentViewModel(contentRepository)
    }


    @Test
    fun `load video set the videos livedata on success`() = runTest {
        //Arrange
         val videolist = listOf(
             Video("1","video title", listOf(VideoFile(quality = "hd", fileType = ".mp4",link="hd_link")),"videoUrl"),
             Video("2","video title 2", listOf(VideoFile(quality = "sd", fileType = ".mp4",link="sd_link")),"videoUrl 2")
        )

        // Stub repository.fetchVideos to return our fake list.
        Mockito.`when`(contentRepository.fetchVideos("inspiration")).thenReturn(videolist)

        // Act: call loadVideos() on the ViewModel.
        contentViewModel.loadVideos("inspiration")


        // Assert: Verify that videos LiveData was updated.
        val result = contentViewModel.videos.getOrAwaitValue()

        assertNotNull("Videos LiveData should not be null",result)
        assertEquals("Expected 2 videos",videolist.size,result.size)
        assertEquals("The returned videos should match the expected list",videolist,result)

    }

    @Test
    fun `loadQuotes sets quotes LiveData on success`() = runBlocking {
        // Arrange
        val quotesList = listOf(
            Quote(id = "1", quote = "Quote 1", author = "Author1", length = 100),
            Quote(id = "2", quote = "Quote 2", author = "Author2", length = 150)
        )

        whenever(contentRepository.fetchQuotes()).thenReturn(quotesList)

        // Act
        contentViewModel.loadQuotes()
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        // Assert
        val result = contentViewModel.quotes.getOrAwaitValue()
        assertNotNull("Quotes LiveData should not be null", result)
        assertEquals("Quotes list should match expected data", quotesList, result)
    }
    @Test
    fun `getQuotesByRange sets quotesByLength LiveData on success`() {
        // Arrange
        val quotesList = listOf(
            Quote(id = "1", quote = "Short quote", author = "Author1", length = 50),
            Quote(id = "2", quote = "Another short quote", author = "Author2", length = 100)
        )
        runBlocking {
            whenever(contentRepository.fetQuotesByRange()).thenReturn(quotesList)
        }
        // Act
        contentViewModel.getQuotesByRange()

        // Force the main looper to process all pending tasks
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        // Assert
        val result = contentViewModel.quotesByLength.getOrAwaitValue()

        assertNotNull("QuotesByLength LiveData should not be null", result)
        assertEquals("QuotesByLength list should match expected quotes", quotesList, result)

    }

    @Test
    fun `getQuotesByRange handles exception and sets error LiveData`() {
        // Arrange
        runBlocking {
        Mockito.`when`(contentRepository.fetQuotesByRange()).thenThrow(RuntimeException("Network error"))
        }
        // Act
        contentViewModel.getQuotesByRange()
        Shadows.shadowOf(Looper.getMainLooper()).idle() // Process coroutine tasks

        // Assert
        val result = contentViewModel.quotesByLength.getOrAwaitValue()
        val error = contentViewModel.error.getOrAwaitValue()
        val loading = contentViewModel.loading.getOrAwaitValue()

        assertEquals("QuotesByLength should be empty on error", emptyList<Quote>(), result)
        assertEquals("Error fetching quotes: Network error", error)
        assertFalse("Loading LiveData should be false after failure", loading)
    }

 @Test
 fun `loadQuotes sets error message and loading false on failure`() = runTest {
     val exception = RuntimeException("Network failure")

     // Stub the repository method so it throws the exception.
     Mockito.`when`(contentRepository.fetchQuotes()).thenThrow(exception)

     // Act: Call loadQuotes() on the ViewModel.
     contentViewModel.loadQuotes()

     // Advance the dispatcher until all tasks complete.


     //Assert
     // verify the error livedata is updating when error occurs
     val error = contentViewModel.error.getOrAwaitValue()
     println("Error Message: $error")
     assertNotNull("Error message should not be null",error)
     assertEquals("Error fetching quotes: ${exception.localizedMessage}", error)

     // verify the loading livedata is set to false
     val loading = contentViewModel.loading.getOrAwaitValue()
     println("Loading State: $loading")
     assertFalse("Loading should be false after error",loading)

     //verify that quotes LiveData remains unchanged (could be empty if you initialize it that way).
     val quoteResult = contentViewModel.quotes.getOrAwaitValue()
     println("Quotes Result should be empty list: $quoteResult")
     assertTrue("Quotes list should be empty on error", quoteResult.isEmpty())

     // Also verify that the repository method was called with the expected parameter if needed.
     Mockito.verify(contentRepository).fetchQuotes()
 }
}