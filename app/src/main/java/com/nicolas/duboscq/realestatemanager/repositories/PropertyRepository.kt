package com.nicolas.duboscq.realestatemanager.repositories

import android.arch.lifecycle.LiveData
import android.support.annotation.WorkerThread
import com.nicolas.duboscq.realestatemanager.database.dao.PropertyDao
import com.nicolas.duboscq.realestatemanager.models.Property

class PropertyRepository(private val propertyDao: PropertyDao) {

    val allProperty : LiveData<List<Property>> = propertyDao.getAll()

    @WorkerThread
    suspend fun insertProperty(property: Property){
        propertyDao.insert(property)
    }
}