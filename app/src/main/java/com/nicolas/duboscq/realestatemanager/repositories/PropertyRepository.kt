package com.nicolas.duboscq.realestatemanager.repositories

import android.arch.lifecycle.LiveData
import com.nicolas.duboscq.realestatemanager.database.dao.PropertyDao
import com.nicolas.duboscq.realestatemanager.models.Property

class PropertyRepository(private val propertyDao: PropertyDao) {

    fun createProperty(property: Property): Long{
        return propertyDao.insert(property)
    }

    fun getProperty(): LiveData<List<Property>>{
        return propertyDao.getAll()
    }
}