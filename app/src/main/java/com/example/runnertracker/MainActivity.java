package com.example.runnertracker;

//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//import androidx.fragment.app.DialogFragment;
//
//import android.Manifest;
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.pm.PackageManager;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.Chronometer;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.Toast;
//
//import com.firebase.ui.auth.AuthUI;
//import com.firebase.ui.auth.IdpResponse;
//import com.google.firebase.auth.FirebaseAuth;
//
//import java.util.Arrays;
//
//
///*
//TO DO
//    1. Allow user to record a journey which is saved to a database. The path, time, distance, date via gps. (DONE)
//    2. Allow user to stop, start a journey. Stopping a journey causes it to be saved. (DONE)
//    3. Allow user to attach image to a saved journey (DONE)
//    4. Allow user to rate a journey out of 5 (DONE)
//    5. Allow user to add comments about a certain journey (DONE)
//    6. Allow user to see a list of recorded journeys filtered by date (DONE)
//    7. Clicking on a recorded journey displays more information (rating, comments, picture, time, distance, average speed, path on google maps) (DONE)
//    8. Allow user to see statistics page which shows how far ran today, this week, this month, all time and could graph these (DONE)
//    9. Allow user to set a goal for km to run every week, display whether the goal has been reached or not in the app. (DONE)
//    10. Ask user for GPS permissions (DONE)
//    11. broadcast receiver so that when battery is low do fewer GPS requests (DONE)
//    12. Display notification while tracking a journey (DONE)
//
//    Need
//        - Service (for GPS tracking)
//        - Activities (to display stats, journeys, single journeys, recording a journey, home page)
//        - Database (to store journey information)
//        - Content Provider (in order to access the database)
//        - Broadcast Receiver to register callbacks and reduce GPS request frequency on low battery
// */
//public class MainActivity extends AppCompatActivity {
//
//    private static final int PERMISSION_GPS_CODE = 1;
//    private static final int PERMISSION_COAL_GPS_CODE = 2;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//    }
//
////    public void onClickLogin(View v) {
////        // go to login
////        Intent journey = new Intent(MainActivity.this, LoginActivity.class);
////        startActivity(journey);
////    }
//
//    public void onClickRecord(View v) {
//        // go to the record journey activity
//        Intent journey = new Intent(MainActivity.this, RecordJourney.class);
//        startActivity(journey);
//    }
//
//    public void onClickView(View v) {
//        // go to the activity for displaying journeys
//        Intent view = new Intent(MainActivity.this, ViewJourneys.class);
//        startActivity(view);
//    }
//
//    public void onClickStatistics(View v) {
//        // go to the activity for displaying statistics
//        Intent stats = new Intent(MainActivity.this, StatisticsActivity.class);
//        startActivity(stats);
//    }
//}

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.example.runnertracker.R;

import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    public static final int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    String userName = firebaseAuth.getCurrentUser().getDisplayName();
                    showWelcome(userName);
                } else {
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.EmailBuilder().build()))
                                    .build(),
                            RC_SIGN_IN);
                }

            }
        };

    }
    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    private void showWelcome(String userName) {
        String welcome = "Welcome " + userName + " !";
        Toast.makeText(this, welcome, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, HomePageActivity.class);
        startActivity(intent);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                String userName =  FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                String welcome = "Welcome " + userName + " !";
                Toast.makeText(this, welcome, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, HomePageActivity.class);
                startActivity(intent);
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
                Toast.makeText(this, "Sign in failed.",Toast.LENGTH_SHORT).show();

            }
        }
    }
}
