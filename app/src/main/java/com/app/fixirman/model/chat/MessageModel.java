package com.app.fixirman.model.chat;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class MessageModel implements Parcelable {

    @NonNull
    @PrimaryKey
    private String hashKey;
    private String message;
    private String timeStamp;
    private int userId;
    private String userName;
    private String userImage;
    private int myId;
    private boolean isSent;
    private boolean isReceive;
    private boolean isRead;

    public MessageModel(){}

    protected MessageModel(Parcel in) {
        message = in.readString();
        timeStamp = in.readString();
        userId = in.readInt();
        userName = in.readString();
        userImage = in.readString();
        myId = in.readInt();
        hashKey = in.readString();
        isSent = in.readByte() != 0;
        isReceive = in.readByte() != 0;
        isRead = in.readByte() != 0;
    }

    @Ignore
    public MessageModel(String message, String timeStamp, int userId, String userName, String userImage, int myId) {
        this.message = message;
        this.timeStamp = timeStamp;
        this.userId = userId;
        this.userName = userName;
        this.userImage = userImage;
        this.myId = myId;
        this.isSent = false;
        this.isReceive = false;
        this.isRead = false;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(message);
        dest.writeString(timeStamp);
        dest.writeInt(userId);
        dest.writeString(userName);
        dest.writeString(userImage);
        dest.writeInt(myId);
        dest.writeString(hashKey);
        dest.writeByte((byte) (isSent ? 1 : 0));
        dest.writeByte((byte) (isReceive ? 1 : 0));
        dest.writeByte((byte) (isRead ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MessageModel> CREATOR = new Creator<MessageModel>() {
        @Override
        public MessageModel createFromParcel(Parcel in) {
            return new MessageModel(in);
        }

        @Override
        public MessageModel[] newArray(int size) {
            return new MessageModel[size];
        }
    };

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public int getMyId() {
        return myId;
    }

    public void setMyId(int myId) {
        this.myId = myId;
    }

    @NonNull
    public String getHashKey() {
        return hashKey;
    }

    public void setHashKey(@NonNull String hashKey) {
        this.hashKey = hashKey;
    }

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }

    public boolean isReceive() {
        return isReceive;
    }

    public void setReceive(boolean receive) {
        isReceive = receive;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
