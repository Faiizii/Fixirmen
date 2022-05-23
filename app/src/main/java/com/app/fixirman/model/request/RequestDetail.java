package com.app.fixirman.model.request;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.app.fixirman.model.request.RequestService;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
@Entity
public class RequestDetail implements Parcelable
{

    @NonNull
    @PrimaryKey
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("worker_id")
    @Expose
    private Integer workerId;
    @SerializedName("initial_cost")
    @Expose
    private Double initialCost;
    @SerializedName("final_cost")
    @Expose
    private Double finalCost;
    @SerializedName("category_id")
    @Expose
    private Integer categoryId;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("time_slot")
    @Expose
    private String timeSlot;
    @SerializedName("service_type")
    @Expose
    private Integer serviceType;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("coupon_id")
    @Expose
    private Integer couponId;
    @SerializedName("coupon_discount")
    @Expose
    private Double couponDiscount;
    @SerializedName("payment_method")
    @Expose
    private String paymentMethod;
    @SerializedName("is_paid")
    @Expose
    private String isPaid;
    @SerializedName("address_id")
    @Expose
    private Integer addressId;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("address_info")
    @Expose
    private String addressInfo;
    @SerializedName("created_by")
    @Expose
    private Integer createdBy;
    @SerializedName("create_date")
    @Expose
    private String createDate;
    @SerializedName("update_date")
    @Expose
    private String updateDate;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("user_image")
    @Expose
    private String userImage;
    @SerializedName("user_phone")
    @Expose
    private String userPhone;
    @SerializedName("provider_name")
    @Expose
    private String providerName;
    @SerializedName("provider_image")
    @Expose
    private String providerImage;
    @SerializedName("provider_phone")
    @Expose
    private String providerPhone;
    @SerializedName("provider_description")
    @Expose
    private String providerDescription;
    @SerializedName("provider_rating")
    @Expose
    private Float providerRating;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("category_image")
    @Expose
    private String categoryImage;
    @SerializedName("service_type_name")
    @Expose
    private String serviceTypeName;
    @SerializedName("service_type_image")
    @Expose
    private String serviceTypeImage;
    @SerializedName("service_type_factor")
    @Expose
    private Double serviceTypeFactor;
    @Ignore
    @SerializedName("services")
    @Expose
    private List<RequestService> services = null;
    @Ignore
    @SerializedName("bidders")
    @Expose
    private List<RequestBid> bidders = null;

    public RequestDetail() {
    }

    protected RequestDetail(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        if (in.readByte() == 0) {
            workerId = null;
        } else {
            workerId = in.readInt();
        }
        if (in.readByte() == 0) {
            initialCost = null;
        } else {
            initialCost = in.readDouble();
        }
        if (in.readByte() == 0) {
            finalCost = null;
        } else {
            finalCost = in.readDouble();
        }
        if (in.readByte() == 0) {
            categoryId = null;
        } else {
            categoryId = in.readInt();
        }
        description = in.readString();
        date = in.readString();
        timeSlot = in.readString();
        if (in.readByte() == 0) {
            serviceType = null;
        } else {
            serviceType = in.readInt();
        }
        status = in.readString();
        if (in.readByte() == 0) {
            couponId = null;
        } else {
            couponId = in.readInt();
        }
        if (in.readByte() == 0) {
            couponDiscount = null;
        } else {
            couponDiscount = in.readDouble();
        }
        paymentMethod = in.readString();
        isPaid = in.readString();
        if (in.readByte() == 0) {
            addressId = null;
        } else {
            addressId = in.readInt();
        }
        address = in.readString();
        if (in.readByte() == 0) {
            latitude = null;
        } else {
            latitude = in.readDouble();
        }
        if (in.readByte() == 0) {
            longitude = null;
        } else {
            longitude = in.readDouble();
        }
        addressInfo = in.readString();
        if (in.readByte() == 0) {
            createdBy = null;
        } else {
            createdBy = in.readInt();
        }
        createDate = in.readString();
        updateDate = in.readString();
        userName = in.readString();
        userImage = in.readString();
        userPhone = in.readString();
        providerName = in.readString();
        providerImage = in.readString();
        providerPhone = in.readString();
        providerDescription = in.readString();
        if (in.readByte() == 0) {
            providerRating = null;
        } else {
            providerRating = in.readFloat();
        }
        categoryName = in.readString();
        categoryImage = in.readString();
        serviceTypeName = in.readString();
        serviceTypeImage = in.readString();
        if (in.readByte() == 0) {
            serviceTypeFactor = null;
        } else {
            serviceTypeFactor = in.readDouble();
        }
        services = in.createTypedArrayList(RequestService.CREATOR);
        bidders = in.createTypedArrayList(RequestBid.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        if (workerId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(workerId);
        }
        if (initialCost == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(initialCost);
        }
        if (finalCost == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(finalCost);
        }
        if (categoryId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(categoryId);
        }
        dest.writeString(description);
        dest.writeString(date);
        dest.writeString(timeSlot);
        if (serviceType == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(serviceType);
        }
        dest.writeString(status);
        if (couponId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(couponId);
        }
        if (couponDiscount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(couponDiscount);
        }
        dest.writeString(paymentMethod);
        dest.writeString(isPaid);
        if (addressId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(addressId);
        }
        dest.writeString(address);
        if (latitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(latitude);
        }
        if (longitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(longitude);
        }
        dest.writeString(addressInfo);
        if (createdBy == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(createdBy);
        }
        dest.writeString(createDate);
        dest.writeString(updateDate);
        dest.writeString(userName);
        dest.writeString(userImage);
        dest.writeString(userPhone);
        dest.writeString(providerName);
        dest.writeString(providerImage);
        dest.writeString(providerPhone);
        dest.writeString(providerDescription);
        if (providerRating == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(providerRating);
        }
        dest.writeString(categoryName);
        dest.writeString(categoryImage);
        dest.writeString(serviceTypeName);
        dest.writeString(serviceTypeImage);
        if (serviceTypeFactor == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(serviceTypeFactor);
        }
        dest.writeTypedList(services);
        dest.writeTypedList(bidders);
    }

    public static final Creator<RequestDetail> CREATOR = new Creator<RequestDetail>() {
        @Override
        public RequestDetail createFromParcel(Parcel in) {
            return new RequestDetail(in);
        }

        @Override
        public RequestDetail[] newArray(int size) {
            return new RequestDetail[size];
        }
    };

    @NonNull
    public Integer getId() {
        return id;
    }

    public void setId(@NonNull Integer id) {
        this.id = id;
    }

    public Integer getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Integer workerId) {
        this.workerId = workerId;
    }

    public Double getInitialCost() {
        return initialCost;
    }

    public void setInitialCost(Double initialCost) {
        this.initialCost = initialCost;
    }

    public Double getFinalCost() {
        return finalCost;
    }

    public void setFinalCost(Double finalCost) {
        this.finalCost = finalCost;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public Integer getServiceType() {
        return serviceType;
    }

    public void setServiceType(Integer serviceType) {
        this.serviceType = serviceType;
    }

    public Integer getCouponId() {
        return couponId;
    }

    public void setCouponId(Integer couponId) {
        this.couponId = couponId;
    }

    public Double getCouponDiscount() {
        return couponDiscount;
    }

    public void setCouponDiscount(Double couponDiscount) {
        this.couponDiscount = couponDiscount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(String isPaid) {
        this.isPaid = isPaid;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getAddressInfo() {
        return addressInfo;
    }

    public void setAddressInfo(String addressInfo) {
        this.addressInfo = addressInfo;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getProviderImage() {
        return providerImage;
    }

    public void setProviderImage(String providerImage) {
        this.providerImage = providerImage;
    }

    public String getProviderPhone() {
        return providerPhone;
    }

    public void setProviderPhone(String providerPhone) {
        this.providerPhone = providerPhone;
    }

    public String getProviderDescription() {
        return providerDescription;
    }

    public void setProviderDescription(String providerDescription) {
        this.providerDescription = providerDescription;
    }

    public Float getProviderRating() {
        return providerRating;
    }

    public void setProviderRating(Float providerRating) {
        this.providerRating = providerRating;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public String getServiceTypeName() {
        return serviceTypeName;
    }

    public void setServiceTypeName(String serviceTypeName) {
        this.serviceTypeName = serviceTypeName;
    }

    public String getServiceTypeImage() {
        return serviceTypeImage;
    }

    public void setServiceTypeImage(String serviceTypeImage) {
        this.serviceTypeImage = serviceTypeImage;
    }

    public Double getServiceTypeFactor() {
        return serviceTypeFactor;
    }

    public void setServiceTypeFactor(Double serviceTypeFactor) {
        this.serviceTypeFactor = serviceTypeFactor;
    }

    public List<RequestService> getServices() {
        return services;
    }

    public void setServices(List<RequestService> services) {
        this.services = services;
    }

    public List<RequestBid> getBidders() {
        return bidders;
    }

    public void setBidders(List<RequestBid> bidders) {
        this.bidders = bidders;
    }

    public int describeContents() {
        return 0;
    }

}