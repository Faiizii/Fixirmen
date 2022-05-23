package com.app.fixirman.view.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.app.fixirman.R;
import com.app.fixirman.adapter.MessageAdapter;
import com.app.fixirman.databinding.ActivityChatBinding;
import com.app.fixirman.databinding.VhMessageBinding;
import com.app.fixirman.model.chat.InboxModel;
import com.app.fixirman.model.chat.MessageModel;
import com.app.fixirman.utils.AppConstants;
import com.app.fixirman.utils.AppUtils;
import com.app.fixirman.utils.SessionManager;
import com.app.fixirman.viewmodel.ChatViewModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.app.fixirman.utils.Crashlytics;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class ChatActivity extends AppCompatActivity {
    private final String TAG = "ChatActivity";

    private final String MESSAGES = "messages";
    private final String INBOX = "inbox";

    ActivityChatBinding binding;

    private SessionManager sessionManager;

    private String userName,userImage,categoryName,messageStr;
    private int userId;
    private String timeStamp;
    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm aa", Locale.ENGLISH);

    private DatabaseReference senderReference,receiverReference;
    //private FirebaseRecyclerAdapter<MessageModel, MyViewHolder> adapter;

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private ChatViewModel viewModel;

    private MessageAdapter adapter;
    private List<MessageModel> adapterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_chat);
        binding.setActivity(this);
        initMe();
    }

    private void initMe() {
        sessionManager = new SessionManager(this);
        if(getIntent().getExtras() != null){
            Bundle bundle = getIntent().getExtras();
            userId = bundle.getInt("user_id",0);
            userName = bundle.getString("user_name","");
            userImage = bundle.getString("user_image","");
            categoryName = bundle.getString("category","");
            binding.tvTitle.setText(userName);
            Glide.with(this).load(AppConstants.HOST_URL+userImage)
                    .apply(new RequestOptions().error(R.drawable.default_user))
                    .placeholder(AppUtils.getImageLoader(this))
                    .into(binding.ivUserImage);
        }
        adjustKeyBoardWithScreen();
        viewModel = new ViewModelProvider(this,viewModelFactory).get(ChatViewModel.class);
        initAdapter();
        senderReference = FirebaseDatabase.getInstance().getReference().child(sessionManager.getUserId()+"");
        receiverReference = FirebaseDatabase.getInstance().getReference().child(userId+"");
    }

    private void initAdapter() {
/*

        Query messagesReference = FirebaseDatabase.getInstance().getReference().child(sessionManager.getUserId())
                .child(MESSAGES).child(userId).orderByPriority();
        FirebaseRecyclerOptions<MessageModel> options = new FirebaseRecyclerOptions.Builder<MessageModel>()
                .setQuery(messagesReference, MessageModel.class)
                .setLifecycleOwner(this)
                .build();
        adapter = new FirebaseRecyclerAdapter<MessageModel, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int i, @NonNull MessageModel model) {
                holder.messageBinding.setModel(model);
                if(sessionManager.getUserId().equalsIgnoreCase(model.getMyId())){
                    //sender
                    holder.messageBinding.layoutReceiver.setVisibility(View.GONE);
                    holder.messageBinding.layoutSender.setVisibility(View.VISIBLE);

                }else{
                    //receiver
                    holder.messageBinding.layoutSender.setVisibility(View.GONE);
                    holder.messageBinding.layoutReceiver.setVisibility(View.VISIBLE);
                }
            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                VhMessageBinding binding = DataBindingUtil.inflate(getLayoutInflater(),R.layout.vh_message,parent,false);
                return new MyViewHolder(binding);
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                binding.rvMessages.smoothScrollToPosition(adapter.getItemCount());
            }
        };
*/

        adapterList = viewModel.getLocalMessages();
        adapter = new MessageAdapter(this,adapterList);
        binding.rvMessages.setLayoutManager(new LinearLayoutManager(this));
        binding.rvMessages.setAdapter(adapter);
        registerFirebaseListener();
    }
    private void adjustKeyBoardWithScreen(){
        //keyboard handling with adjustments of recycler view message layout
        binding.main.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            int heightDiff = binding.main.getRootView().getHeight() - binding.main.getHeight();
            if (heightDiff > AppUtils.dpToPx(ChatActivity.this, 200)) {
                // if more than 200 dp, it's probably a keyboard...
                if (adapterList != null) {
                    binding.rvMessages.smoothScrollToPosition(adapterList.size());
                }
                Log.e("KeyBoardState", "Open");
            } else {
                Log.e("KeyBoardState", "Closed");
            }
        });
    }

    public void registerFirebaseListener(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(sessionManager.getUserId()+"")
                .child(MESSAGES).child(userId+"");
        ref.orderByKey().startAt(sessionManager.getLastFetchedKey()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                try{
                    MessageModel model = snapshot.getValue(MessageModel.class);
                    if(model != null) {
                        int tempSize = viewModel.getLocalMessages().size();
                        viewModel.insertMessage(model);
                        //removes the last entry duplication
                        if(tempSize != viewModel.getLocalMessages().size()) {
                            adapter.addItem(model);
                            //save the last loaded child reference to start the listener for onward
                            sessionManager.saveLastFetchedKey(snapshot.getKey());
                            if(model.getMyId() != sessionManager.getUserId()){
                                snapshot.getRef().child("receive").setValue(true);
                                snapshot.getRef().child("read").setValue(true);
                            }
                        }
                        binding.rvMessages.smoothScrollToPosition(adapter.getItemCount()-1);
                    }else{
                        Crashlytics.log("model is null in added child chat screen");
                    }
                }catch (Exception e){
                    Log.e(TAG, "onChildAdded: ",e );
                    Crashlytics.log("on child added at chat screen observer on message reference "+ref.toString());
                    Crashlytics.logException(e);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                try{
                    MessageModel model = snapshot.getValue(MessageModel.class);
                    if(model != null) {
                        viewModel.updateMessage(model);
                        adapter.updateItem(model);
                    }else{
                        Crashlytics.log("model is null in changed child chat screen");
                    }
                }catch (Exception e){
                    Log.e(TAG, "onChildAdded: ",e );
                    Crashlytics.log("on child added at chat screen observer on message reference "+ref.toString());
                    Crashlytics.logException(e);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private boolean validate() {
        boolean check = true;
        messageStr = binding.etMessage.getText().toString().trim();

        if(messageStr.isEmpty()){
            check = false;
            Toast.makeText(this, "Message Empty", Toast.LENGTH_SHORT).show();
        }
        timeStamp = format.format(Calendar.getInstance().getTime());
        return check;
    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.btn_send:
                if(validate()){
                    if(AppUtils.isNetworkAvailable(this)){
                        sendMessage();
                    }else{
                        AppUtils.createNetworkError(this);
                    }
                }
                break;
        }

    }

    private void sendMessage() {
        showProgressBar();
        MessageModel receiverModel = new MessageModel(messageStr,timeStamp,sessionManager.getUserId(),sessionManager.getUserName()
                ,sessionManager.getUserImage(),userId);

        DatabaseReference tempReference = receiverReference.child(MESSAGES).child(sessionManager.getUserId()+"").push();
        String hashKey = tempReference.getKey();
        receiverModel.setHashKey(hashKey);
        tempReference.setValue(receiverModel)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        //add to the recycler view
                        sendMessageNotification();// send notification to receiver of new message received
                        updateReceiverInbox();
                    }else{
                        Crashlytics.log("chat screen message sending to receiver method");
                        Crashlytics.logException(task.getException());
                        hideProgressBar();
                    }
                });
    }
    private void updateReceiverInbox(){
        InboxModel receiverInbox = new InboxModel(userId,sessionManager.getUserId(),timeStamp,messageStr,
                sessionManager.getUserImage(),sessionManager.getUserName(),categoryName);

        receiverReference.child(INBOX).child(receiverInbox.getId()).setValue(receiverInbox)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        addMessageForSender();
                    }else{
                        Crashlytics.log("chat screen inbox model inserting for receiver at firebase");
                        Crashlytics.logException(task.getException());
                        hideProgressBar();
                    }
                });
    }

    private void addMessageForSender(){
        MessageModel senderModel = new MessageModel(messageStr,timeStamp,userId,userName,userImage,sessionManager.getUserId());

        DatabaseReference tempReference = senderReference.child(MESSAGES).child(userId+"").push();

        String hashKey = tempReference.getKey();
        senderModel.setHashKey(hashKey);

        tempReference.setValue(senderModel)
                .addOnCompleteListener( task -> {
                    if(task.isSuccessful()){
                        //add to the recycler view
                        tempReference.child("sent").setValue(true);
                        senderModel.setSent(true); // update to list
                        updateSenderInbox();
                    }else{
                        Crashlytics.log("chat screen message adding for sender at firebase");
                        Crashlytics.logException(task.getException());
                        hideProgressBar();
                    }
                });
    }
    private void updateSenderInbox(){
        InboxModel senderInbox = new InboxModel(sessionManager.getUserId(),userId,timeStamp,messageStr,
                userImage,userName,categoryName);
        senderReference.child(INBOX).child(senderInbox.getId()).setValue(senderInbox)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        hideProgressBar();
                    }else{
                        hideProgressBar();
                        Crashlytics.log("chat screen inbox model inserting for sender at firebase");
                        Crashlytics.logException(task.getException());
                    }
                });
    }

    private void sendMessageNotification() {

    }
    private void showProgressBar(){
        binding.btnSend.setVisibility(View.GONE);
        AppUtils.showProgressBar(binding.pb);
    }
    private void hideProgressBar(){
        AppUtils.hideProgressBar(binding.pb);
        binding.btnSend.setVisibility(View.VISIBLE);
    }
}