package com.google.firebase.quickstart.database.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.transition.Visibility;
import android.support.v4.content.res.ResourcesCompat;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.arlib.floatingsearchview.util.Util;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.quickstart.database.MainActivity;
import com.google.firebase.quickstart.database.R;
import com.google.firebase.quickstart.database.data.DataHelper;
import com.google.firebase.quickstart.database.data.LocationSuggetion;
import com.google.firebase.quickstart.database.models.Profile;
import com.google.firebase.quickstart.database.viewholder.SearchViewHolder;
import com.wang.avi.AVLoadingIndicatorView;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.List;

import static android.R.layout.simple_spinner_item;


public class SearchBarFragment extends BaseExampleFragment {
    private final String TAG = "BlankFragment";

    public static final long FIND_SUGGESTION_SIMULATED_DELAY = 250;

    private FloatingSearchView mSearchView;
    private RecyclerView mSearchResultsList;
    private boolean mIsDarkSearchTheme = false;
    boolean isCalled=false;

    private String mLastQuery = "";

    public ImageButton go;
    public ImageButton addLocation;
    public boolean isLocationPressed=false;
    ExpandableLayout searchLayout;
    private LinearLayoutManager mManager;
    private RecyclerView mRecycler;
    private DatabaseReference mDatabase;
    private FirebaseRecyclerAdapter<Profile, SearchViewHolder> mAdapter;

    Spinner serviceList;
    String location;
    String skill;
    private RelativeLayout resulSection;
    AVLoadingIndicatorView loader;
    private TextView searchLog;
    public static  int count=0;
    private ImageView cancelBtn;

    public SearchBarFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_search_bar_fragmrnt, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Profile");
        mRecycler = (RecyclerView) rootView.findViewById(R.id.search_list);
        mRecycler.setHasFixedSize(true);



        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSearchView = (FloatingSearchView) view.findViewById(R.id.floating_search_view);
        mSearchResultsList = (RecyclerView) view.findViewById(R.id.search_results_list);
        go=(ImageButton)view.findViewById(R.id.search_btn);
        addLocation=(ImageButton)view.findViewById(R.id.location_btn) ;

        loader=(AVLoadingIndicatorView)view.findViewById(R.id.loader);
        //loader.hide();
        //loader.show();

        searchLayout = (ExpandableLayout)view.findViewById(R.id.expandableLayout);
        searchLayout.collapse(true);

        resulSection=(RelativeLayout) view.findViewById(R.id.result_section);
        searchLog=(TextView)view.findViewById(R.id.search_log);
        cancelBtn=(ImageView)view.findViewById(R.id.cancel);


        selectService(view);
        playWithLocation();
        setupFloatingSearch();
        setupDrawer();

        if(isCalled==false){
            InitializeRecom();
        }



        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skill=serviceList.getSelectedItem().toString();

                    resulSection.setVisibility(View.VISIBLE);


                    if(searchLayout.isExpanded()) {
                        searchLayout.collapse(true);

                    }

                    loader.show();
                    searchLog.setText("Loading");

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ShowResult(getQuery(mDatabase));
                            searchLog.setText("Result for Your search");
                        }
                    }, 2000);
            }
        });


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mRecycler.setAdapter(null);
                //resulSection.setVisibility(View.GONE);
                searchLog.setText("Loading");

                loader.show();


                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ShowResult(getQueryForRecom(mDatabase));
                            searchLog.setText("Showing Recommended Service Provider");
                        }
                    }, 2000);




                    //searchLayout.collapse(true);
                }
        });






    }


    public void playWithLocation(){
        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resulSection.setVisibility(View.GONE);

                if(searchLayout.isExpanded()){
                    searchLayout.collapse(true);
                    mRecycler.setAdapter(null);
                }
                else {
                    searchLayout.expand(true);
                    mRecycler.setAdapter(null);
                    //resulSection.setVisibility(View.GONE);
                }
            }
        });
    }






    private void setupFloatingSearch() {
        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {

            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {




                if (!oldQuery.equals("") && newQuery.equals("")) {
                    mSearchView.clearSuggestions();
                } else {

                    mSearchView.showProgress();

                    //simulates a query call to a data source
                    //with a new query.
                    DataHelper.findSuggestions(getActivity(), newQuery, 5,
                            250, new DataHelper.OnFindSuggestionsListener() {

                                @Override
                                public void onResults(List<LocationSuggetion> results) {


                                    mSearchView.swapSuggestions(results);

                                    //let the users know that the background
                                    //process has completed
                                    mSearchView.hideProgress();
                                }
                            });
                }

                Log.d(TAG, "onSearchTextChanged()");
            }
        });

        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {

                LocationSuggetion suggetion =(LocationSuggetion)searchSuggestion;
                location=suggetion.getBody();


                //save to history
                for(int i=0; i<DataHelper.sLocation.size();i++){
                    if(suggetion.getBody().equals(DataHelper.sLocation.get(i).getBody())){
                        DataHelper.sLocation.get(i).setIsHistory(true);
                    }
                }
                mLastQuery = searchSuggestion.getBody();
            }

            @Override
            public void onSearchAction(String query) {



                //Toast.makeText(MainActivity.mainContext,query,Toast.LENGTH_SHORT);
                Log.d(TAG, "onSearchAction()");
                //textView.setText("Action:"+query);
                /*mLastQuery = query;

                DataHelper.findColors(getActivity(), query,
                        new DataHelper.OnFindColorsListener() {

                            @Override
                            public void onResults(List<ColorWrapper> results) {
                                mSearchResultsAdapter.swapData(results);
                            }

                        });
                Log.d(TAG, "onSearchAction()");*/
            }
        });

        mSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {

                //show suggestions when search bar gains focus (typically history suggestions)
                mSearchView.swapSuggestions(DataHelper.getHistory(getActivity(), 8));

                Log.d(TAG, "onFocus()");
            }

            @Override
            public void onFocusCleared() {

                //set the title of the bar so that when focus is returned a new query begins
               // mSearchView.setSearchBarTitle(mLastQuery);

                //you can also set setSearchText(...) to make keep the query there when not focused and when focus returns
                //mSearchView.setSearchText(searchSuggestion.getBody());

                Log.d(TAG, "onFocusCleared()");
            }
        });


        //handle menu clicks the same way as you would
        //in a regular activity
      /*  mSearchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {

                if (item.getItemId() == R.id.action_change_colors) {

                    mIsDarkSearchTheme = true;

                    //demonstrate setting colors for items
                    mSearchView.setBackgroundColor(Color.parseColor("#787878"));
                    mSearchView.setViewTextColor(Color.parseColor("#e9e9e9"));
                    mSearchView.setHintTextColor(Color.parseColor("#e9e9e9"));
                    mSearchView.setActionMenuOverflowColor(Color.parseColor("#e9e9e9"));
                    mSearchView.setMenuItemIconColor(Color.parseColor("#e9e9e9"));
                    mSearchView.setLeftActionIconColor(Color.parseColor("#e9e9e9"));
                    mSearchView.setClearBtnColor(Color.parseColor("#e9e9e9"));
                    mSearchView.setDividerColor(Color.parseColor("#BEBEBE"));
                    mSearchView.setLeftActionIconColor(Color.parseColor("#e9e9e9"));
                } else {

                    //just print action
                    Toast.makeText(getActivity().getApplicationContext(), item.getTitle(),
                            Toast.LENGTH_SHORT).show();
                }

            }
        });*/

        //use this listener to listen to menu clicks when app:floatingSearch_leftAction="showHome"
        mSearchView.setOnHomeActionClickListener(new FloatingSearchView.OnHomeActionClickListener() {
            @Override
            public void onHomeClicked() {

                Log.d(TAG, "onHomeClicked()");
            }
        });

        /*
         * Here you have access to the left icon and the text of a given suggestion
         * item after as it is bound to the suggestion list. You can utilize this
         * callback to change some properties of the left icon and the text. For example, you
         * can load the left icon images using your favorite image loading library, or change text color.
         *
         *
         * Important:
         * Keep in mind that the suggestion list is a RecyclerView, so views are reused for different
         * items in the list.
         */
        mSearchView.setOnBindSuggestionCallback(new SearchSuggestionsAdapter.OnBindSuggestionCallback() {
            @Override
            public void onBindSuggestion(View suggestionView, ImageView leftIcon,
                                         TextView textView, SearchSuggestion item, int itemPosition) {
                LocationSuggetion colorSuggestion = (LocationSuggetion) item;

                String textColor = mIsDarkSearchTheme ? "#ffffff" : "#000000";
                String textLight = mIsDarkSearchTheme ? "#bfbfbf" : "#787878";

                if (colorSuggestion.getIsHistory()) {
                    leftIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
                            R.drawable.ic_history_black_24dp, null));

                    Util.setIconColor(leftIcon, Color.parseColor(textColor));
                    leftIcon.setAlpha(.36f);
                } else {
                    leftIcon.setAlpha(0.0f);
                    leftIcon.setImageDrawable(null);
                }

                textView.setTextColor(Color.parseColor(textColor));
                String text = colorSuggestion.getBody()
                        .replaceFirst(mSearchView.getQuery(),
                                "<font color=\"" + textLight + "\">" + mSearchView.getQuery() + "</font>");
                textView.setText(Html.fromHtml(text));
            }

        });

        //listen for when suggestion list expands/shrinks in order to move down/up the
        //search results list
        mSearchView.setOnSuggestionsListHeightChanged(new FloatingSearchView.OnSuggestionsListHeightChanged() {
            @Override
            public void onSuggestionsListHeightChanged(float newHeight) {
                mSearchResultsList.setTranslationY(newHeight);
            }
        });

        /*
         * When the user types some text into the search field, a clear button (and 'x' to the
         * right) of the search text is shown.
         *
         * This listener provides a callback for when this button is clicked.
         */
        mSearchView.setOnClearSearchActionListener(new FloatingSearchView.OnClearSearchActionListener() {
            @Override
            public void onClearSearchClicked() {



                Log.d(TAG, "onClearSearchClicked()");
            }
        });
    }

   /* private void setupResultsList() {
        mSearchResultsAdapter = new SearchResultsListAdapter();
        mSearchResultsList.setAdapter(mSearchResultsAdapter);
        mSearchResultsList.setLayoutManager(new LinearLayoutManager(getContext()));
    }*/

    @Override
    public boolean onActivityBackPress() {
        //if mSearchView.setSearchFocused(false) causes the focused search
        //to close, then we don't want to close the activity. if mSearchView.setSearchFocused(false)
        //returns false, we know that the search was already closed so the call didn't change the focus
        //state and it makes sense to call supper onBackPressed() and close the activity


        if (!mSearchView.setSearchFocused(false)) {
            return false;
        }
        return true;
    }

    private void setupDrawer() {
        attachSearchViewActivityDrawer(mSearchView);
    }


    private void selectService(View view)
    {
         final String[] COUNTRIES = new String[] {
                "--What are u looking for?","Electritian", "Mechanic", "Household Worker", "Sanitary Worker", "Carpenter","Internet Service Provider"
        };

        ArrayAdapter<CharSequence> langAdapter = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_dropdown_item, COUNTRIES );
        //langAdapter.setDropDownViewResource(R.layout.spinner_drop_down);
        serviceList = (Spinner)
                view.findViewById(R.id.spinner_service_list);

        serviceList.setAdapter(langAdapter);


    }


    public final void ShowResult(Query query)
    {

        count=0;
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);



        // Set up FirebaseRecyclerAdapter with the Query
        Query postsQuery = getQuery(mDatabase);


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
        loader.hide();



        //searchLog.setText("Showing Result for Your Search");


    }


    public  Query getQuery(DatabaseReference databaseReference){

        /*Query query=databaseReference.orderByChild("sl").
                startAt("Electritian:Azimpur,Dhaka").endAt("Electritian:Azimpur,Dhaka:5.0:");*/

        if(location==null){
            location="Azimpur,Dhaka";
        }
        Query query=databaseReference.orderByChild("sl").startAt(skill+":"+location).
                endAt(skill+":"+location+":5.0:");



        return query;
    }

    public  Query getQueryForRecom(DatabaseReference databaseReference){

        /*Query query=databaseReference.orderByChild("sl").
                startAt("Electritian:Azimpur,Dhaka").endAt("Electritian:Azimpur,Dhaka:5.0:");*/

        if(location==null){
            location="Azimpur,Dhaka";
        }

        Query query=databaseReference.child("location").startAt(location).endAt(location);

        return query;
    }


    void InitializeRecom(){


        loader.show();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ShowResult(getQueryForRecom(mDatabase));
                searchLog.setText("Showing Recommended Service Provider");
                isCalled=true;
            }
        }, 2000);



    }


}