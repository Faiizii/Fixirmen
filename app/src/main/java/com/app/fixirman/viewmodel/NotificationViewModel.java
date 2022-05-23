package com.app.fixirman.viewmodel;

import android.widget.ProgressBar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.app.fixirman.api_db_helper.DaoHelper;
import com.app.fixirman.model.notification.Notification;

import java.util.List;

import javax.inject.Inject;

public class NotificationViewModel extends ViewModel {
    private DaoHelper helper;

    @Inject
    public NotificationViewModel(DaoHelper helper){
        this.helper = helper;
    }

    public void init(ProgressBar pb, int userId) {
        helper.getOnlineNotifications(pb,userId);
    }

    public LiveData<List<Notification>> getNotificationList(){
        return helper.getNotifications();
    }
    public LiveData<Integer> getUnreadNotificationCount(){
        return helper.getUnreadNotificationCount();
    }
    public void readNotification(int id,int userId){
        helper.readNotification(id,userId);
    }

    public void getOnlineAppointmentDetail(String appointmentId, int userId) {
        helper.getOnlineAppointmentDetail(userId,appointmentId);
    }
}
