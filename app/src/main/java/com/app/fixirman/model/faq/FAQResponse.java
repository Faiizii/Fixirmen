package com.app.fixirman.model.faq;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FAQResponse implements Parcelable
{

    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("faq")
    @Expose
    private List<FAQModel> faq = null;
    public final static Parcelable.Creator<FAQResponse> CREATOR = new Creator<FAQResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public FAQResponse createFromParcel(Parcel in) {
            return new FAQResponse(in);
        }

        public FAQResponse[] newArray(int size) {
            return (new FAQResponse[size]);
        }

    }
            ;

    protected FAQResponse(Parcel in) {
        this.success = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.faq, (FAQModel.class.getClassLoader()));
    }

    public FAQResponse() {
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

    public List<FAQModel> getFaq() {
        return faq;
    }

    public void setFaq(List<FAQModel> faq) {
        this.faq = faq;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(success);
        dest.writeValue(message);
        dest.writeList(faq);
    }

    public int describeContents() {
        return 0;
    }

}