package com.example.ajitaawno;

import android.os.Parcel;
import android.os.Parcelable;

public class Contact implements Parcelable {

    private String uid;
    private String username;
    private String lastMessage;
    private long timestamp;
    private String photoUrl;

    public Contact() {
    }

    protected Contact(Parcel in) {
        uid = in.readString();
        username = in.readString();
        lastMessage = in.readString();
        timestamp = in.readLong();
        photoUrl = in.readString();
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(username);
        dest.writeString(lastMessage);
        dest.writeLong(timestamp);
        dest.writeString(photoUrl);
    }
}
