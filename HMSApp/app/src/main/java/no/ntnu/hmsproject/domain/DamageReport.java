package no.ntnu.hmsproject.domain;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DamageReport {
    @SerializedName("damageTitle")
    String damageTitle;

    @SerializedName("damageDescription")
    String damageDescription;

    List<DamageImage> photos = new ArrayList<>();

    String sendBookingId;

    @SerializedName("reportId")
    String reportId;

    @SerializedName("booking")
    Booking booking;

    public DamageReport(String damageTitle, String bookingid, String damageDesc) {
    }

    public String getDamageTitle() {
        return damageTitle;
    }

    public void setDamageTitle(String damageTitle) {
        this.damageTitle = damageTitle;
    }

    public String getDamageDescription() {
        return damageDescription;
    }

    public void setDamageDescription(String damageDescription) {
        this.damageDescription = damageDescription;
    }

    public String getSendBookingId() {
        return sendBookingId;
    }

    public void setSendBookingId(String sendBookingId) {
        this.sendBookingId = sendBookingId;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }
}
