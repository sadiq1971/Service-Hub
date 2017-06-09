package com.google.firebase.quickstart.database;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.quickstart.database.data.DataHelper;
import com.google.firebase.quickstart.database.models.Activity;
import com.google.firebase.quickstart.database.models.Post;
import com.google.firebase.quickstart.database.models.Profile;
import com.google.firebase.quickstart.database.models.User;

import org.xml.sax.ext.DeclHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class NewPostActivity extends BaseActivity {

    private static final String TAG = "NewPostActivity";
    private static final String REQUIRED = "Required";
    String fname[]={"Aman ","Ontu ","Mirag ","Bappa ","Farhan ","Anik ","Fahim ","Tahmid ",
            "Sojib ","Ratul ",
    "Hasib ","Nahiyan "};
    String lname[]={"borua","rahman","islam","ilahi","mondol","das",",mojumder","talukder",
            "hassan","ali"};


    final String[] COUNTRIES = new String[] {
            "--What are u looking for?","Electritian", "Mechanic", "Household Worker", "Sanitary Worker", "Carpenter","Internet Service Provider"
    };

    public int id=0;

    private DatabaseReference mDatabase;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        submit=(Button)findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPost();
            }
        });
    }

    private void submitPost() {

        Toast.makeText(this, "Posting...", Toast.LENGTH_SHORT).show();


       // final String userId = getUid();


        Random r=new Random();
        int f=Math.abs(r.nextInt()%fname.length);
        int l=Math.abs(r.nextInt()%lname.length);
        int lIndex=Math.abs(r.nextInt()% DataHelper.sLocation.size());
        int sIndex=Math.abs(r.nextInt()%(COUNTRIES.length-1)) +1;
        float rating =  r.nextFloat() * (4.9f - 0.0f) + 0.0f;

        String Id;

        int i=Math.abs(r.nextInt()%1000 * r.nextInt()%1000);

        Id=String.valueOf(id);

       /* Profile profile = new Profile(fname[f]+lname[l],
                DataHelper.sLocation.get(lIndex).getBody(),rating,COUNTRIES[sIndex]);*/


        int pUid_i=Math.abs(r.nextInt()%660)+4;
        int rpUid_i=Math.abs(r.nextInt()%660)+4;
        String pUid=String.valueOf(pUid_i);
        String rpUid=String.valueOf(rpUid_i);

        String date="June 9,2017";
        String body="He is very good person.If you need such kind of service i obviously advice you " +
                "to choose this person. He has also very good personality.";




        Activity activity=new Activity(pUid,rpUid,date,body);

        mDatabase.child("Activity").child(Id).setValue(activity);
        id++;

    }

}
