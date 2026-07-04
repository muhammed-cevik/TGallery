package com.tdev.tgallery.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.tdev.tgallery.model.MediaItem
import com.tdev.tgallery.ui.theme.Bg
import com.tdev.tgallery.ui.theme.TextDim
import com.tdev.tgallery.ui.theme.TextPrimary
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PhotoDetailScreen(item: MediaItem, onBack: () -> Unit) {
    val zoomState = rememberZoomState()
    val formatter = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale("tr"))

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Bg)
    ) {
        // Fotoğraf (pinch-to-zoom)
        AsyncImage(
            model = item.uri,
            contentDescription = item.name,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize()
                .zoomable(zoomState)
        )

        // Üst bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .background(Bg.copy(alpha = 0.5f)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Geri",
                tint = TextPrimary,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onBack() }
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = item.name,
                    color = TextPrimary,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (item.dateTaken > 0) {
                    Text(
                        text = formatter.format(Date(item.dateTaken)),
                        color = TextDim,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}
