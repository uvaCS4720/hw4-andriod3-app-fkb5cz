package edu.nd.pmcburne.hello.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import edu.nd.pmcburne.hello.data.repository.PlacemarkRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CampusMapViewModel (private val repository: PlacemarkRepository) : ViewModel() {
    private val _selectedTag = MutableStateFlow("core")
    val selectedTag: StateFlow<String> = _selectedTag

    // get placemarks from database
    val placemarks = repository.allPlacemarks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // sort tag list
    val allTags = placemarks
        .combine(selectedTag) { places, _ ->
            places.flatMap { it.tagList() }
                .distinct()
                .sorted()
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // filtered placements based on selected tag
    val filteredPlacemarks = placemarks
        .combine(selectedTag) { places, tag ->
            places.filter { tag in it.tagList()}
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // sync data from API when viewmodel created
    init {
        viewModelScope.launch {
            repository.syncFromApi()
        }
    }

    // update selected tag
    fun setSelectedTag(tag: String) {
        _selectedTag.value = tag
    }
}

class CampusViewModelFactory(private val repository: PlacemarkRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create (modelClass: Class<T>): T {
        return CampusMapViewModel(repository) as T
    }
}
