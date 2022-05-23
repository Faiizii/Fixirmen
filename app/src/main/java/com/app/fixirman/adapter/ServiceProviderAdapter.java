package com.app.fixirman.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.app.fixirman.R;
import com.app.fixirman.databinding.VhServiceProviderBinding;
import com.app.fixirman.model.categroy.ServiceProviders;
import com.app.fixirman.utils.AppConstants;
import com.app.fixirman.utils.AppUtils;
import com.app.fixirman.view.activity.ProviderDetailsActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class ServiceProviderAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private FragmentActivity context;
    private List<ServiceProviders> list;

    public ServiceProviderAdapter(FragmentActivity context, List<ServiceProviders> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        VhServiceProviderBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.vh_service_provider,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ServiceProviders model = list.get(position);
        holder.serviceProviderBinding.tvName.setText(model.getEmployeeName());
        holder.serviceProviderBinding.btnRatingStars.setText(model.getEmployeeRating());
        holder.serviceProviderBinding.tvDistant.setText(model.getDistanceAway()+" KM Away");
        holder.serviceProviderBinding.tvProfession.setText(model.getCategoryName());
        holder.serviceProviderBinding.tvRate.setText("$"+model.getCost()+" per hour");
        holder.serviceProviderBinding.main.setOnClickListener(v->{
            //open details
            Intent i = new Intent(context, ProviderDetailsActivity.class);
            i.putExtra("service_provider",model);
            context.startActivity(i);
        });
        Glide.with(context).load(AppConstants.HOST_URL+"")
                .apply(new RequestOptions().error(R.drawable.default_user))
                .placeholder(AppUtils.getImageLoader(context))
                .into(holder.serviceProviderBinding.ivProfileImage);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
