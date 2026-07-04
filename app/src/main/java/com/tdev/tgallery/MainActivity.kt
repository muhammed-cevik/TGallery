package com.tdev.tgallery

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tdev.tgallery.data.GalleryUiState
import com.tdev.tgallery.data.GalleryViewModel
import com.tdev.tgallery.model.MediaItem
import com.tdev.tgallery.model.MediaType
import com.tdev.tgallery.ui.screens.*
import com.tdev.tgallery.ui.theme.Bg
import com.tdev.tgallery.ui.theme.TGalleryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TGalleryTheme {
                TGalleryApp()
            }
        }
    }
}

private sealed class Screen {
    object Gallery : Screen()
    data class PhotoDetail(val item: MediaItem) : Screen()
    data class VideoPlayer(val item: MediaItem) : Screen()
    data class Album(val name: String) : Screen()
}

@Composable
private fun TGalleryApp() {
    val vm: GalleryViewModel = viewModel()
    val state by vm.state.collectAsState()
    var screen by remember { mutableStateOf<Screen>(Screen.Gallery) }

    val permissions = remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            arrayOf(Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO)
        else
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    val permLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        if (results.values.any { it }) vm.load() else vm.permissionDenied()
    }

    LaunchedEffect(Unit) { permLauncher.launch(permissions) }

    fun onMediaClick(item: MediaItem) {
        screen = if (item.type == MediaType.VIDEO) Screen.VideoPlayer(item)
        else Screen.PhotoDetail(item)
    }

    when (val s = screen) {
        is Screen.Gallery -> when (val st = state) {
            is GalleryUiState.Loading -> LoadingScreen()
            is GalleryUiState.PermissionNeeded -> PermissionScreen { permLauncher.launch(permissions) }
            is GalleryUiState.Ready -> GalleryScreen(
                state = st,
                onMediaClick = ::onMediaClick,
                onAlbumClick = { name -> screen = Screen.Album(name) },
                onTabChange = vm::setTab
            )
        }
        is Screen.PhotoDetail -> PhotoDetailScreen(item = s.item, onBack = { screen = Screen.Gallery })
        is Screen.VideoPlayer -> VideoPlayerScreen(item = s.item, onBack = { screen = Screen.Gallery })
        is Screen.Album -> {
            val st = state
            val items = if (st is GalleryUiState.Ready)
                st.albums.find { it.name == s.name }?.items ?: emptyList()
            else emptyList()
            AlbumScreen(
                albumName = s.name,
                items = items,
                onBack = { screen = Screen.Gallery },
                onMediaClick = ::onMediaClick
            )
        }
    }
}

@Composable
private fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize().background(Bg),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Color.White)
    }
}
