package com.nicolas.duboscq.realestatemanager.repositories

import androidx.lifecycle.LiveData
import androidx.annotation.WorkerThread
import com.nicolas.duboscq.realestatemanager.database.dao.PictureDao
import com.nicolas.duboscq.realestatemanager.models.Picture

class PictureRepository(private val pictureDao: PictureDao) {

    fun createPicture(picture: Picture) {
        pictureDao.insert(picture)
    }

    fun getFirstPicture(): LiveData<MutableList<Picture>> {
        return pictureDao.getFirstPicture()
    }
}