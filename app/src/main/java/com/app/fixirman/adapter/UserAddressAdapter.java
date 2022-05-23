package com.app.fixirman.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.app.fixirman.R;
import com.app.fixirman.api.http.ApiUtils;
import com.app.fixirman.databinding.VhUserAddressBinding;
import com.app.fixirman.databinding.VhUserAddressMapPickerBinding;
import com.app.fixirman.model.NormalResponse;
import com.app.fixirman.model.user.UserAddress;
import com.app.fixirman.my_interfaces.AdapterOnClickListener;
import com.app.fixirman.utils.AppConstants;
import com.app.fixirman.utils.AppUtils;
import com.app.fixirman.utils.SessionManager;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.gson.stream.MalformedJsonException;

import java.net.SocketTimeoutException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserAddressAdapter extends RecyclerView.Adapter<UserAddressAdapter.MyVh> {

    public static int RV_TYPE_MAP = 1;
    public static int RV_TYPE_LISTING = 2;

    private FragmentActivity context;
    private List<UserAddress> list;
    private AdapterOnClickListener mapItemClickListener,deleteClickListener;
    private int rvSelectedType ;
    private int selectedItem;
    public UserAddressAdapter(FragmentActivity context, List<UserAddress> list,int type) {
        this.context = context;
        this.list = list;
        rvSelectedType = type;
        selectedItem = -1;
    }

    @NonNull
    @Override
    public MyVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(rvSelectedType == RV_TYPE_MAP){
            VhUserAddressMapPickerBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                    R.layout.vh_user_address_map_picker,parent,false);
            return new MyVh(binding);
        }else{
            VhUserAddressBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                    R.layout.vh_user_address,parent,false);
            return new MyVh(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyVh holder, int position) {
        UserAddress model = list.get(position);

        int resourceId; String title;
        switch (model.getAddressType().toLowerCase()){
            case "home":
                title = "Home";
                resourceId = R.drawable.address_home;
                break;
            case "office":
                title = "Office";
                resourceId = R.drawable.address_office;
                break;
            default: {
                title = "Other";
                resourceId = R.drawable.address_other;
            }
        }

        if(rvSelectedType == RV_TYPE_MAP){
            Glide.with(context).load(resourceId).into(holder.pickerBinding.ivAddressType);
            if(position == selectedItem){
                holder.pickerBinding.fabSelected.show();
            }else{
                holder.pickerBinding.fabSelected.hide();
            }
        }else{
            Glide.with(context).load(resourceId).into(holder.addressBinding.ivIconType);
            holder.addressBinding.tvAddress.setText(model.getAddress());
            holder.addressBinding.tvAddressTitle.setText(model.getAddressTitle());
            holder.addressBinding.tvAddressType.setText(title);
        }
    }
    public void onAddressSelection(AdapterOnClickListener clickListener){
        mapItemClickListener = clickListener;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setSelectedItem(int i) {
        selectedItem = i;
        notifyDataSetChanged();
    }

    public void onDeleteClick(AdapterOnClickListener clickListener) {
        deleteClickListener = clickListener;
    }

    public int getAddressId(int position) {
        try {
            return list.get(position).getId();
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }


    class MyVh extends RecyclerView.ViewHolder {
        public VhUserAddressBinding addressBinding;
        public VhUserAddressMapPickerBinding pickerBinding;

        public MyVh(@NonNull VhUserAddressBinding itemView) {
            super(itemView.getRoot());
            addressBinding = itemView;
            addressBinding.btnDelete.setOnClickListener(v->{
                deleteAddress(addressBinding.btnDelete,addressBinding.pb,getAdapterPosition());
            });
        }

        public MyVh(@NonNull VhUserAddressMapPickerBinding itemView) {
            super(itemView.getRoot());
            pickerBinding = itemView;
            pickerBinding.main.setOnClickListener(v->{
                selectedItem = getAdapterPosition();
                if(mapItemClickListener != null){
                    mapItemClickListener.onItemClickListener(v,getAdapterPosition());
                }
                notifyDataSetChanged();
            });
        }
    }

    private void deleteAddress(MaterialButton btnDelete, ProgressBar pb, int index) {
        showProgressBar(btnDelete,pb);
        ApiUtils.getApiInterface().deleteAddressOnServer(new SessionManager(context).getUserId(),getAddressId(index)).enqueue(new Callback<NormalResponse>() {
            @Override
            public void onResponse(@NonNull Call<NormalResponse> call,@NonNull Response<NormalResponse> response) {
                hideProgressBar(btnDelete,pb);

                if(response.isSuccessful()){
                    NormalResponse normalResponse = response.body();
                    if(normalResponse.getSuccess() == 1){
                        if(deleteClickListener != null){
                            deleteClickListener.onItemClickListener(btnDelete,index);
                        }
                    }else{
                        AppUtils.createFailedDialog(context,normalResponse.getMessage());
                    }
                }else{
                    AppUtils.createFailedDialog(context,"Server Connection Error");
                    Log.e("addressAdapter", "onResponse: "+response.code() );
                }
            }

            @Override
            public void onFailure(@NonNull Call<NormalResponse> call,@NonNull Throwable t) {
                hideProgressBar(btnDelete,pb);
                AppUtils.handleResponse(context,t,"delete address api");
            }
        });
    }
    private void showProgressBar(MaterialButton delete,ProgressBar pb){
        delete.setText("");
        delete.setEnabled(false);
        AppUtils.showProgressBar(pb);
    }

    private void hideProgressBar(MaterialButton delete,ProgressBar pb){
        delete.setText("Delete");
        delete.setEnabled(true);
        AppUtils.hideProgressBar(pb);
    }
}
