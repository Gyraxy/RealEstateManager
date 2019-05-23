package com.nicolas.duboscq.realestatemanager.viewmodels

import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nicolas.duboscq.realestatemanager.models.Address
import com.nicolas.duboscq.realestatemanager.models.Picture
import com.nicolas.duboscq.realestatemanager.models.Property
import com.nicolas.duboscq.realestatemanager.repositories.AddressRepository
import com.nicolas.duboscq.realestatemanager.repositories.PictureRepository
import com.nicolas.duboscq.realestatemanager.repositories.PropertyRepository
import java.util.concurrent.Executor
import android.widget.AdapterView
import com.nicolas.duboscq.realestatemanager.utils.Notifications
import com.nicolas.duboscq.realestatemanager.utils.Utils

class PropertyAddUpdateViewModel (
    private val propertyDataSource: PropertyRepository,
    private val addressDataSource : AddressRepository,
    private val pictureDataSource: PictureRepository,
    private val executor: Executor
): ViewModel() {

    var property = MutableLiveData<Property>(Property("",0,0,0,0,0,"","", Utils.getTodayDate()))
    var address = MutableLiveData<Address>(Address(0,"","","","","",0.0,0.0))
    var picture = MutableLiveData<Picture>(Picture(0,"","",0))

    var pictureLinkList = MutableLiveData<MutableList<String>>(mutableListOf())
    var pictureDescriptionList = MutableLiveData<MutableList<String>>(mutableListOf())

    var _pictureLinkList: MutableList<String> = mutableListOf()
    var _pictureDescriptionList: MutableList<String> = mutableListOf()

    var toast = MutableLiveData<Boolean>(false)

    fun createPropertyandAddress(context: Context){
        if (!property.value!!.equals(Property("",0,0,0,0,0,"","", Utils.getTodayDate())) &&
                !pictureLinkList.value!!.size.equals(0) &&
                !pictureDescriptionList.value!!.size.equals(0) &&
                !address.value!!.city.equals(0) &&
                !address.value!!.zipcode.equals(0) &&
                !address.value!!.streetName.equals(0) &&
                !address.value!!.streetNumber.equals(0) &&
                !address.value!!.country.equals(0)){

            executor.execute {
                val id = propertyDataSource.createProperty(property.value!!)

                address.value!!.propertyId = id

                val strAddress = "${address.value!!.streetNumber} ${address.value!!.streetName} ${address.value!!.zipcode} ${address.value!!.city} ${address.value!!.country}"

                address.value!!.lat = Utils.getLocationFromAddress(context,strAddress).latitude
                address.value!!.lng = Utils.getLocationFromAddress(context,strAddress).longitude
                addressDataSource.createAddress(address.value!!)

                for (i in 0..pictureLinkList.value!!.size-1){
                    val picture =
                        Picture(id, pictureLinkList.value!!.get(i), pictureDescriptionList.value!!.get(i), i)
                    pictureDataSource.createPicture(picture)
                }
            }
            Notifications().sendNotification(context,"create")
            onClearPropertyAddUpdateViewModel()
        }
        else {
            toast.value = true
            toast.value = false
        }
    }

    fun onClearPropertyAddUpdateViewModel(){
        property.value = Property("",0,0,0,0,0,"","", Utils.getTodayDate())
        address.value = Address(0,"","","","","",0.0,0.0)
        _pictureLinkList.clear()
        _pictureDescriptionList.clear()
        pictureLinkList.value = _pictureLinkList
        pictureDescriptionList.value = _pictureDescriptionList
        picture.value = Picture(0,"","",0)
    }

    fun addPictureLinkToList(pictureLink: String){
        _pictureLinkList.add(pictureLink)
        pictureLinkList.value = _pictureLinkList
        Log.i("PictureVM",pictureLinkList.value?.size.toString())
    }

    fun addPictureDescriptionToList(pictureDescription: String){
        _pictureDescriptionList.add(pictureDescription)
        pictureDescriptionList.value = _pictureDescriptionList
    }

    fun removePictureLinkToList(position: Int){
        _pictureLinkList.removeAt(position)
        pictureLinkList.value = _pictureLinkList

    }

    fun removePictureDescriptionToList(position: Int){
        _pictureDescriptionList.removeAt(position)
        pictureDescriptionList.value = _pictureDescriptionList

    }

    fun onStatusSelectItem(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
        property.value?.status = parent.selectedItem.toString()
    }

    fun onTypeSelectItem(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
        property.value?.type = parent.selectedItem.toString()
    }
}