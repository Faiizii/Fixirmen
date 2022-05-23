package com.app.fixirman.utils;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.List;
import java.util.Locale;

import static com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY;

public class LocationHandler {
    private final String TAG = "LocationHandler";
    private FragmentActivity context;
    private FusedLocationProviderClient locationClient;
    public static LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private boolean requestOneTime;
    private float requestAccuracy = 100;
    private RequestCallBack requestCallBack;
    private Geocoder geocoder;

    //just to make sure user cant initialized it with default constructor
    private LocationHandler(){}
    public LocationHandler getInstance(FragmentActivity context){
        this.context = context;
        return new LocationHandler();
    }
    public LocationHandler(FragmentActivity context){
        this.context = context;
        geocoder = new Geocoder(context, Locale.getDefault());
        locationClient = LocationServices.getFusedLocationProviderClient(context);
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if(location.getAccuracy() < requestAccuracy){

                        if(requestCallBack != null){
                            requestCallBack.sendLocation(location);
                        }
                        if(requestOneTime){
                            locationClient.removeLocationUpdates(locationCallback);
                        }
                    }
                }
            }

            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
            }
        };
        locationRequest = new LocationRequest();
        locationRequest.setPriority(PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(GPS_MIN_TIME_LOCATION_SYNC);
        locationRequest.setSmallestDisplacement(GPS_MIN_DISTANCE_LOCATION_SYNC);
    }
    public void registerLocationUpdates(){
        if (ActivityCompat.checkSelfPermission( context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        } else {
            locationClient.requestLocationUpdates(locationRequest,locationCallback, Looper.getMainLooper());
        }
    }
    public void registerLocationUpdates(boolean isOneTime){
        if (ActivityCompat.checkSelfPermission( context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        } else {
            setRequestOneTime(isOneTime);
            locationClient.requestLocationUpdates(locationRequest,locationCallback, Looper.getMainLooper());
        }
    }

    public void permissionInFragment(Fragment fragment){
        if (ActivityCompat.checkSelfPermission( context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            fragment.requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
        }
    }

    public void getLastSavedLocation() {

        if (ActivityCompat.checkSelfPermission( context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        } else {
            locationClient.getLastLocation().addOnSuccessListener(location -> {
                if(location != null) {
                    if(requestCallBack != null){
                        requestCallBack.sendLocation(location);
                    }
                }
            });
        }
    }
    public String getAddress(double lat,double lng){
        try {
            List<Address> addressList = geocoder.getFromLocation(lat,lng,1);
            //Lahore punjab pakistan
            //return addressList.get(0).getSubAdminArea()+", "+addressList.get(0).getAdminArea()+", "+addressList.get(0).getCountryName();
            return addressList.get(0).getAddressLine(0);
        } catch (Exception e) {
            Log.e(TAG, "getAddress: ", e);
            return "Not Found";
        }
    }
    public String getCompleteAddress(double lat,double lng){
        try {
            List<Address> addressList = geocoder.getFromLocation(lat,lng,1);
            //Lahore punjab pakistan
            return addressList.get(0).getAddressLine(0);
        } catch (Exception e) {
            Log.e(TAG, "getAddress: ", e);
            return "Not Found";
        }
    }

    public void setLocationCallBack(RequestCallBack locationCallBack) {

        this.requestCallBack = locationCallBack;
    }

    public boolean isRequestOneTime() {

        return requestOneTime;
    }

    public void setRequestOneTime(boolean requestOneTime) {

        this.requestOneTime = requestOneTime;
    }

    public float getRequestAccuracy() {

        return requestAccuracy;
    }

    public void setRequestAccuracy(float requestAccuracy) {

        this.requestAccuracy = requestAccuracy;
    }
    public interface RequestCallBack{
        public void sendLocation(Location location);
    }
    public static  final int PERMISSION_REQUEST_CODE = 101;
    //gps gives the less frequent location but accurate location,moreover give gps enough time to be ready for preparing update
    public static final long GPS_MIN_TIME_LOCATION_SYNC = 10*1000;
    public static final long GPS_MIN_DISTANCE_LOCATION_SYNC = 5;

}
