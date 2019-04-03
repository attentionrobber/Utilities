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

    public ImageItem(String path, String title, String date, String size) {
        this.path = path;
        this.title = title;
        this.date = date;
        this.size = size;
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
}
