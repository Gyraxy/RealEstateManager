package com.nicolas.duboscq.realestatemanager.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nicolas.duboscq.realestatemanager.repositories.PropertyRepository
import java.util.concurrent.Executor

class SearchViewModel(
    private val propertyDataSource: PropertyRepository,
    private val executor: Executor
) : ViewModel(){

    private var query = "Select * FROM Property"
    private var args = arrayListOf<Any>()

    var priceMin : MutableLiveData<Int> = MutableLiveData(0)
    var priceMax : MutableLiveData<Int> = MutableLiveData(0)
    var roomMin : MutableLiveData<Int> = MutableLiveData(0)
    var roomMax : MutableLiveData<Int> = MutableLiveData(0)
    var surfaceMin : MutableLiveData<Int> = MutableLiveData(0)
    var surfaceMax : MutableLiveData<Int> = MutableLiveData(0)
    var bathroomMin : MutableLiveData<Int> = MutableLiveData(0)
    var bathroomMax : MutableLiveData<Int> = MutableLiveData(0)
    var bedroomMin : MutableLiveData<Int> = MutableLiveData(0)
    var bedroomMax : MutableLiveData<Int> = MutableLiveData(0)
    var pictureMin : MutableLiveData<Int> = MutableLiveData(0)
    var pictureMax : MutableLiveData<Int> = MutableLiveData(0)
    var dateMin : MutableLiveData<String> = MutableLiveData("")
    var dateMax : MutableLiveData<String> = MutableLiveData("")
    val localisation : MutableLiveData<String> = MutableLiveData("")
    val type: MutableLiveData<String> = MutableLiveData("")
    val status: MutableLiveData<String> = MutableLiveData("")

    fun findProperty(){
        query = "Select * FROM Property"
        args = arrayListOf<Any>()
        var conditions = false

        if (!type.value.equals("")) {
            query += " WHERE type = :${type.value}"
            args.add(type.value!!)
            conditions = true
        }

        query += if (conditions) " AND pictureList >= ?" else " WHERE pictureList >= ?"


    }
}