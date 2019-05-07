package com.nicolas.duboscq.realestatemanager.injections

import android.content.Context
import com.nicolas.duboscq.realestatemanager.database.AppDatabase
import com.nicolas.duboscq.realestatemanager.repositories.AddressRepository
import com.nicolas.duboscq.realestatemanager.repositories.PictureRepository
import com.nicolas.duboscq.realestatemanager.repositories.PropertyRepository
import java.util.concurrent.Executor
import java.util.concurrent.Executors

object Injection {

    fun providePropertyDataSource(context: Context): PropertyRepository {
        val database = AppDatabase.getDatabase(context)
        return PropertyRepository(database.propertyDao())
    }

    fun provideAddressDataSource(context: Context): AddressRepository {
        val database = AppDatabase.getDatabase(context)
        return AddressRepository(database.addressDao())
    }

    fun providePictureDataSource(context: Context): PictureRepository {
        val database = AppDatabase.getDatabase(context)
        return PictureRepository(database.pictureDao())
    }

    fun provideExecutor(): Executor {
        return Executors.newSingleThreadExecutor()
    }

    fun provideViewModelFactory(context: Context): ViewModelFactory {
        val dataSourceProperty = providePropertyDataSource(context)
        val dataSourceAddress = provideAddressDataSource(context)
        val dataSourcePicture = providePictureDataSource(context)
        val executor = provideExecutor()
        return ViewModelFactory(dataSourceProperty,dataSourceAddress,dataSourcePicture,executor)
    }
}