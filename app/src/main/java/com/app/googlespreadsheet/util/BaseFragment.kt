package com.app.kotlindemo.util

import android.content.Context
import androidx.fragment.app.Fragment
import com.app.googlespreadsheet.R
import com.app.googlespreadsheet.viewmodel.RecordViewModel

/**
 * TODO: Add a class header comment!
 */
open class BaseFragment : Fragment() {


    fun replaceFragment(context: Context, fragment: Fragment, activity: BaseActivity) {
        val ft = activity.supportFragmentManager.beginTransaction()
        ft.replace(R.id.container, fragment, fragment.javaClass.name)
        ft.commit()
    }

    fun addFragment(context: Context, fragment: Fragment, activity: BaseActivity) {
        val ft = activity.supportFragmentManager.beginTransaction()
        ft.add(R.id.container, fragment, fragment.javaClass.name)
        ft.addToBackStack(null)
        ft.commit()
    }
}