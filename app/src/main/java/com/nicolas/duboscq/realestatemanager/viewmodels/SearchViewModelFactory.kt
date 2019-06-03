package com.nicolas.duboscq.realestatemanager.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nicolas.duboscq.realestatemanager.repositories.PropertyRepository
import java.util.concurrent.Executor

class SearchViewModelFactory(
    private val propertyDataSource: PropertyRepository,
    private val executor: Executor
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java!!)) {
            return SearchViewModel(
                propertyDataSource,
                executor
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}