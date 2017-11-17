package com.example.ghazar.chalange.Objects;

public class RowItem {
    private int imageId;
    private String title;
    private String desc;
    private String ID;

    public RowItem(int imageId, String title, String desc, String id) {
        this.imageId = imageId;
        this.title = title;
        this.desc = desc;
        ID = id;
    }

    public String getID() {
        return ID;
    }
    public void setID(String id) {
        this.ID = id;
    }
    public int getImageId() {
        return imageId;
    }
    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    @Override
    public String toString() {
        return title + "\n" + desc;
    }
}
