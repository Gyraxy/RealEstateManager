package com.nicolas.duboscq.realestatemanager.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.sqlite.db.SimpleSQLiteQuery
import com.nicolas.duboscq.realestatemanager.models.Address
import com.nicolas.duboscq.realestatemanager.models.Property
import com.nicolas.duboscq.realestatemanager.repositories.AddressRepository
import com.nicolas.duboscq.realestatemanager.repositories.PropertyRepository
import java.util.concurrent.Executor

class SearchViewModel(
    private val propertyDataSource: PropertyRepository,
    private val addressDataSource: AddressRepository,
    private val executor: Executor
) : ViewModel(){

    private var query = "Select * FROM Property"
    private var args = arrayListOf<Any>()

    var priceMin : MutableLiveData<String> = MutableLiveData("")
    var priceMax : MutableLiveData<String> = MutableLiveData("")
    var roomMin : MutableLiveData<String> = MutableLiveData("")
    var roomMax : MutableLiveData<String> = MutableLiveData("")
    var surfaceMin : MutableLiveData<String> = MutableLiveData("")
    var surfaceMax : MutableLiveData<String> = MutableLiveData("")
    var bathroomMin : MutableLiveData<String> = MutableLiveData("")
    var bathroomMax : MutableLiveData<String> = MutableLiveData("")
    var bedroomMin : MutableLiveData<String> = MutableLiveData("")
    var bedroomMax : MutableLiveData<String> = MutableLiveData("")
    var pictureMin : MutableLiveData<String> = MutableLiveData("")
    var pictureMax : MutableLiveData<String> = MutableLiveData("")
    var dateEntryMin : MutableLiveData<String> = MutableLiveData("")
    var dateEntryMax : MutableLiveData<String> = MutableLiveData("")
    var dateSoldMin : MutableLiveData<String> = MutableLiveData("")
    var dateSoldMax : MutableLiveData<String> = MutableLiveData("")
    var localisation : MutableLiveData<String> = MutableLiveData("")
    var type: MutableLiveData<String> = MutableLiveData("")
    var status: MutableLiveData<String> = MutableLiveData("")

    var toast:MutableLiveData<Boolean> = MutableLiveData(false)

    var propertyListResult : MutableLiveData<MutableList<Property>> = MutableLiveData(mutableListOf())
    var addressListResult : MutableLiveData<MutableList<Address>> = MutableLiveData(mutableListOf())

    fun findProperty(){

        query = "Select * FROM Property"
        args = arrayListOf()
        var conditions = false

        if (!type.value.equals("")) {
            query += " WHERE type = :${type.value}"
            args.add(type.value!!)
            conditions = true
        }

        if (!status.value.equals("")){
            query += if (conditions) " AND " else " WHERE "; conditions = true
            query += "status = :${status.value}"
            args.add(status.value!!)
        }

        if (!priceMin.value.equals("")) {
            query += if (conditions) " AND " else " WHERE "; conditions = true
            query += "price >= :${priceMin.value!!.toInt()}"
            args.add(priceMin.value!!.toInt())
            Log.i("PropertyPriceMin", priceMin.value)
        }

        if (!priceMax.value.equals("")) {
            query += if (conditions) " AND " else " WHERE "; conditions = true
            query += "price <= :${priceMax.value!!.toInt()}"
            args.add(priceMax.value!!.toInt())
            Log.i("PropertyPriceMax", priceMax.value)
        }

        if (!roomMin.value.equals("")) {
            query += if (conditions) " AND " else " WHERE "; conditions = true
            query += "room >= :${roomMin.value!!.toInt()}"
            args.add(roomMin.value!!.toInt())
            Log.i("PropertyRoomMin", roomMin.value)
        }

        if (!roomMax.value.equals("")) {
            query += if (conditions) " AND " else " WHERE "; conditions = true
            query += "room <= :${roomMax.value!!.toInt()}"
            args.add(roomMax.value!!.toInt())
            Log.i("PropertyRoomMax", roomMax.value)
        }

        if (!bedroomMin.value.equals("")) {
            query += if (conditions) " AND " else " WHERE "; conditions = true
            query += "bedroom >= :${bedroomMin.value!!.toInt()}"
            args.add(bedroomMin.value!!.toInt())
            Log.i("PropertyBedroomMin", bedroomMin.value)
        }

        if (!bedroomMax.value.equals("")) {
            query += if (conditions) " AND " else " WHERE "; conditions = true
            query += "bedroom <= :${bedroomMax.value!!.toInt()}"
            args.add(bedroomMax.value!!.toInt())
            Log.i("PropertyBedroomMax", bedroomMax.value)
        }

        if (!bathroomMin.value.equals("")) {
            query += if (conditions) " AND " else " WHERE "; conditions = true
            query += "bathroom >= :${bathroomMin.value!!.toInt()}"
            args.add(bathroomMin.value!!.toInt())
            Log.i("PropertyBathroomMin", bathroomMin.value)
        }

        if (!bathroomMax.value.equals("")) {
            query += if (conditions) " AND " else " WHERE "; conditions = true
            query += "bathroom <= :${bathroomMax.value!!.toInt()}"
            args.add(bathroomMax.value!!.toInt())
            Log.i("PropertyBathroomMax", bathroomMax.value)
        }

        if (!surfaceMin.value.equals("")) {
            query += if (conditions) " AND " else " WHERE "; conditions = true
            query += "surface >= :${surfaceMin.value!!.toInt()}"
            args.add(surfaceMin.value!!.toInt())
            Log.i("PropertySurfaceMin", surfaceMin.value)
        }

        if (!surfaceMax.value.equals("")) {
            query += if (conditions) " AND " else " WHERE "; conditions = true
            query += "surface <= :${surfaceMax.value!!.toInt()}"
            args.add(surfaceMax.value!!.toInt())
            Log.i("PropertySurfaceMax", surfaceMax.value)
        }

        executor.execute {
            val querySQL = SimpleSQLiteQuery(query, args.toArray())
            val list = propertyDataSource.getPropertyBySearch(querySQL)
            updateSearchlist(list)
        }
    }

    private fun updateSearchlist(list:MutableList<Property>){
        propertyListResult.postValue(list)
    }

    fun getAddressList(list:MutableList<Property>){

    }
}