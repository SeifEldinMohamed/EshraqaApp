package com.seif.eshraqaapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navController = findNavController(R.id.navHost)
        setupActionBarWithNavController(navController)
        
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.navHost).navigateUp()||
                super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        val navigationController = findNavController(R.id.navHost)
        if (navigationController.currentDestination?.id == R.id.homeFragment) {
            Log.d("main", "on back pressed")
            finish()
        } else {
            super.onBackPressed()
        }
    }
}