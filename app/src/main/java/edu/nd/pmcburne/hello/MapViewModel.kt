package edu.nd.pmcburne.hello

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MapUiState(
    val tags: List<String> = emptyList(),
    val selectedTag: String = "core",
    val locations: List<LocationWithTags> = emptyList(),
    val loading: Boolean = true
)

class MapViewModel(
    private val repository: LocationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MapUiState())
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.syncFromApi()

            val tags = repository.getAllTags()
            val defaultTag = if (tags.contains("core")) "core" else tags.firstOrNull().orEmpty()
            val locations = if (defaultTag.isNotEmpty()) {
                repository.getLocationsForTag(defaultTag)
            } else {
                emptyList()
            }

            _uiState.value = MapUiState(
                tags = tags,
                selectedTag = defaultTag,
                locations = locations,
                loading = false
            )
        }
    }

    fun onTagSelected(tag: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val locations = repository.getLocationsForTag(tag)
            _uiState.value = _uiState.value.copy(
                selectedTag = tag,
                locations = locations
            )
        }
    }
}

class MapViewModelFactory(
    private val repository: LocationRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MapViewModel(repository) as T
    }
}