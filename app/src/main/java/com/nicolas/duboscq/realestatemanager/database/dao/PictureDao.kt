package com.nicolas.duboscq.realestatemanager.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.nicolas.duboscq.realestatemanager.models.Picture

@Dao
interface PictureDao {

    @Query("SELECT * from picture")
    fun getAll(): LiveData<List<Picture>>

    @Insert
    fun insert(picture: Picture)

    @Query("DELETE FROM picture")
    fun deleteAll()
}