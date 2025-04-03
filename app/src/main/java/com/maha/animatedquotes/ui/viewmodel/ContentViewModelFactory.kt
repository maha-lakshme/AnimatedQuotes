package com.maha.animatedquotes.ui.viewmodel
// app/src/main/java/com/example/motivationalapp/ui/viewmodel/ContentViewModelFactory.kt


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.maha.animatedquotes.data.repository.ContentRepository



class ContentViewModelFactory(private val repository: ContentRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ContentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
