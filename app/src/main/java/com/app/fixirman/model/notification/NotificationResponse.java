
package com.app.fixirman.model.notification;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationResponse implements Parcelable
{

    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("notifications")
    @Expose
    private List<Notification> notifications = null;
    public final static Creator<NotificationResponse> CREATOR = new Creator<NotificationResponse>() {


        @SuppressWarnings({
            "unchecked"
        })
        public NotificationResponse createFromParcel(Parcel in) {
            return new NotificationResponse(in);
        }

        public NotificationResponse[] newArray(int size) {
            return (new NotificationResponse[size]);
        }

    }
    ;

    protected NotificationResponse(Parcel in) {
        this.success = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.notifications, (Notification.class.getClassLoader()));
    }

    public NotificationResponse() {
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(success);
        dest.writeValue(message);
        dest.writeList(notifications);
    }

    public int describeContents() {
        return  0;
    }

}
