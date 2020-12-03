package no.ntnu.hmsproject.domain;

import com.google.gson.annotations.SerializedName;

public class Booking {
    @SerializedName("bookingId")
    String bookingId;

    //Booking specific fields
    @SerializedName("bookingStartDate")
    String bookingStartDate;
    @SerializedName("bookingEndDate")
    String bookingEndDate;
    @SerializedName("bookingAccepted")
    String bookingAccepted;
    @SerializedName("bookingStatus")
    String bookingStatus;

    //RoomType
    String roomType;

    @SerializedName("roomTypes")
    RoomType rt;

    //Room
    String rooms;

    @SerializedName("room")
    Room rn;

    @SerializedName("user")
    User user;

    public Booking(String bookingid, String bookingRoomType, String booingStartDate, String bookingEndDate) {
    }

    public Booking(String bookingRoomType, String booingStartDate, String bookingEndDate) {
    }

    public RoomType getRt() {
        return rt;
    }

    public void setRt(RoomType rt) {
        this.rt = rt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getBookingStartDate() {
        return bookingStartDate;
    }

    public void setBookingStartDate(String bookingStartDate) {
        this.bookingStartDate = bookingStartDate;
    }

    public String getBookingEndDate() {
        return bookingEndDate;
    }

    public void setBookingEndDate(String bookingEndDate) {
        this.bookingEndDate = bookingEndDate;
    }

    public String getBookingAccepted() {
        return bookingAccepted;
    }

    public void setBookingAccepted(String bookingAccepted) {
        this.bookingAccepted = bookingAccepted;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public Room getRn() {
        return rn;
    }

    public void setRn(Room rn) {
        this.rn = rn;
    }
}