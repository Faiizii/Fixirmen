package com.app.fixirman.view.activity;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;


import com.app.fixirman.R;
import com.app.fixirman.adapter.FAQExpandableListAdapter;
import com.app.fixirman.databinding.ActivityFaqBinding;
import com.app.fixirman.model.faq.FAQModel;
import com.app.fixirman.utils.AppUtils;
import com.app.fixirman.utils.SessionManager;
import com.app.fixirman.viewmodel.FAQViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;


public class FAQActivity extends AppCompatActivity {
    ActivityFaqBinding binding;
    Activity mActivity;
    SessionManager sessionManager;
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private FAQViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_faq);
        binding.setActivity(this);

        initializeVariable();
    }

    public void initializeVariable() {
        mActivity = FAQActivity.this;
        sessionManager=new SessionManager(mActivity);

        AppUtils.showProgressBar(binding.pb);
        this.configureDagger();
        this.configureViewModel();

    }
    // -----------------
    // CONFIGURATION
    // -----------------

    private void configureDagger() {
        AndroidInjection.inject(this);
    }

    private void configureViewModel() {
        viewModel = new ViewModelProvider(this, viewModelFactory).get(FAQViewModel.class);
        viewModel.init(binding.pb,new SessionManager(this).getUserId());
        viewModel.getFAQs(binding.pb,new SessionManager(this).getUserId())
                .observe(this, this::updateFAQUI);

    }

    // -----------------
    // UPDATE FAQ UI
    // -----------------

    private void updateFAQUI(@Nullable List<FAQModel> faqList) {
        if (faqList != null) {
            FAQExpandableListAdapter listAdapter = new FAQExpandableListAdapter(mActivity, faqList);
            // setting list adapter
            binding.lvExp.setAdapter(listAdapter);
            if (faqList.size() > 0) {
                binding.cnstProgramNotFound.setVisibility(View.GONE);
            } else {
                binding.cnstProgramNotFound.setVisibility(View.VISIBLE);
            }
        }
        else {
            binding.cnstProgramNotFound.setVisibility(View.VISIBLE);
        }
    }
    public  void onClick(View v){
        switch (v.getId()){
            case R.id.btn_back:
                onBackPressed();
                break;
        }
    }
}
