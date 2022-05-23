package com.app.fixirman.model.appointment;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class AppointmentStatus implements Parcelable
{

    @NonNull
    @PrimaryKey
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("request_id")
    @Expose
    private String requestId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("update_by")
    @Expose
    private String updateBy;
    @SerializedName("user_type")
    @Expose
    private String userType;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    public final static Parcelable.Creator<AppointmentStatus> CREATOR = new Creator<AppointmentStatus>() {


        @SuppressWarnings({
                "unchecked"
        })
        public AppointmentStatus createFromParcel(Parcel in) {
            return new AppointmentStatus(in);
        }

        public AppointmentStatus[] newArray(int size) {
            return (new AppointmentStatus[size]);
        }

    }
            ;

    protected AppointmentStatus(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.requestId = ((String) in.readValue((String.class.getClassLoader())));
        this.status = ((String) in.readValue((String.class.getClassLoader())));
        this.updateBy = ((String) in.readValue((String.class.getClassLoader())));
        this.userType = ((String) in.readValue((String.class.getClassLoader())));
        this.updatedAt = ((String) in.readValue((String.class.getClassLoader())));
    }

    public AppointmentStatus() {
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(requestId);
        dest.writeValue(status);
        dest.writeValue(updateBy);
        dest.writeValue(userType);
        dest.writeValue(updatedAt);
    }

    public int describeContents() {
        return 0;
    }

}