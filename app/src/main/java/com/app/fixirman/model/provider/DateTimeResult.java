package com.app.fixirman.model.provider;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DateTimeResult implements Parcelable
{

    @SerializedName("dates")
    @Expose
    private List<DateModel> dates = null;
    @SerializedName("slots")
    @Expose
    private List<String> slots = null;
    public final static Parcelable.Creator<DateTimeResult> CREATOR = new Creator<DateTimeResult>() {


        @SuppressWarnings({
                "unchecked"
        })
        public DateTimeResult createFromParcel(Parcel in) {
            return new DateTimeResult(in);
        }

        public DateTimeResult[] newArray(int size) {
            return (new DateTimeResult[size]);
        }

    }
            ;

    protected DateTimeResult(Parcel in) {
        in.readList(this.dates, (DateModel.class.getClassLoader()));
        in.readList(this.slots, (java.lang.String.class.getClassLoader()));
    }

    public DateTimeResult() {
    }

    public List<DateModel> getDates() {
        return dates;
    }

    public void setDates(List<DateModel> dates) {
        this.dates = dates;
    }

    public List<String> getSlots() {
        return slots;
    }

    public void setSlots(List<String> slots) {
        this.slots = slots;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(dates);
        dest.writeList(slots);
    }

    public int describeContents() {
        return 0;
    }

}