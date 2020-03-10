package com.sktbd.driboard.ui.activity

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.google.android.material.navigation.NavigationView
import com.sktbd.driboard.R
import com.sktbd.driboard.databinding.ActivityMainBinding

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
        supportActionBar?.title = "Driboard"
        toolbar.title = "Driboard"

        toolbar.setupWithNavController(navController, appBarConfiguration)
        navView.setNavigationItemSelectedListener(this)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.main_navigation)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.navdrawer_menu, menu);
//        return super.onCreateOptionsMenu(menu)
//    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.shotBoardFragment-> {
                findNavController(R.id.main_navigation).navigate(R.id.shotBoardFragment)
                return true
            }
            R.id.log_out-> {
                val dialog = logoutDialog()
                dialog.show()
                return true
            }
            R.id.userFragment -> {
                findNavController(R.id.main_navigation).navigate(R.id.shotBoardFragment)
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
