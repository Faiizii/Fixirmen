
package com.app.fixirman.model.user;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class User implements Parcelable
{

    @NonNull
    @PrimaryKey
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("backup_phone")
    @Expose
    private String backupPhone;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("cnic")
    @Expose
    private String cnic;
    @SerializedName("is_completed")
    @Expose
    private String isCompleted;
    @SerializedName("is_verified")
    @Expose
    private String isVerified;
    @SerializedName("rating")
    @Expose
    private float rating;
    @SerializedName("total_reviews")
    @Expose
    private int totalReviews;
    @Ignore
    @SerializedName("addresses")
    @Expose
    private List<UserAddress> userAddress = null;
    public final static Creator<User> CREATOR = new Creator<User>() {


        @SuppressWarnings({
            "unchecked"
        })
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return (new User[size]);
        }

    }
    ;

    protected User(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.rating = ((float) in.readValue((float.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.description = ((String) in.readValue((String.class.getClassLoader())));
        this.image = ((String) in.readValue((String.class.getClassLoader())));
        this.phone = ((String) in.readValue((String.class.getClassLoader())));
        this.backupPhone = ((String) in.readValue((String.class.getClassLoader())));
        this.email = ((String) in.readValue((String.class.getClassLoader())));
        this.cnic = ((String) in.readValue((String.class.getClassLoader())));
        this.isCompleted = ((String) in.readValue((String.class.getClassLoader())));
        this.isVerified = ((String) in.readValue((String.class.getClassLoader())));
        this.totalReviews = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.userAddress, (UserAddress.class.getClassLoader()));
    }

    public User() {
    }

    @NonNull
    public Integer getId() {
        return id;
    }

    public void setId(@NonNull Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBackupPhone() {
        return backupPhone;
    }

    public void setBackupPhone(String backupPhone) {
        this.backupPhone = backupPhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(String isCompleted) {
        this.isCompleted = isCompleted;
    }

    public String getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(String isVerified) {
        this.isVerified = isVerified;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getTotalReviews() {
        return totalReviews;
    }

    public void setTotalReviews(int totalReviews) {
        this.totalReviews = totalReviews;
    }

    public List<UserAddress> getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(List<UserAddress> userAddress) {
        this.userAddress = userAddress;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(name);
        dest.writeValue(description);
        dest.writeValue(image);
        dest.writeValue(phone);
        dest.writeValue(backupPhone);
        dest.writeValue(email);
        dest.writeValue(cnic);
        dest.writeValue(isCompleted);
        dest.writeValue(isVerified);
        dest.writeValue(rating);
        dest.writeValue(totalReviews);
        dest.writeList(userAddress);
    }

    public int describeContents() {
        return  0;
    }

}
