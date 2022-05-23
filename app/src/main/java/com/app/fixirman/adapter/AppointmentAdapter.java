package com.app.fixirman.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.app.fixirman.R;
import com.app.fixirman.databinding.VhAppointmentBinding;
import com.app.fixirman.model.appointment.Appointment;
import com.app.fixirman.utils.AppConstants;
import com.app.fixirman.utils.AppUtils;
import com.app.fixirman.view.fragment.AppointmentDetailFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AppointmentAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private FragmentActivity context;
    private List<Appointment> list;

    public AppointmentAdapter(FragmentActivity context, List<Appointment> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        VhAppointmentBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.vh_appointment,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Appointment model = list.get(position);
        holder.appointmentBinding.tvCategoryName.setText(model.getCategoryName());
        holder.appointmentBinding.tvSubCategory.setText(model.getSubCategoryName());
        holder.appointmentBinding.tvName.setText(model.getEmployeeName());
        holder.appointmentBinding.tvStatus.setText(model.getStatusString());
        holder.appointmentBinding.tvRate.setText(model.getCost());
        holder.appointmentBinding.tvDateTime.setText(formatDate(model.getAppointmentDate())+" "+model.getAppointmentTime());
        switch (model.getStatus()){
            case AppConstants.ACCEPT:
            case AppConstants.FINISHED:
                holder.appointmentBinding.tvStatus.setTextColor(context.getResources().getColor(R.color.green));
                break;
            case AppConstants.IN_PROCESS:
                holder.appointmentBinding.tvStatus.setTextColor(context.getResources().getColor(R.color.orange));
                break;
            case AppConstants.REJECT:case AppConstants.CANCELLED:case AppConstants.FAILED:
                holder.appointmentBinding.tvStatus.setTextColor(context.getResources().getColor(R.color.red));
                break;
            default:
                holder.appointmentBinding.tvStatus.setTextColor(context.getResources().getColor(R.color.white));
        }
        Glide.with(context).load(AppConstants.HOST_URL+model.getEmployeeImage())
                .apply(new RequestOptions().error(R.drawable.default_user))
                .placeholder(AppUtils.getImageLoader(context))
                .into(holder.appointmentBinding.ivProfileImage);

        holder.appointmentBinding.main.setOnClickListener(v->{
            AppointmentDetailFragment fragment = new AppointmentDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("object",model);
            fragment.setArguments(bundle);
            context.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main,fragment).addToBackStack("appointment_detail")
                    .commit();
        });
    }
    private String formatDate(String date){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatParse = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        SimpleDateFormat formatVisible = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        try{
            calendar.setTime(formatParse.parse(date));
            return formatVisible.format(calendar.getTime());
        }catch (Exception e){
            return date;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
