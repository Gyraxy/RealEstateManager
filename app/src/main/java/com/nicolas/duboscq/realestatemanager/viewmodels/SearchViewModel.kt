package com.nicolas.duboscq.realestatemanager.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.sqlite.db.SimpleSQLiteQuery
import com.nicolas.duboscq.realestatemanager.models.Address
import com.nicolas.duboscq.realestatemanager.models.Picture
import com.nicolas.duboscq.realestatemanager.models.Property
import com.nicolas.duboscq.realestatemanager.repositories.AddressRepository
import com.nicolas.duboscq.realestatemanager.repositories.PictureRepository
import com.nicolas.duboscq.realestatemanager.repositories.PropertyRepository
import java.util.*
import java.util.concurrent.Executor

class SearchViewModel(
    private val propertyDataSource: PropertyRepository,
    private val addressDataSource: AddressRepository,
    private val pictureDataSource: PictureRepository,
    private val executor: Executor
) : ViewModel(){

    //Query for SQLLite Query
    private var query = "Select * FROM Property"
    //Argument for SQLLite Query
    private var args = arrayListOf<Any>()

    //Search Values
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
    var dateEntryMin : MutableLiveData<Date?> = MutableLiveData(null)
    var dateEntryMax : MutableLiveData<Date?> = MutableLiveData(null)
    var dateSoldMin : MutableLiveData<Date?> = MutableLiveData(null)
    var dateSoldMax : MutableLiveData<Date?> = MutableLiveData(null)
    var localisation : MutableLiveData<String> = MutableLiveData("")
    var type: MutableLiveData<String> = MutableLiveData("")
    var status: MutableLiveData<String> = MutableLiveData("")
    var pointOfInterest: MutableLiveData<String> = MutableLiveData("")

    //Toast value to show toast if Date not well taken or ValueMin>ValueMax
    var toastValue:MutableLiveData<Boolean> = MutableLiveData(false)
    var toastDate:MutableLiveData<Boolean> = MutableLiveData(false)

    //For Toast No Search Resul found
    var message:MutableLiveData<String> = MutableLiveData("")

    //Property,Address,Picture Result
    var propertyListResult : MutableLiveData<MutableList<Property>> = MutableLiveData(mutableListOf())
    var addressListResult : MutableLiveData<MutableList<Address>> = MutableLiveData(mutableListOf())
    var pictureListResult : MutableLiveData<MutableList<Picture>> = MutableLiveData(mutableListOf())

    //Method to find Property according to Search Arguments
    fun findProperty(){

        if (checkMinValueLowerMaxValue()){
            if (checkDateMinBeforeDateMax()) {
                query = "Select * FROM Address a JOIN Picture pi ON (a.property_id = pi.property_id) JOIN Property p ON (a.property_id=p.id) WHERE picture_property_number=0"
                args = arrayListOf()

                if (!type.value.equals("")) {
                    query += " AND type = :type"
                    args.add(type.value!!)
                }

                if (!status.value.equals("")){
                    query += " AND status = :status"
                    args.add(status.value!!)
                }

                if (!priceMin.value.equals("")) {
                    query += " AND price >= :priceMin"
                    args.add(priceMin.value!!.toInt())
                    Log.i("PropertyPriceMin", priceMin.value)
                }

                if (!priceMax.value.equals("")) {
                    query += " AND price <= :priceMax"
                    args.add(priceMax.value!!.toInt())
                    Log.i("PropertyPriceMax", priceMax.value)
                }

                if (!roomMin.value.equals("")) {
                    query += " AND room >= :roomMin"
                    args.add(roomMin.value!!.toInt())
                    Log.i("PropertyRoomMin", roomMin.value)
                }

                if (!roomMax.value.equals("")) {
                    query += " AND room <= :roomMax"
                    args.add(roomMax.value!!.toInt())
                    Log.i("PropertyRoomMax", roomMax.value)
                }

                if (!bedroomMin.value.equals("")) {
                    query += " AND bedroom >= :bedroomMin"
                    args.add(bedroomMin.value!!.toInt())
                    Log.i("PropertyBedroomMin", bedroomMin.value)
                }

                if (!bedroomMax.value.equals("")) {
                    query += " AND bedroom <= :bedroomMax"
                    args.add(bedroomMax.value!!.toInt())
                    Log.i("PropertyBedroomMax", bedroomMax.value)
                }

                if (!bathroomMin.value.equals("")) {
                    query += " AND bathroom >= :bathroomMin"
                    args.add(bathroomMin.value!!.toInt())
                    Log.i("PropertyBathroomMin", bathroomMin.value)
                }

                if (!bathroomMax.value.equals("")) {
                    query += " AND bathroom <= :bathroomMax"
                    args.add(bathroomMax.value!!.toInt())
                    Log.i("PropertyBathroomMax", bathroomMax.value)
                }

                if (!surfaceMin.value.equals("")) {
                    query += " AND surface >= :surfaceMin"
                    args.add(surfaceMin.value!!.toInt())
                    Log.i("PropertySurfaceMin", surfaceMin.value)
                }

                if (!surfaceMax.value.equals("")) {
                    query += " AND surface <= :surfaceMax"
                    args.add(surfaceMax.value!!.toInt())
                    Log.i("PropertySurfaceMax", surfaceMax.value)
                }

                if (!pictureMin.value.equals("")) {
                    query += " AND nb_of_pictures >= :pictureMin"
                    args.add(pictureMin.value!!.toInt())
                    Log.i("PropertyPictureMin", pictureMin.value)
                }

                if (!pictureMax.value.equals("")) {
                    query += " AND p.nb_of_pictures <= :pictureMax"
                    args.add(pictureMax.value!!.toInt())
                    Log.i("PropertyPictureMax", pictureMax.value)
                }

                if (!pointOfInterest.value.equals("")){
                    query += " AND p.points_of_interest = :pointOfInterest"
                    args.add(pointOfInterest.value!!)
                }

                if (!localisation.value.equals("")){
                    query += " AND city = :localisation"
                    args.add(localisation.value!!)
                }

                if (dateEntryMin.value != null){
                    query += " AND date_entry >= :dateEntryMin"
                    args.add(dateEntryMin.value!!.time)
                    Log.i("PropertyDateEntry",dateEntryMin.value.toString())
                }

                if (dateEntryMax.value != null){
                    query += " AND date_entry <= :dateEntryMax"
                    args.add(dateEntryMax.value!!.time)
                    Log.i("PropertyDateEntry",dateEntryMax.value.toString())
                }

                if (dateSoldMin.value != null){
                    query += " AND date_entry >= :dateSoldMin"
                    args.add(dateSoldMin.value!!.time)
                    Log.i("PropertyDateEntry",dateSoldMin.value.toString())
                }

                if (dateSoldMax.value != null){
                    query += " AND date_entry <= :dateSoldMax"
                    args.add(dateSoldMax.value!!.time)
                    Log.i("PropertyDateEntry",dateSoldMax.value.toString())
                }

                executor.execute {
                        val querySQL = SimpleSQLiteQuery(query, args.toArray())
                        val listProperty = propertyDataSource.getPropertyBySearch(querySQL)
                        updatePropertySearchlist(listProperty)
                        val listAddress= addressDataSource.getAddressBySearch(querySQL)
                        updateAddressSearchList(listAddress)
                        val listPicture = pictureDataSource.getPictureBySearch(querySQL)
                        updatePictureSearchList(listPicture)
                        updateMessage(listProperty)
                    }
                }
            else {
                toastDate.value = true
                toastDate.value = false
            }
        }
        else {
            toastValue.value = true
            toastValue.value = false
        }
    }

    //Methods to update PropertyList/AddressList & PictureList
    private fun updatePropertySearchlist(list:MutableList<Property>){
        propertyListResult.postValue(list)
    }

    private fun updateAddressSearchList(list:MutableList<Address>){
        addressListResult.postValue(list)
    }

    private fun updatePictureSearchList(list:MutableList<Picture>){
        pictureListResult.postValue(list)
    }

    //Method to show Message if no result found
    private fun updateMessage(list:MutableList<Property>){
        if (list.isEmpty()){
            message.postValue("empty")
        }
    }

    //Method to check that Min value < Max Value
    private fun checkMinValueLowerMaxValue():Boolean{
        if (!priceMin.value.equals("")&&!priceMax.value.equals("")){
            return (priceMin.value!!.toInt())<(priceMax.value!!.toInt())
        }
        else if (!surfaceMin.value.equals("")&&!surfaceMax.value.equals("")){
            return (surfaceMin.value!!.toInt())<(surfaceMax.value!!.toInt())
        }
        else if (!roomMin.value.equals("")&&!roomMax.value.equals("")){
            return (roomMin.value!!.toInt())<(roomMax.value!!.toInt())
        }
        else if (!bedroomMin.value.equals("")&&!bedroomMax.value.equals("")){
            return (bedroomMin.value!!.toInt())<(bedroomMax.value!!.toInt())
        }
        else if (!pictureMin.value.equals("")&&!pictureMax.value.equals("")){
            return (pictureMin.value!!.toInt())<(pictureMax.value!!.toInt())
        }
        else if (!bedroomMin.value.equals("")&&!bedroomMax.value.equals("")){
            return (bedroomMin.value!!.toInt())<(bedroomMax.value!!.toInt())
        }
        else return true
    }

    //Method to check MinDate before MaxDate
    private fun checkDateMinBeforeDateMax(): Boolean{
        if (dateEntryMin.value!=null &&dateEntryMax.value!=null){
            return (dateEntryMin.value!!.before(dateEntryMax.value))
        }
        else if (dateSoldMin.value!=null &&dateSoldMax.value!=null){
            return (dateSoldMin.value!!.before(dateSoldMax.value))
        }
        else return true
    }
}