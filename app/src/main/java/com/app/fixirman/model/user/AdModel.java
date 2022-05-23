
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
public class AdModel implements Parcelable {

    @NonNull
    @PrimaryKey
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("added_by")
    @Expose
    private String addedBy;
    public final static Creator<AdModel> CREATOR = new Creator<AdModel>() {


        @SuppressWarnings({
            "unchecked"
        })
        public AdModel createFromParcel(Parcel in) {
            return new AdModel(in);
        }

        public AdModel[] newArray(int size) {
            return (new AdModel[size]);
        }

    }
    ;

    protected AdModel(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.image = ((String) in.readValue((String.class.getClassLoader())));
        this.addedBy = ((String) in.readValue((String.class.getClassLoader())));
    }

    public AdModel() {
    }

    @NonNull
    public Integer getId() {
        return id;
    }

    public void setId(@NonNull Integer id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(image);
        dest.writeValue(addedBy);
    }

    public int describeContents() {
        return  0;
    }

}
