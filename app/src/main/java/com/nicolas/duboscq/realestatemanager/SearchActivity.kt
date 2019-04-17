package com.nicolas.duboscq.realestatemanager

import android.app.DatePickerDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.widget.*
import kotlinx.android.synthetic.main.activity_search.*
import java.util.*
import android.graphics.Paint.UNDERLINE_TEXT_FLAG
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.simple_spinner_item.view.*


class SearchActivity : AppCompatActivity(){

    private val searchType = arrayOf("Aucun", "Appartement", "Maison", "Duplex", "Penthouse")
    private val searchPrice = arrayOf("0", "10000", "50000", "100000", "500000", "1000000", "2000000")
    private val searchSurface = arrayOf("0", "50", "100", "150", "200", "250", "300")
    private val searchStatus = arrayOf("Aucun", "A Vendre", "Vendu")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        configureToolBar()
        configureAllSpinner()
        configureDatePicker()
        activity_search_btn.setOnClickListener { Toast.makeText(this,activity_search_type_sp.spinner_text.text,Toast.LENGTH_SHORT).show() }
    }

    private fun configureToolBar() {
        setSupportActionBar(toolbar)
        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        return true
    }

    // SPINNER CONFIGURATION
    private fun configureSpinner(idRStringArray: Array<String>,spinner: Spinner){

        val spinnerAdapter = ArrayAdapter<String>(this, R.layout.simple_spinner_item,idRStringArray)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        spinner.adapter = spinnerAdapter
        spinner.onItemSelectedListener
    }

    private fun configureAllSpinner(){
        this.configureSpinner(searchType,activity_search_type_sp)
        this.configureSpinner(searchPrice,activity_search_price_min_sp)
        this.configureSpinner(searchPrice,activity_search_price_max_sp)
        this.configureSpinner(searchSurface,activity_search_surf_min_sp)
        this.configureSpinner(searchSurface,activity_search_surf_max_sp)
        this.configureSpinner(searchStatus,activity_search_status_sp)
    }

    private fun configureDatePicker(){
        underlineEditText(activity_search_date_min_edt)
        underlineEditText(activity_search_date_max_edt)
        activity_search_date_min_edt.setOnClickListener{showDatePicker(activity_search_date_min_edt)}
        activity_search_date_max_edt.setOnClickListener{showDatePicker(activity_search_date_max_edt)}
    }

    private fun showDatePicker(idRDateEditText: TextView){
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

    private fun underlineEditText(idRDateEditText: TextView){
        idRDateEditText.paintFlags = UNDERLINE_TEXT_FLAG
        idRDateEditText.text = ("                          ")
    }
}