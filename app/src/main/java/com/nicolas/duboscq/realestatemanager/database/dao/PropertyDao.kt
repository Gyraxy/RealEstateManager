package com.nicolas.duboscq.realestatemanager.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.nicolas.duboscq.realestatemanager.models.Property


@Dao
interface PropertyDao {

    @Query("SELECT * from property")
    fun getAll(): LiveData<List<Property>>

    @Insert
    fun insert(property: Property) : Long?

    @Query("DELETE FROM property")
    fun deleteAll()
}