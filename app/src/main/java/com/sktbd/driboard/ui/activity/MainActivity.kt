package com.sktbd.driboard.ui.activity

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.sktbd.driboard.R
import com.sktbd.driboard.utils.Constants

class MainActivity : AppCompatActivity() {
    private var ivPreview:ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ivPreview = findViewById(R.id.ivPreview)

    }

}
