package alamin.game.discountappofshopers.model;

public class RegistrationModelCustomer {

    private String fb_id;
    private String phone;
    private String email;
    private String password;
    private String name;
    private String gender;
    private String date_of_birth;
    private String profile_pic_url;

    private double latitude;
    private double longitude;
    private Boolean status;

    private String bksh;
    private String bksh_number;

    private String rocket;
    private String rocket_number;

    private String mCash;
    private String mCash_number;

    private String MyCash;
    private String MyCash_number;

    private String uCash;
    private String uCash_number;

    private String nogod;
    private String nogod_number;

    private String sureCash;
    private String sureCash_number;


    public RegistrationModelCustomer() {
    }

    public RegistrationModelCustomer(String fb_id, String phone, String email, String password, String name, String gender, String date_of_birth, String profile_pic_url, double latitude, double longitude, Boolean status, String bksh, String bksh_number, String rocket, String rocket_number, String mCash, String mCash_number, String myCash, String myCash_number, String uCash, String uCash_number, String nogod, String nogod_number, String sureCash, String sureCash_number) {
        this.fb_id = fb_id;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.date_of_birth = date_of_birth;
        this.profile_pic_url = profile_pic_url;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.bksh = bksh;
        this.bksh_number = bksh_number;
        this.rocket = rocket;
        this.rocket_number = rocket_number;
        this.mCash = mCash;
        this.mCash_number = mCash_number;
        MyCash = myCash;
        MyCash_number = myCash_number;
        this.uCash = uCash;
        this.uCash_number = uCash_number;
        this.nogod = nogod;
        this.nogod_number = nogod_number;
        this.sureCash = sureCash;
        this.sureCash_number = sureCash_number;
    }



    public String getFb_id() {
        return fb_id;
    }

    public void setFb_id(String fb_id) {
        this.fb_id = fb_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getProfile_pic_url() {
        return profile_pic_url;
    }

    public void setProfile_pic_url(String profile_pic_url) {
        this.profile_pic_url = profile_pic_url;
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

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getBiksh() {
        return bksh;
    }

    public void setBiksh(String biksh) {
        this.bksh = biksh;
    }

    public String getBiksh_number() {
        return bksh_number;
    }

    public void setBiksh_number(String biksh_number) {
        this.bksh_number = biksh_number;
    }

    public String getRocket() {
        return rocket;
    }

    public void setRocket(String rocket) {
        this.rocket = rocket;
    }

    public String getRocket_number() {
        return rocket_number;
    }

    public void setRocket_number(String rocket_number) {
        this.rocket_number = rocket_number;
    }

    public String getmCash() {
        return mCash;
    }

    public void setmCash(String mCash) {
        this.mCash = mCash;
    }

    public String getmCash_number() {
        return mCash_number;
    }

    public void setmCash_number(String mCash_number) {
        this.mCash_number = mCash_number;
    }

    public String getMyCash() {
        return MyCash;
    }

    public void setMyCash(String myCash) {
        MyCash = myCash;
    }

    public String getMyCash_number() {
        return MyCash_number;
    }

    public void setMyCash_number(String myCash_number) {
        MyCash_number = myCash_number;
    }

    public String getuCash() {
        return uCash;
    }

    public void setuCash(String uCash) {
        this.uCash = uCash;
    }

    public String getuCash_number() {
        return uCash_number;
    }

    public void setuCash_number(String uCash_number) {
        this.uCash_number = uCash_number;
    }

    public String getNogod() {
        return nogod;
    }

    public void setNogod(String nogod) {
        this.nogod = nogod;
    }

    public String getNogod_number() {
        return nogod_number;
    }

    public void setNogod_number(String nogod_number) {
        this.nogod_number = nogod_number;
    }

    public String getSureCash() {
        return sureCash;
    }

    public void setSureCash(String sureCash) {
        this.sureCash = sureCash;
    }

    public String getSureCash_number() {
        return sureCash_number;
    }

    public void setSureCash_number(String sureCash_number) {
        this.sureCash_number = sureCash_number;
    }
}
