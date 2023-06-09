package com.moel32.creditbuddy

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Handler
import android.telephony.TelephonyManager
import androidx.core.content.ContextCompat

class UssdUtil(private val context: Context) {

    /**
     * TODO
     *
     * @param ussdCode
     * @param callback
     */
    fun executeUssdCode(ussdCode: String, callback: (Boolean, String?) -> Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            callback(false, "CALL_PHONE permission not granted")
            return
        }

        /**
         * Telephony manager
         */
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager
        if (telephonyManager != null) {
            try {
                telephonyManager.sendUssdRequest(ussdCode, object : TelephonyManager.UssdResponseCallback() {
                    // On success response
                    /**
                     * TODO
                     *
                     * @param telephonyManager
                     * @param request
                     * @param response
                     */
                    override fun onReceiveUssdResponse(telephonyManager: TelephonyManager, request: String, response: CharSequence) {
                        callback(true, response.toString())
                    }

                    // On failed response
                    /**
                     * TODO
                     *
                     * @param telephonyManager
                     * @param request
                     * @param failureCode
                     */
                    override fun onReceiveUssdResponseFailed(telephonyManager: TelephonyManager, request: String, failureCode: Int) {
                        callback(false, "USSD request $request failed with code $failureCode")
                    }
                }, Handler())
            } catch (e: SecurityException) {
                callback(false, "SecurityException: ${e.message}")
            }
        } else {
            callback(false, "TelephonyManager is null")
        }
    }
}