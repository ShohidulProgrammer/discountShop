package alamin.game.discountappofshopers.model;

public class ReceiptModelShopper {

    private String receipt_no;
    private String receipt_amount;
    private String receipt_discount;
    private String customer_return_value;
    private String receipt_pic_url;
    private String receipt_shop_uid;
    private String receipt_customer_uid;
    private String receipt_date;
    private String CollectDate;
    private String random_key;
    private Boolean paid_status;


    public ReceiptModelShopper() {
    }

    public ReceiptModelShopper(String receipt_no, String receipt_amount, String receipt_discount, String customer_return_value, String receipt_pic_url, String receipt_shop_uid, String receipt_customer_uid, String receipt_date, String collectDate, String random_key, Boolean paid_status) {
        this.receipt_no = receipt_no;
        this.receipt_amount = receipt_amount;
        this.receipt_discount = receipt_discount;
        this.customer_return_value = customer_return_value;
        this.receipt_pic_url = receipt_pic_url;
        this.receipt_shop_uid = receipt_shop_uid;
        this.receipt_customer_uid = receipt_customer_uid;
        this.receipt_date = receipt_date;
        CollectDate = collectDate;
        this.random_key = random_key;
        this.paid_status = paid_status;
    }

    public String getReceipt_no() {
        return receipt_no;
    }

    public void setReceipt_no(String receipt_no) {
        this.receipt_no = receipt_no;
    }

    public String getReceipt_amount() {
        return receipt_amount;
    }

    public void setReceipt_amount(String receipt_amount) {
        this.receipt_amount = receipt_amount;
    }

    public String getReceipt_discount() {
        return receipt_discount;
    }

    public void setReceipt_discount(String receipt_discount) {
        this.receipt_discount = receipt_discount;
    }

    public String getCustomer_return_value() {
        return customer_return_value;
    }

    public void setCustomer_return_value(String customer_return_value) {
        this.customer_return_value = customer_return_value;
    }

    public String getReceipt_pic_url() {
        return receipt_pic_url;
    }

    public void setReceipt_pic_url(String receipt_pic_url) {
        this.receipt_pic_url = receipt_pic_url;
    }

    public String getReceipt_shop_uid() {
        return receipt_shop_uid;
    }

    public void setReceipt_shop_uid(String receipt_shop_uid) {
        this.receipt_shop_uid = receipt_shop_uid;
    }

    public String getReceipt_customer_uid() {
        return receipt_customer_uid;
    }

    public void setReceipt_customer_uid(String receipt_customer_uid) {
        this.receipt_customer_uid = receipt_customer_uid;
    }

    public String getReceipt_date() {
        return receipt_date;
    }

    public void setReceipt_date(String receipt_date) {
        this.receipt_date = receipt_date;
    }

    public String getCollectDate() {
        return CollectDate;
    }

    public void setCollectDate(String collectDate) {
        CollectDate = collectDate;
    }

    public String getRandom_key() {
        return random_key;
    }

    public void setRandom_key(String random_key) {
        this.random_key = random_key;
    }

    public Boolean getPaid_status() {
        return paid_status;
    }

    public void setPaid_status(Boolean paid_status) {
        this.paid_status = paid_status;
    }
}
