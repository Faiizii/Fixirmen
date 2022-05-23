
package com.app.fixirman.model.notification;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class Notification implements Parcelable
{

    @NonNull
    @PrimaryKey
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("user_type")
    @Expose
    private String userType;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("entry_date")
    @Expose
    private String entryDate;
    @SerializedName("schedule_date")
    @Expose
    private String scheduleDate;
    @SerializedName("read_status")
    @Expose
    private String readStatus;
    @SerializedName("read_date")
    @Expose
    private String readDate;
    @SerializedName("notification_type")
    @Expose
    private String notificationType;
    @SerializedName("job_id")
    @Expose
    private String appointmentId;
    @SerializedName("sent_status")
    @Expose
    private String sentStatus;
    public final static Creator<Notification> CREATOR = new Creator<Notification>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Notification createFromParcel(Parcel in) {
            return new Notification(in);
        }

        public Notification[] newArray(int size) {
            return (new Notification[size]);
        }

    }
    ;

    protected Notification(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.userId = ((String) in.readValue((String.class.getClassLoader())));
        this.image = ((String) in.readValue((String.class.getClassLoader())));
        this.userType = ((String) in.readValue((String.class.getClassLoader())));
        this.title = ((String) in.readValue((String.class.getClassLoader())));
        this.description = ((String) in.readValue((String.class.getClassLoader())));
        this.entryDate = ((String) in.readValue((String.class.getClassLoader())));
        this.scheduleDate = ((String) in.readValue((String.class.getClassLoader())));
        this.readStatus = ((String) in.readValue((String.class.getClassLoader())));
        this.readDate = ((String) in.readValue((String.class.getClassLoader())));
        this.notificationType = ((String) in.readValue((String.class.getClassLoader())));
        this.appointmentId = ((String) in.readValue((String.class.getClassLoader())));
        this.sentStatus = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Notification() {
    }

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public String getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(String scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public String getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(String readStatus) {
        this.readStatus = readStatus;
    }

    public String getReadDate() {
        return readDate;
    }

    public void setReadDate(String readDate) {
        this.readDate = readDate;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getSentStatus() {
        return sentStatus;
    }

    public void setSentStatus(String sentStatus) {
        this.sentStatus = sentStatus;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(userId);
        dest.writeValue(image);
        dest.writeValue(userType);
        dest.writeValue(title);
        dest.writeValue(description);
        dest.writeValue(entryDate);
        dest.writeValue(scheduleDate);
        dest.writeValue(readStatus);
        dest.writeValue(readDate);
        dest.writeValue(notificationType);
        dest.writeValue(appointmentId);
        dest.writeValue(sentStatus);
    }

    public int describeContents() {
        return  0;
    }

}
