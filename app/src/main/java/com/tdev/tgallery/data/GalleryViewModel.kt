package com.tdev.tgallery.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tdev.tgallery.model.Album
import com.tdev.tgallery.model.MediaItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class GalleryUiState {
    object Loading : GalleryUiState()
    object PermissionNeeded : GalleryUiState()
    data class Ready(
        val allMedia: List<MediaItem>,
        val albums: List<Album>,
        val tab: Tab = Tab.ALL
    ) : GalleryUiState()
}

enum class Tab { ALL, ALBUMS }

class GalleryViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = MediaRepository(application)

    private val _state = MutableStateFlow<GalleryUiState>(GalleryUiState.Loading)
    val state: StateFlow<GalleryUiState> = _state

    fun load() {
        viewModelScope.launch {
            _state.value = GalleryUiState.Loading
            val media = repo.loadAllMedia()
            val albums = repo.loadAlbums()
            _state.value = GalleryUiState.Ready(media, albums)
        }
    }

    fun permissionDenied() {
        _state.value = GalleryUiState.PermissionNeeded
    }

    fun setTab(tab: Tab) {
        val s = _state.value
        if (s is GalleryUiState.Ready) _state.value = s.copy(tab = tab)
    }
}
