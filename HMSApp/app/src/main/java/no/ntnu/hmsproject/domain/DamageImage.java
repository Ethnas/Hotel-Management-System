package no.ntnu.hmsproject.domain;

import com.google.gson.annotations.SerializedName;

public class DamageImage {
    @SerializedName("imageId")
    String id;
    @SerializedName("image")
    byte[] image;
    @SerializedName("reportId")
    DamageReport damageReport;
    @SerializedName("mimetype")
    String mimeType;
    @SerializedName("fileSize")
    int fileSize;

    public DamageImage() {
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

    public DamageReport getDamageReport() {
        return damageReport;
    }

    public void setDamageReport(DamageReport damageReport) {
        this.damageReport = damageReport;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }
}
