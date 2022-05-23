package com.app.fixirman.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.app.fixirman.R;
import com.app.fixirman.api.http.ApiUtils;
import com.app.fixirman.databinding.ActivityForgotPasswordBinding;
import com.app.fixirman.model.user.UserResponse;
import com.app.fixirman.utils.AppUtils;
import com.app.fixirman.utils.Crashlytics;
import com.google.gson.stream.MalformedJsonException;

import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {

    private final String TAG = "ForgotPassword";

    private ActivityForgotPasswordBinding binding;

    private String phone,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_forgot_password);
        binding.setActivity(this);
        initMe();
    }

    private void initMe() {
        phone = getIntent().getStringExtra("phone");
        if(phone == null){
            phone = "";
        }
    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.mb_updatePassword:
                if(AppUtils.isNetworkAvailable(this)){
                    if(validate()){
                        updatePassword();
                    }
                }else{
                    AppUtils.createNetworkError(this);
                }
                break;
        }
    }

    private void updatePassword() {
        showProgressBar();
        ApiUtils.getApiInterface().updatePassword(phone,password).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserResponse> call,@NonNull Response<UserResponse> response) {
                hideProgressBar();
                if(response.isSuccessful()){
                    UserResponse userResponse = response.body();
                    if(userResponse.getSuccess() == 1){
                        Toast.makeText(ForgotPasswordActivity.this, "Password updated Please login again to continue", Toast.LENGTH_SHORT).show();
                        openLoginScreen();
                    }else{
                        onApiCallFailed(userResponse.getMessage());
                    }
                }else{
                    onApiCallFailed("Server Error");
                    Crashlytics.log("response code "+response.code());
                    Crashlytics.logException(new Exception("update password: "+response.message()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable t) {
                AppUtils.handleResponse(ForgotPasswordActivity.this,t,"Update password api");
            }
        });
    }

    private void openLoginScreen() {
        Intent i = new Intent(this,UserLoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    private void onApiCallFailed(String message) {
        hideProgressBar();
        AppUtils.createFailedDialog(this,message);
    }
    private void hideProgressBar() {
        binding.mbUpdatePassword.setText("Update Password");
        binding.mbUpdatePassword.setEnabled(true);
        AppUtils.hideProgressBar(binding.pb);
    }
    private void showProgressBar(){
        binding.mbUpdatePassword.setText("");
        binding.mbUpdatePassword.setEnabled(false);
        AppUtils.showProgressBar(binding.pb);
    }

    private boolean validate() {
        boolean check = true;
        password = binding.etPassword.getText().toString().trim();
        String confirmPassword = binding.etConfirmPassword.getText().toString().trim();
        if(phone.isEmpty()){
            check  = false;
            Toast.makeText(this, "Invalid phone, Please try again", Toast.LENGTH_SHORT).show();
        }
        if(password.isEmpty()){
            check = false;
            binding.ilPassword.setError("Password is empty");
        }else{
            if(password.length() < 4){
                check = false;
                binding.ilPassword.setError("Password is too short");
            }else{
                if(confirmPassword.equals(password)){
                    binding.ilPassword.setError(null);
                    binding.ilConfirmPassword.setError(null);
                }else{
                    check = false;
                    binding.ilConfirmPassword.setError("Password mismatch");
                }
            }
        }
        return check;
    }
}
