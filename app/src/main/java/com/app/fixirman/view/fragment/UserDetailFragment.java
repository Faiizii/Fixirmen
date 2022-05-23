package com.app.fixirman.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.adapters.ChronometerBindingAdapter;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import dagger.android.AndroidInjection;
import dagger.android.support.AndroidSupportInjection;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.fixirman.R;
import com.app.fixirman.adapter.UserAddressAdapter;
import com.app.fixirman.databinding.FragmentUserDetailBinding;
import com.app.fixirman.model.user.User;
import com.app.fixirman.model.user.UserAddress;
import com.app.fixirman.my_interfaces.AdapterOnClickListener;
import com.app.fixirman.utils.AppConstants;
import com.app.fixirman.utils.AppUtils;
import com.app.fixirman.utils.SessionManager;
import com.app.fixirman.view.activity.ChangeAddressActivity;
import com.app.fixirman.view.activity.MainActivity;
import com.app.fixirman.view.activity.UpdateProfileActivity;
import com.app.fixirman.viewmodel.UserViewModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static android.view.View.GONE;
import static com.app.fixirman.adapter.UserAddressAdapter.RV_TYPE_LISTING;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserDetailFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    FragmentUserDetailBinding binding;
    @Inject
    ViewModelProvider.Factory factory;
    private UserViewModel viewModel;

    private FragmentActivity activity;
    private UserAddressAdapter addressAdapter;
    private List<UserAddress> adapterList;

    private SessionManager sessionManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_user_detail, container, false);
        binding.setFragment(this);
        activity = getActivity();
        initMe();
        return binding.getRoot();
    }

    private void initMe() {
        sessionManager = new SessionManager(activity);

        adapterList = new ArrayList<>();
        addressAdapter = new UserAddressAdapter(activity,adapterList,RV_TYPE_LISTING);
        binding.rvAddresses.setAdapter(addressAdapter);
        addressAdapter.onDeleteClick((view, position) -> {
            viewModel.deleteAddress(addressAdapter.getAddressId(position));
        });
        sessionManager.setOnChangeListener(this);
        updateUserDetail(sessionManager.getUser());
    }
    private void updateUserDetail(User user){
        if(user != null) {

            Glide.with(activity).load(AppConstants.HOST_URL + user.getImage()).apply(
                    new RequestOptions().error(R.drawable.default_user))
                    .fitCenter()
                    .placeholder(AppUtils.getImageLoader(activity))
                    .into(binding.ivUserProfile);
            binding.tvUserName.setText(user.getName());
            binding.tvPhone.setText(user.getPhone());
            binding.tvEmail.setText(user.getEmail());
            binding.tvCnic.setText(user.getCnic());
            binding.rbRating.setRating(user.getRating());
            binding.tvStarCount.setText(user.getTotalReviews()+"");

        }else{
            activity.finish();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        //AndroidInjection.inject(this);
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onAttachFragment(@NonNull Fragment childFragment) {
        AndroidSupportInjection.inject(this);
        super.onAttachFragment(childFragment);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(this,factory).get(UserViewModel.class);
        viewModel.getAddresses(sessionManager.getUserId()).observe(getViewLifecycleOwner(),this::updateUI);
    }

    private void updateUI(List<UserAddress> userAddresses) {
        if(userAddresses != null){
            adapterList.clear();
            adapterList.addAll(userAddresses);
            addressAdapter.notifyDataSetChanged();
        }
    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_back:
                activity.onBackPressed();
                break;
            case R.id.btn_addAddress:
                Intent iAddress = new Intent(activity, ChangeAddressActivity.class);
                startActivity(iAddress);
                break;
            case R.id.fab_edit:
                Intent iUpdate = new Intent(activity, UpdateProfileActivity.class);
                iUpdate.putExtra("type", AppConstants.UPDATE_PROFILE);
                startActivity(iUpdate);
                break;
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        updateUserDetail(sessionManager.getUser());
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)activity).visibleNavigationView(GONE);
    }
}
