
package com.app.fixirman.model.user;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
@Entity
public class UserAddress implements Parcelable
{

    @NonNull
    @PrimaryKey
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("address_type")
    @Expose
    private String addressType;
    @SerializedName("address_title")
    @Expose
    private String addressTitle;
    public final static Creator<UserAddress> CREATOR = new Creator<UserAddress>() {


        @SuppressWarnings({
            "unchecked"
        })
        public UserAddress createFromParcel(Parcel in) {
            return new UserAddress(in);
        }

        public UserAddress[] newArray(int size) {
            return (new UserAddress[size]);
        }

    }
    ;

    protected UserAddress(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.address = ((String) in.readValue((String.class.getClassLoader())));
        this.addressType = ((String) in.readValue((String.class.getClassLoader())));
        this.addressTitle = ((String) in.readValue((String.class.getClassLoader())));
        this.latitude = ((Double) in.readValue((Double.class.getClassLoader())));
        this.longitude = ((Double) in.readValue((Double.class.getClassLoader())));
        this.userId = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public UserAddress() {
    }

    @NonNull
    public Integer getId() {
        return id;
    }

    public void setId(@NonNull Integer id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getAddressTitle() {
        return addressTitle;
    }

    public void setAddressTitle(String addressTitle) {
        this.addressTitle = addressTitle;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(address);
        dest.writeValue(addressType);
        dest.writeValue(addressTitle);
        dest.writeValue(latitude);
        dest.writeValue(longitude);
        dest.writeValue(userId);
    }

    public int describeContents() {
        return  0;
    }

}
