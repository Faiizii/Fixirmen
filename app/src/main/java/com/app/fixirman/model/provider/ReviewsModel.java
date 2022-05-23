
package com.app.fixirman.model.provider;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class ReviewsModel implements Parcelable
{

    @NonNull
    @PrimaryKey
    @SerializedName("rating_id")
    @Expose
    private String ratingId;
    @SerializedName("employee_id")
    @Expose
    private String employeeId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("rating")
    @Expose
    private int rating;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("request_id")
    @Expose
    private String requestId;
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("user_image")
    @Expose
    private String userImage;
    @SerializedName("category")
    @Expose
    private String category;
    public final static Creator<ReviewsModel> CREATOR = new Creator<ReviewsModel>() {


        @SuppressWarnings({
            "unchecked"
        })
        public ReviewsModel createFromParcel(Parcel in) {
            return new ReviewsModel(in);
        }

        public ReviewsModel[] newArray(int size) {
            return (new ReviewsModel[size]);
        }

    }
    ;

    protected ReviewsModel(Parcel in) {
        this.ratingId = ((String) in.readValue((String.class.getClassLoader())));
        this.employeeId = ((String) in.readValue((String.class.getClassLoader())));
        this.userId = ((String) in.readValue((String.class.getClassLoader())));
        this.categoryId = ((String) in.readValue((String.class.getClassLoader())));
        this.rating = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.comment = ((String) in.readValue((String.class.getClassLoader())));
        this.requestId = ((String) in.readValue((String.class.getClassLoader())));
        this.createdDate = ((String) in.readValue((String.class.getClassLoader())));
        this.userName = ((String) in.readValue((String.class.getClassLoader())));
        this.userImage = ((String) in.readValue((String.class.getClassLoader())));
        this.category = ((String) in.readValue((String.class.getClassLoader())));
    }

    public ReviewsModel() {
    }

    @NonNull
    public String getRatingId() {
        return ratingId;
    }

    public void setRatingId(@NonNull String ratingId) {
        this.ratingId = ratingId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(ratingId);
        dest.writeValue(employeeId);
        dest.writeValue(userId);
        dest.writeValue(categoryId);
        dest.writeValue(rating);
        dest.writeValue(comment);
        dest.writeValue(requestId);
        dest.writeValue(createdDate);
        dest.writeValue(userName);
        dest.writeValue(userImage);
        dest.writeValue(category);
    }

    public int describeContents() {
        return  0;
    }

}
