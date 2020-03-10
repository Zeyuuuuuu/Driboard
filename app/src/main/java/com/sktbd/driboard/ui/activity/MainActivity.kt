package com.sktbd.driboard.ui.activity

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.google.android.material.navigation.NavigationView
import com.sktbd.driboard.R
import com.sktbd.driboard.broadcastreceiver.NetworkChangeReceiver
import com.sktbd.driboard.databinding.ActivityMainBinding
import com.sktbd.driboard.services.ReminderService
import com.sktbd.driboard.utils.Constants

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{
    private var ivPreview:ImageView? = null
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        navView = binding.navView
        drawerLayout = binding.drawerLayout
        val navController = this.findNavController(R.id.main_navigation)
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        toolbar.setupWithNavController(navController, appBarConfiguration)
        navView.setNavigationItemSelectedListener(this)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        Intent(this, ReminderService::class.java).also { intent ->
            startService(intent)
        }
        val intentFilter = IntentFilter()
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        val networkChangeReceiver = NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver,intentFilter);

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.main_navigation)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.shotBoardFragment-> {
                findNavController(R.id.main_navigation).navigate(R.id.shotBoardFragment)
                drawerLayout.closeDrawers()
                return true
            }
            R.id.log_out-> {
                val dialog = logoutDialog()
                dialog.show()
                drawerLayout.closeDrawers()
                return true
            }
            R.id.profile -> {
                findNavController(R.id.main_navigation).navigate(R.id.userFragment)
                drawerLayout.closeDrawers()
                return true
            }
            R.id.draftListFragment -> {
                findNavController(R.id.main_navigation).navigate(R.id.draftListFragment)
                drawerLayout.closeDrawers()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun logoutDialog() : AlertDialog {
        val myDialog = AlertDialog.Builder(this)
            .setTitle("Log out")
            .setMessage("Do you want to Log out?")
            .setPositiveButton("Confirm") { dialog, which ->
                val sharedPref: SharedPreferences.Editor = getSharedPreferences("auth", Context.MODE_PRIVATE)!!.edit()
                sharedPref.remove("accessToken")
                sharedPref.apply()
                findNavController(R.id.main_navigation).navigate(R.id.mainFragment)
            }
            .setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }
            .create()
        return myDialog
    }

    //    private fun loginDialog():
}
