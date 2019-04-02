package com.example.utilities.Gallery;

import java.io.Serializable;

public class ImageItem implements Serializable {

    private String path;


    public ImageItem(String path) {
        this.path = path;
    }


    public String getPath() {
        return path;
    }


    //public boolean isDirectory() { return isDirectory; }


    //public Bitmap getImage() { return image; }
}
