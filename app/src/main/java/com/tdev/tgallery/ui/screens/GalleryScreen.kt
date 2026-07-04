package com.tdev.tgallery.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.tdev.tgallery.data.GalleryUiState
import com.tdev.tgallery.data.Tab
import com.tdev.tgallery.model.MediaItem
import com.tdev.tgallery.ui.components.MediaThumb
import com.tdev.tgallery.ui.theme.*

@Composable
fun GalleryScreen(
    state: GalleryUiState.Ready,
    onMediaClick: (MediaItem) -> Unit,
    onAlbumClick: (String) -> Unit,
    onTabChange: (Tab) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("TGallery", color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Medium)
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                TabLabel("Tümü", state.tab == Tab.ALL) { onTabChange(Tab.ALL) }
                TabLabel("Albümler", state.tab == Tab.ALBUMS) { onTabChange(Tab.ALBUMS) }
            }
        }

        when (state.tab) {
            Tab.ALL -> MediaGrid(items = state.allMedia, onMediaClick = onMediaClick)
            Tab.ALBUMS -> AlbumGrid(albums = state.albums, onAlbumClick = onAlbumClick)
        }
    }
}

@Composable
private fun TabLabel(text: String, selected: Boolean, onClick: () -> Unit) {
    Text(
        text = text,
        color = if (selected) TextPrimary else TextDim,
        fontSize = 14.sp,
        fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal,
        modifier = Modifier.clickable { onClick() }
    )
}

@Composable
private fun MediaGrid(items: List<MediaItem>, onMediaClick: (MediaItem) -> Unit) {
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

@Composable
private fun AlbumGrid(
    albums: List<com.tdev.tgallery.model.Album>,
    onAlbumClick: (String) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(12.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(albums, key = { it.name }) { album ->
            Column(modifier = Modifier.clickable { onAlbumClick(album.name) }) {
                Box(modifier = Modifier.aspectRatio(1f)) {
                    AsyncImage(
                        model = album.coverUri,
                        contentDescription = album.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(album.name, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                Text("${album.count} öğe", color = TextDim, fontSize = 12.sp)
            }
        }
    }
}
