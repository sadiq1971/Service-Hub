package com.google.firebase.quickstart.database.models;

/**
 * Created by sadiq on 08/06/17.
 */

public class Activity {
    public String pUID;
    public String body;
    public String rpUID;
    public String date;

    public Activity(){

    }

    public Activity(String pUID,String rpUID,String date,String body){
        this.pUID=pUID;
        this.rpUID=rpUID;
        this.date=date;
        this.body=body;
    }

}
