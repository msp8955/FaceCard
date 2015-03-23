package edu.gatech.cc.cs7470.facecard.View.activities;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;

import edu.gatech.cc.cs7470.facecard.R;
import edu.gatech.cc.cs7470.facecard.View.fragments.FriendListFragment;
import edu.gatech.cc.cs7470.facecard.View.fragments.MainFragment;
import edu.gatech.cc.cs7470.facecard.View.fragments.NavigationDrawerFragment;

public class MainActivity extends BaseActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private static final String TAG = "FaceCard LoginActivity";

    /**
     * Fragment
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private FragmentManager fm;
    private FragmentTransaction ft;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MainFragment()).commit();
        }

        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();

//        tv_profile_description = (TextView)findViewById(R.id.profile_description);
//        tv_profile_organization = (TextView)findViewById(R.id.profile_organization);
//        iv_profile_picture = (ImageView)findViewById(R.id.profile_picture);
//        ll_profile_background = (LinearLayout)findViewById(R.id.profile_background);

        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        Log.d(TAG, "onCreate");
    }

    @Override
    protected void onSignedOut() {
        Log.d(TAG, "onSignedOut Start");
        finish();
        Intent myIntent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(myIntent);
        Log.d(TAG, "onSignedOut End");
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch(position){
            case 1:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, MainFragment.newInstance())
                        .commit();
                break;
            case 2:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, FriendListFragment.newInstance())
                        .commit();
                break;
            case 3:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, MainFragment.newInstance())
                        .commit();
            case 4:
                //logout
                if(signOutFromGplus()){
                    onSignedOut();
                }else{
                    //Throw Error Message
                }
//                fragmentManager.beginTransaction()
//                        .replace(R.id.container, PlaceholderFragment.newInstance(position + 1, mGoogleApiClient))
//                        .commit();
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
                if(signOutFromGplus()){
                    onSignedOut();
                }else{
                    //Throw Error Message
                }
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

}