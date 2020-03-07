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
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.sktbd.driboard.BuildConfig
import com.sktbd.driboard.R
import com.sktbd.driboard.ui.viewmodel.ShotEditViewModel
import com.sktbd.driboard.utils.Constants
import kotlinx.android.synthetic.main.shot_edit_fragment.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.Logger


class ShotEditFragment : Fragment() {

    private var mImageFileLocation = ""
    var progressBar:ProgressBar? = null
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ShotEditViewModel::class.java)
        progressBar = activity?.findViewById<ProgressBar>(R.id.progressbar)

        val title = view?.findViewById<TextInputEditText>(R.id.title_edit)
        title?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.onTitleChanged(p0.toString())
            }
        })

        val description = view?.findViewById<TextInputEditText>(R.id.description_edit)
        description?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.onDescriptionChanged(p0.toString())
            }
        })


        val tags = view?.findViewById<TextInputEditText>(R.id.tags_edit)
        val tagsInput = view?.findViewById<TextInputLayout>(R.id.tags_input)
        val chipGroup = view?.findViewById<ChipGroup>(R.id.chipGroup)
        tags?.setOnEditorActionListener(object:TextView.OnEditorActionListener{
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                tagsInput?.error = null

                if(actionId == EditorInfo.IME_ACTION_DONE) {
                    val tagText = tags.editableText.toString()

                    if (chipGroup?.childCount == 12){
                        tagsInput?.error = "Limited to a maximum of 12 tags."
                    }
                    else if(viewModel.hasTag(tagText)!!){
                        tagsInput?.error = "Tag exists."

                    }
                    else if (tagText == ""){
                        tagsInput?.error = "Tag cannot be empty."
                    }
                    else{
                        val chip = Chip(context)
                        chip.text = tagText
                        viewModel.onTagsChanged(tagText)
                        chipGroup?.addView(chip as View)
                        chip.isCloseIconVisible = true
                        chip.setOnCloseIconClickListener{
                            viewModel.onTagsRemove(tagText)
                            chipGroup?.removeView(chip as View)
                        }
                        tags.text = null
                    }

                    return true
                }

                return false
            }
        })
        tags?.onFocusChangeListener =
            View.OnFocusChangeListener { _, _ -> tagsInput?.error = null }

        tags?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                tagsInput?.error = null
            }
        })

        val btPublish = view?.findViewById<Button>(R.id.btPublish)
        btPublish?.setOnClickListener{
            if (currentImgPath == null || currentImgPath.toString() == ""){
                MaterialDialog(context!!).show {
                    title(R.string.error_message)
                    message(R.string.image_is_required)
                }
            }
            else if(title?.text.toString() == ""){
                MaterialDialog(context!!).show {
                    title(R.string.error_message)
                    message(R.string.title_is_required)
                }
            }
            else{
                showProgressBar()

                viewModel.publish(context,currentImgPath)

            }

        }

        val btSave = view?.findViewById<Button>(R.id.btSave)
        btSave?.setOnClickListener{
            viewModel.save()
        }

        val ivPreview = view?.findViewById<ImageView>(R.id.ivPreview)
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

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        val uriExternal: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        if(resultCode == RESULT_OK){
            when(requestCode){
                Constants.REQUEST_GALLERY_PHOTO -> {
                    if (data != null){
                        val uploadUri = data.data
                        val imgDatas = arrayOf(MediaStore.Images.Media.DATA)
                        var cursor = activity?.contentResolver?.query(uploadUri!!, imgDatas, null, null, null)
                        if (cursor != null) {
                            cursor!!.moveToFirst()
                            val columnIndex = cursor.getColumnIndexOrThrow(imgDatas[0])
//                            imgUri = Uri.withAppendedPath(uriExternal, "" + cursor.getString(columnIndexID))
//                            MediaStore.Image.Media.DATA is deprecated because it was unsafe
//                            ivPreview.setImageURI(imgUri)
//                            currentImgUri = imgUri
                            imgPath = cursor.getString(columnIndex)
                            title_edit.text = Editable.Factory.getInstance().newEditable(File(imgPath).name)
                            ivPreview.setImageBitmap(BitmapFactory.decodeFile(imgPath));
                            currentImgPath = imgPath
                            cursor.close()

                        }
                    }
                }
                Constants.REQUEST_CAMERA_PHOTO -> {
                    Glide.with(this).load(mImageFileLocation).into(ivPreview)
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


    fun showProgressBar(){
        if(progressBar?.visibility != View.VISIBLE){
            progressBar?.visibility = View.VISIBLE
        }
    }
    fun hideProgressBar(){
        if(progressBar?.visibility == View.VISIBLE){
            progressBar?.visibility = View.GONE
        }
    }
}



