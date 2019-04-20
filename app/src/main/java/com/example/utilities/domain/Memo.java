package com.example.utilities.domain;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;

/**
 * Used by:
 * Created by KHS on 2017-02-14.
 */

@DatabaseTable(tableName = "memo")
public class Memo implements Serializable {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String title;

    @DatabaseField
    private String content;

    @DatabaseField
    private Date currentDate;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private String[] imgUri = new String[10]; // 최대 10개 이미지 저장 가능

    public Memo() {

    }

    // create 에 사용할 생성자
    public Memo(String title, String content, Date currentDate, String[] imgUri) {
        this.title = title;
        this.content = content;
        this.currentDate = currentDate;
        this.imgUri = imgUri;
    }

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

    public String[] getImgUri() {
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

    public void setImgUri(String[] imgUri) {
        this.imgUri = imgUri;
    }

}
