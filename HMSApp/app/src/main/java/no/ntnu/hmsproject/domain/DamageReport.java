package no.ntnu.hmsproject.domain;

public class DamageReport {
    String damageTitle;
    String damageDesc;
    String bookingId;



    String damageId;

    public DamageReport(String damageTitle, String bookingid, String damageDesc) {
    }

    public String getDamageTitle() {
        return damageTitle;
    }

    public void setDamageTitle(String damageTitle) {
        this.damageTitle = damageTitle;
    }

    public String getDamageDesc() {
        return damageDesc;
    }

    public void setDamageDesc(String damageDesc) {
        this.damageDesc = damageDesc;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getDamageId() {
        return damageId;
    }

    public void setDamageId(String damageId) {
        this.damageId = damageId;
    }
}
