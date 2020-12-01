package no.ntnu.hmsproject.domain;

import com.google.gson.annotations.SerializedName;

public class Booking {
    @SerializedName("bookingId")
    int bookingId;

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
    @SerializedName("roomNumber")
    String room;

    @SerializedName("user")
    User user;

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

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
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

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}