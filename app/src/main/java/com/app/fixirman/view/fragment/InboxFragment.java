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
import com.app.fixirman.adapter.InboxAdapter;
import com.app.fixirman.databinding.FragmentInboxBinding;
import com.app.fixirman.model.chat.InboxModel;
import com.app.fixirman.view.activity.MainActivity;
import com.app.fixirman.viewmodel.ChatViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * A simple {@link Fragment} subclass.
 */
public class InboxFragment extends Fragment {

    private final String TAG = "InboxFragment";
    private FragmentActivity activity;
    private FragmentInboxBinding binding;

    @Inject
    ViewModelProvider.Factory factory;
    private ChatViewModel viewModel;

    private List<InboxModel> adapterList;
    private InboxAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_inbox, container, false);
        binding.setFragment(this);
        activity = getActivity();
        initMe();
        return binding.getRoot();
    }

    private void initMe() {
        adapterList = new ArrayList<>();
        adapter = new InboxAdapter(activity,adapterList);
        binding.rvInbox.setLayoutManager(new LinearLayoutManager(activity));
        binding.rvInbox.setAdapter(adapter);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(this,factory).get(ChatViewModel.class);
        viewModel.getInboxListing().observe(getViewLifecycleOwner(),this::updateUI);
    }

    private void updateUI(List<InboxModel> inboxModels) {
        if(inboxModels != null){
            if(inboxModels.size() > 0){
                dataFound();
                adapterList.clear();
                adapterList.addAll(inboxModels);
                adapter.notifyDataSetChanged();
            }else{
                noDataFound();
            }
        }else{
            noDataFound();
        }
    }

    private void dataFound() {
        binding.tvNoDataFound.setVisibility(View.GONE);
        binding.rvInbox.setVisibility(VISIBLE);
    }

    private void noDataFound() {
        binding.rvInbox.setVisibility(View.GONE);
        binding.tvNoDataFound.setVisibility(VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        //((MainActivity)activity).setSelectIndex(3);
        ((MainActivity)activity).visibleNavigationView(GONE);
    }
}
