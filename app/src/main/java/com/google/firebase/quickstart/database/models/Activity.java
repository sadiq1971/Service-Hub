package com.google.firebase.quickstart.database.models;

/**
 * Created by sadiq on 08/06/17.
 */

public class Activity {
    public String pName;
    public String pUID;
    public String pRating;
    public String pBody;
    public String uName;
    public String uUID;

    public Activity(String pName,String uName,String pRating,String pBody){
        this.pName=pName;
        this.uName=uName;
        this.pRating=pRating;
        this.pBody=pBody;

    }

}
