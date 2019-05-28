package com.nicolas.duboscq.realestatemanager.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.text.DecimalFormat

class LoanSimulationViewModel : ViewModel(){
    var loan: MutableLiveData<Int> = MutableLiveData(0)
    var period: MutableLiveData<Int> = MutableLiveData(0)
    var monthlyPayment: MutableLiveData<String> = MutableLiveData("")
    var contribution: MutableLiveData<Int> = MutableLiveData(0)
    var rate: MutableLiveData<Double> = MutableLiveData(0.0)
    var toast:MutableLiveData<Boolean> = MutableLiveData(false)

    fun loanCalcul(){
        if (!loan.value?.equals(0)!! &&
            !period.value?.equals(0)!! &&
            !rate.value?.equals(0)!!){
            val decimalFormat = DecimalFormat("#.##")
            monthlyPayment.value = decimalFormat.format(((loan.value!! - contribution.value!!)*(1+(rate.value!!/100)))/(period.value!!*12))
        }
        else {
            toast.value = true
            toast.value = false
        }
    }
}