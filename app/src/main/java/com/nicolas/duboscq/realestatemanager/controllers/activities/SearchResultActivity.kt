package com.nicolas.duboscq.realestatemanager.controllers.activities


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.nicolas.duboscq.realestatemanager.R
import com.nicolas.duboscq.realestatemanager.controllers.fragments.ResultFragment
import com.nicolas.duboscq.realestatemanager.controllers.fragments.SearchFragment
import com.nicolas.duboscq.realestatemanager.utils.Injection
import com.nicolas.duboscq.realestatemanager.viewmodels.SearchViewModel
import kotlinx.android.synthetic.main.activity_search_result.toolbar

class SearchResultActivity : AppCompatActivity() {

    private val searchFragment = SearchFragment()
    private val resultFragment = ResultFragment()
    private lateinit var viewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)
        val factory = Injection.provideSearchViewModelFactory(this)
        viewModel = ViewModelProviders.of(this,factory).get(SearchViewModel::class.java)
        this.configureToolBar()
        openFragment(searchFragment)
    }

    //UI

    //TOOLBAR CONFIGURATION
    private fun configureToolBar() {
        setSupportActionBar(toolbar)
        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun openFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.search_activity_frame_layout, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    fun openResultFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.search_activity_frame_layout, resultFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}
