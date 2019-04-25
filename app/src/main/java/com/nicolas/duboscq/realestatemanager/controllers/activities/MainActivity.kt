package com.nicolas.duboscq.realestatemanager.controllers.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.view.MenuItem
import android.widget.Toast
import com.facebook.stetho.Stetho
import com.nicolas.duboscq.realestatemanager.controllers.fragments.MapFragment
import com.nicolas.duboscq.realestatemanager.R
import com.nicolas.duboscq.realestatemanager.controllers.fragments.DetailFragment
import com.nicolas.duboscq.realestatemanager.controllers.fragments.ListFragment
import com.nicolas.duboscq.realestatemanager.database.AppDatabase
import com.nicolas.duboscq.realestatemanager.models.PropertyViewModel
import com.nicolas.duboscq.realestatemanager.utils.Utils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val listFragment = ListFragment()
    private val mapFragment = MapFragment()
    private lateinit var mode : String
    private lateinit var propertyViewModel: PropertyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        propertyViewModel = ViewModelProviders.of(this).get(PropertyViewModel::class.java)
        propertyViewModel.allProperty.observe(this, Observer {  })

        Stetho.initializeWithDefaults(this)
        mode = Utils.getModeConfiguration(main_activity_frame_layout_detail)

        when (mode){
            "phone"-> configureAndShowPhone()
            "tablet"-> configureAndShowTablet()
        }
        configureToolBar()
        openFragment(listFragment)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        when (mode){
            "phone"-> {
                menuInflater.inflate(R.menu.main_activity_toolbar, menu)
                menu.getItem(0).isVisible = false
                menu.getItem(2).isVisible = false
            }
            "tablet"-> {
                menuInflater.inflate(R.menu.main_activity_toolbar, menu)
            }
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
                val positionIntent = Intent(this, MapDetailActivity::class.java)
                positionIntent.putExtra("activity","location")
                startActivity(positionIntent)
            }
            R.id.menu_activity_main_add -> {
                val addIntent = Intent(this, EditUpdateActivity::class.java)
                startActivity(addIntent)
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
        bottom_navigation!!.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }
}
