package com.ecdc.androidlibs.database.entity;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import static com.ecdc.androidlibs.network.NetworkEndPoint.BASE_RESOURCE;


public final class User implements Serializable {

    @SerializedName("_id")
    private String id;

    @SerializedName("hospital")
    private String hospitalId;

    @SerializedName("loyalty")
    private Loyalty loyalty;

    @SerializedName("is_update_profile")
    private int isUpdatedProfile;

    @SerializedName("fcm")
    private String fcm;

    @SerializedName("token")
    private String token;

    @SerializedName("dob")
    private String dob;

    @SerializedName("avatar_url")
    private String avatarUrl;

    @SerializedName("email")
    private String email;

    @SerializedName("address")
    private String address;

    @SerializedName("delivery_phone_number")
    private String deliveryPhone;

    @SerializedName("phone_number")
    private String phone;

    @SerializedName("fullname")
    private String fullname;

    @SerializedName("username")
    private String userName;

    public String getUserName() {
        return userName;
    }

    public String getId() {
        return id;
    }

    public int getPoints() {
        if (loyalty != null)
            return loyalty.getPoints();
        else
            return -1;
    }

    public String getAddress() {
        return address;
    }

    public int getLevel() {
        if (loyalty != null)
            return loyalty.getLevel();
        return -1;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFullName(String fullname) {
        this.fullname = fullname;
    }

    public String getFullName() {
        return fullname;
    }

    public String getDob() {
        return dob;
    }

    public String getAvatar() {
        return BASE_RESOURCE + avatarUrl;
    }

    public void setAvatarUrl(String avt) {
        this.avatarUrl = avt;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public void setFcm(String fcm) {
        this.fcm = fcm;
    }

    public String getAuthorization() {
        return token;
    }

    public String getFcm() {
        return this.fcm == null ? "Nothing" : fcm;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public String getDeliveryPhone() {
        return deliveryPhone;
    }

    public void setDeliveryPhone(String deliveryPhone) {
        this.deliveryPhone = deliveryPhone;
    }

    public boolean isUpdated() {
        return this.isUpdatedProfile == 1;
    }

    public void updated() {
        if (isUpdatedProfile == 0) isUpdatedProfile = 1;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public class Loyalty implements Serializable {
        @SerializedName("points")
        int points;
        @SerializedName("level")
        int level;

        public int getPoints() {
            return points;
        }

        public int getLevel() {
            return level;
        }

        public void setPoints(int points) {
            this.points = points;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }

}
