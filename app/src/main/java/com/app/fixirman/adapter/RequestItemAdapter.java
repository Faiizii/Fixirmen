package com.app.fixirman.adapter;

import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.app.fixirman.R;
import com.app.fixirman.databinding.VhInboxBinding;
import com.app.fixirman.databinding.VhRequestSelectedItemBinding;
import com.app.fixirman.model.categroy.SubCategory;
import com.app.fixirman.my_interfaces.AdapterOnClickListener;
import com.app.fixirman.utils.AppConstants;
import com.app.fixirman.view.create_request.RequestModel;
import com.bumptech.glide.Glide;

public class RequestItemAdapter extends RecyclerView.Adapter<RequestItemAdapter.MyVh>{
    private FragmentActivity context;
    private ArrayMap<Integer, RequestModel> list;

    private AdapterOnClickListener clickListener;

    public RequestItemAdapter(FragmentActivity context, ArrayMap<Integer, RequestModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        VhRequestSelectedItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.vh_request_selected_item,parent,false);
        return new MyVh(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyVh holder, int position) {
        RequestModel model = list.get(list.keyAt(position));
        try {
            holder.binding.tvCategoryName.setText(model.getCategory().getTitle());
            Glide.with(context).load(AppConstants.HOST_URL+model.getCategory().getImage()).into(holder.binding.ivCategoryImage);
            ArrayMap<Integer, SubCategory> tempLIst = model.getServices();
            if(tempLIst != null && tempLIst.size() > 0){

                String tempStr = tempLIst.get(tempLIst.keyAt(0)).getTitle();
                if(model.getServices().size() > 1)
                    tempStr = tempStr+" and "+ (tempLIst.size() -1)+" More";

                holder.binding.tvSubCategories.setText(tempStr);
                holder.binding.tvPrice.setText(model.getDate()+" b/t "+model.getTime());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void onRemoveClickListener(AdapterOnClickListener clickListener){
        this.clickListener = clickListener;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public int getKeyId(int position) {
        return list.keyAt(position);
    }

    class MyVh extends RecyclerView.ViewHolder {
        public VhRequestSelectedItemBinding binding;
        public MyVh(@NonNull VhRequestSelectedItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
            binding.btnRemove.setOnClickListener(v->{
                if(clickListener != null){
                    clickListener.onItemClickListener(v,getAdapterPosition());
                }
            });
        }
    }
}
