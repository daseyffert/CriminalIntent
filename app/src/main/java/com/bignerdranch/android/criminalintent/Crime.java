package com.bignerdranch.android.criminalintent;

import java.util.UUID;

/**
 * Created by Daniel on 10/29/2015.
 * Model Layer Class
 * PURPOSE: Model the crimes the user can search for
 */
public class Crime {
    private UUID mId;
    private String mTitle;


    /**
    * CONSTRUCTOR CLASS: generates a unique identifier
    */
    public Crime(){
        this.mId = UUID.randomUUID();
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
}
