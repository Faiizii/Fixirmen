package com.app.fixirman.view.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.app.fixirman.R;
import com.app.fixirman.api.http.ApiUtils;
import com.app.fixirman.databinding.ActivityPhoneBinding;
import com.app.fixirman.model.user.User;
import com.app.fixirman.model.user.UserResponse;
import com.app.fixirman.model.user.UserResult;
import com.app.fixirman.utils.AppConstants;
import com.app.fixirman.utils.AppUtils;
import com.app.fixirman.utils.SessionManager;
import com.app.fixirman.viewmodel.UserViewModel;
import com.app.fixirman.utils.Crashlytics;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.stream.MalformedJsonException;

import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhoneActivity extends AppCompatActivity {
    private final String TAG = "PhoneActivity";

    private ActivityPhoneBinding binding;

    private String phoneNumber = "",code = "",verificationId = "";
    private String countryCode = "";
    boolean isCodeSend = false;
    private final long CODE_TIME_OUT = 60L;
    private CountDownTimer timer;

    private FirebaseAuth auth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private PhoneAuthProvider.ForceResendingToken forceResendingToken;

    private UserResult userResult;

    private int TYPE = AppConstants.PHONE_ACTIVITY_CHECK;


    @Inject
    ViewModelProvider.Factory factory;
    private UserViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_phone);
        binding.setActivity(this);
        init();
    }

    private void init() {
        auth = FirebaseAuth.getInstance();
        binding.ccp.registerPhoneNumberTextView(binding.etPhone);
        if(getIntent() != null){
            TYPE = getIntent().getIntExtra("type", AppConstants.PHONE_ACTIVITY_CHECK);
        }else{
            Log.e(TAG, "init: type not found intent is null" );
        }
        initViewModel();
        initTimer();
        initCodeCallBack();
        initCountrySpinner();
        initOtpCompleteListener();
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this,factory).get(UserViewModel.class);
    }

    private void initOtpCompleteListener() {
        try {
            binding.otpView.setOtpCompletionListener(this::verifyCode);
        }catch (Exception e){
            Log.e(TAG, "initOtpCompleteListener: ",e );
            Crashlytics.logException(e);
        }
    }

    private void initCountrySpinner() {
        binding.ccp.setOnCountryChangeListener(selectedCountry -> {
            countryCode = "+"+selectedCountry.getPhoneCode();
            Log.e(TAG, "onCountrySelected: "+countryCode );
        });

    }

    private void initTimer() {
        timer = new CountDownTimer(1000*CODE_TIME_OUT, 1000) {
            @Override
            public void onTick(long l) {
                hideResendCode();
                binding.tvResendTimer.setText("Resend Code in "+(l/1000));
            }

            @Override
            public void onFinish() {
                showResendCode();
            }
        };
    }

    private void initCodeCallBack() {
        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithCredential(phoneAuthCredential);
                hideSendCodeProgressBar();
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Crashlytics.logException(e);
                hideSendCodeProgressBar();
                if(e instanceof FirebaseAuthInvalidCredentialsException){
                    binding.etPhone.setError("Invalid Mobile Number. Please check mobile format");
                }else{
                    binding.etPhone.setError("Verification Failed");
                }
                Log.e(TAG, "onVerificationFailed: ",e );
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                super.onCodeSent(s, forceResendingToken);
                isCodeSend = true;
                hideSendCodeProgressBar();
                verificationId = s;
                forceResendingToken = token;
                timer.cancel();
                timer.start();
                showCodeLayout();
            }
        };
    }



    public void onClick(View v){
        AppUtils.hideSoftKeyboard(this);
        int id = v.getId();
        if (id == R.id.btn_sendCode) {
            if (AppUtils.isNetworkAvailable(this)) {
                if (phoneValidate()) {
                    checkPhoneNumberExistenceOnServer();
                }
            } else {
                AppUtils.createNetworkError(this);
            }
        } else if (id == R.id.tv_resendCode) {
            if (AppUtils.isNetworkAvailable(this)) {
                resendCode();
            } else {
                AppUtils.createNetworkError(this);
            }
        } else if (id == R.id.btn_verify) {
            if (AppUtils.isNetworkAvailable(this)) {
                if (validateCode()) {
                    verifyCode(code);
                }
            } else {
                AppUtils.createNetworkError(this);
            }
        }
    }

    private boolean validateCode() {
        boolean check = true;
        try {
            code = binding.otpView.getText().toString().trim();
        }catch (Exception e){
            Log.e(TAG, "validateCode: ", e);
            Crashlytics.logException(e);
        }finally {
            if(code.isEmpty()){
                check = false;
               binding.tlCode.setError("Can't be Empty");
            }else if(code.length() < 6){
                check = false;
                binding.tlCode.setError("Invalid Code");
            }else{
                binding.tlCode.setError(null);
            }
        }

        return check;
    }

    private void resendCode() {
        if(forceResendingToken == null)
            return;

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(phoneNumber)
                        .setForceResendingToken(forceResendingToken)// Phone number to verify
                        .setTimeout(CODE_TIME_OUT, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private boolean phoneValidate() {
        boolean check = true;
        phoneNumber = binding.etPhone.getText().toString().trim();
        phoneNumber =  phoneNumber.replaceAll("\\s+","");
        if(phoneNumber.length() >= 10){
            binding.etPhone.setError(null);
            if(countryCode.isEmpty())
               countryCode =  "+"+binding.ccp.getSelectedCountryCode();
            //phoneNumber = "+"+countryCode+ phoneNumber;
            phoneNumber = countryCode+ phoneNumber;
            Log.e(TAG, "phoneValidate: "+phoneNumber );
        }else{
            check = false;
            binding.etPhone.setError("Invalid Phone");
        }
        return check;
    }

    private void checkPhoneNumberExistenceOnServer(){
        if(phoneNumber.isEmpty())
            return;

        showSendCodeProgressBar();
        ApiUtils.getApiInterface().verifyMyPhone(phoneNumber,AppConstants.TYPE_USER,TYPE == AppConstants.PHONE_ACTIVITY_CHECK ? "check" : "sign_up",
                Build.MANUFACTURER, Build.MODEL, "Android", Settings.Secure.ANDROID_ID,
                Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID), Build.VERSION.RELEASE,new SessionManager(this).getToken(),
                "A", AppConstants.APP_VERSION)
                .enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserResponse> call,@NonNull Response<UserResponse> response) {
                if(response.isSuccessful()){
                    UserResponse userResponse = response.body();
                    switch (TYPE){
                        case AppConstants.PHONE_ACTIVITY_SIGN_UP:
                            if(userResponse.getSuccess() == 0){
                                sendCode();
                            }else{
                                onApiCallFailed(userResponse.getMessage());
                            }
                            break;
                        case AppConstants.PHONE_ACTIVITY_FORGOT_PASSWORD:
                        case AppConstants.PHONE_ACTIVITY_ADD_NEW_NUMBER:
                        case AppConstants.PHONE_ACTIVITY_CHECK:
                            if(userResponse.getSuccess() == 1){
                                userResult = userResponse.getUserResult();
                                sendCode();
                            }else{
                                onApiCallFailed(userResponse.getMessage());
                            }
                            break;
                        default:
                            onApiCallFailed(userResponse.getMessage());
                    }
                }else{
                    onApiCallFailed("Server Connection Error");
                    Crashlytics.log("response code "+response.code());
                    Crashlytics.logException(new Exception("verify my phone: "+response.message()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserResponse> call,@NonNull Throwable t) {
                AppUtils.handleResponse(PhoneActivity.this,t,"verify phone api");
            }
        });
    }

    private void onApiCallFailed(String message) {
        hideSendCodeProgressBar();
        AppUtils.createFailedDialog(this,message);
    }

    private void sendCode() {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(CODE_TIME_OUT, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private void verifyCode(String otp) {
        AppUtils.hideSoftKeyboard(this);
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,otp);
        signInWithCredential(credential);
    }
    private void signInWithCredential(PhoneAuthCredential phoneAuthCredential) {
        showVerifyProgressBar();
        auth.signInWithCredential(phoneAuthCredential).addOnSuccessListener(authResult -> {
            //navigate to next
            isCodeSend = false;
            hideVerifyProgressBar();
            Intent i = null;
            if(TYPE == AppConstants.PHONE_ACTIVITY_FORGOT_PASSWORD){
                i = new Intent(PhoneActivity.this,ForgotPasswordActivity.class);
            }else if(TYPE == AppConstants.PHONE_ACTIVITY_SIGN_UP){
                saveUserLocal();
                i = new Intent(PhoneActivity.this,UpdateProfileActivity.class);
                i.putExtra("type", AppConstants.COMPLETE_PROFILE);
            }else if(TYPE == AppConstants.PHONE_ACTIVITY_CHECK){
                saveUserLocal();
                i = new Intent(this,MainActivity.class);
            }
            if(i != null){
                i .putExtra("phone",phoneNumber);
                startActivity(i);
                this.finish();
            }else{
                Log.e(TAG, "signInWithCredential: intent null no type found" );
            }
        }).addOnFailureListener(e ->{
            hideVerifyProgressBar();
            if(e instanceof FirebaseAuthInvalidCredentialsException){
                binding.tlCode.setError("Invalid Code! Please Enter the Correct Code");
            }
            Log.e(TAG, "onFailure: ",e );
            Crashlytics.logException(e);
            //hideProgressBarVerify();
        });
    }

    private void saveUserLocal() {
        if(userResult != null){
            User user = userResult.getUser();
            if(user != null){
                new SessionManager(this).saveUser(user);
                viewModel.insertAddress(user.getUserAddress());
            }
            if(userResult.getAds() != null)
                viewModel.insertAds(userResult.getAds());
        }
    }

    @Override
    public void onBackPressed() {
        if(binding.llVerifyCode.getVisibility() == View.VISIBLE){
            timer.cancel();
            isCodeSend = false;
            showPhoneLayout();
        }else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        isCodeSend = savedInstanceState.getBoolean("code_sent");
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean("code_sent",isCodeSend);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isCodeSend){
            hideSendCodeProgressBar();
            timer.cancel();
            timer.start();
            showCodeLayout();
        }
    }

    private void showPhoneLayout(){
        binding.llVerifyCode.setVisibility(View.GONE);
        binding.llSendCode.setVisibility(View.VISIBLE);
        binding.btnSendCode.setVisibility(View.VISIBLE);
    }
    private void showCodeLayout(){
        binding.llSendCode.setVisibility(View.GONE);
        binding.llVerifyCode.setVisibility(View.VISIBLE);
        binding.btnSendCode.setVisibility(View.GONE);
    }
    private void showResendCode(){
        binding.tvResendTimer.setVisibility(View.GONE);
        binding.llResendCode.setVisibility(View.VISIBLE);
    }
    private void hideResendCode(){
        binding.llResendCode.setVisibility(View.GONE);
        binding.tvResendTimer.setVisibility(View.VISIBLE);
    }

    private void showSendCodeProgressBar(){
        binding.btnSendCode.setEnabled(false);
        binding.btnSendCode.setText("");
        AppUtils.showProgressBar(binding.pbSendCode);
    }
    private void hideSendCodeProgressBar(){
        binding.btnSendCode.setEnabled(true);
        binding.btnSendCode.setText(getResources().getString(R.string.send_code));
        AppUtils.hideProgressBar(binding.pbSendCode);
    }
    private void showVerifyProgressBar(){
        binding.btnVerify.setEnabled(false);
        binding.btnVerify.setText("");
        AppUtils.showProgressBar(binding.pbVerifyCode);
    }
    private void hideVerifyProgressBar(){
        binding.btnVerify.setEnabled(true);
        binding.btnVerify.setText(getResources().getString(R.string.verify));
        AppUtils.hideProgressBar(binding.pbVerifyCode);
    }
}
