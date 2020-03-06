package com.sktbd.driboard.ui.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.sktbd.driboard.R
import com.sktbd.driboard.databinding.LoginFragmentBinding
import com.sktbd.driboard.ui.viewmodel.LoginViewModel


class LoginFragment : Fragment() {

    private val CLIENT_ID: String = "49626b5423f2f8bffd1bf1dea659bbec21e4759bcf6433671e5085bc2903cd2e"
    private val REDIRECT_URI: String = "driboard://callback"
    private var CODE: String = ""
    private var accessToken: String = ""

    private lateinit var binding: LoginFragmentBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.login_fragment,
            container,
            false
        )

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        Log.i("LoginViewModel", "Called ViewModelProvider")
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.loginBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://dribbble.com/oauth/authorize?client_id=$CLIENT_ID&redirect_uri=$REDIRECT_URI")
            activity?.startActivity(intent)
        }
        accessToken = loadData()
        if (accessToken != "") {
            println("Already logged in")
            println(accessToken)
            //TODO: Navigate to userFragment
            this.findNavController().navigate(R.id.action_loginFragment_to_userFragment)
//            val intent = Intent(this, UserActivity::class.java)
//            Log.i("LoginActivity", "to user page")
//            startActivity(intent)
        } else {
            viewModel.accessToken.observe(viewLifecycleOwner, Observer {token ->
                val sharedPref: SharedPreferences.Editor = activity?.getSharedPreferences("auth", Context.MODE_PRIVATE)!!.edit()
                if (token != "") {
                    sharedPref.putString("accessToken", token)
                    Log.i("LoginActivity", "token changed $token")
                    sharedPref.apply()
                    // TODO: navigate to userFragment
                    this.findNavController().navigate(R.id.action_loginFragment_to_userFragment)
//                    val intent = Intent(this, UserActivity::class.java)
//                    Log.i("LoginActivity", "to user page")
//                    startActivity(intent)
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        val data: Uri? = activity?.intent?.data
        println(accessToken)
        if (accessToken == "" && !TextUtils.isEmpty(data?.scheme)) {
            println("no accessToken, fetch a new one")
            val code: String? = data?.getQueryParameter("code")
            if (code != null && !TextUtils.isEmpty((code))) {
                Toast.makeText(activity, R.string.login, Toast.LENGTH_LONG).show()
                CODE = code
                viewModel.getAccessToken(CODE)
            }
        }
    }
//
    private fun loadData(): String {
        val sharedPref = activity?.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token: String =  sharedPref!!.getString("accessToken", "")!!
        return token
    }

}
