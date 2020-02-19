package com.sktbd.driboard.ui.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sktbd.driboard.R
import com.sktbd.driboard.databinding.ActivityLoginBinding
import com.sktbd.driboard.ui.viewmodel.LoginViewModel


class LoginActivity : AppCompatActivity() {

    private val CLIENT_ID: String = "49626b5423f2f8bffd1bf1dea659bbec21e4759bcf6433671e5085bc2903cd2e"
    private val REDIRECT_URI: String = "driboard://callback"
    private var CODE: String = ""
    private var accessToken: String = ""

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        Log.i("LoginViewModel", "Called ViewModelProvider")

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        binding.loginBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://dribbble.com/oauth/authorize?client_id=$CLIENT_ID&redirect_uri=$REDIRECT_URI")
            if(intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
        }
        accessToken = loadData()
        if (accessToken != "") {
            println("Already logged in")
            println(accessToken)
            //TODO: navigate to main page
            val intent = Intent(this, UserActivity::class.java)
            Log.i("LoginActivity", "to user page")
            startActivity(intent)
        } else {
            viewModel.accessToken.observe(this, Observer {token ->
                val sharedPref: SharedPreferences.Editor = getSharedPreferences("auth", Context.MODE_PRIVATE).edit()
                if (token != "") {
                    sharedPref.putString("accessToken", token)
                    Log.i("LoginActivity", "token changed $token")
                    sharedPref.apply()
                    val intent = Intent(this, UserActivity::class.java)
                    Log.i("LoginActivity", "to user page")
                    startActivity(intent)
                }
            })
        }

    }

    override fun onResume() {
        super.onResume()

        val data: Uri? = intent.data
        println(accessToken)
        if (accessToken == "" && !TextUtils.isEmpty(data?.scheme)) {
            println("no accessToken, fetch a new one")
            val code: String? = data?.getQueryParameter("code")
            if (code != null && !TextUtils.isEmpty((code))) {
                Toast.makeText(this, R.string.login, Toast.LENGTH_LONG).show()
                CODE = code
                viewModel.getAccessToken(CODE)
            }
        }
    }

    private fun loadData(): String {
        val sharedPref = getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token: String =  sharedPref!!.getString("accessToken", "")!!
        return token
    }

}
