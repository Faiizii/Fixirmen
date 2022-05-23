package com.app.fixirman.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.fixirman.databinding.VhAppointmentBinding;
import com.app.fixirman.databinding.VhCategoryBinding;
import com.app.fixirman.databinding.VhInboxBinding;
import com.app.fixirman.databinding.VhMessageBinding;
import com.app.fixirman.databinding.VhNotificationBinding;
import com.app.fixirman.databinding.VhRatingBinding;
import com.app.fixirman.databinding.VhServiceProviderBinding;
import com.app.fixirman.databinding.VhSubCategoryBinding;

public class MyViewHolder extends RecyclerView.ViewHolder {

    public VhCategoryBinding categoryBinding;
    public VhSubCategoryBinding subCategoryBinding;
    public VhServiceProviderBinding serviceProviderBinding;
    public VhAppointmentBinding appointmentBinding;
    public VhNotificationBinding notificationBinding;
    public VhInboxBinding inboxBinding;
    public VhMessageBinding messageBinding;
    public VhRatingBinding ratingBinding;

    public MyViewHolder( VhRatingBinding ratingBinding) {
        super(ratingBinding.getRoot());
        this.ratingBinding = ratingBinding;
    }
    public MyViewHolder( VhMessageBinding messageBinding) {
        super(messageBinding.getRoot());
        this.messageBinding = messageBinding;
    }

    public MyViewHolder( VhInboxBinding inboxBinding) {
        super(inboxBinding.getRoot());
        this.inboxBinding = inboxBinding;
    }
    public MyViewHolder( VhNotificationBinding notificationBinding) {
        super(notificationBinding.getRoot());
        this.notificationBinding = notificationBinding;
    }
    public MyViewHolder( VhAppointmentBinding vhAppointmentBinding) {
        super(vhAppointmentBinding.getRoot());
        this.appointmentBinding = vhAppointmentBinding;
    }

    public MyViewHolder( VhCategoryBinding categoryBinding) {
        super(categoryBinding.getRoot());
        this.categoryBinding = categoryBinding;
    }

    public MyViewHolder( VhSubCategoryBinding subCategoryBinding) {
        super(subCategoryBinding.getRoot());
        this.subCategoryBinding = subCategoryBinding;
    }

    public MyViewHolder( VhServiceProviderBinding serviceProviderBinding) {
        super(serviceProviderBinding.getRoot());
        this.serviceProviderBinding = serviceProviderBinding;
    }

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
    }
}
