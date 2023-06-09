package com.moel32.creditbuddy

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {

    /**
     * Companion
     *
     * @constructor Create empty Companion
     */
    companion object {
        const val PERMISSION_REQUEST_CODE = 1
    }

    /**
     * TODO
     *
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find the MTN button view by its ID
        /**
         * Mtn button
         */
        val mtnButton = findViewById<Button>(R.id.mtn_button)

        // Set a click listener for the MTN button
        mtnButton.setOnClickListener {
            val intent = Intent(this, MtnActivity::class.java)
            startActivity(intent)
        }

        // Find the Orange button view by its ID
        /**
         * Orange button
         */
        val orangeButton = findViewById<Button>(R.id.orange_button)

        // Set a click listener for the Orange button
        orangeButton.setOnClickListener {
            val intent = Intent(this, OrangeActivity::class.java)
            startActivity(intent)
        }

        // Find the Camtel button view by its ID
        /**
         * Camtel button
         */
        val camtelButton = findViewById<Button>(R.id.camtel_button)

        // Set a click listener for the Camtel button
        camtelButton.setOnClickListener {
            val intent = Intent(this, CamtelActivity::class.java)
            startActivity(intent)
        }
    }
}