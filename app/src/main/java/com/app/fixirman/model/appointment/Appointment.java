
package com.app.fixirman.model.appointment;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
@Entity
public class Appointment implements Parcelable
{

    @NonNull
    @PrimaryKey
    @SerializedName("request_id")
    @Expose
    private String requestId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("employee_id")
    @Expose
    private String employeeId;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("sub_category_id")
    @Expose
    private String subCategoryId;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("cost")
    @Expose
    private String cost;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("created_on")
    @Expose
    private String createdOn;
    @SerializedName("adress")
    @Expose
    private String adress;
    @SerializedName("latitued")
    @Expose
    private String latitued;
    @SerializedName("longitued")
    @Expose
    private String longitued;
    @SerializedName("status_string")
    @Expose
    private String statusString;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("sub_category_name")
    @Expose
    private String subCategoryName;
    @SerializedName("employee_name")
    @Expose
    private String employeeName;
    @SerializedName("employee_image")
    @Expose
    private String employeeImage;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("user_image")
    @Expose
    private String userImage;

    @SerializedName("appointment_time")
    @Expose
    private String appointmentTime;

    @SerializedName("appointment_date")
    @Expose
    private String appointmentDate;

    @Ignore
    @SerializedName("status_history")
    @Expose
    private List<AppointmentStatus> statusHistory = null;
    public final static Creator<Appointment> CREATOR = new Creator<Appointment>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Appointment createFromParcel(Parcel in) {
            return new Appointment(in);
        }

        public Appointment[] newArray(int size) {
            return (new Appointment[size]);
        }

    }
    ;

    protected Appointment(Parcel in) {
        this.requestId = ((String) in.readValue((String.class.getClassLoader())));
        this.userId = ((String) in.readValue((String.class.getClassLoader())));
        this.employeeId = ((String) in.readValue((String.class.getClassLoader())));
        this.categoryId = ((String) in.readValue((String.class.getClassLoader())));
        this.subCategoryId = ((String) in.readValue((String.class.getClassLoader())));
        this.status = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.cost = ((String) in.readValue((String.class.getClassLoader())));
        this.description = ((String) in.readValue((String.class.getClassLoader())));
        this.createdOn = ((String) in.readValue((String.class.getClassLoader())));
        this.adress = ((String) in.readValue((String.class.getClassLoader())));
        this.latitued = ((String) in.readValue((String.class.getClassLoader())));
        this.longitued = ((String) in.readValue((String.class.getClassLoader())));
        this.statusString = ((String) in.readValue((String.class.getClassLoader())));
        this.categoryName = ((String) in.readValue((String.class.getClassLoader())));
        this.subCategoryName = ((String) in.readValue((String.class.getClassLoader())));
        this.employeeName = ((String) in.readValue((String.class.getClassLoader())));
        this.employeeImage = ((String) in.readValue((String.class.getClassLoader())));
        this.userName = ((String) in.readValue((String.class.getClassLoader())));
        this.userImage = ((String) in.readValue((String.class.getClassLoader())));
        this.appointmentTime = ((String) in.readValue((String.class.getClassLoader())));
        this.appointmentDate = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.statusHistory, (AppointmentStatus.class.getClassLoader()));
    }

    public Appointment() {
    }

    @NonNull
    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(@NonNull String requestId) {
        this.requestId = requestId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(String subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getLatitued() {
        return latitued;
    }

    public void setLatitued(String latitued) {
        this.latitued = latitued;
    }

    public String getLongitued() {
        return longitued;
    }

    public void setLongitued(String longitued) {
        this.longitued = longitued;
    }

    public String getStatusString() {
        return statusString;
    }

    public void setStatusString(String statusString) {
        this.statusString = statusString;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeImage() {
        return employeeImage;
    }

    public void setEmployeeImage(String employeeImage) {
        this.employeeImage = employeeImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public List<AppointmentStatus> getStatusHistory() {
        return statusHistory;
    }

    public void setStatusHistory(List<AppointmentStatus> statusHistory) {
        this.statusHistory = statusHistory;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(requestId);
        dest.writeValue(userId);
        dest.writeValue(employeeId);
        dest.writeValue(categoryId);
        dest.writeValue(subCategoryId);
        dest.writeValue(status);
        dest.writeValue(cost);
        dest.writeValue(description);
        dest.writeValue(createdOn);
        dest.writeValue(adress);
        dest.writeValue(latitued);
        dest.writeValue(longitued);
        dest.writeValue(statusString);
        dest.writeValue(categoryName);
        dest.writeValue(subCategoryName);
        dest.writeValue(employeeName);
        dest.writeValue(employeeImage);
        dest.writeValue(userName);
        dest.writeValue(userImage);
        dest.writeValue(appointmentTime);
        dest.writeValue(appointmentDate);
        dest.writeList(statusHistory);
    }

    public int describeContents() {
        return  0;
    }

}
