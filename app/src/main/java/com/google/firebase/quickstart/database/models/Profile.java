package com.google.firebase.quickstart.database.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;
import java.util.List;

/**
 * Created by sadiq on 5/30/17.
 */

@IgnoreExtraProperties
public class Profile {
    public String uid;
    public String name;
    public String email;
    public float rating;
    public String skill;
    public String location;
    public String sl;


    public Profile(){

    }

    public Profile(String name,String location,float rating,String skill){
        //this.uid=uid;
        this.name=name;
        this.location=location;
        this.rating=rating;
        this.skill=skill;
        this.sl=skill+":"+location+":"+rating+":";

    }


}
