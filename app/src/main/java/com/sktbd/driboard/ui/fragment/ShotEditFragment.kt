package com.sktbd.driboard.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.ImageSpan
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.sktbd.driboard.R
import com.sktbd.driboard.ui.viewmodel.ShotEditViewModel


class ShotEditFragment : Fragment() {

    companion object {
        fun newInstance() = ShotEditFragment()
    }

    private lateinit var viewModel: ShotEditViewModel
    private var SpannedLength = 0
    private var chipLength = 4


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.shot_edit_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ShotEditViewModel::class.java)


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
        val tags_input = view?.findViewById<TextInputLayout>(R.id.tags_input)
        val chipGroup = view?.findViewById<ChipGroup>(R.id.chipGroup)
        tags?.setOnEditorActionListener(object:TextView.OnEditorActionListener{
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                tags_input?.error = null

                if(actionId == EditorInfo.IME_ACTION_DONE) {
                    val tagText = tags.editableText.toString()

                    if (chipGroup?.childCount == 12){
                        tags_input?.error = "Limited to a maximum of 12 tags."
                    }
                    else if(viewModel.hasTag(tagText)!!){
                        tags_input?.error = "Tag exists."

                    }
                    else if (tagText == null){
                        tags_input?.error = "Tag cannot be empty."
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
        tags?.setOnFocusChangeListener(object:View.OnFocusChangeListener{
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                tags_input?.error = null
            }
        })
        tags?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                tags_input?.error = null
            }
        })

        val btPublish = view?.findViewById<Button>(R.id.btPublish)
        btPublish?.setOnClickListener{
            viewModel.publish()
        }

        val btSave = view?.findViewById<Button>(R.id.btSave)
        btSave?.setOnClickListener{
            viewModel.save()
        }

    }

}
