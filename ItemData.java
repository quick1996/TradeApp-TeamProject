package com.example.signupin;

public class ItemData {
    private String title;
    private String content;
    private String price;
    private String time;

    public ItemData() {

    }

    public ItemData(String title, String content, String price, String time) {
        this.title = title;
        this.content = content;
        this.price = price;
        this.time = time;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}