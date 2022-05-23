
package com.app.fixirman.model.user;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserResult implements Parcelable
{

    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("ads")
    @Expose
    private List<AdModel> ads = null;
    public final static Creator<UserResult> CREATOR = new Creator<UserResult>() {


        @SuppressWarnings({
            "unchecked"
        })
        public UserResult createFromParcel(Parcel in) {
            return new UserResult(in);
        }

        public UserResult[] newArray(int size) {
            return (new UserResult[size]);
        }

    }
    ;

    protected UserResult(Parcel in) {
        this.user = ((User) in.readValue((User.class.getClassLoader())));
        in.readList(this.ads, (AdModel.class.getClassLoader()));
    }

    public UserResult() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<AdModel> getAds() {
        return ads;
    }

    public void setAds(List<AdModel> ads) {
        this.ads = ads;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(user);
        dest.writeList(ads);
    }

    public int describeContents() {
        return  0;
    }

}
