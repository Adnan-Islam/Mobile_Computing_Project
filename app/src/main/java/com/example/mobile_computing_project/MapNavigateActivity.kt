package com.example.mobile_computing_project

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import kotlinx.android.synthetic.main.activity_map_navigate.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import java.lang.Exception
import java.util.*

class MapNavigateActivity : AppCompatActivity(), OnMapReadyCallback {


    lateinit var gMap: GoogleMap
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var selectedLocation:LatLng

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_navigate)

        (map_fragment as SupportMapFragment).getMapAsync(this)
        button_create.setOnClickListener {
            val reminderText = editText.text.toString()
            if (reminderText.isEmpty()) {
                toast("Please provide reminder text")
                return@setOnClickListener
            }

            if (selectedLocation == null) {
                toast("Please select a location on the map")
                return@setOnClickListener
            }

            val reminder = Reminder (
                uid = null,
                time = null,
                location = String.format("%.3f,%.3f",selectedLocation.latitude,selectedLocation.latitude),
                message = reminderText
            )
        }
    }


    override fun onMapReady(map: GoogleMap?) {
        gMap=map ?:return
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
        gMap.isMyLocationEnabled=true
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->

            if(location!=null) {
                var latLong = LatLng(location.latitude, location.longitude)
                with(gMap){
                    animateCamera(CameraUpdateFactory.newLatLngZoom(latLong, 13f))
                }
            }
          }
        } else{
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION), 123)
        }


        gMap.setOnMapClickListener { location:LatLng->
            with(gMap){
                clear()
                animateCamera(CameraUpdateFactory.newLatLngZoom(location, 13f))
                val geocoder=Geocoder(applicationContext, Locale.getDefault())
                var title=""
                var city=""
                try {
                    val addressList= geocoder.getFromLocation(location.latitude,location.longitude, 1)
                    city=addressList.get(0).locality
                    title=addressList.get(0).getAddressLine(0)

                }catch (e:Exception){

                }

                val marker=addMarker(MarkerOptions().position(location).snippet(title).title(city))
                marker.showInfoWindow()

                selectedLocation=location


            }
        }
    }

}
