package com.app.fixirman.model.provider;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DateModel implements Parcelable
{

    @SerializedName("day_name")
    @Expose
    private String dayName;
    @SerializedName("day_number")
    @Expose
    private String dayNumber;
    @SerializedName("date")
    @Expose
    private String date;
    public final static Parcelable.Creator<DateModel> CREATOR = new Creator<DateModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public DateModel createFromParcel(Parcel in) {
            return new DateModel(in);
        }

        public DateModel[] newArray(int size) {
            return (new DateModel[size]);
        }

    }
            ;

    protected DateModel(Parcel in) {
        this.dayName = ((String) in.readValue((String.class.getClassLoader())));
        this.dayNumber = ((String) in.readValue((String.class.getClassLoader())));
        this.date = ((String) in.readValue((String.class.getClassLoader())));
    }

    public DateModel() {
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public String getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(String dayNumber) {
        this.dayNumber = dayNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(dayName);
        dest.writeValue(dayNumber);
        dest.writeValue(date);
    }

    public int describeContents() {
        return 0;
    }

}