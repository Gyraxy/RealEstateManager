package com.nicolas.duboscq.realestatemanager.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.nicolas.duboscq.realestatemanager.models.Property


@Dao
interface PropertyDao {

    @Query("SELECT * from property")
    fun getAll(): LiveData<MutableList<Property>>

    @Query("SELECT * from property WHERE property.id = :prop_id")
    fun getPropertyById(prop_id:Int) : LiveData<Property>

    @Insert
    fun insert(property: Property) : Long

    @Query("UPDATE property SET status= :status,price= :price,surface= :surface,room= :room,bedroom= :bedroom,bathroom= :bathroom,description= :description,type= :type,date_modified= :dateModified WHERE property.id = :prop_id")
    fun updatePropertyById(prop_id:Int,status: String,price: Int, surface:Int,room: Int,bedroom:Int,bathroom: Int,description:String,type:String,dateModified:String)

    @Query("DELETE FROM property")
    fun deleteAll()
}