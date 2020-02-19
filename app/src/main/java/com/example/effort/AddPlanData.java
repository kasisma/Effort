package com.example.effort;

public class AddPlanData {
    String title;
    String content;
    String day;

    public AddPlanData(String title, String content, String day) {
        this.title = title;
        this.content = content;
        this.day = day;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
