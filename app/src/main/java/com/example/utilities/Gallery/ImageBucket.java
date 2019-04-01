package com.example.utilities.Gallery;

public class ImageBucket {

    private String name;
    private String firstImageContainedPath;

    public ImageBucket(String name, String firstImageContainedPath) {
        this.name = name;
        this.firstImageContainedPath = firstImageContainedPath;
    }

    public String getName() {
        return name;
    }

    public String getFirstImageContainedPath() {
        return firstImageContainedPath;
    }
}