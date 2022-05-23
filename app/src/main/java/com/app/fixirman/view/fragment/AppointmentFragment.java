package com.app.fixirman.view.fragment;

import android.content.Context;
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
import com.app.fixirman.adapter.AppointmentAdapter;
import com.app.fixirman.adapter.RequestAdapter;
import com.app.fixirman.databinding.FragmentAppointmentBinding;
import com.app.fixirman.model.appointment.Appointment;
import com.app.fixirman.model.request.RequestDetail;
import com.app.fixirman.utils.AppUtils;
import com.app.fixirman.utils.SessionManager;
import com.app.fixirman.view.activity.MainActivity;
import com.app.fixirman.view.create_request.RequestModel;
import com.app.fixirman.viewmodel.AppointmentViewModel;
import com.app.fixirman.utils.Crashlytics;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

import static android.view.View.VISIBLE;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppointmentFragment extends Fragment {
    private final String TAG = "AppointmentFragment";
    private FragmentActivity activity;
    private FragmentAppointmentBinding binding;


    @Inject
    ViewModelProvider.Factory factory;
    private AppointmentViewModel viewModel;

    private int eventType = 0;
    private List<RequestDetail> allRequests,adapterList;
    private RequestAdapter adapter;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_appointment, container, false);
        binding.setFragment(this);
        activity = getActivity();
        initMe();
        return binding.getRoot();
    }

    private void initMe() {
        eventType = 0;
        allRequests = new ArrayList<>();adapterList = new ArrayList<>();
        adapter = new RequestAdapter(activity,adapterList);
        binding.rvRequests.setAdapter(adapter);

        binding.tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                eventType = tab.getPosition();
                if(viewModel != null){
                    loadData();
                }else{
                    Crashlytics.log("job status screen view model is null in tab selected listener");
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

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
        viewModel = new ViewModelProvider(this,factory).get(AppointmentViewModel.class);
        if(AppUtils.isNetworkAvailable(activity)){
            viewModel.init(binding.pb,new SessionManager(activity).getUserId());
        }else{
            Crashlytics.log("appointment screen is open without network");
        }
        loadData();
    }
    private void loadData(){
        if(eventType == 0){
            viewModel.getUpcomingRequests().observe(getViewLifecycleOwner(),this::updateUI);
        }else{
            viewModel.getPastRequests().observe(getViewLifecycleOwner(),this::updateUI);
        }
    }

    private void updateUI(List<RequestDetail> appointments) {
        if(appointments != null){
            if(appointments.size() >0 ){
                allRequests.clear();
                adapterList.clear();
                allRequests.addAll(appointments); adapterList.addAll(appointments);
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
        binding.rvRequests.setVisibility(VISIBLE);
    }

    private void noDataFound() {
        binding.rvRequests.setVisibility(View.GONE);
        binding.tvNoDataFound.setVisibility(VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)activity).setSelectIndex(0);
        ((MainActivity)activity).visibleNavigationView(VISIBLE);
    }
}
