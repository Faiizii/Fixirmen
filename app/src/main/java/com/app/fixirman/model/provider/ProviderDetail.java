
package com.app.fixirman.model.provider;

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
public class ProviderDetail implements Parcelable
{

    @NonNull
    @PrimaryKey
    @SerializedName("employee_id")
    @Expose
    private String employeeId;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("mobile_no")
    @Expose
    private String mobileNo;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("email_id")
    @Expose
    private String emailId;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("salt")
    @Expose
    private String salt;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("latitued")
    @Expose
    private String latitued;
    @SerializedName("longitued")
    @Expose
    private String longitued;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("activate")
    @Expose
    private String activate;
    @SerializedName("created_on")
    @Expose
    private String createdOn;
    @SerializedName("distance_away")
    @Expose
    private String distanceAway;
    @SerializedName("cost")
    @Expose
    private String cost;
    @Ignore
    @SerializedName("reviews")
    @Expose
    private List<ReviewsModel> reviewsModel = null;
    public final static Creator<ProviderDetail> CREATOR = new Creator<ProviderDetail>() {


        @SuppressWarnings({
            "unchecked"
        })
        public ProviderDetail createFromParcel(Parcel in) {
            return new ProviderDetail(in);
        }

        public ProviderDetail[] newArray(int size) {
            return (new ProviderDetail[size]);
        }

    }
    ;

    protected ProviderDetail(Parcel in) {
        this.employeeId = ((String) in.readValue((String.class.getClassLoader())));
        this.userName = ((String) in.readValue((String.class.getClassLoader())));
        this.fullName = ((String) in.readValue((String.class.getClassLoader())));
        this.mobileNo = ((String) in.readValue((String.class.getClassLoader())));
        this.image = ((String) in.readValue((String.class.getClassLoader())));
        this.emailId = ((String) in.readValue((String.class.getClassLoader())));
        this.password = ((String) in.readValue((String.class.getClassLoader())));
        this.salt = ((String) in.readValue((String.class.getClassLoader())));
        this.address = ((String) in.readValue((String.class.getClassLoader())));
        this.latitued = ((String) in.readValue((String.class.getClassLoader())));
        this.longitued = ((String) in.readValue((String.class.getClassLoader())));
        this.description = ((String) in.readValue((String.class.getClassLoader())));
        this.status = ((String) in.readValue((String.class.getClassLoader())));
        this.rating = ((String) in.readValue((String.class.getClassLoader())));
        this.activate = ((String) in.readValue((String.class.getClassLoader())));
        this.createdOn = ((String) in.readValue((String.class.getClassLoader())));
        this.distanceAway = ((String) in.readValue((String.class.getClassLoader())));
        this.cost = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.reviewsModel, (ReviewsModel.class.getClassLoader()));
    }

    public ProviderDetail() {
    }

    @NonNull
    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(@NonNull String employeeId) {
        this.employeeId = employeeId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getActivate() {
        return activate;
    }

    public void setActivate(String activate) {
        this.activate = activate;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getDistanceAway() {
        return distanceAway;
    }

    public void setDistanceAway(String distanceAway) {
        this.distanceAway = distanceAway;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public List<ReviewsModel> getReviewsModel() {
        return reviewsModel;
    }

    public void setReviewsModel(List<ReviewsModel> reviewsModel) {
        this.reviewsModel = reviewsModel;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(employeeId);
        dest.writeValue(userName);
        dest.writeValue(fullName);
        dest.writeValue(mobileNo);
        dest.writeValue(image);
        dest.writeValue(emailId);
        dest.writeValue(password);
        dest.writeValue(salt);
        dest.writeValue(address);
        dest.writeValue(latitued);
        dest.writeValue(longitued);
        dest.writeValue(description);
        dest.writeValue(status);
        dest.writeValue(rating);
        dest.writeValue(activate);
        dest.writeValue(createdOn);
        dest.writeValue(distanceAway);
        dest.writeValue(cost);
        dest.writeList(reviewsModel);
    }

    public int describeContents() {
        return  0;
    }

}
