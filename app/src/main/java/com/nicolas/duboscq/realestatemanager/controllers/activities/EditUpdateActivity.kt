package com.nicolas.duboscq.realestatemanager.controllers.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.nicolas.duboscq.realestatemanager.R
import com.nicolas.duboscq.realestatemanager.models.Property
import com.nicolas.duboscq.realestatemanager.models.PropertyViewModel

import kotlinx.android.synthetic.main.activity_edit_update.*
import kotlinx.android.synthetic.main.activity_edit_update.toolbar
import kotlinx.android.synthetic.main.simple_spinner_item.view.*

class EditUpdateActivity : AppCompatActivity() {

    private val editType = arrayOf("Type", "Appartement", "Maison", "Duplex", "Penthouse")
    private val editStatus = arrayOf("Status", "A Vendre", "Vendu")
    private lateinit var propertyViewModel: PropertyViewModel

    // PROPERTY INFORMATION
    private var status :String = ""
    private var description :String = ""
    private var type:String = ""
    private var surface :Int = 0
    private var price :Int = 0
    private var room :Int = 0
    private var bedroom :Int = 0
    private var bathroom :Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_update)
        configureToolBar()
        configureAllSpinner()
        propertyViewModel = ViewModelProviders.of(this).get(PropertyViewModel::class.java)
        propertyViewModel.allProperty.observe(this, Observer { })
        activity_search_fl_btn.setOnClickListener {
            getAllPropertyInfo()
           propertyViewModel.insert(Property(status,price,surface,room,bedroom,bathroom,description,type))
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

    // GET INFORMATION
    private fun getAllPropertyInfo(){
        price = activity_edit_update_price_edt.text.toString().toInt()
        room = activity_edit_update_room_edt.text.toString().toInt()
        bedroom = activity_edit_update_bedroom_edt.text.toString().toInt()
        bathroom = activity_edit_update_bathroom_edt.text.toString().toInt()
        surface = activity_edit_update_surface_edt.text.toString().toInt()
        description = activity_edit_update_description_edt.text.toString()
        type = activity_edit_update_type_sp.spinner_text.toString()
        status = activity_edit_update_status_sp.spinner_text.toString()
    }
}
