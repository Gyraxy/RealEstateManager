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
import com.facebook.stetho.Stetho
import com.nicolas.duboscq.realestatemanager.controllers.fragments.MapFragment
import com.nicolas.duboscq.realestatemanager.R
import com.nicolas.duboscq.realestatemanager.controllers.fragments.DetailFragment
import com.nicolas.duboscq.realestatemanager.controllers.fragments.ListFragment
import com.nicolas.duboscq.realestatemanager.models.PropertyViewModel
import com.nicolas.duboscq.realestatemanager.utils.Utils
import kotlinx.android.synthetic.main.activity_main.*
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.nicolas.duboscq.realestatemanager.views.PropertyAdapter
import com.nicolas.duboscq.realestatemanager.injections.Injection
import com.nicolas.duboscq.realestatemanager.models.Address
import com.nicolas.duboscq.realestatemanager.models.Property
import com.nicolas.duboscq.realestatemanager.utils.DividerItemDecoration
import kotlinx.android.synthetic.main.fragment_list.*


class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks{

    private val listFragment = ListFragment()
    private val mapFragment = MapFragment()
    private lateinit var mode : String
    private lateinit var propertyViewModel: PropertyViewModel
    private lateinit var propertyAdapter: PropertyAdapter
    private lateinit var propertylist : List<Property>

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private val PERMS = ACCESS_FINE_LOCATION
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.configureViewModel()
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
                enableLocation()
                return@OnNavigationItemSelectedListener true
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

    // VIEW MODEL

    private fun configureViewModel(){
        val mViewModelFactory = Injection.provideViewModelFactory(this)
        this.propertyViewModel = ViewModelProviders.of(this, mViewModelFactory).get(PropertyViewModel::class.java!!)
        this.propertyViewModel.getProperty().observe(this, Observer {
            if (it != null) {
                if (it.isEmpty()) {
                    fragment_list_recycler_view_empty.visibility = View.VISIBLE
                } else {
                    fragment_list_recycler_view_empty.visibility = View.GONE
                    propertylist = it
                    this.configureRecyclerView()
                }
            }
        })
    }

    // RECYCLERVIEW

    private fun configureRecyclerView(){
        propertyAdapter = PropertyAdapter(propertylist)
        val mDividerItemDecoration = DividerItemDecoration(recyclerView.context, R.drawable.horizontal_divider)
        recyclerView.adapter = propertyAdapter
        recyclerView.addItemDecoration(mDividerItemDecoration)
        recyclerView.layoutManager = LinearLayoutManager(this)
    }


    // STETHOFACEBOOK - To get information of Database

    private fun configureStetho(){
        Stetho.initializeWithDefaults(this)
    }

    // PERMISSION
    private fun enableLocation(){
        EasyPermissions.requestPermissions(
            PermissionRequest.Builder(this, LOCATION_PERMISSION_REQUEST_CODE, PERMS)
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
        openFragment(mapFragment)
    }
}

