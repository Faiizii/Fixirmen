package com.app.fixirman.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.app.fixirman.R;
import com.app.fixirman.databinding.VhInboxBinding;
import com.app.fixirman.model.chat.InboxModel;
import com.app.fixirman.utils.AppConstants;
import com.app.fixirman.utils.AppUtils;
import com.app.fixirman.view.activity.ChatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class InboxAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private FragmentActivity context;
    private List<InboxModel> list;

    public InboxAdapter(FragmentActivity context, List<InboxModel> models) {
        this.context = context;
        this.list = models;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        VhInboxBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.vh_inbox,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        InboxModel model = list.get(position);
        holder.inboxBinding.setModel(model);
        Glide.with(context).load(AppConstants.HOST_URL+model.getUserImage())
                .apply(new RequestOptions().error(R.drawable.default_user))
                .placeholder(AppUtils.getImageLoader(context))
                .into(holder.inboxBinding.ivProfileImage);

        holder.inboxBinding.main.setOnClickListener(v->{
            Intent i = new Intent(context, ChatActivity.class);
            i.putExtra("user_id",model.getUserId());
            i.putExtra("user_name",model.getUserName());
            i.putExtra("user_image",model.getUserImage());
            i.putExtra("category",model.getCategory());
            i.putExtra("inbox_id",model.getId());
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
