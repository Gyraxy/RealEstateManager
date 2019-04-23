package com.nicolas.duboscq.realestatemanager.models

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.nicolas.duboscq.realestatemanager.database.AppDatabase
import com.nicolas.duboscq.realestatemanager.repositories.PropertyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class PropertyViewModel (application: Application) : AndroidViewModel(application) {

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    private val repository: PropertyRepository
    val allProperty: LiveData<List<Property>>

    init {
        val propertyDao = AppDatabase.getDatabase(application).propertyDao()
        repository = PropertyRepository(propertyDao)
        allProperty = repository.allProperty
    }

    fun insert(property: Property) = scope.launch(Dispatchers.IO) {
        repository.insertProperty(property)
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

}