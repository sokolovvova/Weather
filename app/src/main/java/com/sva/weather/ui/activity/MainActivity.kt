package com.sva.weather.ui.activity

import android.content.Context
import android.content.IntentFilter
import android.location.LocationManager
import android.os.Bundle
import android.view.WindowManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.sva.weather.R
import com.sva.weather.databinding.ActivityMainBinding
import com.sva.weather.others.GpsBroadcastListener
import com.sva.weather.receivers.GpsStatusReceiver
import com.sva.weather.room.DataDB
import com.sva.weather.work_manager.WeatherUpdateWork
import java.lang.Exception
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(),GpsBroadcastListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataDB.init(context = applicationContext)

        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        supportActionBar?.hide()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_weather, R.id.navigation_fav
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        checkGpsStatus()
        val myGpsReceiver = GpsStatusReceiver(this)
        this.registerReceiver(myGpsReceiver, IntentFilter("android.location.PROVIDERS_CHANGED"))
        val saveRequest = PeriodicWorkRequestBuilder<WeatherUpdateWork>(15,TimeUnit.MINUTES).build()
        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork("weather_update",ExistingPeriodicWorkPolicy.KEEP,saveRequest)
    }

    override fun updateGpsStatus() {
        checkGpsStatus()
    }

    private fun checkGpsStatus(){
        val locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var gpsEnabled = false
        try{
            gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        }
        catch (e: Exception){}
        if(gpsEnabled){
            binding.topAppbar.menu.getItem(0).setIcon(R.drawable.ic_gps_fixed_24dp)
        }
        else binding.topAppbar.menu.getItem(0).setIcon(R.drawable.ic_gps_off_24dp)
    }
}