package com.nicolas.duboscq.realestatemanager.controllers.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.room.Database
import com.nicolas.duboscq.realestatemanager.controllers.fragments.MapFragment
import com.nicolas.duboscq.realestatemanager.R
import com.nicolas.duboscq.realestatemanager.controllers.fragments.BlankFragment
import com.nicolas.duboscq.realestatemanager.controllers.fragments.DetailFragment
import com.nicolas.duboscq.realestatemanager.database.AppDatabase
import com.nicolas.duboscq.realestatemanager.database.dao.PropertyDao
import kotlinx.android.synthetic.main.activity_map_detail.*

class MapDetailActivity : AppCompatActivity() {

    private val mapFragment = MapFragment()
    private val detailFragment = DetailFragment()
    private val blankFragment = BlankFragment()
    private lateinit var activity: String
    private var propertyId = 0
    private var propertySold:Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_detail)
        activity = intent.extras.getString("activity","none")
        propertyId = intent.extras.getInt("id")
        if (propertyId!=0&& activity=="detail"){
            AppDatabase.getDatabase(this).propertyDao().getPropertyById(propertyId).observe(this, Observer {
                if(it.status.equals("Vendu")){
                    propertySold = true
                }
            })
        }
        configFragment()
        configureToolBar()
    }

    //FRAGMENT CONFIGURATION
    private fun configFragment(){
        when (activity){
            "location" -> openFragment(mapFragment)
            "detail" -> {
                val args = Bundle()
                args.putInt("property_id",propertyId)
                detailFragment.arguments = args
                openFragment(detailFragment)
            }
            "none" -> openFragment(blankFragment)

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
                for (i in 0..4) { menu.getItem(i).isVisible = false}
            }
            "detail" -> {
                if (propertySold){
                    menu.getItem(2).isVisible = false
                }
                menu.getItem(0).isVisible = false
                menu.getItem(1).isVisible = false
                menu.getItem(3).isVisible = false
                menu.getItem(4).isVisible = false
            }
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_activity_main_modify -> {
                val editIntent = Intent(this, AddUpdateActivity::class.java)
                editIntent.putExtra("activity","edit")
                editIntent.putExtra("propertyId",propertyId)
                startActivity(editIntent)
            }
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
