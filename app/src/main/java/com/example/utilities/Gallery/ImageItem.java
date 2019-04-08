package com.example.utilities.Gallery;

import java.io.Serializable;

/**
 * 각 Image 의 정보
 * Used by : GalleryActivity
 */
public class ImageItem implements Serializable {

    private String path;
    private String title;
    private String date;
    private String size;
    private int position;

    ImageItem(String path, String title, String date, String size, int position) {
        this.path = path;
        this.title = title;
        this.date = date;
        this.size = size;
        this.position = position;
    }

    public String getPath() {
        return path;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getSize() {
        return size;
    }

    public int getPosition() { return position; }
}
