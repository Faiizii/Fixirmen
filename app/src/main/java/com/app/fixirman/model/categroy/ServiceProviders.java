
package com.app.fixirman.model.categroy;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class ServiceProviders implements Parcelable
{

    @NonNull
    @PrimaryKey
    @SerializedName("emp_category_id")
    @Expose
    private String empCategoryId;
    @SerializedName("employee_id")
    @Expose
    private String employeeId;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("sub_category_id")
    @Expose
    private String subCategoryId;
    @SerializedName("cost")
    @Expose
    private String cost;
    @SerializedName("employee_name")
    @Expose
    private String employeeName;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("sub_category_name")
    @Expose
    private String subCategoryName;
    @SerializedName("distance_away")
    @Expose
    private String distanceAway;
    @SerializedName("employee_rating")
    @Expose
    private String employeeRating;
    public final static Creator<ServiceProviders> CREATOR = new Creator<ServiceProviders>() {


        @SuppressWarnings({
            "unchecked"
        })
        public ServiceProviders createFromParcel(Parcel in) {
            return new ServiceProviders(in);
        }

        public ServiceProviders[] newArray(int size) {
            return (new ServiceProviders[size]);
        }

    }
    ;

    protected ServiceProviders(Parcel in) {
        this.empCategoryId = ((String) in.readValue((String.class.getClassLoader())));
        this.employeeId = ((String) in.readValue((String.class.getClassLoader())));
        this.categoryId = ((String) in.readValue((String.class.getClassLoader())));
        this.subCategoryId = ((String) in.readValue((String.class.getClassLoader())));
        this.cost = ((String) in.readValue((String.class.getClassLoader())));
        this.employeeName = ((String) in.readValue((String.class.getClassLoader())));
        this.categoryName = ((String) in.readValue((String.class.getClassLoader())));
        this.subCategoryName = ((String) in.readValue((String.class.getClassLoader())));
        this.distanceAway = ((String) in.readValue((String.class.getClassLoader())));
        this.employeeRating = ((String) in.readValue((String.class.getClassLoader())));
    }

    public ServiceProviders() {
    }

    public String getEmpCategoryId() {
        return empCategoryId;
    }

    public void setEmpCategoryId(String empCategoryId) {
        this.empCategoryId = empCategoryId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(String subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public String getDistanceAway() {
        return distanceAway;
    }

    public void setDistanceAway(String distanceAway) {
        this.distanceAway = distanceAway;
    }

    public String getEmployeeRating() {
        return employeeRating;
    }

    public void setEmployeeRating(String employeeRating) {
        this.employeeRating = employeeRating;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(empCategoryId);
        dest.writeValue(employeeId);
        dest.writeValue(categoryId);
        dest.writeValue(subCategoryId);
        dest.writeValue(cost);
        dest.writeValue(employeeName);
        dest.writeValue(categoryName);
        dest.writeValue(subCategoryName);
        dest.writeValue(distanceAway);
        dest.writeValue(employeeRating);
    }

    public int describeContents() {
        return  0;
    }

}
