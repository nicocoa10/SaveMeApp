package com.example.savemeapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.savemeapp.databinding.ActivityMainBinding
import com.google.zxing.integration.android.IntentIntegrator


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var readedString : String = "Empty"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_main
        )

        val navController = this.findNavController(R.id.myNavHostFragment)

        NavigationUI.setupActionBarWithNavController(this,navController)

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return navController.navigateUp()
    }

    //once scanner starts this will determine what we will do with the contents
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (result != null) {
                if (result.contents == null) { //If something is not scanned
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
                } else { // if something is scanned
                    readedString= result.contents
                    //need to store the string in table
                    //need to parse the string
                    //maybe make a toast with the scanned results parsed
                    Toast.makeText(this, "Scanned: " + readedString, Toast.LENGTH_LONG).show()

                }
            } else {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }



}