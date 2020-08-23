package alamin.game.discountappofshopers.model;

public class MarketingPeopleModel {
    String name;
    String phone;
    String referral;

    public MarketingPeopleModel() {
    }

    public MarketingPeopleModel(String name, String phone, String referral) {
        this.name = name;
        this.phone = phone;
        this.referral = referral;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getReferral() {
        return referral;
    }

    public void setReferral(String referral) {
        this.referral = referral;
    }
}
