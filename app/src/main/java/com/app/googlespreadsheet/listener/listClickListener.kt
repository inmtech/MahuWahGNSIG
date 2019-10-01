package com.app.kotlindemo.view.listener

/**
 * TODO: Add a class header comment!
 */
interface listClickListener {

    fun <T> onListItemClickListener(position: Int, listObj: T)
}