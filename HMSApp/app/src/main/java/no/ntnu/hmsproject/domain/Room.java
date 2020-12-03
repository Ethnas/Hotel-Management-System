package no.ntnu.hmsproject.domain;

import com.google.gson.annotations.SerializedName;

public class Room {
    @SerializedName("roomStatus")
    String roomStatus;
    @SerializedName("type")
    RoomType roomType;
    @SerializedName("roomNumber")
    int roomNumber;

    public String getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(String roomStatus) {
        this.roomStatus = roomStatus;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

}
