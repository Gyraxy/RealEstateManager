package com.nicolas.duboscq.realestatemanager.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.nicolas.duboscq.realestatemanager.database.dao.AddressDao
import com.nicolas.duboscq.realestatemanager.database.dao.PictureDao
import com.nicolas.duboscq.realestatemanager.database.dao.PropertyDao
import com.nicolas.duboscq.realestatemanager.models.Address
import com.nicolas.duboscq.realestatemanager.models.Picture
import com.nicolas.duboscq.realestatemanager.models.Property
import com.nicolas.duboscq.realestatemanager.utils.DATABASE_NAME

@Database(entities = [(Property::class),(Address::class),(Picture::class)], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun propertyDao(): PropertyDao
    abstract fun addressDao():AddressDao
    abstract fun pictureDao():PictureDao

    companion object {

        @Volatile private var INSTANCE: AppDatabase? = null
        var TEST_MODE = false

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this){
                if (TEST_MODE){
                    val instance = Room.inMemoryDatabaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java
                    )
                        .allowMainThreadQueries()
                        .build()
                    INSTANCE = instance
                    instance
                } else {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        DATABASE_NAME
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                    instance
                }

            }
        }
    }
}