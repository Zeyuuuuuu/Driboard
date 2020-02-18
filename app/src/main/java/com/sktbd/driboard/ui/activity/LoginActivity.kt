package com.sktbd.driboard.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sktbd.driboard.R
import com.sktbd.driboard.data.model.User
import com.sktbd.driboard.ui.fragment.UserFragment

class LoginActivity : AppCompatActivity() {

    private val OAUTH_STRING: String = ""
    private val CLIENT_ID: String = "49626b5423f2f8bffd1bf1dea659bbec21e4759bcf6433671e5085bc2903cd2e"
    private val REDIRECT_URI: String = "com.sktbd.driboard:/oauth2redirect"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }
}
