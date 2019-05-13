package com.nicolas.duboscq.realestatemanager.controllers.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.arch.lifecycle.ViewModelProviders
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.bumptech.glide.Glide
import com.nicolas.duboscq.realestatemanager.models.Property
import com.nicolas.duboscq.realestatemanager.models.PropertyViewModel
import com.nicolas.duboscq.realestatemanager.utils.Utils
import kotlinx.android.synthetic.main.activity_edit_update.*
import kotlinx.android.synthetic.main.activity_edit_update.toolbar
import com.nicolas.duboscq.realestatemanager.R
import com.nicolas.duboscq.realestatemanager.injections.Injection
import pub.devrel.easypermissions.EasyPermissions
import com.nicolas.duboscq.realestatemanager.adapters.PictureAdapter
import com.nicolas.duboscq.realestatemanager.utils.ItemClickSupport
import kotlinx.android.synthetic.main.diag_description.view.*
import pub.devrel.easypermissions.AfterPermissionGranted
import java.io.File
import java.io.IOException


class EditUpdateActivity : AppCompatActivity(){

    //UI SPINNER
    private val editType = arrayOf(" ", "Appartement", "Maison", "Duplex", "Penthouse")
    private val editStatus = arrayOf(" ", "A Vendre", "Vendu")

    // PROPERTY INFORMATION
    private var status :String = " "
    private var description :String = " "
    private var type:String = " "
    private var surface :Int = 0
    private var price :Int = 0
    private var room :Int = 0
    private var bedroom :Int = 0
    private var bathroom :Int = 0
    private var streetNumber:Int = 0
    private var streetName:String =" "
    private var zipcode:Int = 0
    private var city:String =" "
    private var country:String =" "
    private lateinit var dateCreation : String

    //PROPERTY PICTURE
    private var picturePath: String = ""
    private lateinit var picturePathList : MutableList<String>
    private lateinit var pictureDescriptionList : MutableList<String>

    //DATA
    private lateinit var propertyViewModel: PropertyViewModel
    private lateinit var pictureAdapter: PictureAdapter
    private val AUTHORITY: String ="com.nicolas.duboscq.realestatemanager.fileprovider"

    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 1
        private const val WRITE_PERMISSION_REQUEST_CODE = 2
        private const val READ_PERMISSION_REQUEST_CODE = 3
        private val REQUEST_CHOOSE_PHOTO = 4
        private val REQUEST_IMAGE_CAPTURE = 101
        private val CAMERA_PERM = Manifest.permission.CAMERA
        private val WRITE_PERM = Manifest.permission.WRITE_EXTERNAL_STORAGE
        private val READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_update)
        this.configureToolBar()
        this.configureAllSpinner()
        this.configureRecyclerView()
        this.configureOnClickRecyclerView()
        this.configureViewModel()
        activity_edit_update_fl_btn.setOnClickListener {
            this.getAllPropertyInfo()
            this.createProperty()
            this.clearAllEditPropertyInfo()
            this.initData()
        }
        activity_edit_update_take_picture_btn.setOnClickListener{
            this.onAccessCamera()
        }
        activity_edit_update_add_picture_btn.setOnClickListener{
            this.onClickAddFile()
        }
    }

    // ---
    // UI
    // ---

    //TOOLBAR CONFIGURATION
    private fun configureToolBar() {
        setSupportActionBar(toolbar)
        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(true)
    }

    // SPINNER CONFIGURATION
    private fun configureSpinner(idRStringArray: Array<String>, spinner: Spinner) {

        val spinnerAdapter = ArrayAdapter<String>(
            this,
            R.layout.simple_spinner_item, idRStringArray
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        spinner.adapter = spinnerAdapter
        spinner.onItemSelectedListener
    }

    private fun configureAllSpinner() {
        this.configureSpinner(editType, activity_edit_update_type_sp)
        this.configureSpinner(editStatus, activity_edit_update_status_sp)
    }

    private fun createAlertDiagDescription(){
        val builder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val view = inflater.inflate(R.layout.diag_description,null)
        builder.setView(view)
        builder.setCancelable(false)
        val ad = builder.show()
        view.diag_description_btn.setOnClickListener{
            val description = view.diag_description_edt.text.toString()
            if (description.isNullOrEmpty()){
                Toast.makeText(this,getString(R.string.no_description),Toast.LENGTH_SHORT).show()
            } else {
                this.pictureDescriptionList.add(description)
                this.picturePathList.add(picturePath)
                pictureAdapter.notifyDataSetChanged()
                ad.dismiss()
            }
        }
    }

    private fun createAlertDiagDelPicture(position: Int){
        val alertDialog: AlertDialog? = this.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setTitle("Suppression Image")
                setMessage("Voulez vous vraiment supprimer cette image ?")
                setPositiveButton(R.string.ok, DialogInterface.OnClickListener {
                        dialog, id ->
                    pictureDescriptionList.removeAt(position)
                    picturePathList.removeAt(position)
                    pictureAdapter.notifyDataSetChanged()
                })
                setNegativeButton(R.string.cancel, DialogInterface.OnClickListener { dialog, id -> })
            }
            builder.create()
            builder.show()
        }
    }

    // ----
    // DATA
    // ----
    private fun initData(){
        status =" "; description =" "; type=" "; streetName=" "; city=" "; country=" "
        surface =0; price=0 ; room=0 ; bedroom=0 ; bathroom=0 ; zipcode=0
    }

    private fun getAllPropertyInfo(){
        if (!activity_edit_update_price_edt.text.toString().equals("")) { price = activity_edit_update_price_edt.text.toString().toInt()}
        if (!activity_edit_update_room_edt.text.toString().equals("")) {room = activity_edit_update_room_edt.text.toString().toInt()}
        if (!activity_edit_update_bedroom_edt.text.toString().equals("")){bedroom = activity_edit_update_bedroom_edt.text.toString().toInt()}
        if (!activity_edit_update_bathroom_edt.text.toString().equals("")){bathroom = activity_edit_update_bathroom_edt.text.toString().toInt()}
        if (!activity_edit_update_surface_edt.text.toString().equals("")){surface = activity_edit_update_surface_edt.text.toString().toInt()}
        if (!activity_edit_update_street_nb_edt.text.toString().equals("")){streetNumber = activity_edit_update_street_nb_edt.text.toString().toInt()}
        if (!activity_edit_update_zipcode_edt.text.toString().equals("")){zipcode = activity_edit_update_zipcode_edt.text.toString().toInt()}
        description = activity_edit_update_description_edt.text.toString()
        streetName = activity_edit_update_street_name_edt.text.toString()
        city = activity_edit_update_city_edt.text.toString()
        country = activity_edit_update_country_edt.text.toString()
        dateCreation = Utils.getTodayDate()
        type = activity_edit_update_type_sp.selectedItem.toString()
        status = activity_edit_update_status_sp.selectedItem.toString()
    }

    // CLEAR INFORMATION
    private fun clearAllEditPropertyInfo(){
        activity_edit_update_price_edt.editableText.clear()
        activity_edit_update_room_edt.editableText.clear()
        activity_edit_update_bedroom_edt.editableText.clear()
        activity_edit_update_bathroom_edt.editableText.clear()
        activity_edit_update_surface_edt.editableText.clear()
        activity_edit_update_description_edt.editableText.clear()
        activity_edit_update_street_nb_edt.editableText.clear()
        activity_edit_update_street_name_edt.editableText.clear()
        activity_edit_update_zipcode_edt.editableText.clear()
        activity_edit_update_city_edt.editableText.clear()
        activity_edit_update_country_edt.editableText.clear()
        activity_edit_update_type_sp.setSelection(0)
        activity_edit_update_status_sp.setSelection(0)
        picturePathList.clear()
        pictureDescriptionList.clear()
        pictureAdapter.notifyDataSetChanged()
    }

    // VIEW MODEL

    private fun configureViewModel(){
        val mViewModelFactory = Injection.provideViewModelFactory(this)
        this.propertyViewModel = ViewModelProviders.of(this, mViewModelFactory).get(PropertyViewModel::class.java!!)
    }

    private fun createProperty() {
        val property = Property(status,price,surface,room,bedroom,bathroom,description,type,dateCreation)
        this.propertyViewModel.createPropertyandAddress(property,streetNumber,streetName,zipcode,city,country,picturePathList,pictureDescriptionList,this)
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
                        AUTHORITY,
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

    @AfterPermissionGranted(READ_PERMISSION_REQUEST_CODE)
    fun onClickAddFile() {
        this.chooseImageFromPhone()
    }

    // --------------------
    // FILE MANAGEMENT
    // --------------------

    private fun chooseImageFromPhone(){
        if (!EasyPermissions.hasPermissions(this, READ_PERM )) {
            EasyPermissions.requestPermissions(this, getString(R.string.popup_title_permission_read_access), READ_PERMISSION_REQUEST_CODE, READ_PERM)
            return
        }
        val i =  Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(i, REQUEST_CHOOSE_PHOTO)
    }

    // RECYCLERVIEW

    private fun configureRecyclerView(){
        picturePathList = mutableListOf()
        pictureDescriptionList = mutableListOf()
        pictureAdapter = PictureAdapter(picturePathList,pictureDescriptionList,Glide.with(this@EditUpdateActivity))
        activity_edit_update_recyclerView.layoutManager = GridLayoutManager(this,2)
        activity_edit_update_recyclerView.adapter = pictureAdapter
    }

    private fun configureOnClickRecyclerView(){
        ItemClickSupport.addTo(activity_edit_update_recyclerView, R.layout.picture_main_item)
            .setOnItemClickListener{recyclerView, position, v ->
                createAlertDiagDelPicture(position)
            }
    }


    // RESULT FROM REQUEST
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE) {
            createAlertDiagDescription()
        }

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CHOOSE_PHOTO){
            this.picturePath = data?.data.toString()
            Log.i("Pictures",picturePath)
            createAlertDiagDescription()
        }
    }
}
