package com.app.fixirman.adapter;

import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.app.fixirman.R;
import com.app.fixirman.databinding.VhSubCategoryBinding;
import com.app.fixirman.model.categroy.SubCategory;

import java.util.List;

public class SubCategoryAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private final FragmentActivity context;
    private final List<SubCategory> list;
    private final ArrayMap<Integer,SubCategory> selectedServices;

    public SubCategoryAdapter(FragmentActivity context, List<SubCategory> list) {
        this.context = context;
        this.list = list;
        selectedServices = new ArrayMap<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        VhSubCategoryBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.vh_sub_category,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SubCategory model = list.get(position);
        holder.subCategoryBinding.setModel(model);
        holder.subCategoryBinding.mbSelect.setText(selectedServices.containsKey(model.getId()) ? "Remove" : "Select");
        holder.subCategoryBinding.mbSelect.setBackgroundColor(selectedServices.containsKey(model.getId()) ? context.getResources().getColor(R.color.red) : context.getResources().getColor(R.color.colorPrimary));
        holder.subCategoryBinding.mbSelect.setOnClickListener(v -> {

            if(selectedServices.containsKey(model.getId())){
                selectedServices.remove(model.getId());
            }else{
                selectedServices.put(model.getId(),model);
            }
            notifyItemChanged(position);
        });
    }

    public ArrayMap<Integer, SubCategory> getSelectedServices() {
        return selectedServices;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
