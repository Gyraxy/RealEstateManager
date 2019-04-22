package com.nicolas.duboscq.realestatemanager.controllers.activities

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import com.nicolas.duboscq.realestatemanager.R

import kotlinx.android.synthetic.main.activity_edit_update.*
import kotlinx.android.synthetic.main.activity_edit_update.toolbar
import kotlinx.android.synthetic.main.activity_search.*

class EditUpdateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_update)
        configureToolBar()
    }


    //TOOLBAR CONFIGURATION
    private fun configureToolBar() {
        setSupportActionBar(toolbar)
        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(true)
    }
}
