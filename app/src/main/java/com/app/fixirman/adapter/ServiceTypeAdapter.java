package com.app.fixirman.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.app.fixirman.R;
import com.app.fixirman.databinding.VhServiceTypeBinding;
import com.app.fixirman.model.categroy.ServiceType;
import com.app.fixirman.utils.AppConstants;
import com.bumptech.glide.Glide;

import java.util.List;

public class ServiceTypeAdapter extends RecyclerView.Adapter<ServiceTypeAdapter.MyVh>{
    private FragmentActivity context;
    private List<ServiceType> list;
    private int selectedItem;

    public ServiceTypeAdapter(FragmentActivity context, List<ServiceType> list) {
        this.context = context;
        this.list = list;
        selectedItem = 0;
    }

    @NonNull
    @Override
    public MyVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        VhServiceTypeBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.vh_service_type,parent,false);
        return new MyVh(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyVh holder, int position) {
        ServiceType model = list.get(position);

        if(position == selectedItem){
            holder.binding.fabSelectedType.show();
        }else{
            holder.binding.fabSelectedType.hide();
        }
        holder.binding.tvTitle.setText(model.getTitle());
        Glide.with(context).load(AppConstants.HOST_URL+model.getImage()).into(holder.binding.ivServiceType);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public int getSelectedServiceType() {
        if(selectedItem > getItemCount())
            selectedItem = 0;
        return list.get(selectedItem).getId();
    }

    class MyVh extends RecyclerView.ViewHolder {
        public VhServiceTypeBinding binding;
        public MyVh(@NonNull VhServiceTypeBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
            binding.main.setOnClickListener(v->{
                selectedItem = getAdapterPosition();
                notifyDataSetChanged();
            });
        }
    }
}
