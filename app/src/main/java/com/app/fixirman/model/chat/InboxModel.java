package com.app.fixirman.model.chat;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity
public class InboxModel implements Parcelable {

    @NonNull
    @PrimaryKey
    private String id;
    private int myID;
    private int userId;
    private String timeStamp;
    private String lastMessage;
    private String userImage;
    private String userName;
    private String category;

    public InboxModel(){}

    @Ignore
    public InboxModel(int senderId, int receiverId, String timeStamp, String lastMessage, String image, String name, String category) {
        this.id = senderId+"-"+receiverId;
        this.myID = senderId;
        this.userId = receiverId;
        this.timeStamp = timeStamp;
        this.lastMessage = lastMessage;
        this.userImage = image;
        this.userName = name;
        this.category = category;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public int getMyID() {
        return myID;
    }

    public void setMyID(int myID) {
        this.myID = myID;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Ignore
    protected InboxModel(Parcel in) {
        id = Objects.requireNonNull(in.readString());
        myID = in.readInt();
        userId = in.readInt();
        timeStamp = in.readString();
        lastMessage = in.readString();
        userImage = in.readString();
        userName = in.readString();
        category = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeInt(myID);
        dest.writeInt(userId);
        dest.writeString(timeStamp);
        dest.writeString(lastMessage);
        dest.writeString(userImage);
        dest.writeString(userName);
        dest.writeString(category);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<InboxModel> CREATOR = new Creator<InboxModel>() {
        @Override
        public InboxModel createFromParcel(Parcel in) {
            return new InboxModel(in);
        }

        @Override
        public InboxModel[] newArray(int size) {
            return new InboxModel[size];
        }
    };
}
