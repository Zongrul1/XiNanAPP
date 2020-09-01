package com.example.xinan.db;

public class News {
    // fruitName to store the string of fruit name
    private String Title;
    private String Desc;
    private int Id;
    private String Url;

    public News(String title, String note, int Id, String Url) {
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

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }
}
