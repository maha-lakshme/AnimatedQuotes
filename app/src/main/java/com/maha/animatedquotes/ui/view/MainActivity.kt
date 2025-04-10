package com.maha.animatedquotes.ui.view



import android.app.ProgressDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.maha.animatedquotes.R
import com.maha.animatedquotes.data.model.Quote
import com.maha.animatedquotes.data.model.Video
import com.maha.animatedquotes.data.remote.RetrofitInstance
import com.maha.animatedquotes.data.repository.ContentRepository
import com.maha.animatedquotes.databinding.ActivityMainBinding
import com.maha.animatedquotes.ui.viewmodel.ContentViewModel
import com.maha.animatedquotes.ui.viewmodel.ContentViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: ContentViewModel
    private lateinit var videoQuoteAdapter: VideoQuoteAdapter

    // Local caches for the separate data sets.
    private var videoList: List<Video> = emptyList()
    private var quoteList: List<Quote> = emptyList()

    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate layout using Data Binding.
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

//        // Start Firebase anonymous sign-in.
//        FirebaseManager.signInAnonymously { success ->
//            if (success) {
//                // Already logged in.
//            } else {
//                Toast.makeText(this, "Firebase sign-in failed.", Toast.LENGTH_LONG).show()
//            }
//        }

        // Initialize adapter.
        videoQuoteAdapter = VideoQuoteAdapter()

        // Set up RecyclerView.
        binding.rvCombined.adapter = videoQuoteAdapter
        binding.rvCombined.layoutManager = LinearLayoutManager(this)

        // Set up ViewModel.
        val repository = ContentRepository(
            RetrofitInstance.pexelsService,
            RetrofitInstance.quotesService
        )
        val factory = ContentViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(ContentViewModel::class.java)

        // Observe loading status and show/hide progress dialog.
        viewModel.loading.observe(this) { isLoading ->
            if (isLoading) {
                if (progressDialog == null) {
                    progressDialog = ProgressDialog(this)
                    progressDialog?.setMessage("Loading...")
                    progressDialog?.setCancelable(false)
                }
                progressDialog?.show()
            } else {
                progressDialog?.dismiss()
            }
        }

        // Observe error messages and show Toast.
        viewModel.error.observe(this) { errorMsg ->
            if (errorMsg != null) {
                Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show()
            }
        }

        // Observe videos and quotes.
        viewModel.videos.observe(this) { videos ->
            videoList = videos
            println("Videos -------"+videoList)
            updateCombinedData()
        }
        viewModel.quotes.observe(this) { quotes ->
            quoteList = quotes
            println("BY Random-------"+quotes)
            updateCombinedData()
        }

        viewModel.quotesByLength.observe(this){ quotes ->
            println("BY RANGE-------"+quotes)
        }
        // Load data.
        viewModel.loadVideos("nature")
        viewModel.loadQuotes()
        viewModel.getQuotesByRange()
    }

    // Merge videos and quotes pairwise.
    private fun updateCombinedData() {
        if (videoList.isEmpty() || quoteList.isEmpty()) return

        // For each video, assign a quote (cycling through quotes if necessary).
        val combinedItems = videoList.mapIndexed { index, video ->
            val quote = quoteList[index % quoteList.size]
            VideoQuoteItem(video, quote)
        }
        videoQuoteAdapter.updateData(combinedItems)
    }
}
