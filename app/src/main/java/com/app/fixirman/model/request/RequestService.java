
package com.app.fixirman.model.request;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class RequestService implements Parcelable
{

    @NonNull
    @PrimaryKey
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("request_id")
    @Expose
    private Integer requestId;
    @SerializedName("category_id")
    @Expose
    private Integer categoryId;
    @SerializedName("service_id")
    @Expose
    private Integer serviceId;
    @SerializedName("price")
    @Expose
    private Double price;
    @SerializedName("service_name")
    @Expose
    private String serviceName;
    public final static Creator<RequestService> CREATOR = new Creator<RequestService>() {


        @SuppressWarnings({
            "unchecked"
        })
        public RequestService createFromParcel(Parcel in) {
            return new RequestService(in);
        }

        public RequestService[] newArray(int size) {
            return (new RequestService[size]);
        }

    }
    ;

    protected RequestService(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.requestId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.categoryId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.serviceId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.price = ((Double) in.readValue((Double.class.getClassLoader())));
        this.serviceName = ((String) in.readValue((String.class.getClassLoader())));
    }

    public RequestService() {
    }

    @NonNull
    public Integer getId() {
        return id;
    }

    public void setId(@NonNull Integer id) {
        this.id = id;
    }

    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(requestId);
        dest.writeValue(categoryId);
        dest.writeValue(serviceId);
        dest.writeValue(price);
        dest.writeValue(serviceName);
    }

    public int describeContents() {
        return  0;
    }

}
