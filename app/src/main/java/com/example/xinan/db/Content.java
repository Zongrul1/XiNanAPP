package com.example.xinan.db;

import androidx.annotation.NonNull;

public class Content {
    private int Id;
    private String Uid;
    private String Title;
    private int Price;
    private String Description;
    private String Tag;
    private String Pic;
    private String Name;
    private int ContactType;
    private String Contact;
    private int Utime;
    private int Ctime;
    private int Status;
    private int Type;
    private String Date;
    private String DateBt;
    private int Love;
    private int HasLoved;
    private Boolean CanDelete;
    private String Nick;
    private String Head;
    private String Class;
    private String Tips;
    private String NickColor;

    public Content(String title,String tag,String name,int contactType,String contact,String description,String pic,int price,int type){
        Title = title;
        Tag = tag;
        Name = name;
        Contact = contact;
        ContactType = contactType;
        Description = description;
        Price = price;
        Type = type;
        Pic = pic;
    }
    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getTag() {
        return Tag;
    }

    public void setTag(String tag) {
        Tag = tag;
    }

    public String getPic() {
        return Pic;
    }

    public void setPic(String pic) {
        Pic = pic;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getContactType() {
        return ContactType;
    }

    public void setContactType(int contactType) {
        ContactType = contactType;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public int getUtime() {
        return Utime;
    }

    public void setUtime(int utime) {
        Utime = utime;
    }

    public int getCtime() {
        return Ctime;
    }

    public void setCtime(int ctime) {
        Ctime = ctime;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getDateBt() {
        return DateBt;
    }

    public void setDateBt(String dateBt) {
        DateBt = dateBt;
    }

    public int getLove() {
        return Love;
    }

    public void setLove(int love) {
        Love = love;
    }

    public int getHasLoved() {
        return HasLoved;
    }

    public void setHasLoved(int hasLoved) {
        HasLoved = hasLoved;
    }

    public Boolean getCanDelete() {
        return CanDelete;
    }

    public void setCanDelete(Boolean canDelete) {
        CanDelete = canDelete;
    }

    public String getNick() {
        return Nick;
    }

    public void setNick(String nick) {
        Nick = nick;
    }

    public String getHead() {
        return Head;
    }

    public void setHead(String head) {
        Head = head;
    }

    public String getclass() {
        return Class;
    }

    public void setClass(String aClass) {
        Class = aClass;
    }

    public String getTips() {
        return Tips;
    }

    public void setTips(String tips) {
        Tips = tips;
    }

    public String getNickColor() {
        return NickColor;
    }

    public void setNickColor(String nickColor) {
        NickColor = nickColor;
    }
}
