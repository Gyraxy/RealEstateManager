package com.nicolas.duboscq.realestatemanager.controllers.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.facebook.stetho.Stetho
import com.nicolas.duboscq.realestatemanager.controllers.fragments.MapFragment
import com.nicolas.duboscq.realestatemanager.R
import com.nicolas.duboscq.realestatemanager.controllers.fragments.DetailFragment
import com.nicolas.duboscq.realestatemanager.controllers.fragments.ListFragment
import com.nicolas.duboscq.realestatemanager.utils.Utils
import kotlinx.android.synthetic.main.activity_main.*
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks{

    private val listFragment = ListFragment()
    private val mapFragment = MapFragment()
    private lateinit var mode : String

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private val LOC_PERMS = ACCESS_FINE_LOCATION
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.configureStetho()
        this.configureDisplayMode()
        this.configureToolBar()
        this.openFragment(listFragment)
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
                val addIntent = Intent(this, AddUpdateActivity::class.java)
                addIntent.putExtra("activity","add")
                startActivity(addIntent)
            }
            R.id.menu_activity_main_loan -> {
                val loanIntent = Intent(this, LoanSimulationActivity::class.java)
                startActivity(loanIntent)
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
                enableLocation()
                if (Utils.isInternetAvailable(this)){
                    return@OnNavigationItemSelectedListener true
                }
                return@OnNavigationItemSelectedListener false
            }
        }
        false
    }

    // UI

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

    private fun configureDisplayMode(){
        mode = Utils.getModeConfiguration(main_activity_frame_layout_detail)

        when (mode){
            "phone"-> configureAndShowPhone()
            "tablet"-> configureAndShowTablet()
        }
    }


    // STETHOFACEBOOK - To get information of Database

    private fun configureStetho(){
        Stetho.initializeWithDefaults(this)
    }

    // PERMISSION
    private fun enableLocation(){
        EasyPermissions.requestPermissions(
            PermissionRequest.Builder(this, LOCATION_PERMISSION_REQUEST_CODE, LOC_PERMS)
                .setRationale(R.string.popup_title_permission_location_access)
                .setPositiveButtonText(R.string.popup_message_answer_yes)
                .setNegativeButtonText(R.string.popup_message_answer_no)
                .build()
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        bottom_navigation?.selectedItemId = R.id.navigation_list
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        if (Utils.isInternetAvailable(this)){
            openFragment(mapFragment)
        }
        else {
            Toast.makeText(this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
        }
    }
}

