package com.example.android.jdevlagos;

import android.graphics.Bitmap;

/**
 * Created by Ahmad on 9/15/2017.
 */

public class DevProfile {

    // developer username
    private String mUsername;
    // image resource
    private String mImageResource;
    // profile page url
    private String mProfilePage;

    /**
     *
     * @param username is the username of the developer
     * @param imageResource is the image id for the developer
     * @param profilePage is the profile page for the developer
     */
    public DevProfile (String username, String imageResource, String profilePage){

        mUsername = username;
        mImageResource = imageResource;
        mProfilePage = profilePage;
    }

    public DevProfile (String username, String profilePage){

        mUsername = username;
        //mImageResource = imageResource;
        mProfilePage = profilePage;
    }


    /**
     * Get the developer username
     */
    public String getUsername(){
        return mUsername;
    }

    /**
     * Get the Image for the developer
     */
    public String getImageResource(){
        return mImageResource;
    }

    /**
     * Get the profile page url
     */
    public String getProfilePage(){
        return mProfilePage;
    }
}
