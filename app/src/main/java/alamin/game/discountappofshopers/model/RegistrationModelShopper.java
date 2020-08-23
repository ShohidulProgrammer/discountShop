package alamin.game.discountappofshopers.model;

public class RegistrationModelShopper {
    private String fb_id;
    private String shop_name;
    private String shopper_phone;
    private String shopper_email;
    private String shopper_password;
    private String shop_creation_date;
    private String shop_pic_url;
    private String shop_location_manually;
    private String shop_google_location;
    private int discount;
    private String food_item;
    private double latitude;
    private double longitude;

    public RegistrationModelShopper() {
    }

    public RegistrationModelShopper(String fb_id, String shop_name, String shopper_phone, String shopper_email, String shopper_password, String shop_creation_date, String shop_pic_url, String shop_location_manually, String shop_google_location, int discount, String food_item, double latitude, double longitude) {
        this.fb_id = fb_id;
        this.shop_name = shop_name;
        this.shopper_phone = shopper_phone;
        this.shopper_email = shopper_email;
        this.shopper_password = shopper_password;
        this.shop_creation_date = shop_creation_date;
        this.shop_pic_url = shop_pic_url;
        this.shop_location_manually = shop_location_manually;
        this.shop_google_location = shop_google_location;
        this.discount = discount;
        this.food_item = food_item;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getFb_id() {
        return fb_id;
    }

    public void setFb_id(String fb_id) {
        this.fb_id = fb_id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getShopper_phone() {
        return shopper_phone;
    }

    public void setShopper_phone(String shopper_phone) {
        this.shopper_phone = shopper_phone;
    }

    public String getShopper_email() {
        return shopper_email;
    }

    public void setShopper_email(String shopper_email) {
        this.shopper_email = shopper_email;
    }

    public String getShopper_password() {
        return shopper_password;
    }

    public void setShopper_password(String shopper_password) {
        this.shopper_password = shopper_password;
    }

    public String getShop_creation_date() {
        return shop_creation_date;
    }

    public void setShop_creation_date(String shop_creation_date) {
        this.shop_creation_date = shop_creation_date;
    }

    public String getShop_pic_url() {
        return shop_pic_url;
    }

    public void setShop_pic_url(String shop_pic_url) {
        this.shop_pic_url = shop_pic_url;
    }

    public String getShop_location_manually() {
        return shop_location_manually;
    }

    public void setShop_location_manually(String shop_location_manually) {
        this.shop_location_manually = shop_location_manually;
    }

    public String getShop_google_location() {
        return shop_google_location;
    }

    public void setShop_google_location(String shop_google_location) {
        this.shop_google_location = shop_google_location;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getFood_item() {
        return food_item;
    }

    public void setFood_item(String food_item) {
        this.food_item = food_item;
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
}
