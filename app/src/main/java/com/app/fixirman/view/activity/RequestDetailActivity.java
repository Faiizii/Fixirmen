package com.app.fixirman.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.app.fixirman.R;
import com.app.fixirman.adapter.BidderAdapter;
import com.app.fixirman.api.http.ApiUtils;
import com.app.fixirman.databinding.ActivityRequestDetailBinding;
import com.app.fixirman.model.NormalResponse;
import com.app.fixirman.model.request.RequestBid;
import com.app.fixirman.model.request.RequestDetail;
import com.app.fixirman.utils.AppConstants;
import com.app.fixirman.utils.AppUtils;
import com.app.fixirman.utils.Crashlytics;
import com.app.fixirman.utils.SessionManager;
import com.app.fixirman.view.create_request.RequestViewModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class RequestDetailActivity extends AppCompatActivity implements OnMapReadyCallback {
    private final String TAG = "requestDetail";

    private ActivityRequestDetailBinding binding;
    private RequestDetail model;

    private LatLng mapLatLng;
    private GoogleMap googleMap;

    @Inject
    ViewModelProvider.Factory factory;
    private RequestViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_request_detail);
        binding.setActivity(this);
        initMe();
    }

    private void initMe() {
        viewModel = new ViewModelProvider(this,factory).get(RequestViewModel.class);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }else{
            Crashlytics.log(TAG+"map fragment instance is null");
        }

        if(getIntent() != null && getIntent().getParcelableExtra("object") != null){
            model = getIntent().getParcelableExtra("object");
            updateUI(model);
        }
        String tempReqId = getIntent().getStringExtra("request_id");
        int id = 0;
        if(model != null){
            id = model.getId();
        }else if(tempReqId != null && !tempReqId.isEmpty()){
            id = Integer.parseInt(tempReqId);
        }

        if(AppUtils.isNetworkAvailable(this)){
            viewModel.getOnlineRequestDetail(binding.pb,id,new SessionManager(this).getUserId());
        }else{
            AppUtils.createNetworkError(this);
        }
        viewModel.getRequestDetail(id).observe(this, requestDetail -> {
            if(requestDetail != null){
                updateUI(requestDetail);
            }
        });
    }

    private void updateUI(RequestDetail m) {
        if(model != null){
            this.model = m;
            binding.setModel(model);
            Glide.with(this).load(AppConstants.HOST_URL+model.getCategoryImage()).into(binding.ivCategoryImage);
            mapLatLng = new LatLng(model.getLatitude(),model.getLongitude());
            if(googleMap != null){
                MarkerOptions options = new MarkerOptions();
                options.position(mapLatLng);
                this.googleMap.clear();
                this.googleMap.addMarker(options);
                this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapLatLng,14));
            }
            if(model.getWorkerId() > 0){
                setWorkerDetail(model);
            }else{
                viewModel.getBidders(model.getId()).observe(this, this::updateUI);
            }
            handleStatus(model.getStatus());
        }else{
            Toast.makeText(this, "Invalid detail Please try again", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void handleStatus(String status) {
        switch (status){
            case "ACCEPT":
            case "START":
                handleUI(GONE,GONE,VISIBLE,GONE,GONE);
                break;
            case "FINISH":
                handleUI(GONE,GONE,GONE,VISIBLE,GONE);
                break;
            case "CANCEL":
            case "FAILED":
                handleUI(GONE,GONE,GONE,GONE,GONE);
                break;
            case "RATED":
                handleUI(GONE,GONE,GONE,GONE,VISIBLE);
                break;
            default:
                handleUI(VISIBLE,GONE,GONE,GONE,GONE);
        }
    }

    private void updateUI(List<RequestBid> requestBids) {
        if(requestBids != null){
            if(requestBids.size() > 0){
                BidderAdapter adapter = new BidderAdapter(this,requestBids);
                binding.rvBidders.setAdapter(adapter);
                handleUI(GONE,VISIBLE,GONE,GONE,GONE);
                adapter.onItemClickListener((view, Position) -> viewModel.getOnlineRequestDetail(binding.pb,model.getId(),new SessionManager(this).getUserId()));
            }else{
                //TODO show still searching
            }
        }else {
            //TODO show still searching
        }
    }

    private void setWorkerDetail(RequestDetail model) {
        Glide.with(this).load(AppConstants.HOST_URL+model.getProviderImage()).apply(
                new RequestOptions().error(R.drawable.default_user))
                .fitCenter()
                .placeholder(AppUtils.getImageLoader(this))
                .into(binding.ivWorkerProfileImage);
    }
    private void handleUI(int searching, int bidders, int workerDetail, int rating, int thankYou){
        binding.layoutSearching.setVisibility(searching);
        binding.rvBidders.setVisibility(bidders);
        binding.layoutWorkerDetail.setVisibility(workerDetail);
        binding.layoutRating.setVisibility(rating);
        binding.layoutThankYou.setVisibility(thankYou);
    }
    public void onClick(View v){
        if (v.getId() == R.id.btn_back) {
            onBackPressed();
        }else if(v.getId() == R.id.btn_submitRating){
            if(AppUtils.isNetworkAvailable(this)){
                if(binding.rbReview.getRating() < 1){
                    AppUtils.createFailedDialog(this,"Please choose the rating first");
                }else{
                    submitRating(binding.rbReview.getRating(),binding.etReview.getText().toString());
                }
            }else{
                AppUtils.createNetworkError(this);
            }
        }
    }

    private void submitRating(float rating, String reviewMessage) {
        int userId = new SessionManager(this).getUserId();
        binding.btnSubmitRating.setEnabled(false);
        Toast.makeText(this, "Submitting please wait...", Toast.LENGTH_SHORT).show();
        ApiUtils.getApiInterface().submitRating(model.getWorkerId(),model.getId(),model.getCategoryId(),rating,reviewMessage,userId)
                .enqueue(new Callback<NormalResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<NormalResponse> call,@NonNull Response<NormalResponse> response) {
                        binding.btnSubmitRating.setEnabled(true);
                        if(response.isSuccessful()){
                            NormalResponse normalResponse = response.body();
                            if(normalResponse.getSuccess() == 1){
                                handleUI(GONE,GONE,GONE,GONE,VISIBLE);//show thank you layout
                            }
                            Toast.makeText(RequestDetailActivity.this, normalResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(RequestDetailActivity.this, "Server Connection Error. Please try again...", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<NormalResponse> call,@NonNull Throwable t) {
                        binding.btnSubmitRating.setEnabled(true);
                        AppUtils.handleResponse(RequestDetailActivity.this,t,"submit rating api");
                    }
                });
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