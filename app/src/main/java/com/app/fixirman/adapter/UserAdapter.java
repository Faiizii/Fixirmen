package com.app.fixirman.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.fixirman.model.user.User;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.app.fixirman.R;
import com.app.fixirman.databinding.VhUserBinding;
import com.app.fixirman.my_interfaces.AdapterOnClickListener;
import com.app.fixirman.utils.AppUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    private AdapterOnClickListener clickListener;

    private static final String TAG = "Adapter :";
    private FragmentActivity context;
    private List<User> userArrayList;

    public UserAdapter(FragmentActivity context, List<User> userArrayList)
    {
        this.context = context;
        this.userArrayList = userArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        VhUserBinding binding = DataBindingUtil.inflate( LayoutInflater.from(viewGroup.getContext())
                , R.layout.vh_user,viewGroup,false);
        return new MyViewHolder(binding);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int i) {
        User user = userArrayList.get(i);
        viewHolder.binding.setModel(user);

        String imgUrl = user.getImage();

        Glide.with(context).load(imgUrl).apply(
                new RequestOptions().error(R.drawable.default_user)
                        .placeholder(R.drawable.default_user).dontAnimate()
                        .fitCenter())
                .dontAnimate()
                .fitCenter()
                .placeholder(AppUtils.getImageLoader(context))
                .into(viewHolder.binding.ivProfile);
    }

    public void setOnItemClickListener(AdapterOnClickListener clickListener)
    {
        this.clickListener = clickListener;
    }

    @Override
    public int getItemCount() {
        //Log.d("check",userArrayList.size()+"");
        return userArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        VhUserBinding binding;
        private MyViewHolder(VhUserBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
            binding.cardLL.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClickListener(v,getAdapterPosition());
        }
    }
}

