package com.app.fixirman.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.app.fixirman.R;
import com.app.fixirman.databinding.VhRatingBinding;
import com.app.fixirman.model.provider.ReviewsModel;
import com.app.fixirman.utils.AppConstants;
import com.app.fixirman.utils.AppUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private FragmentActivity context;
    private List<ReviewsModel> list;

    public ReviewsAdapter(FragmentActivity context, List<ReviewsModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        VhRatingBinding binding = DataBindingUtil.inflate(inflater, R.layout.vh_rating,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ReviewsModel model = list.get(position);
        holder.ratingBinding.setModel(model);
        Glide.with(context).load(AppConstants.HOST_URL+model.getUserImage())
                .apply(new RequestOptions().error(R.drawable.default_user))
                .placeholder(AppUtils.getImageLoader(context))
                .into(holder.ratingBinding.ivProfileImage);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
