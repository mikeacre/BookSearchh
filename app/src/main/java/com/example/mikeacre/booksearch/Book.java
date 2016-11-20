package com.example.mikeacre.booksearch;

import java.net.URL;

/**
 * Created by mikeacre on 11/19/2016.
 */

public class Book {

    private String mTitle;
    private String mAuthor;
    private String mSummary;
    private String mThumbnail;


    public Book(String title, String author, String summary, String thumbnail){
        mTitle = title;
        mAuthor = author;
        mSummary = summary;
        mThumbnail = thumbnail;
    }

    public String getTitle(){return mTitle;}
    public String getAuthor(){return mAuthor;}
    public String getSummary(){return mSummary;}
    public String getThumbnail() {return mThumbnail;
    }
}
