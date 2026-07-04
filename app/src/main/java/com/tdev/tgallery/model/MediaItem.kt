package com.tdev.tgallery.model

import android.net.Uri

enum class MediaType { IMAGE, VIDEO }

data class MediaItem(
    val id: Long,
    val uri: Uri,
    val name: String,
    val bucketName: String,   // albüm adı
    val dateTaken: Long,      // epoch ms
    val durationMs: Long = 0, // sadece video
    val type: MediaType
)

data class Album(
    val name: String,
    val coverUri: Uri,
    val count: Int,
    val items: List<MediaItem>
)
