package com.app.fixirman.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.app.fixirman.R;
import com.app.fixirman.api.http.ApiUtils;
import com.app.fixirman.databinding.ActivityUserRegisterBinding;
import com.app.fixirman.model.user.UserResponse;
import com.app.fixirman.utils.AppConstants;
import com.app.fixirman.utils.AppUtils;
import com.app.fixirman.utils.SessionManager;
import com.app.fixirman.utils.Crashlytics;
import com.google.gson.stream.MalformedJsonException;

import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRegisterActivity extends AppCompatActivity {
    private final String TAG = "UserRegister";

    private ActivityUserRegisterBinding binding;
    private String username,phone,email,password,fullName;
    private SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         binding = DataBindingUtil.setContentView(this,R.layout.activity_user_register);
         binding.setActivity(this);
         initMe();
    }

    private void initMe() {
        sessionManager = new SessionManager(this);
        phone = getIntent().getStringExtra("phone");
        if(phone == null){
            phone = "";
        }
    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_register:
                if(AppUtils.isNetworkAvailable(this)){
                    if(validate()){
                        signUpUser();
                    }
                }else{
                    AppUtils.createNetworkError(this);
                }
                break;
        }
    }

    private void signUpUser() {
        showProgressBar();
        ApiUtils.getApiInterface().RegisterUser(phone,password,email,username,fullName,"","","","", Build.MANUFACTURER, Build.MODEL, "Android", Settings.Secure.ANDROID_ID,
                Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID), Build.VERSION.RELEASE,sessionManager.getToken(),
                "A", AppConstants.APP_VERSION).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserResponse> call,@NonNull Response<UserResponse> response) {
                hideProgressBar();
                if(response.isSuccessful()){
                    UserResponse userResponse = response.body();
                    if(userResponse.getSuccess() == 1){
                        //sessionManager.saveUser(userResponse.getUser());
                        sessionManager.saveUserLogin(phone,password);
                        openMainActivity();
                    }else{
                        onApiCallFailed(userResponse.getMessage());
                    }
                }else{
                    onApiCallFailed("Server Connection Error");
                    Crashlytics.log("response code "+response.code());
                    Crashlytics.logException(new Exception("register user: "+response.message()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserResponse> call,@NonNull Throwable t) {
                AppUtils.handleResponse(UserRegisterActivity.this,t,"register user api");
            }
        });
    }

    private void openMainActivity() {
        Intent i = new Intent(this,MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    private void onApiCallFailed(String message) {
        hideProgressBar();
        AppUtils.createFailedDialog(this,message);
    }
    private void hideProgressBar() {
        binding.btnRegister.setText("Register");
        binding.btnRegister.setEnabled(true);
        AppUtils.hideProgressBar(binding.pb);
    }
    private void showProgressBar(){
        binding.btnRegister.setText("");
        binding.btnRegister.setEnabled(false);
        AppUtils.showProgressBar(binding.pb);
    }

    public boolean validate(){
        boolean check = true;
        username = binding.etUsername.getText().toString().trim();
        email = binding.etEmail.getText().toString().trim();
        password = binding.etPassword.getText().toString().trim();
        String confirmPassword = binding.etConfirmPassword.getText().toString().trim();
        fullName = binding.etName.getText().toString().trim();

        if(username.isEmpty()){
            check = false;
            binding.ilUsername.setError("Username is empty");
        }else{
            binding.ilUsername.setError(null);
        }

        if(email.isEmpty()){
            check = false;
            binding.ilEmail.setError("Email is empty");
        }else{
            binding.ilEmail.setError(null);
        }

        if(fullName.isEmpty()){
            check = false;
            binding.ilName.setError("Full name is empty");
        }else{
            binding.ilName.setError(null);
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
        if(phone.isEmpty()){
            check = false;
            Toast.makeText(this, "Invalid phone, Try Again Later", Toast.LENGTH_SHORT).show();
        }
        return check;
    }
}
