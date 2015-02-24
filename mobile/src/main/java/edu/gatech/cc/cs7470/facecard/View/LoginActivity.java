package edu.gatech.cc.cs7470.facecard.View;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;


import edu.gatech.cc.cs7470.facecard.Constants;
import edu.gatech.cc.cs7470.facecard.R;

/**
 * Created by miseonpark on 2/24/15.
 */
public class LoginActivity extends Activity implements
        ConnectionCallbacks, OnConnectionFailedListener{

    private static final String TAG = "facecard-login";

    private static final int STATE_DEFAULT = 0;
    private static final int STATE_SIGN_IN = 1;
    private static final int STATE_IN_PROGRESS = 2;

    private static final int RC_SIGN_IN = 0;

    private static final int DIALOG_PLAY_SERVICES_ERROR = 0;

    private static final String SAVED_PROGRESS = "sign_in_progress";

    // GoogleApiClient wraps our service connection to Google Play services and
    // provides access to the users sign in state and Google's APIs.
    private GoogleApiClient mGoogleApiClient;

    // We use mSignInProgress to track whether user has clicked sign in.
    // mSignInProgress can be one of three values:
    //
    //       STATE_DEFAULT: The default state of the application before the user
    //                      has clicked 'sign in', or after they have clicked
    //                      'sign out'.  In this state we will not attempt to
    //                      resolve sign in errors and so will display our
    //                      Activity in a signed out state.
    //       STATE_SIGN_IN: This state indicates that the user has clicked 'sign
    //                      in', so resolve successive errors preventing sign in
    //                      until the user has successfully authorized an account
    //                      for our app.
    //   STATE_IN_PROGRESS: This state indicates that we have started an intent to
    //                      resolve an error, and so we should not start further
    //                      intents until the current intent completes.
    private int mSignInProgress;

    // Used to store the PendingIntent most recently returned by Google Play
    // services until the user clicks 'sign in'.
    private PendingIntent mSignInIntent;

    // Used to store the error code most recently returned by Google Play services
    // until the user clicks 'sign in'.
    private int mSignInError;

    private Activity activity;
    private SignInButton btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        activity = this;
        btnSignIn = (SignInButton) findViewById(R.id.sign_in_button);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveAccountPreference("test_id");
                Intent i = new Intent(activity, MainActivity.class);
                startActivity(i);
            }
        });
    }

    private void saveAccountPreference(String id){
        SharedPreferences prefs = getSharedPreferences(Constants.PACKAGE_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.SHARED_PREFERENCES_ACCOUNT, id);
        editor.commit();
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

//    @Override
//    protected Dialog onCreateDialog(int id) {
//        switch(id) {
//            case DIALOG_PLAY_SERVICES_ERROR:
//                if (GooglePlayServicesUtil.isUserRecoverableError(mSignInError)) {
//                    return GooglePlayServicesUtil.getErrorDialog(
//                            mSignInError,
//                            this,
//                            RC_SIGN_IN,
//                            new DialogInterface.OnCancelListener() {
//                                @Override
//                                public void onCancel(DialogInterface dialog) {
//                                    Log.e(TAG, "Google Play services resolution cancelled");
//                                    mSignInProgress = STATE_DEFAULT;
//                                }
//                            });
//                } else {
//                    return new AlertDialog.Builder(this)
//                            .setMessage(R.string.play_services_error)
//                            .setPositiveButton(R.string.close,
//                                    new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            Log.e(TAG, "Google Play services error could not be "
//                                                    + "resolved: " + mSignInError);
//                                            mSignInProgress = STATE_DEFAULT;
//                                        }
//                                    }).create();
//                }
//            default:
//                return super.onCreateDialog(id);
//        }
//    }
}
