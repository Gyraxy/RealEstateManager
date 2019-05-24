package com.nicolas.duboscq.realestatemanager.repositories

import androidx.lifecycle.LiveData
import com.nicolas.duboscq.realestatemanager.database.dao.PropertyDao
import com.nicolas.duboscq.realestatemanager.models.Property

class PropertyRepository(private val propertyDao: PropertyDao) {

    fun createProperty(property: Property): Long{
        return propertyDao.insert(property)
    }

    fun getProperty(): LiveData<MutableList<Property>>{
        return propertyDao.getAll()
    }

    fun getPropertyById(prop_id: Int): LiveData<Property>{
        return propertyDao.getPropertyById(prop_id)
    }

    fun updatePropertyById(prop_id:Int,status: String,price: Int, surface:Int,room: Int,bedroom:Int,bathroom: Int,description:String,type:String,dateModified:String){
        return propertyDao.updatePropertyById(prop_id,status,price,surface,room,bedroom,bathroom,description,type,dateModified)
    }
}