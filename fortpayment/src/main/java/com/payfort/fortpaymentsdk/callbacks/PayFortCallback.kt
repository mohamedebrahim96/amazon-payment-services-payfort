package com.payfort.fortpaymentsdk.callbacks

/**
 * PayFortCallback is Callback used with PayButton @PayfortPayButton.class
 * used to return the result status of Payment
 */
interface PayFortCallback {
    fun startLoading()
    fun onSuccess(requestParamsMap: Map<String, Any>, fortResponseMap:Map<String, Any> )
    fun onFailure(requestParamsMap: Map<String, Any>, fortResponseMap:Map<String, Any> )
}