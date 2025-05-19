package com.example.loginsignup;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.location.LocationRequest;


//public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
//    // public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
//
//    private GoogleMap myMap;
//    private static final int REQUEST_LOCATION_PERMISSION = 1;
//    private FusedLocationProviderClient fusedLocationClient;
////    double latitude ;
////    double longitude;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_map);
//
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//
//        // Check for permission to access location
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // Request location permission if not granted
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
//        } else {
//            // Permission already granted, fetch location
//            getLocation();
//        }
//
//
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//////        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//////        toolbar.setTitle(getString(R.string.app_name));
//////        setSupportActionBar(toolbar);
////
////
//    }
//    private void getLocation() {
//        // Check if the app has location permissions
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // If permissions are not granted, return and request permissions
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION);
//            return;
//        }
//
//        // If permissions are granted, fetch the last known location
//        fusedLocationClient.getLastLocation()
//                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
//                    @Override
//                    public void onSuccess(Location location) {
//                        // Check if location is not null
//                        if (location != null) {
//                            // Handle location object, e.g., get latitude and longitude
//                            double latitude = location.getLatitude();
//                            double longitude = location.getLongitude();
//                            // Display latitude and longitude in a toast message
//                            Toast.makeText(MapActivity.this, "Latitude: " + latitude + ", Longitude: " + longitude, Toast.LENGTH_SHORT).show();
//                        } else {
//                            // If location is null, display a message indicating inability to retrieve location
//                            Toast.makeText(MapActivity.this, "Unable to retrieve location", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }
//    // Handle permission request result
//    @Override
//    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == REQUEST_LOCATION_PERMISSION) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Permission granted, fetch location
//                getLocation();
//            } else {
//                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        myMap = googleMap;
//        LatLng sydney = new LatLng(20.881070, 77.748855);        //,20.881070, 77.748855
//       // LatLng sydney = new LatLng(latitude, longitude);        //,20.881070, 77.748855
//        myMap.addMarker(new MarkerOptions().position(sydney).title("MH27-AW2365"));
//        myMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//    }
//
//
//
//
//
//}
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap myMap;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Check for permission to access location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request location permission if not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            // Permission already granted, fetch location
            getLocation();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void getLocation() {
        // Check if the app has location permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // If permissions are not granted, return and request permissions
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION);
            return;
        }

        // If permissions are granted, continuously fetch the device's location
        fusedLocationClient.requestLocationUpdates(getLocationRequest(), new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // Update map marker with the new location
                if (locationResult != null && locationResult.getLastLocation() != null) {
                    Location location = locationResult.getLastLocation();
                    updateMapMarker(new LatLng(location.getLatitude(), location.getLongitude()));
                }
            }
        }, null);
    }

    private LocationRequest getLocationRequest() {
        com.google.android.gms.location.LocationRequest locationRequest = com.google.android.gms.location.LocationRequest.create();
        locationRequest.setInterval(10000); // Update location every 10 seconds
        locationRequest.setFastestInterval(5000); // The fastest update interval in milliseconds
        locationRequest.setPriority(com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    private void updateMapMarker(LatLng latLng) {
        if (myMap != null) {
            myMap.clear(); // Clear previous markers
//            myMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            myMap.addMarker(new MarkerOptions().position(latLng).title("CAR Location"));

            Switch mapTypeSwitch = findViewById(R.id.map_type_switch);

            // Set OnCheckedChangeListener on the switch
            mapTypeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        // If the switch is checked, apply satellite map type
                        myMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        mapTypeSwitch.setText("Satellite"); // Change switch text
                    } else {
                        // If the switch is unchecked, apply normal map type
                        myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        mapTypeSwitch.setText("Normal"); // Change switch text
                    }
                }
            });

            //myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f)); // Zoom in to the updated location
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, fetch location
                getLocation();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}






