package com.google.firebase.quickstart.database.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.quickstart.database.R;
import com.google.firebase.quickstart.database.models.Activity;

/**
 * Created by sadiq on 08/06/17.
 */

public class ActivityViewHolder extends RecyclerView.ViewHolder{
    public TextView review;
    public TextView pName;
    public TextView date;
    public TextView body;
    public TextView rpName;
    public TextView rpSkill;
    public TextView rpLocation;
    public TextView rpRatingText;
    public RatingBar rpRatingBar;
    public String pName_S;
    public String rpName_S;



    public ActivityViewHolder(View itemView) {
        super(itemView);

        review=(TextView)itemView.findViewById(R.id.reviewtext);
        pName=(TextView)itemView.findViewById(R.id.uName);
        date=(TextView)itemView.findViewById(R.id.date);
        body=(TextView)itemView.findViewById(R.id.body);
        rpName=(TextView)itemView.findViewById(R.id.name);
        rpSkill=(TextView)itemView.findViewById(R.id.skill);
        rpLocation=(TextView)itemView.findViewById(R.id.location);
        rpRatingBar=(RatingBar) itemView.findViewById(R.id.rating);
        rpRatingText=(TextView)itemView.findViewById(R.id.rating_text);


    }

    public void bindToActivity(final Activity activity){

        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("Profile").child(activity.pUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 pName_S = (String) dataSnapshot.child("name").getValue();
                 pName.setText(pName_S);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabase.child("Profile").child(activity.rpUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                rpName_S = (String) dataSnapshot.child("name").getValue();
                String location = (String) dataSnapshot.child("location").getValue();
                String skill = (String) dataSnapshot.child("skill").getValue();
                double r=(double)dataSnapshot.child("rating").getValue();

                float rate=(float)r;



                rpName.setText(rpName_S);
                rpLocation.setText(location);

                rpSkill.setText(skill);
                rpRatingText.setText(String.format("%.2f", rate));
                rpRatingBar.setRating(rate);

                review.setText(rpName_S+" was reviewed 5 star");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        date.setText(activity.date);
        body.setText(activity.body);


    }
}
