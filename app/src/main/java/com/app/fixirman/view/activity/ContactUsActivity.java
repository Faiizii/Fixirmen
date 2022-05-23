package com.app.fixirman.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.app.fixirman.R;
import com.app.fixirman.api.http.ApiUtils;
import com.app.fixirman.databinding.ActivityContactUsBinding;
import com.app.fixirman.model.NormalResponse;
import com.app.fixirman.utils.AppUtils;
import com.app.fixirman.utils.SessionManager;
import com.app.fixirman.utils.Crashlytics;
import com.google.gson.stream.MalformedJsonException;

import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactUsActivity extends AppCompatActivity {
    private final String TAG = "ContactUs";

    private ActivityContactUsBinding binding;
    private SessionManager sessionManager;
    private String email,message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_contact_us);
        binding.setActivity(this);
        initMe();
    }

    private void initMe() {
        sessionManager = new SessionManager(this);
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.btn_submit:
                if(AppUtils.isNetworkAvailable(this)){
                    if(validate()){
                        sendMail();
                    }
                }else{
                    AppUtils.createNetworkError(this);
                }
                break;
        }
    }

    private void sendMail() {
        showProgressBar();
        ApiUtils.getApiInterface().sendMailToCustomerSupport(sessionManager.getUserId(),email,message).enqueue(new Callback<NormalResponse>() {
            @Override
            public void onResponse(@NonNull Call<NormalResponse> call, @NonNull Response<NormalResponse> response) {
                hideProgressBar();
                if(response.isSuccessful()){
                    NormalResponse normalResponse = response.body();
                    if(normalResponse.getSuccess() == 1){
                        resetAll();
                        Toast.makeText(ContactUsActivity.this, normalResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }else{
                        onApiCallFailed(normalResponse.getMessage());
                    }
                }else{
                    Crashlytics.log("response code "+response.code());
                    Crashlytics.logException(new Exception("send mail at contact us screen: "+response.message()));
                    onApiCallFailed("Server Connection Error");
                }
            }

            @Override
            public void onFailure(@NonNull Call<NormalResponse> call, @NonNull Throwable t) {
                hideProgressBar();
                AppUtils.handleResponse(ContactUsActivity.this,t,"send email at contact us screen");
            }
        });
    }

    private void onApiCallFailed(String message) {
        AppUtils.createFailedDialog(this,message);

    }

    private void hideProgressBar() {
        AppUtils.hideProgressBar(binding.pb);
        binding.btnSubmit.setEnabled(true);
        binding.btnSubmit.setText("Submit");
    }
    private void showProgressBar(){
        binding.btnSubmit.setEnabled(false);
        binding.btnSubmit.setText("");
        AppUtils.showProgressBar(binding.pb);
    }

    private void resetAll() {
        binding.etEmail.setText("");
        binding.etMessage.setText("");
    }

    private boolean validate() {
        boolean check = true;
        email = binding.etEmail.getText().toString().trim();
        message = binding.etMessage.getText().toString().trim();

        if(email.isEmpty()){
            binding.ilEmail.setError("Can't be empty");
            check = false;
        }else{
            binding.ilEmail.setError(null);
        }

        if(message.isEmpty()){
            binding.ilMessage.setError("Can't be empty");
            check = false;
        }else{
            binding.ilMessage.setError(null);
        }
        return check;
    }
}