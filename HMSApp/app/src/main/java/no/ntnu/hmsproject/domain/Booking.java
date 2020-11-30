package no.ntnu.hmsproject.domain;

public class Booking {
    int bookingId;

    //Booking specific fields
    String bookingStartDate;
    String bookingEndDate;
    String bookingAccepted;
    String bookingStatus;

    //RoomType
    String roomType;

    //Room
    String room;

    public Booking(String bookingRoomType, String booingStartDate, String bookingEndDate) {
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