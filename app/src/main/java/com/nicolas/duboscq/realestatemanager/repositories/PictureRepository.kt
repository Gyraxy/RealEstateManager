package com.nicolas.duboscq.realestatemanager.repositories

import android.arch.lifecycle.LiveData
import android.support.annotation.WorkerThread
import com.nicolas.duboscq.realestatemanager.database.dao.PictureDao
import com.nicolas.duboscq.realestatemanager.models.Picture

class PictureRepository(private val pictureDao: PictureDao) {

    val allPicture : LiveData<List<Picture>> = pictureDao.getAll()

    @WorkerThread
    suspend fun insertPicture(picture: Picture){
        pictureDao.insert(picture)
    }
}