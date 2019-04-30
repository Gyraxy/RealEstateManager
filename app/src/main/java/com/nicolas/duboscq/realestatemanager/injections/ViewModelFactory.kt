package com.nicolas.duboscq.realestatemanager.injections

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.nicolas.duboscq.realestatemanager.models.PropertyViewModel
import com.nicolas.duboscq.realestatemanager.repositories.AddressRepository
import com.nicolas.duboscq.realestatemanager.repositories.PropertyRepository
import java.util.concurrent.Executor

class ViewModelFactory(
    private val propertyDataSource: PropertyRepository,
    private val addressDataSource: AddressRepository,
    private val executor: Executor
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PropertyViewModel::class.java!!)) {
            return PropertyViewModel(propertyDataSource,addressDataSource,executor) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}