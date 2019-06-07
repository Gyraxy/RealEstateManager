package com.nicolas.duboscq.realestatemanager.controllers.activities

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.textfield.TextInputEditText
import com.nicolas.duboscq.realestatemanager.BR.viewmodel
import com.nicolas.duboscq.realestatemanager.databinding.ActivitySearchBinding
import com.nicolas.duboscq.realestatemanager.utils.Injection
import com.nicolas.duboscq.realestatemanager.viewmodels.LoanSimulationViewModel
import com.nicolas.duboscq.realestatemanager.viewmodels.SearchViewModel
import java.text.SimpleDateFormat


class SearchActivity : AppCompatActivity() {

    private val AUTOCOMPLETE_REQUEST_CODE = 1
    private val fields = asList(Place.Field.ID, Place.Field.NAME)
    private lateinit var viewModel:SearchViewModel
    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val factory = Injection.provideSearchViewModelFactory(this)

        viewModel = ViewModelProviders.of(this,factory).get(SearchViewModel::class.java)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_search)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this@SearchActivity

        configureToolBar()
        configureAllSpinner()
        configureDatePicker()
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, "AIzaSyDE8xBMmrWat5ugUmWhLANHGDui3ngNJjI")
        }
        binding.autocompleteclicklistener = View.OnClickListener { autocompletePlace() }

        viewModel.listResult.observe(this,androidx.lifecycle.Observer {
            if (!it.isNullOrEmpty()){
                Log.i("PropertySearch",it.size.toString())
            }
        })
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

    //SPINNER CONFIGURATION
    private fun configureAllSpinner(){
        binding.typeclicklistener = View.OnClickListener{ this.displayPopupMenu(resources.getStringArray(R.array.type_spinner), activity_search_type_spinner) }
        binding.statusclicklistener = View.OnClickListener { this.displayPopupMenu(resources.getStringArray(R.array.status_spinner), activity_search_status_spinner)  }
    }

    private fun displayPopupMenu(listToDisplay: Array<String>, view: TextInputEditText) {
        val popupMenu = PopupMenu(this, view)
        (0 until listToDisplay.size).forEach { it ->
            popupMenu.menu.add(Menu.NONE, it, it, listToDisplay[it])
            popupMenu.setOnMenuItemClickListener { view.setText(it.title);true }
        }
        popupMenu.show()
    }

    //DATE PICKER
    private fun configureDatePicker() {
        activity_search_date_min_edt.setOnClickListener { showDatePicker(activity_search_date_min_edt) }
        activity_search_date_max_edt.setOnClickListener { showDatePicker(activity_search_date_max_edt) }
    }

    private fun showDatePicker(idRDateEditText: TextView) {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)
        val c = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR)
        val mMonth = c.get(Calendar.MONTH)
        val mDay = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val date = sdf.format(Date(year - 1900, monthOfYear, dayOfMonth))
            idRDateEditText.text = date
        }, mYear, mMonth, mDay)
        dpd.show()
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