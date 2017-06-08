package com.google.firebase.quickstart.database.Auth;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.quickstart.database.MainActivity;
import com.google.firebase.quickstart.database.R;


/**
 * Created by sadiq on 5/23/2017.
 */

public class SignUpAuth {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    Context context;
    Activity activity;

    public SignUpAuth(Context context, Activity activity) {
        this.context=context;
        this.activity=activity;
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                } else {

                }

            }
        };


    }


    public void SignUpWithFirebase(String name,String email,String password,final ProgressBar progressBar){

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.INVISIBLE);

                       if (!task.isSuccessful()) {
                            try {
                                    throw task.getException();
                                } catch(FirebaseAuthInvalidCredentialsException e) {
                                    Toast.makeText(context, R.string.error_invalid_email,
                                            Toast.LENGTH_SHORT).show();
                                } catch(FirebaseAuthUserCollisionException e) {
                                    Toast.makeText(context, R.string.error_email_exit,
                                            Toast.LENGTH_SHORT).show();
                                } catch(Exception e) {
                                    Toast.makeText(context, R.string.others_auth_error,
                                            Toast.LENGTH_SHORT).show();
                                }
                            }


                       else {
                            Toast.makeText(context, R.string.signup_successful,
                                    Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(activity, MainActivity.class);
                            activity.startActivity(intent);
                           activity.finish();

                       }

                    }
                });
    }


    public void SignInWithFirebase(String email, String password, final ProgressBar progressBar){

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.INVISIBLE);

                        if (!task.isSuccessful()) {
                            Toast.makeText(context, R.string.sign_in_error,
                                    Toast.LENGTH_SHORT).show();

                        }
                        else {

                            Toast.makeText(context, "Logged in",
                                    Toast.LENGTH_SHORT).show();

                            Intent intent=new Intent(activity, MainActivity.class);
                            activity.startActivity(intent);
                            activity.finish();
                        }


                    }
                });



    }


}

