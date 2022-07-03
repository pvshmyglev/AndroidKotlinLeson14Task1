package ru.netology.nmedia

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container_for_fragments) as NavHostFragment

        val navController = navHostFragment.navController

        checkGoogleAPI()

    }

    private fun checkGoogleAPI() {

       with(GoogleApiAvailability.getInstance()) {

           val codeResult = isGooglePlayServicesAvailable(this@MainActivity)

           if (codeResult == ConnectionResult.SUCCESS) {

               return@with

           }

           if (isUserResolvableError(codeResult)){

               getErrorDialog(this@MainActivity, codeResult, 9000)?.show()

               return

           }

           Toast.makeText(this@MainActivity, "Google API OK!", Toast.LENGTH_LONG).show()

       }

    }
}