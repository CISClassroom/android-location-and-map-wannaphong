package com.wannaphong.mymapapplication

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

import kotlinx.android.synthetic.main.activity_main.*
//import java.util.jar.Manifest

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    lateinit var LocationClient: FusedLocationProviderClient
    private lateinit var mMap: GoogleMap
    var latitude_temp:String = ""
    var longitude_temp:String =""
    var googleMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        LocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLocation()
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


    }
    private fun moveon(){
        val sydney = LatLng(latitude_temp.toDouble(), longitude_temp.toDouble())

        googleMap?.addMarker(MarkerOptions().position(sydney).title("คุณอยู่ที่นี่"))
        googleMap?.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        // Add a marker in Sydney and move the camera
        if(latitude_temp!==""&&longitude_temp!="") {
            moveon()
        }
    }

    private fun getLocation() {
        // check permission
            //yes
            //get lat long
            //display
        if(checkPermission()){
                LocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    var location: Location? = task.result

                    if (location == null) {
                        //requestNewLocationData()
                    } else {
                        var latitude = location.latitude.toString()
                        var longitude = location.longitude.toString()
                        longitude_temp=longitude
                        latitude_temp = latitude
                        //textView.text = latitude.toString()+","+longitude.toString()
                        moveon()
                        Toast.makeText(this,latitude.toString()+","+longitude.toString(),Toast.LENGTH_LONG)
                    }
                }

        }else{
            requestPermissions()
        }
    }
    private fun checkPermission():Boolean {
        if(ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED) {
            return true
        }
        return false
    }
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION),
            101
        )
    }
}
