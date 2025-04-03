package com.maha.animatedquotes.ui.viewmodel

// app/src/main/java/com/example/motivationalapp/ui/viewmodel/ContentViewModel.kt

import androidx.annotation.OpenForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maha.animatedquotes.data.model.Quote
import com.maha.animatedquotes.data.model.Video
import com.maha.animatedquotes.data.repository.ContentRepository
import kotlinx.coroutines.launch
@OpenForTesting
class ContentViewModel(private val repository: ContentRepository) : ViewModel() {

    private val _videos = MutableLiveData<List<Video>>()
    val videos: LiveData<List<Video>> = _videos

    private val _quotes = MutableLiveData<List<Quote>>()
    val quotes: LiveData<List<Quote>> = _quotes

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _quotesByLength = MutableLiveData<List<Quote>>()
    val quotesByLength: LiveData<List<Quote>> = _quotesByLength

    private val _quotesByAuthor = MutableLiveData<List<Quote>>()
    val quotesByAuthor: LiveData<List<Quote>> = _quotesByAuthor

    fun loadVideos(query: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val data = repository.fetchVideos(query)
                _videos.value = data
            } catch (e: Exception) {
                _videos.value = emptyList()
                _error.value = "Error fetching videos: ${e.localizedMessage}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun loadQuotes() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val data = repository.fetchQuotes()
                println("Fetched data: $data") // Add log for debugging
                _quotes.value = data
            } catch (e: Exception) {
                _quotes.value = emptyList()
                _error.value = "Error fetching quotes: ${e.localizedMessage}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun getQuotesByRange(){
        viewModelScope.launch {
            try {
                _loading.value = true
                val quotes= repository.fetQuotesByRange()
                println("Fetched data: $quotes") // Add log for debugging

                _quotesByLength.value =quotes

            }catch (e:Exception){
                _quotesByLength.value = emptyList()
                _error.value ="Error fetching quotes: ${e.localizedMessage}"
            }finally {
                _loading.value = false
            }
        }
    }

    fun getQuotesByAuthor(){
        viewModelScope.launch {
            try {
                _loading.value =true
                 _quotesByAuthor.value = repository.fecthQuotesByAuthor("John doe")
            }catch (e:Exception){
                _quotesByAuthor.value = emptyList()
                _error.value = "Error fetching quotes: ${e.localizedMessage}"
            }finally {
                _loading.value = false
            }
        }
    }
}
