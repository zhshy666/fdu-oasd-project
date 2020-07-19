package com.oasd.backend.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TravelImage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int imageId;

    private String title;
    private String description;
    private double latitude;
    private double longitude;
    private int cityCode;
    private String country_RegionCodeISO;
    private String username;
    private String PATH;
    private String content;
    private int heat;
    private String releasedTime;
    private String author;

    public TravelImage() {
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public String getCountry_RegionCodeISO() {
        return country_RegionCodeISO;
    }

    public void setCountry_RegionCodeISO(String country_RegionCodeISO) {
        this.country_RegionCodeISO = country_RegionCodeISO;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPATH() {
        return PATH;
    }

    public void setPATH(String PASS) {
        this.PATH = PASS;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getHeat() {
        return heat;
    }

    public void setHeat(int heat) {
        this.heat = heat;
    }

    public String getReleasedTime() {
        return releasedTime;
    }

    public void setReleasedTime(String releasedTime) {
        this.releasedTime = releasedTime;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "TravelImage{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", cityCode=" + cityCode +
                ", country_RegionCodeISO='" + country_RegionCodeISO + '\'' +
                ", username='" + username + '\'' +
                ", PATH='" + PATH + '\'' +
                ", content='" + content + '\'' +
                ", heat=" + heat +
                ", releasedTime='" + releasedTime + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
