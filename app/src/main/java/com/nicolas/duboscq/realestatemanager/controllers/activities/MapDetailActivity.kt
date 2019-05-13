package com.nicolas.duboscq.realestatemanager.controllers.activities

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.nicolas.duboscq.realestatemanager.controllers.fragments.MapFragment
import com.nicolas.duboscq.realestatemanager.R
import com.nicolas.duboscq.realestatemanager.controllers.fragments.DetailFragment
import com.nicolas.duboscq.realestatemanager.database.AppDatabase
import com.nicolas.duboscq.realestatemanager.models.Address
import com.nicolas.duboscq.realestatemanager.models.Property
import kotlinx.android.synthetic.main.activity_map_detail.*
import kotlinx.android.synthetic.main.fragment_detail.*

class MapDetailActivity : AppCompatActivity() {

    private val mapFragment = MapFragment()
    private val detailFragment = DetailFragment()
    private lateinit var activity: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_detail)
        activity = intent.extras.getString("activity")
        configFragment()
        configureToolBar()
    }

    //FRAGMENT CONFIGURATION
    private fun configFragment(){
        when (activity){
            "location" -> openFragment(mapFragment)
            "detail" -> {
                openFragment(detailFragment)
                val database = AppDatabase.getDatabase(this)
                val propertyList = database.propertyDao().getAll()
                val status = propertyList.value?.get(0)?.price
                println(status)
            }
        }
    }

    private fun openFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.main_activity_frame_layout_list, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    //TOOLBAR CONFIGURATION
    private fun configureToolBar() {
        setSupportActionBar(activity_map_detail_toolbar)
        val ab = supportActionBar
        ab?.setDisplayHomeAsUpEnabled(true)
        when (activity){
            "location" -> ab?.title = "Localisation"
            "detail" -> ab?.title = "Detail"
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_activity_toolbar, menu)
        when (activity){
            "location" -> {
                for (i in 0..3) { menu.getItem(i).isVisible = false}
            }
            "detail" -> {
                menu.getItem(0).isVisible = false
                menu.getItem(1).isVisible = false
                menu.getItem(3).isVisible = false
            }
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_activity_main_modify -> {
                val editIntent = Intent(this, EditUpdateActivity::class.java)
                startActivity(editIntent)
            }
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
