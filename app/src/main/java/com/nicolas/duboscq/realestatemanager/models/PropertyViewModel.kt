package com.nicolas.duboscq.realestatemanager.models

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.nicolas.duboscq.realestatemanager.database.AppDatabase
import com.nicolas.duboscq.realestatemanager.repositories.AddressRepository
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

    private val propRepository: PropertyRepository
    private val addRepository: AddressRepository
    val allProperty: LiveData<List<Property>>
    val allAddress: LiveData<List<Address>>

    init {
        val propertyDao = AppDatabase.getDatabase(application).propertyDao()
        val addressDao = AppDatabase.getDatabase(application).addressDao()
        propRepository = PropertyRepository(propertyDao)
        addRepository = AddressRepository(addressDao)
        allProperty = propRepository.allProperty
        allAddress = addRepository.allAddress
    }

    fun insert(property: Property) = scope.launch(Dispatchers.IO) {
        propRepository.insertProperty(property)
    }

    fun insert(address: Address) = scope.launch(Dispatchers.IO) {
        addRepository.insertAddress(address)
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }
}