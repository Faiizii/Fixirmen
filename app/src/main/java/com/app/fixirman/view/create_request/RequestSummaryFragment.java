package com.app.fixirman.view.create_request;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.app.fixirman.R;
import com.app.fixirman.adapter.CategoryAdapter;
import com.app.fixirman.adapter.RequestItemAdapter;
import com.app.fixirman.databinding.DialogSelectCategoryBinding;
import com.app.fixirman.databinding.FragmentRequestSummaryBinding;
import com.app.fixirman.model.categroy.Category;
import com.app.fixirman.model.user.UserAddress;
import com.app.fixirman.my_interfaces.AdapterOnClickListener;
import com.app.fixirman.utils.LocationHandler;
import com.app.fixirman.utils.SessionManager;
import com.app.fixirman.view.activity.LocationPickerActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

import static android.app.Activity.RESULT_OK;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.app.fixirman.utils.LocationHandler.PERMISSION_REQUEST_CODE;

public class RequestSummaryFragment extends Fragment implements LocationHandler.RequestCallBack, OnMapReadyCallback {
    private final String TAG = "RequestSummary";
    @Inject
    ViewModelProvider.Factory factory;
    private RequestViewModel viewModel;

    private FragmentRequestSummaryBinding binding;
    private FragmentActivity activity;

    private RequestItemAdapter adapter;
    private ArrayMap<Integer,RequestModel> list;

    private LatLng mapLatLng;
    private GoogleMap googleMap;
    private LocationHandler locationHandler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_request_summary, container, false);
        binding.setFragment(this);
        activity = getActivity();
        initMe();
        return binding.getRoot();
    }

    private void initMe() {
        initAdapter();
        try {
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }catch (Exception e){
            Log.e(TAG, "onCreate: ",e );
        }
        //init location handler
        locationHandler = new LocationHandler(activity);
        locationHandler.setRequestOneTime(true);
        locationHandler.permissionInFragment(this);
        locationHandler.getLastSavedLocation();
        locationHandler.setLocationCallBack(this);
        locationHandler.setRequestAccuracy(100);
    }

    private void initAdapter() {
        list = new ArrayMap<>();
        adapter = new RequestItemAdapter(activity,list);
        binding.rvSelectServices.setAdapter(adapter);
        adapter.onRemoveClickListener((view, position) -> {
            int keyId = adapter.getKeyId(position);
            viewModel.removeItem(keyId);
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(activity,factory).get(RequestViewModel.class);
        viewModel.getSelectedItems().observe(getViewLifecycleOwner(),this::updateUI);
    }

    private void updateUI(ArrayMap<Integer, RequestModel> selectedList) {
        if(selectedList != null){
            list.clear();
            list.putAll(selectedList);
            adapter.notifyDataSetChanged();
        }else{

        }
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_addService:
                if(viewModel.isFirstAdded()){
                    //show category listing for selection
                    openCategoryDialog();
                }else{
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, new RequestScheduleFragment()).addToBackStack("schedule")
                            .commit();
                }
                break;
            case R.id.view_tap:
            case R.id.tv_address:
            case R.id.tv_tap:
                Intent locationPicker = new Intent(activity, LocationPickerActivity.class);
                if(mapLatLng != null) {
                    locationPicker.putExtra("lat", mapLatLng.latitude);
                    locationPicker.putExtra("lng", mapLatLng.longitude);
                    locationPicker.putExtra("address", binding.tvAddress.getText().toString().trim());
                }
                startActivityForResult(locationPicker,16);
                break;
        }
    }

    private void openCategoryDialog() {
        Dialog dialog = new Dialog(activity);
        DialogSelectCategoryBinding dialogBinding = DataBindingUtil.inflate(LayoutInflater.from(activity),
                R.layout.dialog_select_category,null,false);
        dialog.setContentView(dialogBinding.getRoot());
        Window window = dialog.getWindow();
        if(window != null){
            window.setLayout(MATCH_PARENT,MATCH_PARENT);
        }
        CategoryAdapter adapter = new CategoryAdapter(activity,viewModel.getCategoryList());
        dialogBinding.rvCategories.setAdapter(adapter);
        adapter.itemClickListener((view, position) -> {
            Category model = adapter.getItem(position);
            if(viewModel.isAlreadyAdded(model.getId())){
                Toast.makeText(activity, "Service is already added", Toast.LENGTH_SHORT).show();
                return;
            }
            viewModel.addCategory(model);
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new RequestScheduleFragment()).addToBackStack("schedule")
                    .commit();
            dialog.dismiss();
        });
        dialogBinding.btnDismiss.setOnClickListener(v->{
            dialog.dismiss();
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == 16){

            UserAddress userAddress = data.getParcelableExtra("object");
            if(userAddress != null && viewModel != null){
                viewModel.setAddress(userAddress);
                updateMarker();
            }


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //Toast.makeText(activity, "called", Toast.LENGTH_SHORT).show();
        if (requestCode == PERMISSION_REQUEST_CODE
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            locationHandler.getLastSavedLocation();
        }
    }



    @Override
    public void onResume() {
        super.onResume();
        ((CreateRequestActivity)activity).updateTitle("Summary");
    }
    @Override
    public void sendLocation(Location location) {
        if(location != null) {
            mapLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            String addressStr = locationHandler.getAddress(location.getLatitude(), location.getLongitude());
            binding.tvAddress.setText(addressStr);
            if(viewModel != null) {
                UserAddress address = new UserAddress();
                address.setUserId(0);
                address.setAddress(addressStr);
                address.setUserId(new SessionManager(activity).getUserId());
                address.setLatitude(location.getLatitude());
                address.setLongitude(location.getLongitude());
                address.setAddressType("other");
                viewModel.setAddress(address);
            }
            updateMarker();
        }
    }

    private void updateMarker() {
        if(viewModel != null && viewModel.getAddress() != null && viewModel.getAddress().getLatitude() > 0 && viewModel.getAddress().getLongitude() > 0)
        if(googleMap != null && viewModel != null){
            UserAddress userAddress = viewModel.getAddress();
            if(userAddress != null && userAddress.getLatitude() > 0 && userAddress.getLongitude() > 0){
                mapLatLng = new LatLng(userAddress.getLatitude(),userAddress.getLongitude());
                binding.tvAddress.setText(userAddress.getAddress());
            }
            MarkerOptions options = new MarkerOptions();
            options.position(mapLatLng);
            this.googleMap.clear();
            this.googleMap.addMarker(options);
            this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapLatLng,14));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        if(this.googleMap != null) {
            this.googleMap.getUiSettings().setAllGesturesEnabled(false);
            this.googleMap.getUiSettings().setZoomControlsEnabled(false);
            this.googleMap.getUiSettings().setZoomGesturesEnabled(false);
            if(mapLatLng != null){
                MarkerOptions options = new MarkerOptions();
                options.position(mapLatLng);
                this.googleMap.clear();
                this.googleMap.addMarker(options);
                this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapLatLng,14));
            }
        }else{
            Log.e(TAG, "onMapReady: map not ready" );
        }
    }
}