package com.tdev.tgallery.data

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.tdev.tgallery.model.Album
import com.tdev.tgallery.model.MediaItem
import com.tdev.tgallery.model.MediaType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MediaRepository(private val context: Context) {

    suspend fun loadAllMedia(): List<MediaItem> = withContext(Dispatchers.IO) {
        val images = queryMedia(
            collection = if (Build.VERSION.SDK_INT >= 29)
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
            else MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            type = MediaType.IMAGE
        )
        val videos = queryMedia(
            collection = if (Build.VERSION.SDK_INT >= 29)
                MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
            else MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            type = MediaType.VIDEO
        )
        (images + videos).sortedByDescending { it.dateTaken }
    }

    suspend fun loadAlbums(): List<Album> = withContext(Dispatchers.IO) {
        val all = loadAllMedia()
        all.groupBy { it.bucketName }
            .map { (name, items) ->
                Album(
                    name = name,
                    coverUri = items.first().uri,
                    count = items.size,
                    items = items
                )
            }
            .sortedByDescending { it.count }
    }

    private fun queryMedia(collection: Uri, type: MediaType): List<MediaItem> {
        val items = mutableListOf<MediaItem>()

        val projection = mutableListOf(
            MediaStore.MediaColumns._ID,
            MediaStore.MediaColumns.DISPLAY_NAME,
            MediaStore.MediaColumns.DATE_TAKEN,
            MediaStore.MediaColumns.BUCKET_DISPLAY_NAME,
        ).also {
            if (type == MediaType.VIDEO) it.add(MediaStore.Video.Media.DURATION)
        }.toTypedArray()

        val sortOrder = "${MediaStore.MediaColumns.DATE_TAKEN} DESC"

        context.contentResolver.query(
            collection, projection, null, null, sortOrder
        )?.use { cursor ->
            val idCol     = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)
            val nameCol   = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
            val dateCol   = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_TAKEN)
            val bucketCol = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.BUCKET_DISPLAY_NAME)
            val durCol    = if (type == MediaType.VIDEO)
                cursor.getColumnIndex(MediaStore.Video.Media.DURATION) else -1

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idCol)
                val baseUri = if (type == MediaType.IMAGE)
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                else MediaStore.Video.Media.EXTERNAL_CONTENT_URI

                items.add(
                    MediaItem(
                        id = id,
                        uri = ContentUris.withAppendedId(baseUri, id),
                        name = cursor.getString(nameCol) ?: "",
                        bucketName = cursor.getString(bucketCol) ?: "Diğer",
                        dateTaken = cursor.getLong(dateCol),
                        durationMs = if (durCol >= 0) cursor.getLong(durCol) else 0L,
                        type = type
                    )
                )
            }
        }
        return items
    }
}
