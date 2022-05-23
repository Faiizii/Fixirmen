package com.app.fixirman.view.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.fixirman.R;
import com.app.fixirman.adapter.ServiceProviderAdapter;
import com.app.fixirman.databinding.FragmentProvidersBinding;
import com.app.fixirman.model.categroy.ServiceProviders;
import com.app.fixirman.utils.AppConstants;
import com.app.fixirman.utils.AppUtils;
import com.app.fixirman.utils.SessionManager;
import com.app.fixirman.view.activity.MainActivity;
import com.app.fixirman.viewmodel.CategoryViewModel;
import com.app.fixirman.utils.Crashlytics;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ProvidersFragment extends Fragment {

    private final String TAG = "ProvidersFragment";

    private FragmentActivity activity;
    @Inject
    ViewModelProvider.Factory factory;
    private CategoryViewModel viewModel;

    private FragmentProvidersBinding binding;
    private List<ServiceProviders> adapterList,allList;
    private ServiceProviderAdapter adapter;

    private String categoryId = "",subCategoryId = "";


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_providers, container, false);
        binding.setFragment(this);
        activity = getActivity();
        initMe();
        return binding.getRoot();
    }

    private void initMe() {
        adapterList = new ArrayList<>();
        allList = new ArrayList<>();
        //check data
        Bundle bundle = getArguments();
        if(bundle != null){
            categoryId = bundle.getString(AppConstants.CATEGORY_ID,"");
            subCategoryId = bundle.getString(AppConstants.SUB_CATEGORY_ID,"");
            binding.tvSubCategoryName.setText(bundle.getString(AppConstants.SUB_CATEGORY_NAME,""));
            binding.tvTitle.setText(bundle.getString(AppConstants.CATEGORY_NAME,"Back"));
            adapter = new ServiceProviderAdapter(activity,adapterList);
            binding.rvSubCategory.setLayoutManager(new LinearLayoutManager(activity));
            binding.rvSubCategory.setAdapter(adapter);
        }
    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_back:
                activity.onBackPressed();
                break;
        }
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
        if(AppUtils.isNetworkAvailable(activity)) {
            viewModel.init(binding.pb, new SessionManager(activity).getUserId(), categoryId, subCategoryId);
        }else{
            Crashlytics.log("provider screen opened without network");
        }
        viewModel.getServiceProviders(categoryId,subCategoryId).observe(getViewLifecycleOwner(),this::updateUI);
    }

    public void updateUI(List<ServiceProviders> list){
        if(list != null){
            if(list.size() > 0){
                allList.clear(); adapterList.clear();
                allList.addAll(list); adapterList.addAll(list);
                adapter.notifyDataSetChanged();
                dataFound();
            }else{
                noData();
            }
        }else{
            //false positive notification
        }
    }
    private void noData() {
        binding.rvSubCategory.setVisibility(GONE);
        binding.tvNoDataFound.setVisibility(View.VISIBLE);
    }

    private void dataFound() {
        binding.tvNoDataFound.setVisibility(GONE);
        binding.rvSubCategory.setVisibility(View.VISIBLE);
    }
    @Override
    public void onResume() {
        super.onResume();
        //((MainActivity)activity).setSelectIndex(2);
        //((MainActivity)activity).visibleNavigationView(GONE);
    }

    @Override
    public void onStop() {
        super.onStop();
        ((MainActivity)activity).visibleNavigationView(GONE);
    }
}