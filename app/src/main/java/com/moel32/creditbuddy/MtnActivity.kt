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

class MtnActivity : AppCompatActivity() {

    // Define the USSD code to execute
    private val ussdCode = "*%2306%23 " // Replace with your desired USSD code

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
            val result = executeUssdCode(ussdCode)
            textView.text = result
        }
    }

    /**
     * Executes the given USSD code.
     *
     * @param ussdCode The USSD code to execute.
     * @return The result of the USSD code execution.
     */
    private fun executeUssdCode(ussdCode: String): String? {
        //val encodedUssdCode = Uri.encode(ussdCode)
        //val ussdUri = Uri.parse("tel:$encodedUssdCode")
        val ussdUri = Uri.parse("tel:$ussdCode")
        val ussdIntent = Intent(Intent.ACTION_CALL, ussdUri)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // Handle permission not granted error
            Log.e(TAG, "CALL_PHONE permission not granted")
            return "CALL_PHONE permission not granted"
        }

        try {
            startActivity(ussdIntent)
        } catch (e: SecurityException) {
            // Handle security exception error
            Log.e(TAG, "SecurityException: ${e.message}")
            return "SecurityException: ${e.message}"
        } catch (e: Exception) {
            // Handle other errors
            Log.e(TAG, "Error: ${e.message}")
            return "Error: ${e.message}"
        }

        // Wait for the USSD response
        val handler = Handler()
        var ussdResponse: String? = null
        handler.postDelayed({
            val ussdResultIntent = Intent(Intent.ACTION_CALL, Uri.parse("tel:*#1234#"))
            try {
                startActivityForResult(ussdResultIntent, 1)
            } catch (e: Exception) {
                // Handle error getting USSD response
                Log.e(TAG, "Error getting USSD response: ${e.message}")
                ussdResponse = "Error getting USSD response: ${e.message}"
            }
        }, 5000) // Wait 5 seconds for the USSD response to arrive

        // Return the USSD response or error message
        return ussdResponse
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val ussdResponse = data?.getStringExtra("ussdResponse")
            Log.d(TAG, "USSD response: $ussdResponse")
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MainActivity.PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, execute the USSD code and display the result
                    val textView = findViewById<TextView>(R.id.textView)
                    val result = executeUssdCode(ussdCode) // Replace with your desired USSD code
                    textView.text = result
                }
            }
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