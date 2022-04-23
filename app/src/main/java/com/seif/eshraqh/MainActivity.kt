package com.seif.eshraqh

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.Window
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.navigation.NavigationView
import com.seif.eshraqh.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var binding: ActivityMainBinding
    private var appBarConfiguration: AppBarConfiguration? = null
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = findNavController(R.id.navHost)
        appBarConfiguration = AppBarConfiguration(
            navController.graph,
            binding.drawerLayout,
        )
        setupActionBarWithNavController(
            navController,
            appBarConfiguration!!
        ) //the most important part
        binding.navDraw.setNavigationItemSelectedListener(this)

        binding.imgFacebook.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(
                        "https://www.facebook.com/Eshraqh228/?ti=as"
                    )
                )
            )
        }
        binding.imgInstagram.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(
                        "https://instagram.com/eshraqh228?igshid=1i26mpeus5kp0"
                    )
                )
            )
        }
        binding.imgTwitter.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(
                        "https://twitter.com/eshraqh228?s=08"
                    )
                )
            )
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration!!) ||
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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_about_app -> aboutApp()
            R.id.menu_share -> shareApp()
            R.id.menu_rate -> rateApp()
            R.id.menu_review -> reviewApp()
            R.id.menu_about -> aboutDeveloper()
        }
        binding.drawerLayout.close()
        return true
    }

    private fun aboutApp() {
        // navigate to about app fragment which contains overview about app.
        navController.navigate(R.id.action_homeFragment_to_aboutAppFragment)
    }

    private fun shareApp() {
        val sendIntent = Intent(Intent.ACTION_SEND)
        sendIntent.putExtra(
            Intent.EXTRA_TEXT,
            "Download Eshraqh app from here:\n" +
                    "https://play.google.com/store/apps/details?id=com.seif.eshraqh"
        )
        sendIntent.type = "text/plain"
        startActivity(Intent.createChooser(sendIntent, "Choose the app you want to share with:"))
    }

    private fun rateApp() {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(
                    "market://details?id=com.seif.eshraqh"
                )
            )
        )
    }

    private fun reviewApp() {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(
                    "mailto:" + "eshraqh228@gmail.com"
                            + "?subject=" + "Message from Eshraqh App"
                )
            )
        )
    }

    private fun aboutDeveloper() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.about_dialog)
        val btnOk = dialog.findViewById<Button>(R.id.btn_ok_about)
        btnOk.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}