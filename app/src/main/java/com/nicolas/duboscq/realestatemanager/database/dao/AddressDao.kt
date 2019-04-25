package com.nicolas.duboscq.realestatemanager.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.nicolas.duboscq.realestatemanager.models.Address

@Dao
interface AddressDao {

    @Query("SELECT * from address")
    fun getAll(): LiveData<List<Address>>

    @Insert
    fun insert(address: Address)

    @Query("DELETE FROM address")
    fun deleteAll()
}