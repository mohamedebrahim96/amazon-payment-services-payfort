package com.payfort.fortpaymentsdk.utils

import android.app.Activity
import android.content.ContextWrapper
import android.view.View
import androidx.appcompat.app.AppCompatActivity

/**
 * Set view visible
 */
fun View.visible() {
  visibility = View.VISIBLE
}

/**
 * Set view invisible
 */
fun View.invisible() {
  visibility = View.INVISIBLE
}

/**
 * Set view gone
 */
fun View.gone() {
  visibility = View.GONE
}

fun View.showVisibility(boolean: Boolean){
  if(boolean) visible() else gone()
}

val View.activity: AppCompatActivity?
  get() {
    var ctx = context
    while (true) {
      if (!ContextWrapper::class.java.isInstance(ctx)) {
        return null
      }
      if (AppCompatActivity::class.java.isInstance(ctx)) {
        return ctx as AppCompatActivity
      }
      ctx = (ctx as ContextWrapper).baseContext
    }
  }