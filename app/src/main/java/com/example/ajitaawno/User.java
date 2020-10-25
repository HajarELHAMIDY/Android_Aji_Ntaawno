package com.example.ajitaawno;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.Exclude;

public class User implements Parcelable {

    @Exclude
    private String id;
    private String nom;
    private String prenom;
    private String phone;
    private String email;
    private String dateNaissance;
    private String user_image;
    private String token;
    private boolean online;

    public User() {
    }

    public String getToken() {
        return token;
    }

    public boolean isOnline() {
        return online;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    protected User(Parcel in) {
        id = in.readString();
        nom = in.readString();
        prenom = in.readString();
        phone = in.readString();
        email = in.readString();
        dateNaissance = in.readString();
        user_image = in.readString();
        token = in.readString();
        online = in.readInt() ==1;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getUser_image() {
        return user_image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(nom);
        dest.writeString(prenom);
        dest.writeString(phone);
        dest.writeString(email);
        dest.writeString(dateNaissance);
        dest.writeString(user_image);
        dest.writeString(token);
        dest.writeInt(online ? 1 : 0);
    }
}
