package com.app.googlespreadsheet.view.activity

import android.os.Bundle
import android.os.SharedMemory
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.widget.Toolbar
import com.app.googlespreadsheet.R
import com.app.googlespreadsheet.util.SharedPrefsUtil
import com.app.googlespreadsheet.view.fragment.AddRecordFragment
import com.app.googlespreadsheet.view.fragment.DisplayRecordFragment
import com.app.googlespreadsheet.view.fragment.UnitSelectionFragment
import com.app.kotlindemo.util.BaseActivity

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        setDrawer()
        replaceFragment(this, AddRecordFragment())
    }

    fun setDrawer() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home -> {
                replaceFragment(this, AddRecordFragment())
            }
            R.id.nav_gallery -> {
                replaceFragment(this, DisplayRecordFragment())
            }
            R.id.nav_slideshow -> {
                replaceFragment(this, UnitSelectionFragment())
            }
            R.id.nav_tools -> {
                finish()
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
