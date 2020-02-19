package com.example.effort;

import java.util.ArrayList;
import java.util.Date;

public class DiaryData {
    private  String publisher;
    private String title;
    private ArrayList<String> contents;
    private Date createdAt;

    public DiaryData(String title, ArrayList<String> contents,String publisher,Date createdAt) {
        this.title = title;
        this.contents = contents;
        this.publisher=publisher;
        this.createdAt=createdAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getContents() {
        return contents;
    }

    public void setContents(ArrayList<String> contents) {
        this.contents = contents;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
