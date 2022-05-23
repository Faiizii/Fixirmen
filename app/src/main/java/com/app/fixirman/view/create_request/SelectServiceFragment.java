package com.app.fixirman.view.create_request;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.fixirman.R;
import com.app.fixirman.adapter.SubCategoryAdapter;
import com.app.fixirman.databinding.FragmentRequestScheduleBinding;
import com.app.fixirman.databinding.FragmentSelectServiceBinding;
import com.app.fixirman.model.categroy.SubCategory;
import com.app.fixirman.utils.AppUtils;
import com.app.fixirman.utils.SessionManager;
import com.app.fixirman.utils.Crashlytics;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

import static android.view.View.GONE;

public class SelectServiceFragment extends Fragment {

    private final String TAG = "SelectService";
    @Inject
    ViewModelProvider.Factory factory;
    private RequestViewModel viewModel;

    private FragmentSelectServiceBinding binding;
    private FragmentActivity activity;
    private SubCategoryAdapter adapter;
    private List<SubCategory> adapterList,allList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_select_service, container, false);
        binding.setFragment(this);
        activity = getActivity();
        initMe();
        return binding.getRoot();
    }

    private void initMe() {
        adapterList = new ArrayList<>();
        allList = new ArrayList<>();
        adapter = new SubCategoryAdapter(activity,adapterList);
        binding.rvSubCategory.setAdapter(adapter);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((CreateRequestActivity)activity).updateTitle("Select Services");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(activity,factory).get(RequestViewModel.class);
        binding.tvCategoryName.setText(viewModel.getCategoryName());
        if(AppUtils.isNetworkAvailable(activity)){
            viewModel.init(binding.pb,new SessionManager(activity).getUserId());
        }else{
            Crashlytics.log("sub category screen open without network");
        }

        viewModel.getSubCategories().observe(getViewLifecycleOwner(), this::updateUI);
    }

    public void updateUI(List<SubCategory> list){
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

    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_done:
                ArrayMap<Integer,SubCategory> list = adapter.getSelectedServices();
                viewModel.setServices(list);
                activity.onBackPressed();
                break;
            case R.id.btn_cancel:
                activity.onBackPressed();
                break;
        }
    }
}