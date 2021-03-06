package com.example.android.mynewsapp;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewsArticle {

    private String ThumbnailUrl;
    private String Header;
    private String Author;
    private String DatePublished;
    private String Section;
    private String Url;


    public NewsArticle(String thumbnail, String header, String section, String author, String date, String url) {

        ThumbnailUrl = thumbnail;
        Header = header;
        Section = section;
        Author = author;
        DatePublished = date;
        Url = url;
    }

    public String getThumbnailUrl() {
        return ThumbnailUrl;
    }

    public String getHeader() {
        return Header;
    }

    public String getSection() {
        return Section;
    }

    public String getAuthor() {
        return Author;
    }

    public String getDatePublished() {

        return DatePublished;
    }

    public String getUrl() {
        return Url;
    }


}
