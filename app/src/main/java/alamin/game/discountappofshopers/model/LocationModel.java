package alamin.game.discountappofshopers.model;

public class LocationModel {

    private double latitude;
    private double longitude;
    private String name;
    private String location;
    private String picture_url;
    private String user_uid;
    private Boolean status;
    public LocationModel() {
    }
    public LocationModel(double latitude, double longitude, String name, String location, String picture_url, String user_uid, Boolean status) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.location = location;
        this.picture_url = picture_url;
        this.user_uid = user_uid;
        this.status = status;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPicture_url() {
        return picture_url;
    }

    public void setPicture_url(String picture_url) {
        this.picture_url = picture_url;
    }

    public String getUser_uid() {
        return user_uid;
    }

    public void setUser_uid(String user_uid) {
        this.user_uid = user_uid;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
