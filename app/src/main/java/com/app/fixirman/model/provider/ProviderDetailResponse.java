package com.app.fixirman.model.provider;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProviderDetailResponse implements Parcelable
{

    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("result")
    @Expose
    private DateTimeResult result;
    public final static Parcelable.Creator<ProviderDetailResponse> CREATOR = new Creator<ProviderDetailResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ProviderDetailResponse createFromParcel(Parcel in) {
            return new ProviderDetailResponse(in);
        }

        public ProviderDetailResponse[] newArray(int size) {
            return (new ProviderDetailResponse[size]);
        }

    }
            ;

    protected ProviderDetailResponse(Parcel in) {
        this.success = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        this.result = ((DateTimeResult) in.readValue((DateTimeResult.class.getClassLoader())));
    }

    public ProviderDetailResponse() {
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

    public DateTimeResult getResult() {
        return result;
    }

    public void setResult(DateTimeResult result) {
        this.result = result;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(success);
        dest.writeValue(message);
        dest.writeValue(result);
    }

    public int describeContents() {
        return 0;
    }

}