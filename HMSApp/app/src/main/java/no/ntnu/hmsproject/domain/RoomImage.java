package no.ntnu.hmsproject.domain;

public class RoomImage {
    String id;
    byte[] image;

    public RoomImage() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
