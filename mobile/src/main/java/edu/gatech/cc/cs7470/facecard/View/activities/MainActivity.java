package edu.gatech.cc.cs7470.facecard.View.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import edu.gatech.cc.cs7470.facecard.Constants;
import edu.gatech.cc.cs7470.facecard.Controller.receivers.BluetoothReceiver;
import edu.gatech.cc.cs7470.facecard.Controller.tasks.BluetoothCommunicationTask;
import edu.gatech.cc.cs7470.facecard.Controller.tasks.RegisterBluetoothTask;
import edu.gatech.cc.cs7470.facecard.Controller.utils.BluetoothUtil;
import edu.gatech.cc.cs7470.facecard.Model.Bluetooth;
import edu.gatech.cc.cs7470.facecard.Model.Profile;
import edu.gatech.cc.cs7470.facecard.R;
import edu.gatech.cc.cs7470.facecard.View.fragments.FriendListFragment;
import edu.gatech.cc.cs7470.facecard.View.fragments.MainFragment;
import edu.gatech.cc.cs7470.facecard.View.fragments.NavigationDrawerFragment;

public class MainActivity extends BaseActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private static final String TAG = "FaceCard MainActivity";

    /**
     * Fragment
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private int currentNavigationFragment;

    private Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        currentNavigationFragment = 0;

//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.container, new MainFragment()).commit();
//        }

//        tv_profile_description = (TextView)findViewById(R.id.profile_description);
//        tv_profile_organization = (TextView)findViewById(R.id.profile_organization);
//        iv_profile_picture = (ImageView)findViewById(R.id.profile_picture);
//        ll_profile_background = (LinearLayout)findViewById(R.id.profile_background);

        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        mNavigationDrawerFragment.setMenuVisibility(false);

        Log.d(TAG, "onCreate");
    }

    @Override
    public void onConnected(Bundle arg) {
        // Reaching onConnected means we consider the user signed in.
        super.onConnected(arg);
        Log.i(TAG, "onConnected");

        if (mGoogleApiClient.isConnected() && Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
            profile = new Profile(Plus.PeopleApi.getCurrentPerson(mGoogleApiClient),
                    Plus.AccountApi.getAccountName(mGoogleApiClient));
        }else{
            Log.d(TAG, "onConnected profile not created");
        }

//        //check for bluetooth-glass
//        SharedPreferences prefs = getSharedPreferences(Constants.PACKAGE_NAME, MODE_PRIVATE);
//        if(!prefs.contains(Constants.SHARED_PREFERENCES_GLASS)){
//            //TODO
//        }
        //check for bluetooth registration
        SharedPreferences prefs = getSharedPreferences(Constants.PACKAGE_NAME, MODE_PRIVATE);
        if(!prefs.contains(Constants.SHARED_PREFERENCES_BLUETOOTH)){
            registerBluetooth();
        }

        BluetoothCommunicationTask task = new BluetoothCommunicationTask(this);
        task.connectToGlass();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new MainFragment()).commit();

    }

    @Override
    public void onConnectionSuspended(int cause) {
        super.onConnectionSuspended(cause);
        onSignedOut();
    }

    @Override
    protected void onSignedOut() {
        Log.d(TAG, "onSignedOut Start");
        //unregister bluetooth receiver
        BluetoothReceiver alarm = new BluetoothReceiver();
        alarm.cancelAlarm(getApplicationContext());

        //change to login screen
        finish();
        Intent myIntent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(myIntent);
        Log.d(TAG, "onSignedOut End");
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        Log.d(TAG, "navigation position " + position);
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch(position){
            case 0:
                if(currentNavigationFragment!=position) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, MainFragment.newInstance())
                            .commit();
                    currentNavigationFragment = position;
                }
                break;
            case 1:
                if(currentNavigationFragment!=position) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, FriendListFragment.newInstance())
                            .commit();
                    currentNavigationFragment = position;
                }
                break;
            case 2:
                if(currentNavigationFragment!=position) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, MainFragment.newInstance())
                            .commit();
                    currentNavigationFragment = position;
                }
            case 3:
                //logout
                signOutFromGplus();
                break;
            default:
                break;
        }

    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.profile);
                break;
            case 2:
                mTitle = getString(R.string.friends);
                break;
            case 3:
                mTitle = getString(R.string.settings);
            case 4:
                //logout
                signOutFromGplus();
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public Profile getProfile(){
        return this.profile;
    }

    private void registerBluetooth(){

        final String uuid = (new BluetoothUtil()).getBluetoothId();
        profile.setBluetoothInfo(new Bluetooth(uuid,profile.getEmail()));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Bluetooth Registration");
        builder.setMessage("You have to register your Bluetooth device to use the application.\n" + uuid);
        //Yes
        builder.setPositiveButton("Register", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                new RegisterBluetoothTask().execute(profile.getEmail(),
                        profile.getBluetoothInfo().getBluetoothId(), profile.getName(),
                        profile.getTagline());

                //save
                SharedPreferences prefs = getSharedPreferences(Constants.PACKAGE_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(Constants.SHARED_PREFERENCES_BLUETOOTH, uuid);
                editor.commit();

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                signOutFromGplus();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public boolean signOutFromGplus() {
        if (mGoogleApiClient.isConnected()) {
            //handle GoogleApiClient
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
            Log.d(TAG, "signOutFromGplus");
            onSignedOut();
            return true;
        }else{
            onSignedOut();
//            Log.d(TAG, "signOutFromGplus failed");
            return false;
        }
//        try {
//            if (mGoogleApiClient.isConnected()) {
//                SharedPreferences prefs = getSharedPreferences(Constants.PACKAGE_NAME, Context.MODE_PRIVATE);
//                prefs.edit().remove(Constants.SHARED_PREFERENCES_ACCOUNT).commit();
//
//                Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
//                mGoogleApiClient.disconnect();
//                mGoogleApiClient.connect();
//            }
//            return true;
//        } catch(Exception e){
//            return false;
//        }
    }

    /**
     * Revoking access from google
     * */
    private void revokeGplusAccess() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status arg0) {
                            Log.e(TAG, "User access revoked!");
                            mGoogleApiClient.connect();
                            finish();
                            Intent myIntent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(myIntent);
                        }

                    });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy GoogleApiClient disconnected");
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

}