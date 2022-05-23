package com.app.fixirman.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.app.fixirman.R;
import com.app.fixirman.databinding.ActivityTermsBinding;
import com.app.fixirman.utils.AppConstants;


public class TermsActivity extends AppCompatActivity {
    Activity mActivity;
    ActivityTermsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_terms);
        binding.setActivity(this);
        mActivity = TermsActivity.this;
        initializeVariable();
    }
    public void initializeVariable() {

        binding.tvTerms.setText(Html.fromHtml(AppConstants.termsAndUse));
    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_back:
                onBackPressed();
                break;
        }
    }
}
