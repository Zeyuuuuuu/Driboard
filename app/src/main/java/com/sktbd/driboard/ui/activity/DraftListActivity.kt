package com.sktbd.driboard.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sktbd.driboard.R
import com.sktbd.driboard.ui.fragment.DraftListFragment

class DraftListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.draft_list_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, DraftListFragment.newInstance())
                .commitNow()
        }
    }
}
