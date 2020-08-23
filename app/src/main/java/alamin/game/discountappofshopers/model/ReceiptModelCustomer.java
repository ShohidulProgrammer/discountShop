package alamin.game.discountappofshopers.model;

public class ReceiptModelCustomer {

    private String customer_uid;
    private String shop_uid;
    private String invoiceNumber;
    private String receipt_pic_url;
    private String receipt_amount;
    private String discount_percentage;
    private String discount_money;
    private String customer_choose_item;
    private String receipt_submit_data;
    private String collect_money_from_shop_date;
    private Boolean collect_money_from_shop_status;
    private String back_money_to_customer_date;
    private Boolean back_money_to_customer_status;
    private Boolean rejected_status;
    private String paymentMethod;
    private String gateway;

    private Boolean withdrawal_request_status;
    private String withdraw_request_date;
    private String withdraw_request_time;

    private Boolean shopper_rejected_request_status;
    private String shopper_rejected_request_date;
    private String shopper_rejected_request_time;


    private String random_key;


    public ReceiptModelCustomer() {
    }

    public ReceiptModelCustomer(String customer_uid, String shop_uid, String invoiceNumber, String receipt_pic_url, String receipt_amount, String discount_percentage, String discount_money, String customer_choose_item, String receipt_submit_data, String collect_money_from_shop_date, Boolean collect_money_from_shop_status, String back_money_to_customer_date, Boolean back_money_to_customer_status, Boolean rejected_status, String paymentMethod, String gateway, Boolean withdrawal_request_status, String withdraw_request_date, String withdraw_request_time, Boolean shopper_rejected_request_status, String shopper_rejected_request_date, String shopper_rejected_request_time, String random_key) {
        this.customer_uid = customer_uid;
        this.shop_uid = shop_uid;
        this.invoiceNumber = invoiceNumber;
        this.receipt_pic_url = receipt_pic_url;
        this.receipt_amount = receipt_amount;
        this.discount_percentage = discount_percentage;
        this.discount_money = discount_money;
        this.customer_choose_item = customer_choose_item;
        this.receipt_submit_data = receipt_submit_data;
        this.collect_money_from_shop_date = collect_money_from_shop_date;
        this.collect_money_from_shop_status = collect_money_from_shop_status;
        this.back_money_to_customer_date = back_money_to_customer_date;
        this.back_money_to_customer_status = back_money_to_customer_status;
        this.rejected_status = rejected_status;
        this.paymentMethod = paymentMethod;
        this.gateway = gateway;
        this.withdrawal_request_status = withdrawal_request_status;
        this.withdraw_request_date = withdraw_request_date;
        this.withdraw_request_time = withdraw_request_time;
        this.shopper_rejected_request_status = shopper_rejected_request_status;
        this.shopper_rejected_request_date = shopper_rejected_request_date;
        this.shopper_rejected_request_time = shopper_rejected_request_time;
        this.random_key = random_key;
    }

    public String getCustomer_uid() {
        return customer_uid;
    }

    public void setCustomer_uid(String customer_uid) {
        this.customer_uid = customer_uid;
    }

    public String getShop_uid() {
        return shop_uid;
    }

    public void setShop_uid(String shop_uid) {
        this.shop_uid = shop_uid;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getReceipt_pic_url() {
        return receipt_pic_url;
    }

    public void setReceipt_pic_url(String receipt_pic_url) {
        this.receipt_pic_url = receipt_pic_url;
    }

    public String getReceipt_amount() {
        return receipt_amount;
    }

    public void setReceipt_amount(String receipt_amount) {
        this.receipt_amount = receipt_amount;
    }

    public String getDiscount_percentage() {
        return discount_percentage;
    }

    public void setDiscount_percentage(String discount_percentage) {
        this.discount_percentage = discount_percentage;
    }

    public String getDiscount_money() {
        return discount_money;
    }

    public void setDiscount_money(String discount_money) {
        this.discount_money = discount_money;
    }

    public String getCustomer_choose_item() {
        return customer_choose_item;
    }

    public void setCustomer_choose_item(String customer_choose_item) {
        this.customer_choose_item = customer_choose_item;
    }

    public String getReceipt_submit_data() {
        return receipt_submit_data;
    }

    public void setReceipt_submit_data(String receipt_submit_data) {
        this.receipt_submit_data = receipt_submit_data;
    }

    public String getCollect_money_from_shop_date() {
        return collect_money_from_shop_date;
    }

    public void setCollect_money_from_shop_date(String collect_money_from_shop_date) {
        this.collect_money_from_shop_date = collect_money_from_shop_date;
    }

    public Boolean getCollect_money_from_shop_status() {
        return collect_money_from_shop_status;
    }

    public void setCollect_money_from_shop_status(Boolean collect_money_from_shop_status) {
        this.collect_money_from_shop_status = collect_money_from_shop_status;
    }

    public String getBack_money_to_customer_date() {
        return back_money_to_customer_date;
    }

    public void setBack_money_to_customer_date(String back_money_to_customer_date) {
        this.back_money_to_customer_date = back_money_to_customer_date;
    }

    public Boolean getBack_money_to_customer_status() {
        return back_money_to_customer_status;
    }

    public void setBack_money_to_customer_status(Boolean back_money_to_customer_status) {
        this.back_money_to_customer_status = back_money_to_customer_status;
    }

    public Boolean getRejected_status() {
        return rejected_status;
    }

    public void setRejected_status(Boolean rejected_status) {
        this.rejected_status = rejected_status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public Boolean getWithdrawal_request_status() {
        return withdrawal_request_status;
    }

    public void setWithdrawal_request_status(Boolean withdrawal_request_status) {
        this.withdrawal_request_status = withdrawal_request_status;
    }

    public String getWithdraw_request_date() {
        return withdraw_request_date;
    }

    public void setWithdraw_request_date(String withdraw_request_date) {
        this.withdraw_request_date = withdraw_request_date;
    }

    public String getWithdraw_request_time() {
        return withdraw_request_time;
    }

    public void setWithdraw_request_time(String withdraw_request_time) {
        this.withdraw_request_time = withdraw_request_time;
    }

    public Boolean getShopper_rejected_request_status() {
        return shopper_rejected_request_status;
    }

    public void setShopper_rejected_request_status(Boolean shopper_rejected_request_status) {
        this.shopper_rejected_request_status = shopper_rejected_request_status;
    }

    public String getShopper_rejected_request_date() {
        return shopper_rejected_request_date;
    }

    public void setShopper_rejected_request_date(String shopper_rejected_request_date) {
        this.shopper_rejected_request_date = shopper_rejected_request_date;
    }

    public String getShopper_rejected_request_time() {
        return shopper_rejected_request_time;
    }

    public void setShopper_rejected_request_time(String shopper_rejected_request_time) {
        this.shopper_rejected_request_time = shopper_rejected_request_time;
    }

    public String getRandom_key() {
        return random_key;
    }

    public void setRandom_key(String random_key) {
        this.random_key = random_key;
    }
}
