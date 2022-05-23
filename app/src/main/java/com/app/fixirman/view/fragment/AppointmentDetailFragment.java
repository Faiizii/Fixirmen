package com.app.fixirman.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.fixirman.R;
import com.app.fixirman.databinding.FragmentAppointmentBinding;
import com.app.fixirman.databinding.FragmentAppointmentDetailBinding;
import com.app.fixirman.databinding.FragmentUserDetailBinding;
import com.app.fixirman.model.appointment.Appointment;
import com.app.fixirman.model.appointment.AppointmentStatus;
import com.app.fixirman.utils.AppConstants;
import com.app.fixirman.utils.AppUtils;
import com.app.fixirman.view.activity.ChatActivity;
import com.app.fixirman.view.activity.MainActivity;
import com.app.fixirman.viewmodel.AppointmentViewModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.app.fixirman.utils.Crashlytics;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class AppointmentDetailFragment extends Fragment {
    private final String TAG = "AppointmentDetail";

    private FragmentActivity activity;
    private FragmentAppointmentDetailBinding binding;

    @Inject
    ViewModelProvider.Factory factory;
    private AppointmentViewModel viewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_appointment_detail, container, false);
        binding.setFragment(this);
        activity =getActivity();
        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(this,factory).get(AppointmentViewModel.class);
        initMe();
    }

    private void initMe() {
        if(getArguments() != null){
            Bundle b = getArguments();
            Appointment model = b.getParcelable("object");
            if(model != null){
                updateUI(model);
            }else{
                String type = b.getString("type","");
                if(AppConstants.NOTIFICATION_APPOINTMENT.equalsIgnoreCase(type)){
                    String appointmentId = b.getString("appointment_id","");
                    if(!appointmentId.isEmpty()){
                        viewModel.getAppointment(appointmentId).observe(getViewLifecycleOwner(), appointment -> {
                            if(appointment != null){
                                updateUI(appointment);
                            }else{
                                Crashlytics.log("appointment detail not found local on id "+appointmentId);
                            }
                        });
                    }else{
                        Crashlytics.log("appointment detail open with null appointment object");
                    }
                }else{
                    Crashlytics.log("appointment detail open with null appointment object");
                }
            }
        }else{
            Crashlytics.log("appointment detail open with null arguments");
        }
    }

    private void updateUI(Appointment model){
        binding.setModel(model);
        Glide.with(activity).load(AppConstants.HOST_URL+model.getEmployeeImage())
                .apply(new RequestOptions().error(R.drawable.default_user))
                .placeholder(AppUtils.getImageLoader(activity))
                .into(binding.ivProfileImage);
        Glide.with(activity).load(AppConstants.HOST_URL+model.getEmployeeImage())
                .apply(new RequestOptions().error(R.drawable.default_user))
                .placeholder(AppUtils.getImageLoader(activity))
                .into(binding.ivEmployeeImage);
        updateStatusSelection(model.getStatus());
        viewModel.getStatusHistory(model.getRequestId()).observe(getViewLifecycleOwner(),this::updateUI);
    }

    private void updateUI(List<AppointmentStatus> list) {
        if(list != null){
            if(list.size() > 0){
                binding.tvPendingStatusDate.setText(formatDate(list.get(0).getUpdatedAt()));
                if(list.size() > 1){
                    binding.tvAcceptStatusDate.setText(formatDate(list.get(1).getUpdatedAt()));
                }
                if(list.size() > 2){
                    binding.tvInProcessStatusDate.setText(formatDate(list.get(2).getUpdatedAt()));
                }
                if(list.size() > 3){
                    binding.tvFinishedStatusDate.setText(formatDate(list.get(3).getUpdatedAt()));
                }
            }else{
                Log.e(TAG, "updateUI: list size is 0" );
            }
        }else{
            Log.e(TAG, "updateUI: status list is null" );
        }
    }

    private void updateStatusSelection(int status) {
        switch (status){
            case 2:
                selectionUIUpdate(true,false,false);
                break;
            case 3:
                selectionUIUpdate(true,true,false);
                break;
            case 4:
                selectionUIUpdate(true,true,true);
                break;
            default:
                selectionUIUpdate(false,false,false);
        }
    }
    private String formatDate(String date){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatParse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        SimpleDateFormat formatVisible = new SimpleDateFormat("hh:mm aa, dd/MM/yyyy", Locale.ENGLISH);
        try{
            calendar.setTime(formatParse.parse(date));
            return formatVisible.format(calendar.getTime());
        }catch (Exception e){
            return date;
        }
    }
    private void selectionUIUpdate(boolean valueTwo, boolean valueThree, boolean valueFour){
        binding.tvPendingStatus.setTextColor(activity.getResources().getColor(R.color.white));

        if(valueTwo){
            binding.tvAcceptStatus.setTextColor(activity.getResources().getColor(R.color.white));
        }else{
            binding.tvAcceptStatus.setTextColor(activity.getResources().getColor(R.color.dull_white));
        }
        if(valueThree){
            binding.tvInProcessStatus.setTextColor(activity.getResources().getColor(R.color.white));
        }else{
            binding.tvInProcessStatus.setTextColor(activity.getResources().getColor(R.color.dull_white));
        }
        if(valueFour){
            binding.tvFinishedStatus.setTextColor(activity.getResources().getColor(R.color.white));
        }else{
            binding.tvFinishedStatus.setTextColor(activity.getResources().getColor(R.color.dull_white));
        }
        binding.tvOne.setSelected(true);
        binding.tvTwo.setSelected(valueTwo);
        binding.tvThree.setSelected(valueThree);
        binding.tvFour.setSelected(valueFour);
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.mb_message:
                Intent i = new Intent(activity, ChatActivity.class);
                i.putExtra("user_id","1001");
                i.putExtra("user_name","faiizii");
                i.putExtra("user_image","https://image.flaticon.com/icons/svg/21/21104.svg");
                i.putExtra("category","Plumber");
                i.putExtra("inbox_id","Unknown_000");
                activity.startActivity(i);
                break;
            case R.id.btn_back:
                activity.onBackPressed();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)activity).visibleNavigationView(GONE);
    }
}