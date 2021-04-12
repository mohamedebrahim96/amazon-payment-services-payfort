package com.payfort.fortpaymentsdk.callbacks

import android.content.Intent
import java.util.*


class FortCallback : FortCallBackManager {
    interface Callback {
        fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean
    }

    /**
     * @param requestCode
     * @param callback
     */
    fun registerCallback(requestCode: Int, callback: Callback?) {
        checkNotNull(callback,{"callback"})
        callbacks[requestCode] = callback
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        val callback = callbacks[requestCode]
        return callback?.onActivityResult(requestCode, resultCode, data) ?: false
    }

    companion object {
        private val callbacks: MutableMap<Int, Callback> = HashMap()
    }
}