package com.sktbd.driboard.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sktbd.driboard.R
import com.sktbd.driboard.ui.fragment.ShotBoardFragment

class ShotBoardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shot_board_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ShotBoardFragment.newInstance())
                .commitNow()
        }
    }
}
