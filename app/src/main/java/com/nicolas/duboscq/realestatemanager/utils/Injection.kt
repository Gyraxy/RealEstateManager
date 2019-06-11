package com.nicolas.duboscq.realestatemanager.utils

import android.content.Context
import com.nicolas.duboscq.realestatemanager.database.AppDatabase
import com.nicolas.duboscq.realestatemanager.repositories.AddressRepository
import com.nicolas.duboscq.realestatemanager.repositories.PictureRepository
import com.nicolas.duboscq.realestatemanager.repositories.PropertyRepository
import com.nicolas.duboscq.realestatemanager.viewmodels.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors

object Injection {

    private fun providePropertyDataSource(context: Context): PropertyRepository {
        val database = AppDatabase.getDatabase(context)
        return PropertyRepository(database.propertyDao())
    }

    private fun provideAddressDataSource(context: Context): AddressRepository {
        val database = AppDatabase.getDatabase(context)
        return AddressRepository(database.addressDao())
    }

    private fun providePictureDataSource(context: Context): PictureRepository {
        val database = AppDatabase.getDatabase(context)
        return PictureRepository(database.pictureDao())
    }

    private fun provideExecutor(): Executor {
        return Executors.newSingleThreadExecutor()
    }

    fun provideListViewModelFactory(context: Context): PropertyListViewModelFactory {
        val dataSourceProperty =
            providePropertyDataSource(context)
        val dataSourceAddress = provideAddressDataSource(context)
        val dataSourcePicture = providePictureDataSource(context)
        val executor = provideExecutor()
        return PropertyListViewModelFactory(
            dataSourceProperty,
            dataSourceAddress,
            dataSourcePicture,
            executor
        )
    }

    fun provideDetailViewModelFactory(context: Context, property_id : Int): PropertyDetailViewModelFactory {
        val dataSourceProperty = providePropertyDataSource(context)
        val dataSourceAddress = provideAddressDataSource(context)
        val dataSourcePictures = providePictureDataSource(context)
        return PropertyDetailViewModelFactory(
            dataSourceProperty,
            dataSourceAddress,
            dataSourcePictures,
            property_id
        )
    }

    fun provideAddUpdateViewModelFactory(context: Context): PropertyAddUpdateViewModelFactory {
        val dataSourceProperty = providePropertyDataSource(context)
        val dataSourceAddress = provideAddressDataSource(context)
        val dataSourcePictures = providePictureDataSource(context)
        val executor = provideExecutor()
        return PropertyAddUpdateViewModelFactory(
            dataSourceProperty,
            dataSourceAddress,
            dataSourcePictures,
            executor
        )
    }

    fun provideMapViewModelFactory(context: Context): PropertyMapViewModelFactory {
        val dataSourceProperty = providePropertyDataSource(context)
        val dataSourceAddress = provideAddressDataSource(context)
        val dataSourcePictures = providePictureDataSource(context)
        return PropertyMapViewModelFactory(
            dataSourceProperty,
            dataSourceAddress,
            dataSourcePictures
        )
    }

    fun provideSearchViewModelFactory(context: Context): SearchViewModelFactory {
        val dataSourceProperty = providePropertyDataSource(context)
        val dataSourceAddress= provideAddressDataSource(context)
        val dataSourcePicture = providePictureDataSource(context)
        val executor = provideExecutor()
        return SearchViewModelFactory(
            dataSourceProperty,
            dataSourceAddress,
            dataSourcePicture,
            executor
        )
    }
}