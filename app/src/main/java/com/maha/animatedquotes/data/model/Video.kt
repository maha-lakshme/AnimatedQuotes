package com.maha.animatedquotes.data.model

data class Video(
    val id: String,
    val title: String,
    val video_files: List<VideoFile>,
    val url: String
)
data class VideoFile(
    val quality: String,
   val fileType: String,
   val link: String
)