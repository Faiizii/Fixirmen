package com.app.fixirman.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.app.fixirman.R;
import com.app.fixirman.databinding.VhCategoryBinding;
import com.app.fixirman.model.categroy.Category;
import com.app.fixirman.my_interfaces.AdapterOnClickListener;
import com.app.fixirman.utils.AppConstants;
import com.bumptech.glide.Glide;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private final FragmentActivity context;
    private final List<Category> list;
    private AdapterOnClickListener clickListener;
    public CategoryAdapter(FragmentActivity context, List<Category> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        VhCategoryBinding binding = DataBindingUtil.inflate(inflater, R.layout.vh_category,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Category model = list.get(position);
        holder.categoryBinding.tvCategoryName.setText(model.getTitle());
        Glide.with(context).load(AppConstants.HOST_URL+model.getImage()).into(holder.categoryBinding.ivCategoryImage);
        holder.categoryBinding.main.setOnClickListener(v -> {
            if(clickListener != null)
                clickListener.onItemClickListener(v,position);
        });
    }
    public void itemClickListener(AdapterOnClickListener clickListener){
        this.clickListener = clickListener;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public Category getItem(int position) {
        return list.get(position);
    }
}
