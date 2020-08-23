package alamin.game.discountappofshopers.model;

public class ReportAndHelpModel {
    private String uid;
    private String time;
    private String date;
    private String message;


    public ReportAndHelpModel() {
    }

    public ReportAndHelpModel(String uid, String time, String date, String message) {
        this.uid = uid;
        this.time = time;
        this.date = date;
        this.message = message;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
