package com.domain.porter.model;

/**
 * Created by pratik on 29/8/15.
 */
public class Parcel {

    private String mName;
    private String mImageUrl;
    private String mEpochDate;
    private String mType;
    private String mWeight;
    private String mPhone;
    private String mPrice;
    private String mQuantity;
    private String mColor;
    private String mLink;
    private Double mLatitude;
    private Double mLongitude;


    public void setName(String name) {
        this.mName = name;
    }

    public String getName() {
        return mName;
    }

    public void setImageUrl(String imageUrl) {
        this.mImageUrl = imageUrl;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setType(String type) {
        this.mType = type;
    }

    public String getType() {
        return mType;
    }

    public void setPhone(String phone) {
        this.mPhone = phone;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setDate(String epochDate) {
        this.mEpochDate = epochDate;
    }

    public String getDate() {
        return mEpochDate;
    }

    public void setWeight(String weight) {
        this.mWeight = weight;
    }

    public String getWeight() {
        return mWeight;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String price) {
        this.mPrice = price;
    }

    public void setQuantity(String quantity) {
        this.mQuantity = quantity;
    }

    public String getQuantity() {
        return mQuantity;
    }

    public void setLink(String link) {
        this.mLink = link;
    }

    public String getLink() {
        return mLink;
    }

    public void setLatitude(Double latitude) {
        this.mLatitude = latitude;
    }

    public Double getLatitude() {
        return mLatitude;
    }

    public void setLongitude(Double longitude) {
        this.mLongitude = longitude;
    }

    public Double getLongitude() {
        return mLongitude;
    }

    public void setColor(String color) {
        this.mColor = color;
    }

    public String getColor() {
        return mColor;
    }
}
