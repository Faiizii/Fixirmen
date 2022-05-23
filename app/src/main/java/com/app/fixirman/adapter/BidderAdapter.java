package com.app.fixirman.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.app.fixirman.R;
import com.app.fixirman.api.http.ApiUtils;
import com.app.fixirman.databinding.VhBidderBinding;
import com.app.fixirman.model.NormalResponse;
import com.app.fixirman.model.request.RequestBid;
import com.app.fixirman.my_interfaces.AdapterOnClickListener;
import com.app.fixirman.utils.AppConstants;
import com.app.fixirman.utils.AppUtils;
import com.app.fixirman.utils.Crashlytics;
import com.app.fixirman.utils.SessionManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BidderAdapter extends RecyclerView.Adapter<BidderAdapter.MyVh> {
    private final FragmentActivity context;
    private final List<RequestBid> list;
    private AdapterOnClickListener clickListener;

    public BidderAdapter(FragmentActivity context, List<RequestBid> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        VhBidderBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.vh_bidder,parent,false);
        return new BidderAdapter.MyVh(binding);
    }

    public void onItemClickListener(AdapterOnClickListener listener){
        clickListener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull MyVh holder, int position) {
        RequestBid model = list.get(position);
        holder.bidderBinding.tvName.setText(model.getName());
        holder.bidderBinding.rbRating.setRating(model.getRating());
        Glide.with(context).load(AppConstants.HOST_URL+model.getImage()).apply(
                new RequestOptions().error(R.drawable.default_user)
                        .placeholder(R.drawable.default_user).dontAnimate()
                        .fitCenter())
                .dontAnimate()
                .fitCenter()
                .placeholder(AppUtils.getImageLoader(context))
                .into(holder.bidderBinding.ivUserImage);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyVh extends RecyclerView.ViewHolder {
        public VhBidderBinding bidderBinding;
        public MyVh(VhBidderBinding bidderBinding) {
            super(bidderBinding.getRoot());
            this.bidderBinding = bidderBinding;
            bidderBinding.btnAccept.setOnClickListener(v -> acceptBid(getAdapterPosition(),bidderBinding.pb));
            bidderBinding.btnCancel.setOnClickListener(v-> rejectBid(getAdapterPosition(),bidderBinding.pb));
        }
    }

    private void acceptBid(int index, View progress) {
        if(!AppUtils.isNetworkAvailable(context)){
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        if(progress.getVisibility() != View.VISIBLE)
            progress.setVisibility(View.VISIBLE);
        ApiUtils.getApiInterface().acceptBid(new SessionManager(context).getUserId(),list.get(index).getId())
                .enqueue(new Callback<NormalResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<NormalResponse> call,@NonNull  Response<NormalResponse> response) {
                        progress.setVisibility(View.GONE);
                        if(response.isSuccessful()){
                            NormalResponse normalResponse = response.body();
                            if(normalResponse.getSuccess() == 1){
                                if(clickListener != null)
                                    clickListener.onItemClickListener(null,index);
                                else
                                    Toast.makeText(context, "Bid Accepted. Please refresh your data to see the changes", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(context, normalResponse.getMessage()+"", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Crashlytics.log("code: "+response.code());
                            Crashlytics.log("message: "+response.message());
                            Crashlytics.logException(new Exception("accept bid connection exception with code:"+response.code()));
                            Toast.makeText(context, "Server Connection Error. Please try again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<NormalResponse> call,@NonNull  Throwable t) {
                        progress.setVisibility(View.GONE);
                        AppUtils.handleResponse(context,t,"accept bid in adapter");
                    }
                });
    }

    private void rejectBid(int index, View progress) {
        if(!AppUtils.isNetworkAvailable(context)){
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        if(progress.getVisibility() != View.VISIBLE)
            progress.setVisibility(View.VISIBLE);

        ApiUtils.getApiInterface().changeRequestStatus(new SessionManager(context).getUserId(),list.get(index).getId(),"REJECT")
                .enqueue(new Callback<NormalResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<NormalResponse> call,@NonNull  Response<NormalResponse> response) {
                        progress.setVisibility(View.GONE);
                        if(response.isSuccessful()){
                            NormalResponse normalResponse = response.body();
                            if(normalResponse.getSuccess() == 1){
                                list.remove(index);
                                notifyItemRemoved(index);
                            }else{
                                Toast.makeText(context, normalResponse.getMessage()+"", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Crashlytics.log("code: "+response.code());
                            Crashlytics.log("message: "+response.message());
                            Crashlytics.logException(new Exception("cancel bid connection exception with code:"+response.code()));
                            Toast.makeText(context, "Server Connection Error. Please try again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<NormalResponse> call,@NonNull  Throwable t) {
                        progress.setVisibility(View.GONE);
                        AppUtils.handleResponse(context,t,"cancel bid in adapter");
                    }
                });
    }
}
