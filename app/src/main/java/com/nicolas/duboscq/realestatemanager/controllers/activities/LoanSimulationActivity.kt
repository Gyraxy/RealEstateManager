package com.nicolas.duboscq.realestatemanager.controllers.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.nicolas.duboscq.realestatemanager.R
import com.nicolas.duboscq.realestatemanager.databinding.ActivityLoanSimulationBinding
import com.nicolas.duboscq.realestatemanager.viewmodels.LoanSimulationViewModel
import kotlinx.android.synthetic.main.activity_loan_simulation.*

class LoanSimulationActivity : AppCompatActivity() {

    private lateinit var viewModel: LoanSimulationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(LoanSimulationViewModel::class.java)
        val binding:ActivityLoanSimulationBinding = DataBindingUtil.setContentView(this,R.layout.activity_loan_simulation)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this@LoanSimulationActivity
        viewModel.toast.observe(this, Observer {
            if (it==true){
                Toast.makeText(this,"Veuillez saisir le montant du pret, le taux et la durée.",Toast.LENGTH_SHORT).show()
            }
        })
        this.configureToolBar()
    }

    // ---
    // UI
    // ---

    // TOOLBAR CONFIGURATION
    private fun configureToolBar() {
        setSupportActionBar(activity_loan_toolbar)
        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(true)
    }
}
