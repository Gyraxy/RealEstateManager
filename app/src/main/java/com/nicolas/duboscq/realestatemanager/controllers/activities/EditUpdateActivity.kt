package com.nicolas.duboscq.realestatemanager.controllers.activities

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.nicolas.duboscq.realestatemanager.models.Property
import com.nicolas.duboscq.realestatemanager.models.PropertyViewModel
import com.nicolas.duboscq.realestatemanager.utils.Utils

import kotlinx.android.synthetic.main.activity_edit_update.*
import kotlinx.android.synthetic.main.activity_edit_update.toolbar
import kotlinx.android.synthetic.main.simple_spinner_item.view.*
import com.nicolas.duboscq.realestatemanager.R
import com.nicolas.duboscq.realestatemanager.injections.Injection
import com.nicolas.duboscq.realestatemanager.models.Address

class EditUpdateActivity : AppCompatActivity() {

    private val editType = arrayOf(" ", "Appartement", "Maison", "Duplex", "Penthouse")
    private val editStatus = arrayOf(" ", "A Vendre", "Vendu")
    private lateinit var propertyViewModel: PropertyViewModel

    // PROPERTY INFORMATION
    private var status :String = " "
    private var description :String = " "
    private var type:String = " "
    private var surface :Int = 0
    private var price :Int = 0
    private var room :Int = 0
    private var bedroom :Int = 0
    private var bathroom :Int = 0
    private var streetNumber:Int = 0
    private var streetName:String =" "
    private var zipcode:Int = 0
    private var city:String =" "
    private var country:String =" "
    private lateinit var dateCreation : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_update)
        this.configureToolBar()
        this.configureAllSpinner()
        this.configureViewModel()
        activity_search_fl_btn.setOnClickListener {
            this.getAllPropertyInfo()
            this.createProperty()
            this.clearAllEditPropertyInfo()
            this.initData()
        }
    }

    //TOOLBAR CONFIGURATION
    private fun configureToolBar() {
        setSupportActionBar(toolbar)
        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(true)
    }

    // SPINNER CONFIGURATION
    private fun configureSpinner(idRStringArray: Array<String>, spinner: Spinner) {

        val spinnerAdapter = ArrayAdapter<String>(
            this,
            R.layout.simple_spinner_item, idRStringArray
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        spinner.adapter = spinnerAdapter
        spinner.onItemSelectedListener
    }

    private fun configureAllSpinner() {
        this.configureSpinner(editType, activity_edit_update_type_sp)
        this.configureSpinner(editStatus, activity_edit_update_status_sp)
    }

    // DATA
    private fun initData(){
        status =" "; description =" "; type=" "; streetName=" "; city=" "; country=" "
        surface =0; price=0 ; room=0 ; bedroom=0 ; bathroom=0 ; zipcode=0
    }

    private fun getAllPropertyInfo(){
        if (!activity_edit_update_price_edt.text.toString().equals("")) { price = activity_edit_update_price_edt.text.toString().toInt()}
        if (!activity_edit_update_room_edt.text.toString().equals("")) {room = activity_edit_update_room_edt.text.toString().toInt()}
        if (!activity_edit_update_bedroom_edt.text.toString().equals("")){bedroom = activity_edit_update_bedroom_edt.text.toString().toInt()}
        if (!activity_edit_update_bathroom_edt.text.toString().equals("")){bathroom = activity_edit_update_bathroom_edt.text.toString().toInt()}
        if (!activity_edit_update_surface_edt.text.toString().equals("")){surface = activity_edit_update_surface_edt.text.toString().toInt()}
        if (!activity_edit_update_street_nb_edt.text.toString().equals("")){streetNumber = activity_edit_update_street_nb_edt.text.toString().toInt()}
        if (!activity_edit_update_zipcode_edt.text.toString().equals("")){zipcode = activity_edit_update_zipcode_edt.text.toString().toInt()}
        description = activity_edit_update_description_edt.text.toString()
        streetName = activity_edit_update_street_name_edt.text.toString()
        city = activity_edit_update_city_edt.text.toString()
        country = activity_edit_update_country_edt.text.toString()
        dateCreation = Utils.getTodayDate()
        type = activity_edit_update_type_sp.selectedItem.toString()
        status = activity_edit_update_status_sp.selectedItem.toString()
    }

    // CLEAR INFORMATION
    private fun clearAllEditPropertyInfo(){
        activity_edit_update_price_edt.editableText.clear()
        activity_edit_update_room_edt.editableText.clear()
        activity_edit_update_bedroom_edt.editableText.clear()
        activity_edit_update_bathroom_edt.editableText.clear()
        activity_edit_update_surface_edt.editableText.clear()
        activity_edit_update_description_edt.editableText.clear()
        activity_edit_update_street_nb_edt.editableText.clear()
        activity_edit_update_street_name_edt.editableText.clear()
        activity_edit_update_zipcode_edt.editableText.clear()
        activity_edit_update_city_edt.editableText.clear()
        activity_edit_update_country_edt.editableText.clear()
        activity_edit_update_type_sp.setSelection(0)
        activity_edit_update_status_sp.setSelection(0)
    }

    // VIEW MODEL

    private fun configureViewModel(){
        val mViewModelFactory = Injection.provideViewModelFactory(this)
        this.propertyViewModel = ViewModelProviders.of(this, mViewModelFactory).get(PropertyViewModel::class.java!!)
    }

    private fun createProperty() {
        val property = Property(status,price,surface,room,bedroom,bathroom,description,type,dateCreation)
        this.propertyViewModel.createPropertyandAddress(property,streetNumber,streetName,zipcode,city,country)
    }
}
