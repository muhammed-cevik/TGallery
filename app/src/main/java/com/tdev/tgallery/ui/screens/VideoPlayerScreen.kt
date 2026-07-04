package com.tdev.tgallery.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem as ExoMediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.tdev.tgallery.model.MediaItem
import com.tdev.tgallery.ui.theme.Bg
import com.tdev.tgallery.ui.theme.TextDim
import com.tdev.tgallery.ui.theme.TextPrimary

@Composable
fun VideoPlayerScreen(item: MediaItem, onBack: () -> Unit) {
    val context = LocalContext.current

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(ExoMediaItem.fromUri(item.uri))
            prepare()
            playWhenReady = true
        }
    }

    DisposableEffect(Unit) {
        onDispose { exoPlayer.release() }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Bg)
    ) {
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = exoPlayer
                    useController = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .aspectRatio(16f / 9f)
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
                val sec = item.durationMs / 1000
                if (sec > 0) {
                    Text(
                        text = "%d:%02d".format(sec / 60, sec % 60),
                        color = TextDim,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}
