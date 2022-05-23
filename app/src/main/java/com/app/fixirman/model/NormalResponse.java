package com.app.fixirman.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class NormalResponse implements Parcelable {

    private int success;
    private String message;
    private String error;

    @SerializedName("payment_id")
    private String paymentId;

    protected NormalResponse(Parcel in) {

        success = in.readInt();
        message = in.readString();
        error = in.readString();
        paymentId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(success);
        dest.writeString(message);
        dest.writeString(error);
        dest.writeString(paymentId);
    }

    @Override
    public int describeContents() {

        return 0;
    }

    public static final Creator<NormalResponse> CREATOR = new Creator<NormalResponse>() {
        @Override
        public NormalResponse createFromParcel(Parcel in) {

            return new NormalResponse(in);
        }

        @Override
        public NormalResponse[] newArray(int size) {

            return new NormalResponse[size];
        }
    };

    public int getSuccess() {

        return success;
    }

    public void setSuccess(int success) {

        this.success = success;
    }

    public String getMessage() {

        return message;
    }

    public void setMessage(String message) {

        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }
}
