package com.nicolas.duboscq.realestatemanager.adapters

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.nicolas.duboscq.realestatemanager.utils.toFRDate
import java.text.SimpleDateFormat
import java.util.*

// Method for two-way Data Binding in AddUpdateActivity to transform Date <-> String
object DateBindingAdapters {
    @BindingAdapter("app:date")
    @JvmStatic fun setDate(view: EditText, value: Date?) {
        if (value == null){
            view.setText("")
        }
        else {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)
            view.setText(dateFormat.format(value))
        }
    }

    @InverseBindingAdapter(attribute = "app:date")
    @JvmStatic fun getDate(editText: EditText): Date? {
        if (editText.text.toString() == "")
        {
            return null
        } else {
        return editText.text.toString().toFRDate()
        }
    }

    @BindingAdapter("dateAttrChanged")
    @JvmStatic fun setListeners(
        view: EditText,
        attrChange: InverseBindingListener?
    ) {
        if (attrChange != null) {
                view.addTextChangedListener(object :TextWatcher{
                    override fun afterTextChanged(s: Editable?) {
                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        attrChange.onChange()
                    }

                })
            }
        }
}

// Method to show text in detail activity from Date value
@BindingAdapter("app:showDate")
fun showDate(view: TextView, value: Date?) {
    if (value == null ){
        view.text = ""
    } else {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)
        view.text = dateFormat.format(value)
    }
}
