package com.nicolas.duboscq.realestatemanager.controllers.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.nicolas.duboscq.realestatemanager.R
import com.nicolas.duboscq.realestatemanager.adapters.PictureAdapter
import com.nicolas.duboscq.realestatemanager.database.AppDatabase
import com.nicolas.duboscq.realestatemanager.databinding.ActivityAddUpdateBinding
import com.nicolas.duboscq.realestatemanager.utils.Injection
import com.nicolas.duboscq.realestatemanager.utils.ItemClickSupport
import com.nicolas.duboscq.realestatemanager.utils.Utils
import com.nicolas.duboscq.realestatemanager.utils.toFRDate
import com.nicolas.duboscq.realestatemanager.viewmodels.PropertyAddUpdateViewModel
import kotlinx.android.synthetic.main.activity_add_update.*
import kotlinx.android.synthetic.main.activity_add_update.toolbar
import kotlinx.android.synthetic.main.diag_description.view.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class AddUpdateActivity : AppCompatActivity() {

    // PROPERTY PICTURE
    private var picturePath: String = ""

    // FOR DATA
    private lateinit var viewModel: PropertyAddUpdateViewModel
    private val authority: String ="com.nicolas.duboscq.realestatemanager.fileprovider"
    private lateinit var picturePathList : MutableList<String>
    private lateinit var pictureDescriptionList : MutableList<String>
    private lateinit var pictureAdapter: PictureAdapter
    private lateinit var binding: ActivityAddUpdateBinding
    private var propertyId: Int = 0
    private lateinit var currentActivity:String


    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 1
        private const val WRITE_PERMISSION_REQUEST_CODE = 2
        private const val READ_PERMISSION_REQUEST_CODE = 3
        private const val REQUEST_CHOOSE_PHOTO = 4
        private const val REQUEST_IMAGE_CAPTURE = 101
        private const val CAMERA_PERM = Manifest.permission.CAMERA
        private const val WRITE_PERM = Manifest.permission.WRITE_EXTERNAL_STORAGE
        private const val READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val factory = Injection.provideAddUpdateViewModelFactory(this)
        viewModel = ViewModelProviders.of(this,factory).get(PropertyAddUpdateViewModel::class.java)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_update)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this@AddUpdateActivity

        this.configureToolBar()
        this.configureAllSpinner()
        this.configureRecyclerView()
        this.configureOnClickRecyclerView()

        propertyId = intent.getIntExtra("propertyId",0)
        currentActivity = intent.getStringExtra("activity")

        if (currentActivity=="edit"){
            getPropertyInfoFromDataBase(propertyId)
        }

        binding.addpictureclicklistener = View.OnClickListener { this.chooseImageFromPhone() }
        binding.takepictureclicklistener = View.OnClickListener { this.onAccessCamera() }
        binding.addeditpropertyclicklistener = View.OnClickListener {
            when (currentActivity){
                "add"-> viewModel.createPropertyandAddress(this)
                "edit"->{
                    Log.i("Edit",viewModel.property.value?.date_sold.toString())
                    viewModel.updatePropertyById(propertyId,this)
                    add_update_scrollview.fullScroll(ScrollView.FOCUS_UP)
                    val mainActivity = Intent(this,MainActivity::class.java)
                    startActivity(mainActivity)
                }

            }
        }
        binding.dateentrypicklistener = View.OnClickListener { showDatePicker(activity_edit_update_entryDate_edt)}
        binding.datesoldpicklistener = View.OnClickListener { showDatePicker(activity_edit_update_soldDate_edt)}
        this.viewModel.toastMissingInfo.observe(this, Observer {
            if (it==true){
                Toast.makeText(this,getString(R.string.activity_edit_not_enough_info),Toast.LENGTH_LONG).show()
            }
        })
        this.viewModel.pictureLinkList.observe(this, Observer {
            if (!it.isNullOrEmpty()){
                picturePathList.clear()
                picturePathList.addAll(it)
            }
            else {
                picturePathList.clear()
                pictureAdapter.notifyDataSetChanged()
            }
        })
        this.viewModel.pictureDescriptionList.observe(this, Observer {
            if (!it.isNullOrEmpty()){
                pictureDescriptionList.clear()
                pictureDescriptionList.addAll(it)
                pictureAdapter.notifyDataSetChanged()
            }
            else {
                pictureDescriptionList.clear()
                pictureAdapter.notifyDataSetChanged()
            }
        })
    }

    // ---
    // UI
    // ---
    
    // TOOLBAR CONFIGURATION
    private fun configureToolBar() {
        setSupportActionBar(toolbar)
        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun configureAllSpinner() {
        binding.typeclicklistener = View.OnClickListener { this.displayPopupMenu(resources.getStringArray(R.array.type_spinner), activity_edit_update_type_sp) }
    }

    private fun displayPopupMenu(listToDisplay: Array<String>, view: TextView) {
        val popupMenu = PopupMenu(this, view)
        (0 until listToDisplay.size).forEach { it ->
            popupMenu.menu.add(Menu.NONE, it, it, listToDisplay[it])
            popupMenu.setOnMenuItemClickListener { view.setText(it.title);true }
        }
        popupMenu.show()
    }

    private fun showDatePicker(idRDateEditText: TextView) {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)
        val c = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR)
        val mMonth = c.get(Calendar.MONTH)
        val mDay = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val date = sdf.format(Date(year - 1900, monthOfYear, dayOfMonth))
            val Date = date.toFRDate()
            idRDateEditText.text = date
        }, mYear, mMonth, mDay)
        dpd.show()
    }

    // RECYCLERVIEW

    private fun configureRecyclerView(){
        picturePathList = mutableListOf()
        pictureDescriptionList = mutableListOf()
        pictureAdapter = PictureAdapter(picturePathList,pictureDescriptionList, Glide.with(this@AddUpdateActivity))
        activity_edit_update_recyclerView.adapter = pictureAdapter
    }

    private fun configureOnClickRecyclerView(){
        ItemClickSupport.addTo(activity_edit_update_recyclerView, R.layout.picture_main_item)
            .setOnItemClickListener{_, position, _ ->
                createAlertDiagDelPicture(position)
            }
    }

    // ------
    // CAMERA
    // ------
    private fun takePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        authority,
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            picturePath = absolutePath
        }
    }

    // ALERTDIALOG

    private fun createAlertDiagDescription(){
        val builder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val view = inflater.inflate(R.layout.diag_description,null)
        builder.setView(view)
        builder.setCancelable(false)
        val ad = builder.show()
        view.diag_description_btn.setOnClickListener{
            val description = view.diag_description_edt.text.toString()
            if (description.isEmpty()){
                Toast.makeText(this,getString(R.string.no_description),Toast.LENGTH_SHORT).show()
            } else {
                this.viewModel.addPictureLinkToList(picturePath)
                this.viewModel.addPictureDescriptionToList(description)
                ad.dismiss()
            }
        }
    }

    private fun createAlertDiagDelPicture(position: Int){
        val alertDialog: AlertDialog? = this.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setTitle(getString(R.string.diag_del_picture_title))
                setMessage(getString(R.string.diag_del_picture_message))
                setPositiveButton(R.string.ok, DialogInterface.OnClickListener {
                        _, _ ->
                    viewModel.removePictureDescriptionToList(position)
                    viewModel.removePictureLinkToList(position)
                })
                setNegativeButton(R.string.cancel, DialogInterface.OnClickListener { _, _ -> })
            }
            builder.create()
            builder.show()
        }
    }

    // ----------
    // PERMISSION
    // ----------

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    @AfterPermissionGranted(CAMERA_PERMISSION_REQUEST_CODE)
    fun onAccessCamera() {
        if (!EasyPermissions.hasPermissions(this, CAMERA_PERM)) {
            EasyPermissions.requestPermissions(
                this,getString(R.string.popup_title_permission_camera_access),
                CAMERA_PERMISSION_REQUEST_CODE,
                CAMERA_PERM
            )
            return
        }
        this.onWriteAccessStorage()
    }

    @AfterPermissionGranted(WRITE_PERMISSION_REQUEST_CODE)
    fun onWriteAccessStorage() {
        if (!EasyPermissions.hasPermissions(this, WRITE_PERM)) {
            EasyPermissions.requestPermissions(
                this,getString(R.string.popup_title_permission_write_access),
                WRITE_PERMISSION_REQUEST_CODE,
                WRITE_PERM
            )
            return
        }
        this.takePictureIntent()
    }

    // --------------------
    // FILE MANAGEMENT
    // --------------------

    @AfterPermissionGranted(READ_PERMISSION_REQUEST_CODE)
    private fun chooseImageFromPhone(){
        if (!EasyPermissions.hasPermissions(this, READ_PERM )) {
            EasyPermissions.requestPermissions(this, getString(R.string.popup_title_permission_read_access), READ_PERMISSION_REQUEST_CODE, READ_PERM)
            return
        }
        val i =  Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(i, REQUEST_CHOOSE_PHOTO)
    }

    // RESULT FROM REQUEST
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE) {
            createAlertDiagDescription()
        }

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CHOOSE_PHOTO){
            val uri: Uri? = data?.data
            this.picturePath = uri.toString()
            createAlertDiagDescription()
        }
    }

    // ----
    // DATA
    // ----

    private fun getPropertyInfoFromDataBase(propertyInfo : Int){
        AppDatabase.getDatabase(this).propertyDao().getPropertyById(propertyInfo).observe(this, Observer {
            this.viewModel.property.value = it
            this.viewModel.property.value!!.dateModified = Utils.getTodayDate()
        })
        AppDatabase.getDatabase(this).addressDao().getAddressFromPropId(propertyInfo).observe(this, Observer {
            this.viewModel.address.value = it
        })
        AppDatabase.getDatabase(this).pictureDao().getPictureFromPropId(propertyInfo).observe(this, Observer {
            for (i in 0..it.size-1){
                viewModel.pictureLinkListVM.add(it[i].pictureLink)
                viewModel.pictureLinkList.value = viewModel.pictureLinkListVM
            }
            for (i in 0..it.size-1){
                viewModel.pictureDescriptionListVM.add(it[i].description)
                viewModel.pictureDescriptionList.value = viewModel.pictureDescriptionListVM
            }
        })
    }
}