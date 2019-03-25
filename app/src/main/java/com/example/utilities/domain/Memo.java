package com.example.utilities.domain;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * Created by KHS on 2017-02-14.
 */

@DatabaseTable(tableName = "memo")
public class Memo {

    @DatabaseField(generatedId = true)
    int id;

    @DatabaseField
    String title;

    @DatabaseField
    String content;

    @DatabaseField
    Date currentDate;

    @DatabaseField
    String imgUri;


    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public String getImgUri() {
        return imgUri;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }

    public Memo() {

    }

    // create시에 사용할 생성자
    public Memo(String title, String content, Date currentDate, String imgUri) {
        this.title = title;
        this.content = content;
        this.currentDate = currentDate;
        this.imgUri = imgUri;
    }

}
