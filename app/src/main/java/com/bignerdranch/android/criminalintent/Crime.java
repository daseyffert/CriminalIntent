package com.bignerdranch.android.criminalintent;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Daniel on 10/29/2015.
 * Model Layer Class
 * PURPOSE: Model the crimes the user can search for
 */
public class Crime {
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private  boolean mSolved;
    private String mSuspect;
    private long mContact;


    //CONSTRUCTOR CLASS: generates a unique identifier
    public Crime(){
        this(UUID.randomUUID());
    }
    public Crime(UUID id) {
        mId = id;
        mDate = new Date();
    }

    //Generate Getter for mId
    //Generate Getter and Setter for mTitle
    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public String getSuspect() {
        return mSuspect;
    }

    public void setSuspect(String suspect) {
        mSuspect = suspect;
    }

    public long getContact() {
        return mContact;
    }

    public void setContact(long contact) {
        mContact = contact;
    }
}
