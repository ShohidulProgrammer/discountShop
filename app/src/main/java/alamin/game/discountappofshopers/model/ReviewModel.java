package alamin.game.discountappofshopers.model;

public class ReviewModel {


    private String uid;
    private String review_message;
    private String reviewr_fb_id;
    private String review_date;




    public ReviewModel() {
    }

    public ReviewModel(String uid, String review_message, String reviewr_fb_id, String review_date) {
        this.uid = uid;
        this.review_message = review_message;
        this.reviewr_fb_id = reviewr_fb_id;
        this.review_date = review_date;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getReview_message() {
        return review_message;
    }

    public void setReview_message(String review_message) {
        this.review_message = review_message;
    }

    public String getReviewr_fb_id() {
        return reviewr_fb_id;
    }

    public void setReviewr_fb_id(String reviewr_fb_id) {
        this.reviewr_fb_id = reviewr_fb_id;
    }

    public String getReview_date() {
        return review_date;
    }

    public void setReview_date(String review_date) {
        this.review_date = review_date;
    }
}
