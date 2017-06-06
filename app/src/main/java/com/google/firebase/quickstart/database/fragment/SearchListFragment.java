package com.google.firebase.quickstart.database.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.quickstart.database.MainActivity;
import com.google.firebase.quickstart.database.PostDetailActivity;
import com.google.firebase.quickstart.database.R;
import com.google.firebase.quickstart.database.models.Post;
import com.google.firebase.quickstart.database.models.Profile;
import com.google.firebase.quickstart.database.viewholder.PostViewHolder;
import com.google.firebase.quickstart.database.viewholder.SearchViewHolder;

import static com.google.firebase.quickstart.database.MainActivity.mainContext;

/**
 * Created by sadiq on 5/30/17.
 */

public class SearchListFragment extends android.support.v4.app.Fragment{
    private static final String TAG = "PostListFragment";

    // [START define_database_reference]
    private DatabaseReference mDatabase;
    // [END define_database_reference]

    private FirebaseRecyclerAdapter<Profile, SearchViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;

    public SearchListFragment() {}

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_search_list, container, false);

        // [START create_database_reference]
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Profile");
        // [END create_database_reference]

        mRecycler = (RecyclerView) rootView.findViewById(R.id.search_list);
        mRecycler.setHasFixedSize(true);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        // Set up FirebaseRecyclerAdapter with the Query
        Query postsQuery = getQuery(mDatabase);
        if (postsQuery==null){
            Toast.makeText(MainActivity.mainContext,"null",Toast.LENGTH_SHORT).show();
        }
        mAdapter = new FirebaseRecyclerAdapter<Profile, SearchViewHolder>(Profile.class, R.layout.search_item,
                SearchViewHolder.class, postsQuery) {

            @Override
            protected void populateViewHolder(SearchViewHolder postViewHolder, Profile post, int i) {

                //final DatabaseReference postRef = getRef(i);

                // Set click listener for the whole post view
                //final String postKey = postRef.getKey();
                /*postViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Launch PostDetailActivity
                        Intent intent = new Intent(getActivity(), PostDetailActivity.class);
                        intent.putExtra(PostDetailActivity.EXTRA_POST_KEY, postKey);
                        startActivity(intent);
                    }
                });*/


                //Toast.makeText(mainContext, "welcome",Toast.LENGTH_SHORT).show();
                // Bind Post to ViewHolder, setting OnClickListener for the star button


                postViewHolder.bindToSearch(post);
            }
        };
        mRecycler.setAdapter(mAdapter);
    }

    // [START post_stars_transaction]
    private void onStarClicked(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Post p = mutableData.getValue(Post.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }

                if (p.stars.containsKey(getUid())) {
                    // Unstar the post and remove self from stars
                    p.starCount = p.starCount - 1;
                    p.stars.remove(getUid());
                } else {
                    // Star the post and add self to stars
                    p.starCount = p.starCount + 1;
                    p.stars.put(getUid(), true);
                }

                // Set value and report transaction success
                mutableData.setValue(p);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                //
                // Log.d(TAG, "postTransaction:onComplete:" + databaseError);
            }
        });
    }
    // [END post_stars_transaction]

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public  Query getQuery(DatabaseReference databaseReference){



        Query query=databaseReference.orderByChild("sl").
                startAt("Electritian:Azimpur,Dhaka").endAt("Electritian:Azimpur,Dhaka:5.0:");

        return query;
    }

}
