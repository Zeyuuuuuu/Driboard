package com.sktbd.driboard.ui.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.sktbd.driboard.BuildConfig
import com.sktbd.driboard.R
import com.sktbd.driboard.data.model.Draft
import com.sktbd.driboard.ui.viewmodel.ShotEditViewModel
import com.sktbd.driboard.utils.Constants
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.shot_edit_fragment.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.Logger


class ShotEditFragment : Fragment() {

    private var mImageFileLocation = ""
    private var progressBar:ProgressBar? = null


    companion object {
        fun newInstance() = ShotEditFragment()
    }

    private lateinit var viewModel: ShotEditViewModel
    private var imgPath:String? = null
    private var currentImgPath:String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.shot_edit_fragment, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ShotEditViewModel::class.java)
        if(viewModel.isNew){
            viewModel.draft.value = Draft(id="",title = "",description = "",tags = ArrayList(),images = Draft.ImageUrl(""))
        }
        else{
            viewModel.getShot()
        }
        viewModel.draft.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer {
                title_edit?.text = Editable.Factory.getInstance().newEditable(it.title)
                if (it.description != null) {
                    description_edit?.text = Editable.Factory.getInstance().newEditable(it.description!!.substring(3,it.description!!.length-4))
                }
                if (it.tags != null){
                    val tagList = it.tags!!
                    for (tag in tagList){
                        addChip(tag,false)
                    }
                }
                if (it.images?.normal!="")
                    currentImgPath = it.images?.normal
                    Picasso.get().load(it.images?.normal ).into(ivPreview)
            }
        )

        progressBar = activity?.findViewById(R.id.progressbar)
        viewModel.isPending.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer {
                if (it == true)
                    progressBar?.visibility = View.VISIBLE
                else
                    progressBar?.visibility = View.GONE

            }
        )


        title_edit?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.onTitleChanged(p0.toString())
            }
        })
        description_edit?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.onDescriptionChanged(p0.toString())
            }
        })
        tags_edit?.setOnEditorActionListener(object:TextView.OnEditorActionListener{
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                tags_input?.error = null

                if(actionId == EditorInfo.IME_ACTION_DONE) {
                    val tagText = tags_edit!!.editableText.toString()

                    when {
                        chipGroup?.childCount == 12 -> {
                            tags_input?.error = "Limited to a maximum of 12 tags."
                        }
                        viewModel.hasTag(tagText)!! -> {
                            tags_input?.error = "Tag exists."

                        }
                        tagText == "" -> {
                            tags_input?.error = "Tag cannot be empty."
                        }
                        else -> {
                            addChip(tagText,true)
                            tags_edit!!.text = null
                        }
                    }

                    return true
                }

                return false
            }
        })
        tags_edit?.onFocusChangeListener = View.OnFocusChangeListener { _, _ -> tags_input?.error = null }
        tags_edit?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                tags_input?.error = null
            }
        })
        if(!viewModel.isNew){
            btPublish!!.text = "Update"
        }
        btPublish?.setOnClickListener{
            if (currentImgPath == null || currentImgPath.toString() == ""){
                MaterialDialog(context!!).show {
                    title(R.string.error_message)
                    message(R.string.image_is_required)
                }
            }
            else if(title_edit?.text.toString() == ""){
                MaterialDialog(context!!).show {
                    title(R.string.error_message)
                    message(R.string.title_is_required)
                }
            }
            else if (viewModel.isNew){
                viewModel.publish(context,currentImgPath)

            }
            else if (!viewModel.isNew){
                viewModel.update()
            }

        }
        btSave?.setOnClickListener{
            viewModel.save()
        }
        ivPreview?.setOnClickListener{
            MaterialDialog(context!!).show{
                title(R.string.select_ur_image)
                listItems(R.array.uploadImages) { _, index, _ ->
                    when (index) {
                        0 -> {
                            val galleryIntent = Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            )
                            startActivityForResult(
                                galleryIntent,
                                Constants.REQUEST_GALLERY_PHOTO
                            )
                        }
                        1 -> takePhoto()
                        2 -> {
                            ivPreview.setImageResource(R.drawable.ic_launcher_background)
                            currentImgPath = null
                        }
                    }
                }
            }
        }
        ivPreview?.isEnabled = false

        if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                Constants.REUQEST_PUBLISH_PERMISSION
            )

        } else {
            if (viewModel.isNew)
                ivPreview?.isEnabled = true
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == Constants.REUQEST_PUBLISH_PERMISSION){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (viewModel.isNew)
                    ivPreview?.isEnabled = true
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        val uriExternal: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        if(resultCode == RESULT_OK){
            when(requestCode){
                Constants.REQUEST_GALLERY_PHOTO -> {
                    if (data != null){
                        val uploadUri = data.data
                        val imgData = arrayOf(MediaStore.Images.Media.DATA)
                        val cursor = activity?.contentResolver?.query(uploadUri!!, imgData, null, null, null)
                        if (cursor != null) {
                            cursor.moveToFirst()
                            val columnIndex = cursor.getColumnIndexOrThrow(imgData[0])
//                            imgUri = Uri.withAppendedPath(uriExternal, "" + cursor.getString(columnIndexID))
//                            MediaStore.Image.Media.DATA is deprecated because it was unsafe
//                            ivPreview.setImageURI(imgUri)
//                            currentImgUri = imgUri
                            imgPath = cursor.getString(columnIndex)
                            title_edit?.text = Editable.Factory.getInstance().newEditable(File(imgPath!!).name)
                            ivPreview?.setImageBitmap(BitmapFactory.decodeFile(imgPath))
                            currentImgPath = imgPath
                            cursor.close()

                        }
                    }
                }
                Constants.REQUEST_CAMERA_PHOTO -> {
                    Glide.with(this).load(mImageFileLocation).into(ivPreview!!)
                    currentImgPath = mImageFileLocation
                }
            }
        }
    }


    private fun takePhoto()
    {
        //use this if Lollipop_Mr1 (API 22) or above
        val callCameraApplicationIntent = Intent()
        callCameraApplicationIntent.action = MediaStore.ACTION_IMAGE_CAPTURE

        // We give some instruction to the intent to save the image
        var photoFile: File? = null
        try {
            // If the createImageFile will be successful, the photo file will have the address of the file
            photoFile = createImageFile()
            // Here we call the function that will try to catch the exception made by the throw function
        } catch (e: IOException) {
            Logger.getAnonymousLogger()
                .info("Exception error in generating the file")
            e.printStackTrace()
        }
        // Here we add an extra file to the intent to put the address on to. For this purpose we use the FileProvider, declared in the AndroidManifest.
        val outputUri: Uri = FileProvider.getUriForFile(
            context!!, BuildConfig.APPLICATION_ID + ".provider", photoFile!!
        )
        callCameraApplicationIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri)

        // The following is a new line with a trying attempt
        callCameraApplicationIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
        Logger.getAnonymousLogger().info("Calling the camera App by intent")

        // The following strings calls the camera app and wait for his file in return.
        startActivityForResult(
            callCameraApplicationIntent,
            Constants.REQUEST_CAMERA_PHOTO
        )


}


    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    fun createImageFile(): File? {
        Logger.getAnonymousLogger().info("Generating the image - method started")

        // Here we create a "non-collision file name", alternatively said, "an unique filename" using the "timeStamp" functionality
        val timeStamp =
            SimpleDateFormat("yyyyMMdd_HHmmSS").format(Date())
        val imageFileName = "IMAGE_$timeStamp"
        // Here we specify the environment location and the exact path where we want to save the so-created file
        val storageDirectory =
            activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/photo_saving_app")
        Logger.getAnonymousLogger().info("Storage directory set")

        // Then we create the storage directory if does not exists
        if (!storageDirectory?.exists()!!) storageDirectory.mkdir()

        // Here we create the file using a prefix, a suffix and a directory
        val image = File(storageDirectory, "$imageFileName.jpg")
        // File image = File.createTempFile(imageFileName, ".jpg", storageDirectory);

        // Here the location is saved into the string mImageFileLocation
        Logger.getAnonymousLogger().info("File name and path set")
        mImageFileLocation = image.absolutePath
        // fileUri = Uri.parse(mImageFileLocation);
        // The file is returned to the previous intent across the camera application
        return image
    }


    fun addChip(tagText:String?,triggerChange:Boolean){
        val chip = Chip(context)
        chip.text = tagText
        if(triggerChange)
            viewModel.onTagsChanged(tagText)
        chipGroup?.addView(chip as View)
        chip.isCloseIconVisible = true
        chip.setOnCloseIconClickListener{
            viewModel.onTagsRemove(tagText)
            chipGroup?.removeView(chip as View)
        }
    }
}



