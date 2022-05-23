package com.app.fixirman.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.app.fixirman.R;
import com.app.fixirman.databinding.VhMessageBinding;
import com.app.fixirman.model.chat.MessageModel;
import com.app.fixirman.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private FragmentActivity context;
    private List<MessageModel> list;
    List<String> addedIds;

    public MessageAdapter(FragmentActivity context, List<MessageModel> list) {
        this.context = context;
        this.list = list;
        addedIds = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        VhMessageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.vh_message,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MessageModel model = list.get(position);
        if(addedIds.contains(model.getHashKey())){
            //do nothing
        }else{
            addedIds.add(model.getHashKey());
        }

        holder.messageBinding.setModel(model);
        if(new SessionManager(context).getUserId() == model.getMyId()){
            //sender
            holder.messageBinding.layoutReceiver.setVisibility(View.GONE);
            holder.messageBinding.layoutSender.setVisibility(View.VISIBLE);

        }else{
            //receiver
            holder.messageBinding.layoutSender.setVisibility(View.GONE);
            holder.messageBinding.layoutReceiver.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addItem(MessageModel model) {
        if(!addedIds.contains(model.getHashKey())) {
            list.add(model);
            notifyItemInserted(list.size() - 1);
        }else{
            //contains already nothing to add
        }
    }

    public void updateItem(MessageModel model) {
        int tempPosition = list.size() - 1;
        while (tempPosition > 0){
            MessageModel tempModel = list.get(tempPosition);
            if(tempModel.getHashKey().equals(model.getHashKey())){
                //replace the model here
                list.set(tempPosition,model);
                break;
            }
            tempPosition = tempPosition - 1;
        }
        notifyItemChanged(tempPosition);
    }
}
