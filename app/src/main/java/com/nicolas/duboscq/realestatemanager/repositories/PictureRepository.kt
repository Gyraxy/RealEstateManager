package com.nicolas.duboscq.realestatemanager.repositories

import androidx.lifecycle.LiveData
import androidx.sqlite.db.SupportSQLiteQuery
import com.nicolas.duboscq.realestatemanager.database.dao.PictureDao
import com.nicolas.duboscq.realestatemanager.models.Picture

class PictureRepository(private val pictureDao: PictureDao) {

    fun createPicture(picture: Picture) {
        pictureDao.insert(picture)
    }

    fun getFirstPicture(): LiveData<MutableList<Picture>> {
        return pictureDao.getFirstPicture()
    }

    fun getPictureByPropId(prop_id: Int): LiveData<MutableList<Picture>> {
        return pictureDao.getPictureFromPropId(prop_id)
    }

    fun delPropertyById(prop_id: Int){
        return pictureDao.delPictureFromPropId(prop_id)
    }

    fun getPictureBySearch(query: SupportSQLiteQuery):MutableList<Picture>{
        return pictureDao.getPictureBySearch(query)
    }
}