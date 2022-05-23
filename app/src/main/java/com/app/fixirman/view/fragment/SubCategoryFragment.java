package com.app.fixirman.view.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.fixirman.R;
import com.app.fixirman.adapter.SubCategoryAdapter;
import com.app.fixirman.databinding.FragmentSubCategoryBinding;
import com.app.fixirman.model.categroy.SubCategory;
import com.app.fixirman.utils.AppConstants;
import com.app.fixirman.utils.AppUtils;
import com.app.fixirman.utils.SessionManager;
import com.app.fixirman.viewmodel.AppointmentViewModel;
import com.app.fixirman.utils.Crashlytics;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class SubCategoryFragment extends DialogFragment {

    private final String TAG = "SubCategory";

    private FragmentActivity activity;

    private FragmentSubCategoryBinding binding;

    @Inject
    ViewModelProvider.Factory factory;
    private AppointmentViewModel viewModel;

    private int categoryId;
    private SubCategoryAdapter adapter;
    private List<SubCategory> adapterList,allList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME,android.R.style.Theme_Material_NoActionBar_Fullscreen);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_sub_category, container, false);
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
            categoryId = bundle.getInt(AppConstants.CATEGORY_ID,0);
            String categoryName = bundle.getString(AppConstants.CATEGORY_NAME,"No Data Found");
            binding.tvCategoryName.setText(categoryName);
            adapter = new SubCategoryAdapter(activity,adapterList);
            binding.rvSubCategory.setLayoutManager(new LinearLayoutManager(activity));
            binding.rvSubCategory.setAdapter(adapter);
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
        viewModel = new ViewModelProvider(activity,factory).get(AppointmentViewModel.class);

        if(AppUtils.isNetworkAvailable(activity)){
            viewModel.init(binding.pb,new SessionManager(activity).getUserId(),categoryId);
        }else{
            Crashlytics.log("sub category screen open without network");
        }

        viewModel.getSubCategories(categoryId).observe(getViewLifecycleOwner(), this::updateUI);
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
            case R.id.btn_back:
                dismiss();
                break;
            case R.id.mb_done:
                ArrayMap<Integer,SubCategory> list = adapter.getSelectedServices();
                viewModel.setSelectList(list);
                dismiss();
                break;
        }
    }
}