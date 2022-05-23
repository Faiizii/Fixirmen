package com.app.fixirman.view.create_request;

import android.util.ArrayMap;

import com.app.fixirman.model.categroy.Category;
import com.app.fixirman.model.categroy.SubCategory;

import java.util.List;

public class RequestModel {
    private Category category;
    private ArrayMap<Integer, SubCategory> services;
    private String date;
    private String time;
    private List<String> images;
    private String description;
    private int couponId;
    private int serviceTypeId;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public ArrayMap<Integer, SubCategory> getServices() {
        return services;
    }

    public void setServices(ArrayMap<Integer, SubCategory> services) {
        this.services = services;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCouponId() {
        return couponId;
    }

    public void setCouponId(int couponId) {
        this.couponId = couponId;
    }

    public int getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(int serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }
}
