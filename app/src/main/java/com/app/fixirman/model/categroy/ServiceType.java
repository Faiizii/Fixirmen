package com.app.fixirman.model.categroy;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class ServiceType implements Parcelable
{

    @NonNull
    @PrimaryKey
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("factor")
    @Expose
    private Double factor;
    public final static Parcelable.Creator<ServiceType> CREATOR = new Creator<ServiceType>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ServiceType createFromParcel(Parcel in) {
            return new ServiceType(in);
        }

        public ServiceType[] newArray(int size) {
            return (new ServiceType[size]);
        }

    }
            ;

    protected ServiceType(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.title = ((String) in.readValue((String.class.getClassLoader())));
        this.image = ((String) in.readValue((String.class.getClassLoader())));
        this.factor = ((Double) in.readValue((Double.class.getClassLoader())));
    }

    public ServiceType() {
    }

    @NonNull
    public Integer getId() {
        return id;
    }

    public void setId(@NonNull Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Double getFactor() {
        return factor;
    }

    public void setFactor(Double factor) {
        this.factor = factor;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(title);
        dest.writeValue(image);
        dest.writeValue(factor);
    }

    public int describeContents() {
        return 0;
    }

}