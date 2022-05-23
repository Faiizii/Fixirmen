package com.app.fixirman.view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.app.fixirman.R;
import com.app.fixirman.api.http.ApiUtils;
import com.app.fixirman.databinding.ActivityChangeAddressBinding;
import com.app.fixirman.model.user.UserAddress;
import com.app.fixirman.model.user.UserResponse;
import com.app.fixirman.utils.AppConstants;
import com.app.fixirman.utils.AppUtils;
import com.app.fixirman.utils.SessionManager;
import com.app.fixirman.viewmodel.UserViewModel;
import com.app.fixirman.utils.Crashlytics;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.gson.stream.MalformedJsonException;

import java.net.SocketTimeoutException;
import java.time.OffsetDateTime;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeAddressActivity extends AppCompatActivity implements OnMapReadyCallback {

    private final String TAG  = "ChangeAddress";
    private ActivityChangeAddressBinding binding;
    private SessionManager sessionManager;

    private GoogleMap mMap;
    private LatLng myLatLng,currentLatLng;
    private String addressLine = "",streetAddress = "",addressType;
    private int addressId = 0;
    private static final String HOME = "home";
    private static final String OFFICE = "office";
    private static final String OTHER = "other";

    @Inject
    ViewModelProvider.Factory factory;
    private UserViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        // Inflate the layout for this fragment
        binding = DataBindingUtil.setContentView(this,R.layout.activity_change_address);
        binding.setActivity(this);
        intiMe();
    }

    private void intiMe() {
        sessionManager = new SessionManager(this);
        try {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }catch (Exception e){
            Log.e(TAG, "onCreate: ",e );
        }
        if(getIntent() != null){
            addressId = getIntent().getIntExtra("address_id",0);
        }
        initViewModel();
        updateButtonSelection(true,false,false);
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this,factory).get(UserViewModel.class);
    }

    public void onClick(View v){
        switch (v.getId()){

            case R.id.btn_back:
                if(!isUpdating){
                    onBackPressed();
                }else{
                    Toast.makeText(this, "Please wait...", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_home:
                updateButtonSelection(true,false,false);
                break;
            case R.id.btn_office:
                updateButtonSelection(false,true,false);
                break;
            case R.id.btn_other:
                updateButtonSelection(false,false,true);
                break;
            case R.id.btn_addAddress:
                if(AppUtils.isNetworkAvailable(this)){
                    if(validate()){
                        updateAddressOnServer();
                    }
                }else{
                    AppUtils.createNetworkError(this);
                }
                break;
        }
    }

    private boolean validate() {
        if(myLatLng == null){
            Toast.makeText(this, "Location Unknown. Please select location", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(addressLine == null){
            Toast.makeText(this, "Location Unknown. Please select location", Toast.LENGTH_SHORT).show();
            return false;
        }else if(addressLine.isEmpty()){
            Toast.makeText(this, "Location Unknown. Please select location", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void updateButtonSelection(boolean isHome,boolean isOffice, boolean isOther){
        addressType = isHome ? HOME : isOffice ?  OFFICE : OTHER;
        binding.btnHome.setStrokeColor(AppCompatResources.getColorStateList(this,isHome ? R.color.colorAccent : android.R.color.transparent ));
        binding.btnOffice.setStrokeColor(AppCompatResources.getColorStateList(this,isOffice ? R.color.colorAccent : android.R.color.transparent ));
        binding.btnOther.setStrokeColor(AppCompatResources.getColorStateList(this,isOther ? R.color.colorAccent : android.R.color.transparent ));
    }

    private void updateAddressOnServer(){
        showProgressBar();
        ApiUtils.getApiInterface().updateAddressOnServer(sessionManager.getUserId(),addressLine,binding.etAddress.getText().toString().trim(),
                myLatLng.latitude,myLatLng.longitude,addressType,addressId)
                .enqueue(new Callback<UserResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                        hideProgressBar();
                        if(response.isSuccessful()){
                            UserResponse userResponse = response.body();
                            if(userResponse.getSuccess() == 1){
                                viewModel.clearUserAddresses(sessionManager.getUserId());
                                viewModel.insertAddress(userResponse.getUserAddress());
                                Toast.makeText(ChangeAddressActivity.this, userResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                //reset the location to current location
                                updateButtonSelection(true,false,false);
                                binding.etAddress.setText("");
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16));

                            }else{
                                onApiCallFailed(userResponse.getMessage());
                            }
                        }else{
                            onApiCallFailed("Server Connection Error");
                            Crashlytics.log("response code "+response.code());
                            Crashlytics.logException(new Exception("update address: "+response.message()));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<UserResponse> call,@NonNull Throwable t) {
                        hideProgressBar();
                        AppUtils.handleResponse(ChangeAddressActivity.this,t,"update address api");
                    }
                });
    }
    private void onApiCallFailed(String message) {
        AppUtils.createFailedDialog(this,message);

    }
    private boolean isUpdating = false;
    private void showProgressBar(){
        isUpdating = true;
        AppUtils.showProgressBar(binding.pb);
    }
    private void hideProgressBar(){
        isUpdating = false;
        AppUtils.hideProgressBar(binding.pb);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(mMap != null) {

            mMap.setOnCameraIdleListener(() -> {

                myLatLng = mMap.getCameraPosition().target;
                if (myLatLng != null) {
                    getAddressFromPosition(myLatLng);
                }else{
                    Log.e(TAG, "onMapReady: lat lng are null" );
                }
            });
            if(myLatLng != null && myLatLng.latitude > 0 && myLatLng.longitude > 0){
                Log.e(TAG, "onMapReady: animate camera to fetch lat lng" );
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, 16));
            }else{
                getLocation();
            }
        }else{
            Log.e(TAG, "onMapReady: map not ready" );
        }
    }
    private FusedLocationProviderClient fusedLocationProviderClient;

    private void getLocation() {

        Log.e(TAG, "onMapReady: get default or current location called" );
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        } else {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                if(location != null && mMap != null) {
                    currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16));
                }else{
                    Log.e(TAG, "getLocation: can't move camera location or map is null" );
                }
            });
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if(fusedLocationProviderClient == null)
                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                    if(location != null && mMap != null){
                        currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16));
                    }else{
                        Log.e(TAG, "onRequestPermissionsResult: map or location is null in result" );
                    }
                });

            }else{
                Toast.makeText(this, "Error: Permission Not Granted", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onRequestPermissionsResult: permission not granted" );
            }
        }
    }
    private void getAddressFromPosition(LatLng latLng){
        if(latLng != null) {
            Address tempAddress = null;
            try {
                List<Address> addresses = new Geocoder(this).getFromLocation(latLng.latitude, latLng.longitude, 1);
                if (addresses.size() > 0 && addresses.get(0).hasLatitude() && addresses.get(0).hasLongitude()) {
                    tempAddress = addresses.get(0);
                    if(tempAddress.getMaxAddressLineIndex() >= 0){
                        streetAddress = tempAddress.getSubThoroughfare();
                        addressLine = tempAddress.getCountryName();
                        addressLine = tempAddress.getAdminArea().concat(", ").concat(addressLine);
                        addressLine = tempAddress.getLocality().concat(", ").concat(addressLine);
                        addressLine = tempAddress.getSubLocality().concat(", ").concat(addressLine);
                    }
                } else {
                    Log.e(TAG, "no address found");
                }
            } catch (Exception e) {
                Log.e(TAG, "searchOnMap: ", e);
            }finally {
                if(tempAddress == null){
                    binding.tvLocationAddress.setText(addressLine);
                }else if(tempAddress.getMaxAddressLineIndex() >= 0){
                    addressLine = tempAddress.getAddressLine(0);
                    binding.tvLocationAddress.setText(tempAddress.getAddressLine(0));
                }else{
                    binding.tvLocationAddress.setText(addressLine);
                }
            }
        }else{
            Log.e(TAG, "getAddressFromPosition: Location latlng are null" );
        }
    }
}