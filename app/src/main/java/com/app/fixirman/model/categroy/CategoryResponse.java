package com.app.fixirman.model.categroy;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryResponse implements Parcelable
{

    @SerializedName("success")
    @Expose
    private int success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("categories")
    @Expose
    private List<Category> categories = null;

    @SerializedName("services")
    @Expose
    private List<SubCategory> subCategories = null;

    @SerializedName("service_providers")
    @Expose
    private List<ServiceProviders> serviceProviders = null;

    @SerializedName("service_types")
    @Expose
    private List<ServiceType> serviceTypes = null;

    public final static Creator<CategoryResponse> CREATOR = new Creator<CategoryResponse>() {


        @SuppressWarnings({
            "unchecked"
        })
        public CategoryResponse createFromParcel(Parcel in) {
            return new CategoryResponse(in);
        }

        public CategoryResponse[] newArray(int size) {
            return (new CategoryResponse[size]);
        }

    }
    ;

    protected CategoryResponse(Parcel in) {
        this.success = ((int) in.readValue((Integer.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.categories, (Category.class.getClassLoader()));
        in.readList(this.subCategories, (SubCategory.class.getClassLoader()));
        in.readList(this.serviceProviders, (ServiceProviders.class.getClassLoader()));
        in.readList(this.serviceTypes, (ServiceType.class.getClassLoader()));
    }

    public CategoryResponse() {}

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

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<SubCategory> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<SubCategory> subCategories) {
        this.subCategories = subCategories;
    }

    public List<ServiceProviders> getServiceProviders() {
        return serviceProviders;
    }

    public void setServiceProviders(List<ServiceProviders> serviceProviders) {
        this.serviceProviders = serviceProviders;
    }

    public List<ServiceType> getServiceTypes() {
        return serviceTypes;
    }

    public void setServiceTypes(List<ServiceType> serviceTypes) {
        this.serviceTypes = serviceTypes;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(success);
        dest.writeValue(message);
        dest.writeList(categories);
        dest.writeList(subCategories);
        dest.writeList(serviceProviders);
        dest.writeList(serviceTypes);
    }

    public int describeContents() {
        return  0;
    }

}
