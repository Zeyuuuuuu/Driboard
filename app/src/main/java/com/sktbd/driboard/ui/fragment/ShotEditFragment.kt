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
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.sktbd.driboard.BuildConfig
import com.sktbd.driboard.R
import com.sktbd.driboard.ui.factory.DraftListViewModelFactory
import com.sktbd.driboard.data.model.Draft
import com.sktbd.driboard.ui.factory.ShotEditViewModelFactory
import com.sktbd.driboard.ui.viewmodel.ShotEditViewModel
import com.sktbd.driboard.utils.Constants
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.shot_edit_fragment.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ShotEditFragment : Fragment() {

    private var mImageFileLocation = ""
//    private var progressBar:ProgressBar? = null


    companion object {
        fun newInstance() = ShotEditFragment()
    }

    private lateinit var viewModel: ShotEditViewModel
    private lateinit var viewModelFactory: ShotEditViewModelFactory
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

        var args =  ShotEditFragmentArgs.fromBundle(arguments!!)
        val accessToken = args.accessToken
        val state = args.state
        when (state) {
            0 -> activity!!.findViewById<Toolbar>(R.id.toolbar).title = "New Shot"
            1 -> activity!!.findViewById<Toolbar>(R.id.toolbar).title = "Edit Shot"
            2 -> activity!!.findViewById<Toolbar>(R.id.toolbar).title = "New Shot"
            3 -> activity!!.findViewById<Toolbar>(R.id.toolbar).title = "Edit Shot"
        }
        val id = args.shotId
        viewModelFactory = ShotEditViewModelFactory(accessToken, state, id)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ShotEditViewModel::class.java)
        viewModel.initDB(context!!)
        viewModel.getShot()
        viewModel.draft.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer {
                title_edit?.text = Editable.Factory.getInstance().newEditable(it.title)
                if (it.description != null && it.description != "") {
                    if (it.description!!.contains("<p>"))
                        description_edit?.text = Editable.Factory.getInstance().newEditable(it.description!!.substring(3,it.description!!.length-4))
                }
                if (it.tags != null && it.tags!!.isNotEmpty()){
                    val tagList = it.tags!!
                    for (tag in tagList){
                        addChip(tag,false)
                    }
                }
                if (it.images?.normal!=""){
                    currentImgPath = it.images?.normal
                    Picasso.get().load(it.images?.normal ).into(ivPreview)
                }
                else if(it.imageUri != "") {
                    currentImgPath = it.imageUri
                    ivPreview.setImageURI(it.imageUri!!.toUri())

                }
            }
        )

//        progressBar = activity?.findViewById(R.id.progressbar)
//        progressBar?.bringToFront()
//        viewModel.isPending.observe(
//            viewLifecycleOwner,
//            androidx.lifecycle.Observer {
//                if (it == true) {
//                    progressBar?.visibility = View.VISIBLE
//                }
//                else {
//                    progressBar?.visibility = View.GONE
//                }
//            }
//        )


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
        if(viewModel.state == Constants.UPDATE_SHOT_STATE || viewModel.state == Constants.UPDATE_DRAFT_STATE){
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
            else if (viewModel.state == Constants.NEW_SHOT_STATE || viewModel.state == Constants.NEW_DRAFT_STATE){
                viewModel.publish(context!!)
                this.findNavController().navigate(R.id.action_shotEditFragment_to_shotBoardFragment)

            }
            else if (viewModel.state == Constants.UPDATE_SHOT_STATE ||viewModel.state == Constants.UPDATE_DRAFT_STATE){
                viewModel.update()
                this.findNavController().navigate(R.id.action_shotEditFragment_to_shotBoardFragment)
            }

        }
        btSave?.setOnClickListener{
            // create draft
            if (viewModel.state == Constants.NEW_SHOT_STATE || viewModel.state == Constants.UPDATE_SHOT_STATE){
                viewModel.save()

            }
            // load draft
            else if (viewModel.state == Constants.NEW_DRAFT_STATE ||viewModel.state == Constants.UPDATE_DRAFT_STATE){
                viewModel.save()
            }
            this.findNavController().navigate(R.id.action_shotEditFragment_to_draftListFragment)

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
                            ivPreview.setImageResource(R.drawable.image_placeholder)
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
            if (viewModel.state == Constants.NEW_SHOT_STATE || viewModel.state == Constants.NEW_DRAFT_STATE)
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
                if (viewModel.state == Constants.NEW_SHOT_STATE || viewModel.state == Constants.NEW_DRAFT_STATE)
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
                            viewModel.onPicUpload(imgPath!!)
                            title_edit?.text = Editable.Factory.getInstance().newEditable(File(imgPath!!).name)
                            Glide.with(this).load(imgPath).into(ivPreview!!)

//                            ivPreview?.setImageBitmap(BitmapFactory.decodeFile(imgPath))
                            currentImgPath = imgPath
                            cursor.close()

                        }
                    }
                }
                Constants.REQUEST_CAMERA_PHOTO -> {
                    Glide.with(this).load(mImageFileLocation).into(ivPreview!!)
                    title_edit?.text = Editable.Factory.getInstance().newEditable(File(mImageFileLocation!!).name)
                    viewModel.onPicUpload(mImageFileLocation!!)
                    currentImgPath = mImageFileLocation

                }
            }
        }
    }


    private fun takePhoto()
    {
        val imageCaptureIntent = Intent()
        imageCaptureIntent.action = MediaStore.ACTION_IMAGE_CAPTURE
        imageCaptureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)

        var image: File? = null
        try {
            image = createImageFile()
        } catch (e: IOException) {
            Log.i("ShotEditFragment_takePhotoFailed","Exception error in generating the file")
            e.printStackTrace()
        }
        val imageUri: Uri = FileProvider.getUriForFile(
            context!!, BuildConfig.APPLICATION_ID + ".provider", image!!
        )
        imageCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)

        startActivityForResult(
            imageCaptureIntent,
            Constants.REQUEST_CAMERA_PHOTO
        )


}


    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    fun createImageFile(): File? {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmSS").format(Date())
        val imageName = "IMAGE_$timeStamp"
        val imageDirectory =
            activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/photo_saving_app")
        if (!imageDirectory?.exists()!!) imageDirectory.mkdir()
        val image = File(imageDirectory, "$imageName.jpg")
        mImageFileLocation = image.absolutePath
        return image
    }


    fun addChip(tagText:String?,triggerChange:Boolean){
        val chip = Chip(context)
        chip.text = tagText
        chip.textAlignment = View.TEXT_ALIGNMENT_CENTER
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



