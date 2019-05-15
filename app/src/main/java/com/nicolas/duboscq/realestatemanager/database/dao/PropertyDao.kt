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

    @Query("DELETE FROM property")
    fun deleteAll()
}