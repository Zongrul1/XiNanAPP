package com.example.xinan.db;

public class Message {
    // fruitName to store the string of fruit name
    private String Title;
    private String Desc;
    private int Id;
    private String Url;

    public Message(String title,String note,int Id,String Url) {
        this.Title = title;
        this.Desc = note;
        this.Id = Id;
        this.Url = Url;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    public String getNote() {
        return Desc;
    }

    public void setNote(String note) {
        this.Desc = note;
    }
}
