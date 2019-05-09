package com.nicolas.duboscq.realestatemanager.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.nicolas.duboscq.realestatemanager.models.Picture

@Dao
interface PictureDao {

    @Query("SELECT * from picture")
    fun getAll(): LiveData<MutableList<Picture>>

    @Query("SELECT * from picture WHERE picture.picture_property_number=0")
    fun getFirstPicture(): LiveData<MutableList<Picture>>

    @Insert
    fun insert(picture: Picture)

    @Query("DELETE FROM picture")
    fun deleteAll()
}