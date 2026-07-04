package com.tdev.tgallery.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tdev.tgallery.model.MediaItem
import com.tdev.tgallery.ui.theme.TextDim
import com.tdev.tgallery.ui.theme.TextPrimary

@Composable
fun AlbumScreen(
    albumName: String,
    items: List<MediaItem>,
    onBack: () -> Unit,
    onMediaClick: (MediaItem) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Geri",
                tint = TextPrimary,
                modifier = Modifier
                    .size(22.dp)
                    .clickable { onBack() }
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(albumName, color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                Text("${items.size} öğe", color = TextDim, fontSize = 12.sp)
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(1.dp),
            horizontalArrangement = Arrangement.spacedBy(1.dp),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            items(items, key = { it.id }) { item ->
                MediaThumb(item = item, onClick = { onMediaClick(item) })
            }
        }
    }
}
