package com.app.fixirman.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.app.fixirman.api_db_helper.DaoHelper;
import com.app.fixirman.model.chat.InboxModel;
import com.app.fixirman.model.chat.MessageModel;

import java.util.List;

import javax.inject.Inject;

public class ChatViewModel extends ViewModel {
    private DaoHelper helper;

    @Inject
    public ChatViewModel(DaoHelper helper){
        this.helper = helper;
    }

    public LiveData<List<InboxModel>> getInboxListing() {
        return helper.getInboxListing();
    }

    public void insertMessage(MessageModel model) {
        helper.insertMessage(model);
    }

    public void updateMessage(MessageModel model) {
        helper.updateMessage(model);
    }

    public List<MessageModel> getLocalMessages() {
        return  helper.getMessages();
    }
}
