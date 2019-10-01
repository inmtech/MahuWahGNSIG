package com.app.kotlindemo.util

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import com.app.googlespreadsheet.R

open class BaseActivity : AppCompatActivity() {

    fun replaceFragment(context: Context, fragment: Fragment) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.container, fragment, fragment.javaClass.name)
        ft.commit()
    }

    fun addFragment(context: Context, fragment: Fragment) {
        val ft = supportFragmentManager.beginTransaction()
        ft.add(R.id.container, fragment, fragment.javaClass.name)
    }
}