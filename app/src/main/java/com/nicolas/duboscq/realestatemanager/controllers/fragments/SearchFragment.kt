package com.nicolas.duboscq.realestatemanager.controllers.fragments

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.textfield.TextInputEditText
import com.nicolas.duboscq.realestatemanager.R
import com.nicolas.duboscq.realestatemanager.controllers.activities.SearchResultActivity
import com.nicolas.duboscq.realestatemanager.databinding.FragmentSearchBinding
import com.nicolas.duboscq.realestatemanager.utils.Injection
import com.nicolas.duboscq.realestatemanager.viewmodels.SearchViewModel
import kotlinx.android.synthetic.main.fragment_search.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class SearchFragment : Fragment() {

    private val autoccompleteRequestCode = 1
    private val fields = Arrays.asList(Place.Field.ID, Place.Field.NAME)
    private lateinit var viewModel: SearchViewModel
    private lateinit var binding : FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val factory = Injection.provideSearchViewModelFactory(activity!!.applicationContext)
        viewModel = ViewModelProviders.of(activity as SearchResultActivity,factory).get(SearchViewModel::class.java)
        binding = DataBindingUtil.inflate<FragmentSearchBinding>(inflater,R.layout.fragment_search,container,false).apply {
            viewmodel = viewModel
            lifecycleOwner = this@SearchFragment
        }
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)
        this.configureDatePicker()
        this.configureAllSpinner()
        if (!Places.isInitialized()) {
            Places.initialize(context as SearchResultActivity, "AIzaSyDE8xBMmrWat5ugUmWhLANHGDui3ngNJjI")
        }
        binding.autocompleteclicklistener = View.OnClickListener { autocompletePlace() }
        viewModel.propertyListResult.observe(this,androidx.lifecycle.Observer {
            if (!it.isNullOrEmpty()){
                Log.i("Fragment Search",it.size.toString())
                val result : SearchResultActivity = activity as SearchResultActivity
                result.openResultFragment()
            }
        })
        viewModel.toastValue.observe(this,androidx.lifecycle.Observer {
            if (it==true){
                Toast.makeText(activity!!.applicationContext,getString(R.string.error_value),Toast.LENGTH_LONG).show()
            }
        })
        viewModel.toastDate.observe(this,androidx.lifecycle.Observer {
            if (it==true){
                Toast.makeText(activity!!.applicationContext,getString(R.string.date_error),Toast.LENGTH_LONG).show()
            }
        })
    }

    //SPINNER CONFIGURATION
    private fun configureAllSpinner(){
        binding.typeclicklistener = View.OnClickListener{ this.displayPopupMenu(resources.getStringArray(R.array.type_spinner), fragment_search_type_spinner) }
        binding.statusclicklistener = View.OnClickListener { this.displayPopupMenu(resources.getStringArray(R.array.status_spinner), fragment_search_status_spinner)  }
    }

    private fun displayPopupMenu(listToDisplay: Array<String>, view: TextInputEditText) {
        val popupMenu = PopupMenu(activity!!.applicationContext, view)
        (0 until listToDisplay.size).forEach { it ->
            popupMenu.menu.add(Menu.NONE, it, it, listToDisplay[it])
            popupMenu.setOnMenuItemClickListener { view.setText(it.title);true }
        }
        popupMenu.show()
    }

    //DATE PICKER
    private fun configureDatePicker() {
        activity_search_entry_date_min_edt.setOnClickListener { showDatePicker(activity_search_entry_date_min_edt) }
        activity_search_entry_date_max_edt.setOnClickListener { showDatePicker(activity_search_entry_date_max_edt) }
        activity_search_sold_date_min_edt.setOnClickListener { showDatePicker(activity_search_sold_date_min_edt) }
        activity_search_sold_date_max_edt.setOnClickListener { showDatePicker(activity_search_sold_date_max_edt) }
    }

    private fun showDatePicker(idRDateEditText: TextView) {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)
        val c = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR)
        val mMonth = c.get(Calendar.MONTH)
        val mDay = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(context as SearchResultActivity, DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val date = sdf.format(Date(year - 1900, monthOfYear, dayOfMonth))
            idRDateEditText.text = date
        }, mYear, mMonth, mDay)
        dpd.show()
    }

    //AUTOCOMPLETE PLACE
    private fun autocompletePlace() {
        val intent = Autocomplete.IntentBuilder(
            AutocompleteActivityMode.FULLSCREEN, fields
        )
            .build(context as SearchResultActivity)
        startActivityForResult(intent, autoccompleteRequestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == autoccompleteRequestCode) {
            when (resultCode){
                Activity.RESULT_OK -> {
                    val place = Autocomplete.getPlaceFromIntent(data!!)
                    search_fragment_place_edt.text = place.name
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    val status = Autocomplete.getStatusFromIntent(data!!)
                    Log.i("Test", status.statusMessage)
                }
                Activity.RESULT_CANCELED -> {}
            }
        }
    }
}
