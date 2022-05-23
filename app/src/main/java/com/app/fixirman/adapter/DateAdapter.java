package com.app.fixirman.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.app.fixirman.R;
import com.app.fixirman.databinding.VhDayDateBinding;
import com.app.fixirman.model.provider.DateModel;
import com.app.fixirman.my_interfaces.AdapterOnClickListener;

import java.util.List;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.MyVh>{
    private FragmentActivity context;
    private List<DateModel> list;
    private int dateSelectedIndex;
    private AdapterOnClickListener clickListener;

    public DateAdapter(FragmentActivity context, List<DateModel> list) {
        this.context = context;
        this.list = list;
        dateSelectedIndex = 0;
    }

    @NonNull
    @Override
    public MyVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        VhDayDateBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.vh_day_date,parent,false);
        return new MyVh(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyVh holder, int position) {

        if(position == dateSelectedIndex){
            holder.dateBinding.main.setBackgroundResource(R.drawable.background_round_corner);
        }else{
            holder.dateBinding.main.setBackgroundResource(R.color.defaultBackgroundColor);
        }
        holder.dateBinding.tvDayName.setText(list.get(position).getDayName());
        holder.dateBinding.tvDayDate.setText(list.get(position).getDayNumber());
        holder.dateBinding.main.setOnClickListener(v->{
            dateSelectedIndex = position;
            notifyDataSetChanged();
            if(clickListener != null){
                clickListener.onItemClickListener(v,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnItemClickListener(AdapterOnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public String getSelectedDate(int position) {
        return list.get(position).getDate();
    }

    public String getSelectedDate() {
        if(dateSelectedIndex > getItemCount())
            dateSelectedIndex = 0;
        return list.get(dateSelectedIndex).getDate();
    }
    class MyVh extends RecyclerView.ViewHolder {
        public VhDayDateBinding dateBinding;
        public MyVh(VhDayDateBinding dateBinding) {
            super(dateBinding.getRoot());
            this.dateBinding = dateBinding;
        }
    }
}
