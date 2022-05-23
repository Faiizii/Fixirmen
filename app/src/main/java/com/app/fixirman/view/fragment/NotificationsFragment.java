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
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.fixirman.R;
import com.app.fixirman.adapter.NotificationAdapter;
import com.app.fixirman.databinding.FragmentNotificationsBinding;
import com.app.fixirman.model.notification.Notification;
import com.app.fixirman.utils.AppConstants;
import com.app.fixirman.utils.AppUtils;
import com.app.fixirman.utils.SessionManager;
import com.app.fixirman.view.activity.MainActivity;
import com.app.fixirman.view.activity.RequestDetailActivity;
import com.app.fixirman.view.create_request.CreateRequestActivity;
import com.app.fixirman.viewmodel.NotificationViewModel;
import com.app.fixirman.utils.Crashlytics;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

import static android.view.View.VISIBLE;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationsFragment extends Fragment {

    private final String TAG = "NotificationsFragment";
    private FragmentActivity activity;

    private FragmentNotificationsBinding binding;

    @Inject
    ViewModelProvider.Factory factory;
    private NotificationViewModel viewModel;

    private List<Notification> adapterList;
    private NotificationAdapter adapter;
    private SessionManager sessionManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_notifications, container, false);
        activity  = getActivity();
        initMe();
        return binding.getRoot();
    }

    private void initMe() {
        sessionManager = new SessionManager(activity);
        adapterList = new ArrayList<>();
        adapter = new NotificationAdapter(activity,adapterList);
        binding.rvNotifications.setLayoutManager(new LinearLayoutManager(activity));
        binding.rvNotifications.setAdapter(adapter);
        adapter.onItemClick((view, Position) -> {
            Notification model = adapter.getItem(Position);
            if(model != null && viewModel != null){
                viewModel.readNotification(model.getId(),sessionManager.getUserId());
                if(AppConstants.NOTIFICATION_APPOINTMENT.equalsIgnoreCase(model.getNotificationType())){
                    viewModel.getOnlineAppointmentDetail(model.getAppointmentId(),sessionManager.getUserId());
                    //open appointment details
                    Intent i = new Intent(activity, RequestDetailActivity.class);
                    i.putExtra("request_id",model.getAppointmentId());
                    startActivity(i);
                }else{
                    Crashlytics.log("notification click at notification screen with type "+model.getNotificationType());
                }
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(this,factory).get(NotificationViewModel.class);
        if(AppUtils.isNetworkAvailable(activity)) {
            viewModel.init(binding.pb, sessionManager.getUserId());
        }else{
            Crashlytics.log("notification screen opened without network");
        }

        viewModel.getNotificationList().observe(getViewLifecycleOwner(), this::updateUI);
    }

    private void updateUI(List<Notification> notifications) {
        if(notifications != null){
            if(notifications.size() > 0){
                adapterList.clear();
                adapterList.addAll(notifications);
                adapter.notifyDataSetChanged();
                dataFound();
            }else{
                noDataFound();
            }
        }else{
            noDataFound();
        }
    }

    private void dataFound() {
        binding.tvNoDataFound.setVisibility(View.GONE);
        binding.rvNotifications.setVisibility(VISIBLE);
    }

    private void noDataFound() {
        binding.rvNotifications.setVisibility(View.GONE);
        binding.tvNoDataFound.setVisibility(VISIBLE);
    }

    public void onClick(View v){
        switch (v.getId()){

        }
    }
    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)activity).setSelectIndex(2);
        ((MainActivity)activity).visibleNavigationView(VISIBLE);
    }
}
