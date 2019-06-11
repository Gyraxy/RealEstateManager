package com.nicolas.duboscq.realestatemanager.repositories

import androidx.lifecycle.LiveData
import androidx.sqlite.db.SupportSQLiteQuery
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

    fun updatePropertyById(prop_id:Int,agent:String, status: String,price: Int, surface:Int,room: Int,bedroom:Int,bathroom: Int,description:String,type:String,points_interest:String,dateEntry:String,dateSold:String,dateModified:String){
        return propertyDao.updatePropertyById(prop_id,agent,status,price,surface,room,bedroom,bathroom,description,type,points_interest,dateEntry,dateSold,dateModified)
    }

    fun getPropertyBySearch(query:SupportSQLiteQuery):MutableList<Property>{
        return propertyDao.getPropertyBySearch(query)
    }
}