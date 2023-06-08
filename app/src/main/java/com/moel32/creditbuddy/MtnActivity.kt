package com.moel32.creditbuddy

import android.Manifest;
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.content.Context
import android.telephony.TelephonyManager

class MtnActivity : AppCompatActivity() {

    // Define the USSD code to execute
    private val ussdCode = "*444*#" // Replace with your desired USSD code

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mtn)

        // Get the support ActionBar and enable the "home" button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Find the text view by its ID
        val textView = findViewById<TextView>(R.id.textView)

        // Check if the app has permission to make phone calls
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            // Request permission to make phone calls if not yet granted
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), MainActivity.PERMISSION_REQUEST_CODE)

        } else {

            // Execute the USSD code if permission has already been granted
            val ussdUtil = UssdUtil(this)
            ussdUtil.executeUssdCode(ussdCode, { success, response ->
                if (success) {
                    // Handle USSD response
                    textView.text = response
                } else {
                    // Handle USSD response failure
                    textView.text = response
                }
            })
        }
    }

    // Handle clicks on the "home" button in the action bar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // If the "home" button is clicked, finish the current activity and return to the previous activity
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}