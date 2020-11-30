package no.ntnu.hmsproject.domain;

public class RoomType {
    String roomtype;
    String roomPrice;

    public RoomType(String roomType, String roomPrice) {
    }

    public String getRoomtype() {
        return roomtype;
    }

    public void setRoomtype(String roomtype) {
        this.roomtype = roomtype;
    }

    public String getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(String roomPrice) {
        this.roomPrice = roomPrice;
    }
}
