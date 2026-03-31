package edu.nd.pmcburne.hello.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import edu.nd.pmcburne.hello.data.repository.PlacemarkRepository

class CampusMapViewModelFactory (private val repository: PlacemarkRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CampusMapViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return CampusMapViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}