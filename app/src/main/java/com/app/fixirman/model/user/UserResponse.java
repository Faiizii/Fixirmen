
package com.app.fixirman.model.user;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import androidx.room.Ignore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserResponse implements Parcelable
{

    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("result")
    @Expose
    private UserResult userResult;
    @Ignore
    @SerializedName("addresses")
    @Expose
    private List<UserAddress> userAddress = null;

    @SerializedName("user")
    @Expose
    private User user;

    public final static Creator<UserResponse> CREATOR = new Creator<UserResponse>() {


        @SuppressWarnings({
            "unchecked"
        })
        public UserResponse createFromParcel(Parcel in) {
            return new UserResponse(in);
        }

        public UserResponse[] newArray(int size) {
            return (new UserResponse[size]);
        }

    }
    ;

    protected UserResponse(Parcel in) {
        this.success = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        this.userResult = ((UserResult) in.readValue((UserResult.class.getClassLoader())));
        this.user = ((User) in.readValue((User.class.getClassLoader())));
        in.readList(this.userAddress, (UserAddress.class.getClassLoader()));
    }

    public UserResponse() {
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

    public UserResult getUserResult() {
        return userResult;
    }

    public void setUserResult(UserResult userResult) {
        this.userResult = userResult;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<UserAddress> getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(List<UserAddress> userAddress) {
        this.userAddress = userAddress;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(success);
        dest.writeValue(message);
        dest.writeValue(userResult);
        dest.writeValue(user);
        dest.writeList(userAddress);
    }

    public int describeContents() {
        return  0;
    }

}
