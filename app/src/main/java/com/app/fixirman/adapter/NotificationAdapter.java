package com.app.fixirman.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.app.fixirman.R;
import com.app.fixirman.databinding.VhNotificationBinding;
import com.app.fixirman.model.notification.Notification;
import com.app.fixirman.my_interfaces.AdapterOnClickListener;
import com.app.fixirman.utils.AppConstants;
import com.app.fixirman.utils.AppUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private FragmentActivity context;
    private List<Notification> list;

    private AdapterOnClickListener clickListener;

    public NotificationAdapter(FragmentActivity context, List<Notification> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        VhNotificationBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.vh_notification,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Notification model = list.get(position);
        holder.notificationBinding.setModel(model);
        Glide.with(context).load(AppConstants.HOST_URL+model.getImage()).apply(
                new RequestOptions().error(R.drawable.default_user))
                .fitCenter()
                .placeholder(AppUtils.getImageLoader(context))
                .into(holder.notificationBinding.ivProfileImage);

        holder.notificationBinding.main.setOnClickListener(v->{
            if(clickListener != null){
                clickListener.onItemClickListener(v,position);
            }
        });
        if(model.getReadStatus().equalsIgnoreCase("Y")){
            holder.notificationBinding.cvDetails.setSelected(false);
            holder.notificationBinding.cvDetails.setAlpha(1);
        }else{
            holder.notificationBinding.cvDetails.setSelected(true);
            holder.notificationBinding.cvDetails.setAlpha(0.4f);
        }
    }

    public void onItemClick(AdapterOnClickListener clickListener){
        this.clickListener = clickListener;
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public Notification getItem(int position) {
        return list.get(position);
    }
}
