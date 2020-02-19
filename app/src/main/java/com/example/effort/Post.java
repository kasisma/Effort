package com.example.effort;

public class Post {

    private String documentID;
    private  String title;
    private  String contents;


    public Post(String documentID, String title, String contents) {
        this.documentID = documentID;
        this.title = title;
        this.contents = contents;
    }

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}
