package com.example.ghazar.chalange.Objects;

import android.widget.ImageView;

public class RowItem {
    private ImageView imageId;
    private String title;
    private String desc;
    private String ID;
    private String time;

    public RowItem(ImageView imageId, String title, String desc, String id) {
        this.imageId = imageId;
        this.title = title;
        this.desc = desc;
        ID = id;
    }

    public RowItem(ImageView imageId, String title, String desc, String time, String id) {
        this.imageId = imageId;
        this.title = title;
        this.desc = desc;
        ID = id;
        this.time = time;
    }

    public String getID() {
        return ID;
    }
    public void setID(String id) {
        this.ID = id;
    }
    public ImageView getImageId() {
        return imageId;
    }
    public void setImageId(ImageView imageId) {
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
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    @Override
    public String toString() {
        return title + "\n" + desc;
    }
}
