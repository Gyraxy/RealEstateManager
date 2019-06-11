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

    var property = MutableLiveData<Property>(Property("","A Vendre",0,0,0,0,0,"","","","","", Utils.getTodayDate()," "))
    var address = MutableLiveData<Address>(Address(0,"","","","",""))
    var picture = MutableLiveData<Picture>(Picture(0,"","",0))

    var pictureLinkList = MutableLiveData<MutableList<String>>(mutableListOf())
    var pictureDescriptionList = MutableLiveData<MutableList<String>>(mutableListOf())

    var pictureLinkListVM: MutableList<String> = mutableListOf()
    var pictureDescriptionListVM: MutableList<String> = mutableListOf()

    var toastMissingInfo = MutableLiveData<Boolean>(false)

    fun createPropertyandAddress(context: Context){
        if (canSaveToDataBase()){

            executor.execute {
                val id = propertyDataSource.createProperty(property.value!!)

                address.value!!.propertyId = id
                addressDataSource.createAddress(address.value!!)

                for (i in 0..pictureLinkList.value!!.size-1){
                    val picture =
                        Picture(id, pictureLinkList.value!![i], pictureDescriptionList.value!![i], i)
                    pictureDataSource.createPicture(picture)
                }
            }
            Notifications().sendNotification(context,"create")
            onClearPropertyAddUpdateViewModel()
        }
        else {
            toastMissingInfo.value = true
            toastMissingInfo.value = false
        }
    }

    fun updatePropertyById(propId:Int,context: Context){
        if (canSaveToDataBase()){
            executor.execute {
                if (!property.value!!.date_sold.equals("")){
                    property.value!!.status = "Vendu"
                }
                propertyDataSource.updatePropertyById(propId,
                    property.value!!.agent,
                    property.value!!.status,
                    property.value!!.price,
                    property.value!!.surface,
                    property.value!!.room,
                    property.value!!.bedroom,
                    property.value!!.bathroom,
                    property.value!!.description,
                    property.value!!.type,
                    property.value!!.points_interest,
                    property.value!!.date_entry,
                    property.value!!.date_sold,
                    property.value!!.dateModified)
                addressDataSource.updateAddressByPropId(propId,address.value!!.streetNumber,
                    address.value!!.streetName,
                    address.value!!.zipcode,
                    address.value!!.city,
                    address.value!!.country)
                pictureDataSource.delPropertyById(propId)

                for (i in 0..pictureLinkList.value!!.size-1){
                    val picture =
                        Picture(propId.toLong(), pictureLinkList.value!![i], pictureDescriptionList.value!![i], i)
                    pictureDataSource.createPicture(picture)
                }
            }
            Notifications().sendNotification(context,"edit")
        }
        else {
            toastMissingInfo.value = true
            toastMissingInfo.value = false
        }
    }

    private fun onClearPropertyAddUpdateViewModel(){
        property.value = Property("","A Vendre",0,0,0,0,0,"","","","","", Utils.getTodayDate()," ")
        address.value = Address(0,"","","","","")
        pictureLinkListVM.clear()
        pictureDescriptionListVM.clear()
        pictureLinkList.value = pictureLinkListVM
        pictureDescriptionList.value = pictureDescriptionListVM
        picture.value = Picture(0,"","",0)
    }

    fun addPictureLinkToList(pictureLink: String){
        pictureLinkListVM.add(pictureLink)
        pictureLinkList.value = pictureLinkListVM
    }

    fun addPictureDescriptionToList(pictureDescription: String){
        pictureDescriptionListVM.add(pictureDescription)
        pictureDescriptionList.value = pictureDescriptionListVM
    }

    fun removePictureLinkToList(position: Int){
        pictureLinkListVM.removeAt(position)
        pictureLinkList.value = pictureLinkListVM

    }

    fun removePictureDescriptionToList(position: Int){
        pictureDescriptionListVM.removeAt(position)
        pictureDescriptionList.value = pictureDescriptionListVM

    }

    private fun canSaveToDataBase():Boolean{
        if (!property.value!!.agent.equals("") &&
            !property.value!!.date_entry.equals("") &&
            !property.value!!.price.equals(0) &&
            !property.value!!.surface.equals(0) &&
            !property.value!!.room.equals(0) &&
            !property.value!!.bedroom.equals(0) &&
            !property.value!!.bathroom.equals(0) &&
            !property.value!!.description.equals("") &&
            !property.value!!.type.equals("")&&
            !property.value!!.points_interest.equals("")&&
            !pictureLinkList.value!!.size.equals(0) &&
            !pictureDescriptionList.value!!.size.equals(0) &&
            !address.value!!.city.equals("") &&
            !address.value!!.zipcode.equals("") &&
            !address.value!!.streetName.equals("") &&
            !address.value!!.streetNumber.equals("") &&
            !address.value!!.country.equals(""))
        {
            return true
        }
        else return false
    }
}