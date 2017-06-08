package com.google.firebase.quickstart.database;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.quickstart.database.Auth.SignUpAuth;
import com.google.firebase.quickstart.database.Painter.GradientBackgroundPainter;


public class Login extends AppCompatActivity {


    private SectionsPagerAdapter mSectionsPagerAdapter;

    public static  int page;
    public static int pos;
    public static ViewPager mViewPager;
    public static Activity lActivity;
    public static Context lcontext;
    GradientBackgroundPainter gradientBackgroundPainter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        lActivity=this;
        lcontext=Login.this;



        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

      /*  TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);*/




        View backgroundImage = findViewById(R.id.root);

        final int[] drawables = new int[6];
        drawables[0] = R.drawable.gradient1;
        drawables[1] = R.drawable.gradient2;
        drawables[2] = R.drawable.gradient3;
        drawables[3]=R.drawable.gradient4;
        drawables[4]=R.drawable.gradient5;
        drawables[5]=R.drawable.gradient6;

        gradientBackgroundPainter = new GradientBackgroundPainter(backgroundImage, drawables);
        gradientBackgroundPainter.start();



    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseAuth auth=FirebaseAuth.getInstance();
        if(auth.getCurrentUser()!=null){
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
    }


    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

       /* @Bind(R.id.input_email)   EditText email;
        @Bind(R.id.input_password) EditText password;
        @Bind(R.id.btn_login)  Button signIn;
        @Bind(R.id.link_signup) TextView _signupLink;*/




        public PlaceholderFragment() {

        }



        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView( LayoutInflater inflater,
                                  ViewGroup container, Bundle savedInstanceState) {


            View rootView;
            page=getArguments().getInt(ARG_SECTION_NUMBER);



            if(page==1) {
                rootView = inflater.inflate(R.layout.fragment_login, container, false);
                SignInActivity(rootView);
                TextView goToSignUp=(TextView)rootView.findViewById(R.id.goto_sign_up);

                goToSignUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(1,true);
                    }
                });


            }
            else {
                rootView =inflater.inflate(R.layout.fragment_signup,container,false);
                SignUpActivity(rootView);
                TextView backTo=(TextView)rootView.findViewById(R.id.back_to_signIn);

                backTo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(0,true);
                    }
                });


            }




            return rootView;
        }



        private  void SignInActivity(final View view){

            final EditText email=(EditText)view.findViewById(R.id.input_email);
            final EditText password=(EditText)view.findViewById(R.id.input_password);
            final Button signIn=(Button)view.findViewById(R.id.btn_sign_in);
            //final ProgressBar progressBar_signIn=(ProgressBar)view.findViewById(R.id.progressBar_signIn);


            signIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String semail=email.getText().toString().trim();
                    String spassword= password.getText().toString().trim();

                    if(semail.isEmpty()){
                        email.setError(null);
                    }
                    else if(spassword.isEmpty()){
                        password.setError(null);
                    }

                    else {
                        //ProgressDialog progressBar_signIn = new ProgressDialog(lcontext, R.style.AppTheme_Dark_Dialog);
                        final ProgressBar progressBar_signIn=(ProgressBar)view.findViewById(R.id.progressBar_signUp);
                        progressBar_signIn.setVisibility(View.VISIBLE);

                        SignUpAuth auth = new SignUpAuth(lcontext, lActivity);
                        auth.SignInWithFirebase(semail, spassword, progressBar_signIn);
                    }
                }
            });
        }



        private void SignUpActivity(final View view) {
            final EditText name= (EditText)view.findViewById(R.id.nameField);
            final Button signUp=(Button)view.findViewById(R.id.btn_sign_in);
            final EditText email=(EditText)view.findViewById(R.id.emailField);
            final EditText password1=(EditText)view.findViewById(R.id.passwordField);
            final EditText password2=(EditText)view.findViewById(R.id.repeatPasswordField);
            final ProgressBar progressBar_signUp=(ProgressBar)view.findViewById(R.id.progressBar_signUp);


            name.setText("Mr.John");
            name.setTextColor(Color.rgb(100,100,100));
            name.setTypeface(null, Typeface.ITALIC);
            name.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(name.getText().toString().equals("Mr.John")) {
                        name.setText("");
                        name.setTextColor(Color.BLACK);
                        name.setTypeface(null,Typeface.NORMAL);
                    }
                    return false;
                }
            });

            email.setText("john1938@gmail.com");
            email.setTextColor(Color.rgb(100,100,100));
            email.setTypeface(null, Typeface.ITALIC);
            email.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(email.getText().toString().equals("john1938@gmail.com")) {
                        email.setText("");
                        email.setTextColor(Color.BLACK);
                        email.setTypeface(null,Typeface.NORMAL);
                    }
                    return false;
                }
            });

            password1.setText("Mr.John");
            password1.setTextColor(Color.rgb(100,100,100));
            password1.setTypeface(null, Typeface.ITALIC);
            password1.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(password1.getText().toString().equals("Mr.John")) {
                        password1.setText("");
                        password1.setTextColor(Color.BLACK);
                        password1.setTypeface(null,Typeface.NORMAL);
                    }
                    return false;
                }
            });

            password2.setText("Mr.John");
            password2.setTextColor(Color.rgb(100,100,100));
            password2.setTypeface(null, Typeface.ITALIC);
            password2.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(password2.getText().toString().equals("Mr.John")) {
                        password2.setText("");
                        password2.setTextColor(Color.BLACK);
                        password2.setTypeface(null,Typeface.NORMAL);
                    }
                    return false;
                }
            });






            signUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String sname=name.getText().toString().trim();
                    String semail=email.getText().toString().trim();
                    String spassword1=password1.getText().toString().trim();
                    String spassword2=password2.getText().toString().trim();

                    boolean isOk=true;

                    if(sname.isEmpty()){
                        name.setError( "Insert Your Name");
                        name.requestFocus();
                        isOk=false;
                    }

                    if(spassword1.length()<6){
                        password1.setError( "Password too short");
                        isOk=false;
                    }

                    if(!spassword1.equals(spassword2)){
                        password2.setError( "Password didn't match");
                        isOk=false;
                    }

                    if(isOk){
                        progressBar_signUp.setVisibility(View.VISIBLE);

                        SignUpAuth auth=new SignUpAuth(lcontext,lActivity);
                        auth.SignUpWithFirebase(sname,semail,spassword1,progressBar_signUp);



                        /*Intent intent=new Intent(lActivity, test.class);
                        startActivity(intent);*/

                    }
                }
            });

        }




    }




    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem( int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "LogIn";
                case 1:
                    return "SignUp";
            }
            return null;
        }
    }

}
