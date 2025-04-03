package com.maha.animatedquotes.ui.view

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.maha.animatedquotes.databinding.ItemVideoQuoteBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player

class VideoQuoteAdapter(
    private var items: List<VideoQuoteItem> = emptyList()
) : RecyclerView.Adapter<VideoQuoteAdapter.VideoQuoteViewHolder>() {

    inner class VideoQuoteViewHolder(private val binding: ItemVideoQuoteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var player: ExoPlayer? = null

        fun bind(item: VideoQuoteItem) {
            binding.tvQuote.text = item.quote.quote
            binding.tvAuthor.text = item.quote.author
            // Create and set up ExoPlayer instance.
            player = ExoPlayer.Builder(binding.root.context).build()
            binding.playerView.player = player

            val mediaItem = MediaItem.fromUri(Uri.parse(item.video.url))
            player?.setMediaItem(mediaItem)

            // Attach a listener for playback events and errors.
            player?.addListener(object : Player.Listener {
                override fun onPlayerErrorChanged(error: PlaybackException?) {
                    if (error != null) {
                        Toast.makeText(
                            binding.root.context,
                            "Error playing video: ${error.localizedMessage}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })

            player?.prepare()
            player?.playWhenReady = true

            // Bind overlay texts for the video and quote.


        }

        fun releasePlayer() {
            player?.release()
            player = null
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoQuoteViewHolder {
        val binding = ItemVideoQuoteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VideoQuoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoQuoteViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<VideoQuoteItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onViewRecycled(holder: VideoQuoteViewHolder) {
        super.onViewRecycled(holder)
        holder.releasePlayer()
    }
}