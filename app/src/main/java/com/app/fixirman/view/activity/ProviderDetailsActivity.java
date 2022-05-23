package com.app.fixirman.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.app.fixirman.R;
import com.app.fixirman.adapter.ReviewsAdapter;
import com.app.fixirman.databinding.ActivityProviderDetailsBinding;
import com.app.fixirman.model.categroy.ServiceProviders;
import com.app.fixirman.model.provider.ProviderDetail;
import com.app.fixirman.model.provider.ReviewsModel;
import com.app.fixirman.utils.AppConstants;
import com.app.fixirman.utils.AppUtils;
import com.app.fixirman.utils.SessionManager;
import com.app.fixirman.viewmodel.AppointmentViewModel;
import com.bumptech.glide.Glide;
import com.app.fixirman.utils.Crashlytics;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ProviderDetailsActivity extends AppCompatActivity {
    private final String TAG = "ProviderDetails";

    private ActivityProviderDetailsBinding binding;

    @Inject
    ViewModelProvider.Factory factory;
    private AppointmentViewModel viewModel;

    private SessionManager sessionManager;
    private String subCategoryId = "",employeeId = "";
    private ServiceProviders serviceProviders;
    private ProviderDetail providerDetail;
    private ReviewsAdapter reviewsAdapter;
    private List<ReviewsModel> reviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_provider_details);
        binding.setActivity(this);
        initMe();
    }

    private void initMe() {
        sessionManager = new SessionManager(this);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            serviceProviders = bundle.getParcelable("service_provider");
            if(serviceProviders != null){
                subCategoryId = serviceProviders.getSubCategoryId();
                employeeId = serviceProviders.getEmployeeId();
            }
        }
        binding.tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()){
                    case 0:
                        handleVisibility(VISIBLE,GONE);
                        break;
                    case 1:
                        handleVisibility(GONE,VISIBLE);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        initReviewAdapter();
        if(subCategoryId.isEmpty() || employeeId.isEmpty()){
            Crashlytics.log("unable to find the data  IDs at providers detail screen");
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }else{
            initViewModel();
        }
    }

    private void initReviewAdapter() {
        reviews = new ArrayList<>();
        reviewsAdapter = new ReviewsAdapter(this,reviews);
        binding.rvReviews.setAdapter(reviewsAdapter);
    }

    private void handleVisibility(int about, int rating) {
        binding.tvAbout.setVisibility(about);
        if(rating == VISIBLE){
            if(reviews.size() > 0){
                reviewsDataFound();
            }else{
                reviewDataNotFound();
            }
        }else{
            binding.rvReviews.setVisibility(rating);
            binding.tvNoDataFound.setVisibility(rating);
        }
    }

    private void reviewDataNotFound() {
        binding.tvNoDataFound.setVisibility(VISIBLE);
        binding.rvReviews.setVisibility(GONE);
    }

    private void reviewsDataFound() {
        binding.tvNoDataFound.setVisibility(GONE);
        binding.rvReviews.setVisibility(VISIBLE);
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this,factory).get(AppointmentViewModel.class);

        if(AppUtils.isNetworkAvailable(this)){
            viewModel.init(binding.pb,sessionManager.getUserId(),subCategoryId,employeeId);
        }else{
            Crashlytics.log("provider detail is opened without internet");
        }
        viewModel.getProviderDetail(employeeId).observe(this,this::updateUI);
        viewModel.getRatingAvg(employeeId).observe(this, integer -> {
            if(integer != null){
                binding.btnRatingStars.setText(integer+"");
            }
        });
        viewModel.getTotalReviews(employeeId).observe(this,integer -> {
            if(integer != null){
                binding.tvRatingCount.setText(integer+"");
            }
        });
        viewModel.getReviews(employeeId).observe(this,this::updateUI);
    }

    private void updateUI(List<ReviewsModel> reviewsModels) {
        if(reviewsModels != null) {
            if (reviewsModels.size() > 0) {
                reviews.clear();
                reviews.addAll(reviewsModels);
                reviewsAdapter.notifyDataSetChanged();
            }
        }
    }
    private void updateUI(ProviderDetail providerDetail) {
        if(providerDetail != null){
            this.providerDetail = providerDetail;
            binding.setModel(providerDetail);
            Glide.with(this).load(AppConstants.HOST_URL+providerDetail.getImage())
                    .placeholder(R.drawable.default_user).into(binding.ivProfilePic);
        }else{
            Log.e(TAG, "updateUI: model is null" );
        }
    }
    public void onClick(View v){
        int id = v.getId();
        if (id == R.id.btn_back) {
            onBackPressed();
        } else if (id == R.id.mb_bookNow) {
            if (AppUtils.isNetworkAvailable(this)) {

            } else {
                AppUtils.createNetworkError(this);
            }
        }
    }
}