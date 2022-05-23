
package com.app.fixirman.model.request;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RequestResponse implements Parcelable
{

    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("request_detail")
    @Expose
    private RequestDetail requestDetail;
    @SerializedName("requests")
    @Expose
    private List<RequestDetail> requests = null;
    public final static Creator<RequestResponse> CREATOR = new Creator<RequestResponse>() {


        @SuppressWarnings({
            "unchecked"
        })
        public RequestResponse createFromParcel(Parcel in) {
            return new RequestResponse(in);
        }

        public RequestResponse[] newArray(int size) {
            return (new RequestResponse[size]);
        }

    }
    ;

    protected RequestResponse(Parcel in) {
        this.success = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        this.requestDetail = ((RequestDetail) in.readValue((RequestDetail.class.getClassLoader())));
        in.readList(this.requests, (RequestDetail.class.getClassLoader()));
    }

    public RequestResponse() {
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

    public RequestDetail getRequestDetail() {
        return requestDetail;
    }

    public void setRequestDetail(RequestDetail requestDetail) {
        this.requestDetail = requestDetail;
    }

    public List<RequestDetail> getRequests() {
        return requests;
    }

    public void setRequests(List<RequestDetail> requests) {
        this.requests = requests;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(success);
        dest.writeValue(message);
        dest.writeValue(requestDetail);
        dest.writeList(requests);
    }

    public int describeContents() {
        return  0;
    }
}
