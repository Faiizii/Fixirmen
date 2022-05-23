package com.app.fixirman.view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.se.omapi.Session;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.app.fixirman.R;
import com.app.fixirman.adapter.UserAddressAdapter;
import com.app.fixirman.databinding.ActivityLocationPickerBinding;
import com.app.fixirman.model.user.UserAddress;
import com.app.fixirman.my_interfaces.AdapterOnClickListener;
import com.app.fixirman.utils.AppUtils;
import com.app.fixirman.utils.SessionManager;
import com.app.fixirman.viewmodel.UserViewModel;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

import static com.app.fixirman.adapter.UserAddressAdapter.RV_TYPE_MAP;

public class LocationPickerActivity extends FragmentActivity implements OnMapReadyCallback{
    private final String TAG = "LocationPicker";
    private GoogleMap mMap;
    private LatLng myLatLng;
    private ActivityLocationPickerBinding binding;
    private String addressLine = "",streetAddress = "";
    private boolean isLoaded = false,isSelectedFromSuggestion = false,isSelectedFromSaved = false;
    // isTextEdited = false;//bound the user to select from suggestions or move marker
    private PlacesClient placesClient;
    private UserAddress userAddress;
    @Inject
    ViewModelProvider.Factory factory;
    private UserViewModel viewModel;
    UserAddressAdapter addressAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_location_picker);
        binding.setActivity(this);
        Places.initialize(getApplicationContext(), getResources().getString(R.string.API_key));
        placesClient = Places.createClient(this);

        //get notified when the map is ready to be used.
        try {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }catch (Exception e){
            Log.e(TAG, "onCreate: ",e );
        }
        init();
    }

    private void init() {
        binding.btnConfirm.setOnClickListener(view -> {
            sendResultBack();
        });
        if(getIntent()!=null && getIntent().getExtras() != null){
            Bundle bundle = getIntent().getExtras();
            myLatLng = new LatLng(bundle.getDouble("lat",0),bundle.getDouble("lng",0));
            addressLine = bundle.getString("address","");
            if(!addressLine.isEmpty()){
                isSelectedFromSuggestion = true;
                binding.acAddress.setText(addressLine);
            }else{
                Log.e(TAG, "init: address line is empty" );
            }
        }else{
            Log.e(TAG, "init: intent or extra is null" );
        }
        initLocationSearchView();
        initViewModel();
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this,factory).get(UserViewModel.class);
        viewModel.getAddresses(new SessionManager(this).getUserId()).observe(this,this::loadAddresses);
    }

    private void loadAddresses(List<UserAddress> userAddresses) {
        if(userAddresses != null){
            addressAdapter = new UserAddressAdapter(this,userAddresses,RV_TYPE_MAP);
            binding.rvAddress.setAdapter(addressAdapter);
            addressAdapter.onAddressSelection((view, position) -> {
                userAddress = userAddresses.get(position);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(userAddress.getLatitude(),userAddress.getLongitude()),15)); // move camera to searched address
                isSelectedFromSaved = true;
                binding.acAddress.setText(userAddress.getAddress());
            });
        }
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_back:
                onBackPressed();
                break;
        }
    }
    private void initLocationSearchView() {
        binding.acAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchLocationHints(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    private void searchLocationHints(String query){
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder().setQuery(query).build();

        List<String> placesList = new ArrayList<>();
        placesClient.findAutocompletePredictions(request).addOnSuccessListener((response) -> {
            Log.e(TAG, "testCode: called success method" );
            for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                placesList.add(prediction.getPrimaryText(null).toString());
                placesList.add(prediction.getSecondaryText(null).toString());
                Log.i(TAG, prediction.getPlaceId());

            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_dropdown_item_1line, placesList);
            binding.acAddress.setAdapter(adapter);
            //binding.acAddress.showDropDown();
            binding.acAddress.setOnItemClickListener((adapterView, view, i, l) -> {
                Log.e(TAG, "onItemClick: " + adapter.getItem(i));
                LatLng latLng = getLatLong(adapter.getItem(i));
                if (latLng != null) {
                    isSelectedFromSuggestion = true;
                    isSelectedFromSaved = false;
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15)); // move camera to searched address
                } else {
                    Toast.makeText(LocationPickerActivity.this, "Something went Wrong Unable to Find Address", Toast.LENGTH_SHORT).show();
                }
            });
        }).addOnFailureListener((exception) -> {
            Log.e(TAG, "testCode: ",exception );
            Log.e(TAG, "testCode: called exception" );
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                Log.e(TAG, "Place not found: " + apiException.getStatusCode());
            }
        });
    }

    private LatLng getLatLong(String addressStr) {
        if(mMap != null){
            try {
                List<Address> addresses = new Geocoder(this).getFromLocationName(addressStr,1);
                if(addresses.size() > 0 && addresses.get(0).hasLatitude() && addresses.get(0).hasLongitude()){
                    Address myTempAddress = addresses.get(0);
                    if(myTempAddress.hasLongitude() && myTempAddress.hasLatitude()){
                        return new LatLng(myTempAddress.getLatitude(),myTempAddress.getLongitude());
                    }else{
                        return null;
                    }

                }else{
                    Log.e(TAG, "no address found" );
                    return null;
                }
            } catch (Exception e) {
                Log.e(TAG, "searchOnMap: ", e);
                return null;
            }
        }else{
            Log.e(TAG, "getDefaultLatLong: map not initialized" );
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void sendResultBack(){
        if(isLoaded) {

            Intent resultIntent = new Intent();
            if(isSelectedFromSaved){
                resultIntent.putExtra("object",userAddress);
            }else{
                if(userAddress == null)
                    userAddress = new UserAddress();
                userAddress.setId(0);
                userAddress.setLongitude(myLatLng.longitude);
                userAddress.setLatitude(myLatLng.latitude);
                userAddress.setAddress(streetAddress+","+addressLine);
                userAddress.setUserId(new SessionManager(this).getUserId());
                resultIntent.putExtra("object",userAddress);
            }

            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }else{
            Toast.makeText(this, "Please wait Loading...", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(mMap != null) {
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setZoomGesturesEnabled(true);
            mMap.getUiSettings().setAllGesturesEnabled(true);
            mMap.setOnCameraMoveListener(() -> isSelectedFromSaved = false);
            mMap.setOnCameraIdleListener(() -> {

                isLoaded = true;
                myLatLng = mMap.getCameraPosition().target;
                if(isSelectedFromSaved){
                    return;
                }
                if(addressAdapter != null){
                    addressAdapter.setSelectedItem(-1); //reset selection
                }
                if (myLatLng != null) {
                    Log.e(TAG, "onCameraIdle: lat lng are " + myLatLng.toString());
                    if(isSelectedFromSuggestion){
                        streetAddress = "";
                        addressLine = binding.acAddress.getText().toString().trim();
                    }else{
                        getAddressFromPosition(myLatLng);
                    }
                    isSelectedFromSuggestion = false;
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
                   //save location
                    Log.e(TAG, "getLocation: animate camera to default location" );
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16));
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
                        //save location
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16));
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
                    binding.acAddress.setText(addressLine);
                }else if(tempAddress.getMaxAddressLineIndex() >= 0){
                    addressLine = tempAddress.getAddressLine(0);
                    binding.acAddress.setText(tempAddress.getAddressLine(0));
                }else{
                    binding.acAddress.setText(addressLine);
                }
            }
        }else{
            Log.e(TAG, "getAddressFromPosition: Location latlng are null" );
        }
    }

    private void showSearchProgress(){
        AppUtils.showProgressBar(binding.pbSearchAddress);
    }
    private void hideSearchProgress(){
        AppUtils.hideProgressBar(binding.pbSearchAddress);
    }
}
