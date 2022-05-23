package com.app.fixirman.view.create_request;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import android.se.omapi.Session;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.fixirman.R;
import com.app.fixirman.adapter.DateAdapter;
import com.app.fixirman.adapter.ServiceTypeAdapter;
import com.app.fixirman.adapter.TimeAdapter;
import com.app.fixirman.api.http.ApiUtils;
import com.app.fixirman.databinding.FragmentRequestScheduleBinding;
import com.app.fixirman.databinding.FragmentRequestSummaryBinding;
import com.app.fixirman.model.appointment.AppointmentResponse;
import com.app.fixirman.model.categroy.ServiceType;
import com.app.fixirman.model.provider.DateModel;
import com.app.fixirman.model.provider.DateTimeResult;
import com.app.fixirman.model.provider.ProviderDetailResponse;
import com.app.fixirman.my_interfaces.AdapterOnClickListener;
import com.app.fixirman.utils.AppUtils;
import com.app.fixirman.utils.SessionManager;
import com.app.fixirman.utils.Crashlytics;
import com.google.gson.Gson;

import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestScheduleFragment extends Fragment {
    private final String TAG = "RequestSchedule";
    @Inject
    ViewModelProvider.Factory factory;
    private RequestViewModel viewModel;

    private FragmentRequestScheduleBinding binding;
    private FragmentActivity activity;
    private DateAdapter dateAdapter;
    private List<DateModel> dateModels;
    private TimeAdapter timeAdapter;
    private List<String> timeSlots;
    private SessionManager sessionManager;
    private List<ServiceType> serviceTypes;
    private ServiceTypeAdapter serviceTypeAdapter;
    private int couponId;
    private boolean isServicesSelected = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_request_schedule, container, false);
        binding.setFragment(this);
        activity = getActivity();
        initMe();
        return binding.getRoot();
    }

    private void initMe() {
        couponId = 0;
        sessionManager = new SessionManager(activity);
        initTimeAdapter();
        intiDateAdapter();
        initServiceAdapter();
        getOnlineDetails();
    }

    private void initServiceAdapter() {
        serviceTypes = new ArrayList<>();
        serviceTypeAdapter = new ServiceTypeAdapter(activity,serviceTypes);
        binding.rvServiceTypes.setAdapter(serviceTypeAdapter);
    }

    private void initTimeAdapter() {
        timeSlots = new ArrayList<>();
        timeAdapter = new TimeAdapter(activity,timeSlots);
        binding.rvTime.setAdapter(timeAdapter);
    }

    private void intiDateAdapter() {
        dateModels = new ArrayList<>();
        dateAdapter = new DateAdapter(activity,dateModels);
        binding.rvDates.setAdapter(dateAdapter);
        dateAdapter.setOnItemClickListener((view, position) -> {
            updateTimeSlots(dateAdapter.getSelectedDate(position));
        });
    }

    private void updateTimeSlots(String selectedDate) {
        //selected date has 2020-09-28 format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Calendar currentCalender = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(dateFormat.parse(selectedDate));
        } catch (Exception e) {
            e.printStackTrace();
        }
        int startIndex;
        if(dateFormat.format(currentCalender.getTime()).equalsIgnoreCase(dateFormat.format(calendar.getTime()))){
            startIndex = currentCalender.get(Calendar.HOUR_OF_DAY);
        }else{
            startIndex = 0;
        }
        List<String> tempList = new ArrayList<>();
        for (int i = startIndex; i < 24; i++) {
            String start = "",end = "";
            if(i < 10){
                start = "0"+i+" AM";
            }else if(i < 12){
                start = i+" AM";
            }else{
                start = i +" PM";
            }
            if(i+1 < 10){
                end = "0"+(i+1)+ " AM";
            }else if(i+1 < 12){
                end = (i+1)+" AM";
            }else{
                end = (i+1)+" PM";
            }
            tempList.add(start+" - "+ end);
        }
        timeAdapter.updateList(tempList);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(activity,factory).get(RequestViewModel.class);
        viewModel.getSelectedServices().observe(getViewLifecycleOwner(), arrayMap -> {
            if(arrayMap != null && arrayMap.size() > 0){
                String tempStr = arrayMap.valueAt(0).getTitle();
                if(arrayMap.size() > 1)
                    tempStr = tempStr+" and "+ (arrayMap.size() -1)+" More";

                binding.tvSubCategories.setText(tempStr);
                isServicesSelected = true;
            }else{
                isServicesSelected = false;
            }
        });
        viewModel.getServiceTypes(sessionManager.getUserId(),binding.pb).observe(getViewLifecycleOwner(),this::loadServices);
    }

    private void loadServices(List<ServiceType> list) {
        if(list != null){
            serviceTypes.clear();
            serviceTypes.addAll(list);
            serviceTypeAdapter.notifyDataSetChanged();
        }else{

        }
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.layout_subCategories :
            case R.id.tv_subCategories:
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new SelectServiceFragment()).addToBackStack("service")
                        .commit();
                break;
            case R.id.btn_confirm:
                if(validate()){
                    viewModel.addToCart();
                    activity.onBackPressed();
                }
                break;
            case R.id.btn_applyCoupon:
                if(AppUtils.isNetworkAvailable(activity)){
                    applyCoupon(binding.etCouponCode.getText().toString().trim());
                }else{
                    AppUtils.createNetworkError(activity);
                }
                break;
        }
    }


    private boolean validate() {
        boolean check = true;
        String description = binding.etDescription.getText().toString().trim();
        viewModel.setDescription(description);
        viewModel.setCouponId(couponId);
        viewModel.setDate(dateAdapter.getSelectedDate());
        viewModel.setTime(timeAdapter.getSelectedTime());
        viewModel.setServiceType(serviceTypeAdapter.getSelectedServiceType());
        if(!isServicesSelected){
            check = false;
            Toast.makeText(activity, "Select Services First", Toast.LENGTH_SHORT).show();
        }
        return check;
    }

    private void applyCoupon(String code) {
        AppUtils.showProgressBar(binding.pb);
         binding.ilCouponCode.setError(null);
        ApiUtils.getApiInterface().applyCoupon(code,sessionManager.getUserId()).enqueue(new Callback<AppointmentResponse>() {
            @Override
            public void onResponse(@NonNull Call<AppointmentResponse> call,@NonNull Response<AppointmentResponse> response) {
                AppUtils.hideProgressBar(binding.pb);
                if(response.isSuccessful()){
                    AppointmentResponse appointmentResponse = response.body();
                    if(appointmentResponse.getSuccess() == 1){
                        couponId = appointmentResponse.getCouponId();
                    }else{
                        binding.ilCouponCode.setError(appointmentResponse.getMessage());
                    }
                }else{
                    binding.ilCouponCode.setError("Server Connection Error");
                }
            }

            @Override
            public void onFailure(@NonNull Call<AppointmentResponse> call,@NonNull Throwable t) {
                AppUtils.hideProgressBar(binding.pb);
                t.printStackTrace();
                if(t instanceof SocketTimeoutException){
                    binding.ilCouponCode.setError("Slow Internet Connection");
                }else{
                    binding.ilCouponCode.setError("Server Error");
                }
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        if(viewModel != null){
            ((CreateRequestActivity)activity).updateTitle(viewModel.getCategoryName());
        }else{
            ((CreateRequestActivity)activity).updateTitle("Select Detail");
        }
    }
    private void getOnlineDetails(){
        AppUtils.showProgressBar(binding.pb);
        ApiUtils.getApiInterface().getTimeSlots(sessionManager.getUserId(),new Gson().toJson(new ArrayList<>())).enqueue(
                new Callback<ProviderDetailResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ProviderDetailResponse> call, @NonNull Response<ProviderDetailResponse> response) {
                        AppUtils.hideProgressBar(binding.pb);
                        if(response.isSuccessful()){
                            ProviderDetailResponse detailResponse = response.body();
                            if(detailResponse.getSuccess() == 1){
                                DateTimeResult result = detailResponse.getResult();
                                if(result != null){
                                    updateUI(result.getDates());
                                }

                            }else{
                                showFailedDialog(detailResponse.getMessage());
                            }
                        }else{
                            Crashlytics.log("code: "+response.code());
                            Crashlytics.log("message: "+response.message());
                            Crashlytics.logException(new Exception("load time slots connection exception with code:"+response.code()));
                            showFailedDialog("Server Connection Error");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ProviderDetailResponse> call, @NonNull Throwable t) {
                        AppUtils.hideProgressBar(binding.pb);
                        AppUtils.handleResponse(activity,t,"get time slots api");
                    }
                }
        );
    }

    private void showFailedDialog(String message) {
        AppUtils.createFailedDialog(activity,message);
    }
    private void updateUI(List<DateModel> dates) {
        if(dates != null){
            this.dateModels.clear();
            this.dateModels.addAll(dates);
            dateAdapter.notifyDataSetChanged();
            if(dates.size() > 0){
                updateTimeSlots(dateAdapter.getSelectedDate(0)); //first index populate time slots
            }
        }else{
            //no date found
            Log.e(TAG, "updateUI: no date in loaded list" );
        }
    }
}