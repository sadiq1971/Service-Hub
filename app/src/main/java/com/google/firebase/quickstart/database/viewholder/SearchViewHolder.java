package com.google.firebase.quickstart.database.viewholder;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.quickstart.database.MainActivity;
import com.google.firebase.quickstart.database.R;
import com.google.firebase.quickstart.database.fragment.SearchBarFragment;
import com.google.firebase.quickstart.database.models.Profile;
import com.google.firebase.quickstart.database.models.User;

import static com.google.firebase.quickstart.database.MainActivity.mainContext;

/**
 * Created by sadiq on 5/27/2017.
 */

public class SearchViewHolder extends RecyclerView.ViewHolder {

    public TextView nameField;
    public TextView skillField;
    public TextView locationField;
    public RatingBar ratingField;
    public TextView ratingTextField;

    public SearchViewHolder(View itemView) {
        super(itemView);

        nameField = (TextView) itemView.findViewById(R.id.name);
        skillField = (TextView) itemView.findViewById(R.id.skill);
        locationField = (TextView) itemView.findViewById(R.id.location);
        ratingField = (RatingBar) itemView.findViewById(R.id.rating);
        ratingTextField=(TextView)itemView.findViewById(R.id.rating_text);
        }





    public void bindToSearch(Profile profile) {

        nameField.setText(profile.name);
        skillField.setText("("+profile.skill+")");
        locationField.setText(profile.location);
        float rating=profile.rating;
        ratingField.setRating(rating);
        ratingTextField.setText(String.valueOf(String.format("%.2f", rating)));

        SearchBarFragment.count++;
    }
}