package com.app.fixirman.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.app.fixirman.R;
import com.app.fixirman.api.http.ApiUtils;
import com.app.fixirman.databinding.ActivitySplashBinding;
import com.app.fixirman.model.user.User;
import com.app.fixirman.model.user.UserResponse;
import com.app.fixirman.utils.AppConstants;
import com.app.fixirman.utils.AppUtils;
import com.app.fixirman.utils.SessionManager;
import com.app.fixirman.viewmodel.UserViewModel;
import com.app.fixirman.utils.Crashlytics;
import com.google.gson.stream.MalformedJsonException;

import java.net.SocketTimeoutException;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    private final String TAG = "SplashActivity";

    private ActivitySplashBinding binding;
    private SessionManager sessionManager;

    @Inject
    ViewModelProvider.Factory factory;
    private UserViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_splash);
        binding.setActivity(this);
        sessionManager = new SessionManager(this);
        initViewModel();
        initMe();
    }

    private void initMe() {
        if(sessionManager.isLogin()){
            checkUserLogin();
        }else{
            openUserLogin();
        }
    }


    private void initViewModel() {
        viewModel = new ViewModelProvider(this,factory).get(UserViewModel.class);
    }

    private void checkUserLogin(){
        ApiUtils.getApiInterface().verifyMyPhone(sessionManager.getPhone(),AppConstants.TYPE_USER, "check",
                Build.MANUFACTURER, Build.MODEL, "Android", Settings.Secure.ANDROID_ID,
                Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID), Build.VERSION.RELEASE,sessionManager.getToken(),
                "A", AppConstants.APP_VERSION)
                .enqueue(new Callback<UserResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<UserResponse> call,@NonNull Response<UserResponse> response) {
                        if(response.isSuccessful()){
                            UserResponse userResponse = response.body();
                            if(userResponse.getSuccess() == 1){
                                if(userResponse.getUserResult() != null){
                                    User user = userResponse.getUserResult() .getUser();
                                    if(user != null){
                                        sessionManager.saveUser(user);
                                        viewModel.insertAddress(user.getUserAddress());
                                    }
                                    if(userResponse.getUserResult() .getAds() != null)
                                        viewModel.insertAds(userResponse.getUserResult() .getAds());

                                    onUserLoginSuccess();
                                }else{
                                    onUserLoginFail();
                                }
                            }else{
                                onUserLoginFail();
                            }
                        }else{
                            onUserLoginSuccess();
                            Crashlytics.log("response code "+response.code());
                            Crashlytics.logException(new Exception("verify my phone: "+response.message()));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<UserResponse> call,@NonNull Throwable t) {
                        Log.e(TAG, "onFailure: ",t );
                        onUserLoginSuccess();
                        Crashlytics.log("very my phone api call with network status "+AppUtils.isNetworkAvailable(SplashActivity.this));
                        Crashlytics.logException(t);
                    }
                });
    }
    private void onApiCallFailed(String message) {
       AppUtils.createFailedDialog(this,message);
       sessionManager.logoutUser();
    }
    private void onUserLoginFail() {
        Intent i = new Intent(this,PhoneActivity.class);
        sessionManager.setIsLogin(false);
        startActivity(i);
    }

    private void onUserLoginSuccess(){
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
    }
    private void openUserLogin(){
        Intent i = new Intent(this,PhoneActivity.class);
        startActivity(i);
    }
}
