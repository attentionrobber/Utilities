package com.example.utilities.Gallery;

/**
 * 이미지가 들어있는 모든 폴더를 나타냄
 * Image 가 속해 있는 디렉토리를 객체화함
 * Used by : GalleryActivity,
 */
public class ImageBucket {

    private String name; // 속해 있는 폴더 이름
    //private int count; // 폴더 안의 이미지 개수
    private String firstImageContainedPath; // 폴더 안의 첫번째 이미지 경로

    ImageBucket(String name, String firstImageContainedPath) {
        this.name = name;
        //this.count = count;
        this.firstImageContainedPath = firstImageContainedPath;
    }

    public String getName() {
        return name;
    }

    //public int getCount() { return count; }

    public String getFirstImageContainedPath() {
        return firstImageContainedPath;
    }
}