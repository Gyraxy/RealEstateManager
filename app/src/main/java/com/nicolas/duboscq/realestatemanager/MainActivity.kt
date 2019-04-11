package com.nicolas.duboscq.realestatemanager

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val listFragment = ListFragment()
    private val mapFragment = MapFragment()
    private lateinit var mode : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (main_activity_frame_layout_detail == null){
            mode = "phone"
        } else mode = "tablet"

        when (mode){
            "phone"-> configureAndShowPhone()
            "tablet"-> configureAndShowTablet()
        }
        configureToolBar()
        openFragment(listFragment)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        when (mode){
            "phone"-> menuInflater.inflate(R.menu.main_activity_toolbar_phone, menu)
            "tablet"-> menuInflater.inflate(R.menu.main_activity_toolbar_tablet, menu)
        }
        return true
    }

    private fun configureToolBar() {
        setSupportActionBar(activity_main_toolbar)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_activity_main_search -> {
                val searchIntent = Intent(this, SearchActivity::class.java)
                startActivity(searchIntent)
            }
            R.id.menu_activity_main_position -> {
                val searchIntent = Intent(this, MapDetailActivity::class.java)
                startActivity(searchIntent)
            }
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_list -> {
                openFragment(listFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_map -> {
                openFragment(mapFragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun openFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.main_activity_frame_layout_list, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun configureAndShowTablet() {
        var detailFragment = supportFragmentManager.findFragmentById(R.id.main_activity_frame_layout_detail)
        if (detailFragment == null && main_activity_frame_layout_detail != null) {
            detailFragment = DetailFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.main_activity_frame_layout_detail, detailFragment)
                .commit()
        }
    }

    private fun configureAndShowPhone(){
        bottom_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }
}
