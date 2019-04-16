package com.nicolas.duboscq.realestatemanager

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        configureToolBar()
        configureAllSpinner()
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
    private fun configureSpinner(idRStringArray: Int,spinner: Spinner){

        val adapter = ArrayAdapter.createFromResource(this, idRStringArray, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        spinner.setAdapter(adapter)
        spinner.onItemSelectedListener
    }

    private fun configureAllSpinner(){
        this.configureSpinner(R.array.search_type,activity_search_type_sp)
        this.configureSpinner(R.array.search_price,activity_search_price_min_sp)
        this.configureSpinner(R.array.search_price,activity_search_price_max_sp)
        this.configureSpinner(R.array.search_surface,activity_search_surf_min_sp)
        this.configureSpinner(R.array.search_surface,activity_search_surf_max_sp)
        this.configureSpinner(R.array.search_status,activity_search_status_sp)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            when(parent!!.id) {
            }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}