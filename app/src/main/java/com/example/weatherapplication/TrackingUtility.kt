package com.example.weatherapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat
import com.google.android.gms.location.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

object TrackingUtility {

    fun hasLocationPermissions(context:Context)=
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.Q){
          EasyPermissions.hasPermissions(
              context,
              android.Manifest.permission.ACCESS_FINE_LOCATION,
              android.Manifest.permission.ACCESS_COARSE_LOCATION
          )
        }else{
            EasyPermissions.hasPermissions(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )

        }



}