package edu.gatech.cc.cs7470.facecard.View.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;

import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;


import edu.gatech.cc.cs7470.facecard.Constants;
import edu.gatech.cc.cs7470.facecard.Controller.receivers.BluetoothReceiver;
import edu.gatech.cc.cs7470.facecard.R;

/**
 * Created by miseonpark on 2/24/15.
 */
public class LoginActivity extends BaseActivity {

    private static final String TAG = "FaceCard LoginActivity";

    private Activity activity;
    private SignInButton btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        activity = this;

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }

//        SharedPreferences prefs = getSharedPreferences(Constants.PACKAGE_NAME, MODE_PRIVATE);
//        if(prefs.contains(Constants.SHARED_PREFERENCES_ACCOUNT)){
//            resolveSignInError();
//            finish();
//            Intent i = new Intent(activity, MainActivity.class);
//            startActivity(i);
//        }

        btnSignIn = (SignInButton) findViewById(R.id.sign_in_button);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                signInWithGplus();
            }
        });

        if (savedInstanceState != null) {
            mSignInProgress = savedInstanceState
                    .getInt(SAVED_PROGRESS, STATE_DEFAULT);
        }
    }

//    @Override
//    protected void onStart() {
//        Log.d(TAG, "onStart");
//        super.onStart();
//        mGoogleApiClient = buildGoogleApiClient();
//        mGoogleApiClient.connect();
//    }

    @Override
    public void onConnected(Bundle connectionHint){
        Log.d(TAG, "onConnected");
        Person currentUser = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
        Log.d(TAG, currentUser.getDisplayName());
        saveAccountPreference(currentUser.getId());
//        mSignInProgress = STATE_DEFAULT;

        finish();
        Intent i = new Intent(activity, MainActivity.class);
        startActivity(i);
//        if(mSignInProgress == STATE_SIGN_IN) {
//            Person currentUser = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
//            Log.d(TAG, currentUser.getDisplayName());
//            saveAccountPreference(currentUser.getId());
//            mSignInProgress = STATE_DEFAULT;
//
//            // start background service
//            BluetoothReceiver alarm = new BluetoothReceiver();
//            alarm.setAlarm(getApplicationContext());
//
//            finish();
//            Intent i = new Intent(activity, MainActivity.class);
//            startActivity(i);
//        }
//        mSignInProgress = STATE_DEFAULT;
    }

    private void signInWithGplus() {
        if (!mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            resolveSignInError();
        }
    }

    @Override
    protected void onSignedOut() {
        //do nothing
    }

    private void saveAccountPreference(String id){
        SharedPreferences prefs = getSharedPreferences(Constants.PACKAGE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.SHARED_PREFERENCES_ACCOUNT, id);
        editor.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        Log.d(TAG, "onActivityResult");

        if (requestCode == RC_SIGN_IN) {
            if (resultCode != RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }

//        switch (requestCode) {
//            case RC_SIGN_IN:
//                if (resultCode == RESULT_OK) {
//                    // If the error resolution was successful we should continue
//                    // processing errors.
//                    mSignInProgress = STATE_SIGN_IN;
//                } else {
//                    // If the error resolution was not successful or the user canceled,
//                    // we should stop processing errors.
//                    mSignInProgress = STATE_DEFAULT;
//                }
//
//                if (!mGoogleApiClient.isConnecting()) {
//                    // If Google Play services resolved the issue with a dialog then
//                    // onStart is not called so we need to re-attempt connection here.
//                    mGoogleApiClient.connect();
//                }
//                break;
//        }
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        Log.d(TAG, "onDestroy do nothing");
//    }

}
