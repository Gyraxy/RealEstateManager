package com.nicolas.duboscq.realestatemanager.controllers.activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.widget.*
import kotlinx.android.synthetic.main.activity_search.*
import java.util.*
import android.graphics.Paint.UNDERLINE_TEXT_FLAG
import android.widget.ArrayAdapter
import com.google.android.libraries.places.api.Places
import com.nicolas.duboscq.realestatemanager.R
import java.util.Arrays.asList
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import android.content.Intent
import android.app.Activity
import android.util.Log


class SearchActivity : AppCompatActivity() {

    private val AUTOCOMPLETE_REQUEST_CODE = 1
    private val fields = asList(Place.Field.ID, Place.Field.NAME)
    private val searchType = arrayOf("Type", "Appartement", "Maison", "Duplex", "Penthouse")
    private val searchStatus = arrayOf("Status", "A Vendre", "Vendu")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        configureToolBar()
        configureAllSpinner()
        configureDatePicker()
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, "AIzaSyDE8xBMmrWat5ugUmWhLANHGDui3ngNJjI")
        }
        activity_search_place_edt.setOnClickListener { autocompletePlace() }
    }

    //TOOLBAR CONFIGURATION
    private fun configureToolBar() {
        setSupportActionBar(toolbar)
        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(true)
    }

    //MENU CONFIGURATION
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        return true
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
        this.configureSpinner(searchType, activity_search_type_sp)
        this.configureSpinner(searchStatus, activity_search_status_sp)
    }

    //DATE PICKER
    private fun configureDatePicker() {
        activity_search_date_min_edt.setOnClickListener { showDatePicker(activity_search_date_min_edt) }
        activity_search_date_max_edt.setOnClickListener { showDatePicker(activity_search_date_max_edt) }
    }

    private fun showDatePicker(idRDateEditText: TextView) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            val date = "$dayOfMonth/$month/$year"
            idRDateEditText.text = date
        }, year, month, day)
        dpd.show()
    }

    private fun underlineEditText(idRDateEditText: TextView) {
        idRDateEditText.paintFlags = UNDERLINE_TEXT_FLAG
        idRDateEditText.text = ("                          ")
    }

    //AUTOCOMPLETE PLACE
    private fun autocompletePlace() {
        val intent = Autocomplete.IntentBuilder(
            AutocompleteActivityMode.FULLSCREEN, fields
        )
            .build(this)
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data!!)
                activity_search_place_edt.text = place.name
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                val status = Autocomplete.getStatusFromIntent(data!!)
                Log.i("Test", status.statusMessage)
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }
}