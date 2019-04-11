package com.nicolas.duboscq.realestatemanager

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import kotlinx.android.synthetic.main.activity_search.*
import android.support.v7.app.AlertDialog
import android.view.MotionEvent
import android.view.View


class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        configureToolBar()
        activity_search_type_edt.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> showAlertDialog(R.layout.alert_dialog_search_type_selection)
                }

                return v?.onTouchEvent(event) ?: true
            }
        })
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

    private fun showAlertDialog(layout : Int){
        val alert = AlertDialog.Builder(this)
        val inflater = getLayoutInflater()
        val view = inflater.inflate(layout,null)
        alert.setView(view)
        alert.show()
    }

}