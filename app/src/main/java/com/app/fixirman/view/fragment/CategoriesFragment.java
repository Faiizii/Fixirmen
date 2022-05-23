package com.app.fixirman.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.fixirman.R;
import com.app.fixirman.adapter.AdsSliderAdapter;
import com.app.fixirman.adapter.CategoryAdapter;
import com.app.fixirman.databinding.FragmentCategoriesBinding;
import com.app.fixirman.model.categroy.Category;
import com.app.fixirman.model.user.AdModel;
import com.app.fixirman.my_interfaces.AdapterOnClickListener;
import com.app.fixirman.utils.AppUtils;
import com.app.fixirman.utils.LocationHandler;
import com.app.fixirman.utils.SessionManager;
import com.app.fixirman.view.activity.MainActivity;
import com.app.fixirman.view.create_request.CreateRequestActivity;
import com.app.fixirman.viewmodel.CategoryViewModel;
import com.app.fixirman.utils.Crashlytics;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.app.fixirman.utils.LocationHandler.PERMISSION_REQUEST_CODE;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoriesFragment extends Fragment implements LocationHandler.RequestCallBack {
    private final String TAG = "CategoriesFragment";

    private FragmentActivity activity;
    private FragmentCategoriesBinding binding;

    @Inject
    ViewModelProvider.Factory factory;
    private CategoryViewModel viewModel;

    private List<Category> allCategories,adapterList;
    private CategoryAdapter categoryAdapter;
    private SessionManager sessionManager;
    private LocationHandler locationHandler;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_categories, container, false);
        binding.setFragment(this);
        activity = getActivity();
        initMe();
        return binding.getRoot();
    }

    private void initMe() {
        sessionManager = new SessionManager(activity);
        binding.tabDots.setupWithViewPager(binding.vpAds,true);
        //init location handler
        locationHandler = new LocationHandler(activity);
        locationHandler.setRequestOneTime(true);
        locationHandler.permissionInFragment(this);
        locationHandler.getLastSavedLocation();
        locationHandler.setLocationCallBack(this);
        locationHandler.setRequestAccuracy(100);

        initCategoryAdapter();
    }

    private void initCategoryAdapter() {
        adapterList = new ArrayList<>();
        allCategories = new ArrayList<>();

        categoryAdapter = new CategoryAdapter(activity,adapterList);
        binding.rvCategories.setAdapter(categoryAdapter);
        categoryAdapter.itemClickListener((view, position) -> {
            Intent i = new Intent(activity, CreateRequestActivity.class);
            i.putExtra("object",categoryAdapter.getItem(position));
            activity.startActivity(i);
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
        viewModel = new ViewModelProvider(this,factory).get(CategoryViewModel.class);

        if(AppUtils.isNetworkAvailable(activity)){
            viewModel.init(binding.pb,sessionManager.getUserId());
        }else{
            Crashlytics.log("category screen open without network");
        }

        viewModel.getCategories().observe(getViewLifecycleOwner(), this::updateUI);
        viewModel.getAds().observe(getViewLifecycleOwner(),this::initAdsAdapter);
    }

    private void initAdsAdapter(List<AdModel> adModels) {
        if(adModels != null){
            if(adModels.size() > 0){
                AdsSliderAdapter adsSliderAdapter = new AdsSliderAdapter(adModels,activity);
                binding.vpAds.setAdapter(adsSliderAdapter);

                binding.vpAds.setVisibility(View.VISIBLE);
                binding.tvNoAds.setVisibility(GONE);
            }else{
                binding.vpAds.setVisibility(View.INVISIBLE);
                binding.tvNoAds.setVisibility(VISIBLE);
            }
        }else{
            binding.vpAds.setVisibility(View.INVISIBLE);
            binding.tvNoAds.setVisibility(VISIBLE);
        }
    }


    private void updateUI(List<Category> categories) {
        if(categories != null){
            if(categories.size() > 0){
                allCategories.clear();
                adapterList.clear();
                allCategories.addAll(categories);
                adapterList.addAll(categories);
                categoryAdapter.notifyDataSetChanged();
                dataFound();
            }else{
                noData();
            }
        }else{
            //false notify here
        }
    }

    private void noData() {
        binding.rvCategories.setVisibility(GONE);
        binding.tvNoDataFound.setVisibility(VISIBLE);
    }

    private void dataFound() {
        binding.tvNoDataFound.setVisibility(GONE);
        binding.rvCategories.setVisibility(VISIBLE);
    }

    public void onClick(View v){
        AppUtils.hideSoftKeyboard(activity);
    }
    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)activity).setSelectIndex(1);
        ((MainActivity)activity).visibleNavigationView(VISIBLE);
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
    public void sendLocation(Location location) {
        if(location != null)
            binding.tvAddress.setText(locationHandler.getAddress(location.getLatitude(),location.getLongitude()));
    }
}
