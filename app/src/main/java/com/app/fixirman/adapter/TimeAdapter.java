package com.app.fixirman.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.app.fixirman.R;
import com.app.fixirman.databinding.VhTimeSlotBinding;

import java.util.List;

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.MyVh>{
    private FragmentActivity context;
    private List<String> list;
    private int timeSelectedIndex;
    public TimeAdapter(FragmentActivity context, List<String> list) {
        this.context = context;
        this.list = list;
        timeSelectedIndex = 0;
    }

    @NonNull
    @Override
    public MyVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        VhTimeSlotBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.vh_time_slot,parent,false);
        return new MyVh(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyVh holder, int position) {

        if(position == timeSelectedIndex){
            holder.timeBinding.main.setBackgroundResource(R.drawable.background_round_corner);
        }else{
            holder.timeBinding.main.setBackgroundResource(R.color.defaultBackgroundColor);
        }
        holder.timeBinding.tvTimeSlot.setText(list.get(position));
        holder.timeBinding.main.setOnClickListener(v->{
            timeSelectedIndex = position;
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateList(List<String> tempList) {
        list.clear();
        list.addAll(tempList);
        if(timeSelectedIndex >= list.size()){
            timeSelectedIndex = 0;
        }
        notifyDataSetChanged();
    }

    public String getSelectedTime() {
        if(timeSelectedIndex > getItemCount())
            timeSelectedIndex = 0;
        return list.get(timeSelectedIndex);
    }

    class MyVh extends RecyclerView.ViewHolder {
        public VhTimeSlotBinding timeBinding;
        public MyVh(VhTimeSlotBinding timeBinding) {
            super(timeBinding.getRoot());
            this.timeBinding = timeBinding;
        }
    }
}