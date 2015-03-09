package edu.gatech.cc.cs7470.facecard.View;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import java.io.InputStream;

import edu.gatech.cc.cs7470.facecard.Constants;
import edu.gatech.cc.cs7470.facecard.R;

public class MainActivity extends BaseActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private static final String TAG = "FaceCard LoginActivity";

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

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
                    .add(R.id.container, new PlaceholderFragment()).commit();
        }

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
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private TextView tv_profile_name;
        private TextView tv_profile_tagline;
        private TextView tv_profile_organization;
        private ImageView iv_profile_picture;
        private LinearLayout ll_profile_background;

        //links
        private TextView tv_google_link;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            tv_profile_name = (TextView)rootView.findViewById(R.id.profile_name);
            tv_profile_tagline = (TextView)rootView.findViewById(R.id.profile_tagline);
            tv_profile_organization = (TextView)rootView.findViewById(R.id.profile_organization);
            iv_profile_picture = (ImageView)rootView.findViewById(R.id.profile_picture);
            ll_profile_background = (LinearLayout)rootView.findViewById(R.id.profile_cover);

            //links
            tv_google_link = (TextView)rootView.findViewById(R.id.google_link);
            getProfileInfo();
            return rootView;
        }

        private void getProfileInfo(){
            try {
                if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                    Person person = Plus.PeopleApi
                            .getCurrentPerson(mGoogleApiClient);
                    String profile_name = person.getDisplayName();
                    String profile_tagline = person.getTagline();
                    String profile_organization = person.getOrganizations().get(0).getName();
                    String profile_picture_url = person.getImage().getUrl();
                    String profile_google_plus_url = person.getUrl();
                    String profile_cover_url = "";
                    if(person.hasCover()){
                        if(person.getCover().hasCoverPhoto()) {
                            profile_cover_url = person.getCover().getCoverPhoto().getUrl();
                        }
                    }

                    String profile_email = Plus.AccountApi.getAccountName(mGoogleApiClient);

                    Log.e(TAG, "Name: " + profile_name + ", plusProfile: "
                            + profile_google_plus_url + ", email: " + profile_email
                            + ", Image: " + profile_picture_url
                            + ", Cover: " + profile_cover_url
                            + ", Tagline: " + person.getTagline());

                    tv_profile_name.setText(profile_name);
                    tv_profile_tagline.setText(profile_tagline);
                    tv_profile_organization.setText(profile_organization);

                    //links
                    tv_google_link.setText(profile_google_plus_url);

                    // by default the profile url gives 50x50 px image only
                    // we can replace the value with whatever dimension we want by
                    // replacing sz=X
                    profile_picture_url = profile_picture_url.substring(0,
                            profile_picture_url.length() - 2)
                            + Constants.PROFILE_PIC_SIZE;

                    new LoadProfileImage(iv_profile_picture).execute(profile_picture_url);
                    if(profile_cover_url.length()>0) {
                        new LoadProfileImage(ll_profile_background).execute(profile_cover_url);
                    }

                } else {
//                    Toast.makeText(getApplicationContext(),
//                            "Person information is null", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * Background Async task to load user profile picture from url
         * */
        private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
            ImageView bmImage;
            LinearLayout coverImage;
            RoundImageHelper roundImageHelper;
            boolean isCoverImage;

            public LoadProfileImage(ImageView bmImage) {
                this.bmImage = bmImage;
                this.isCoverImage = false;
                roundImageHelper = new RoundImageHelper();
            }

            public LoadProfileImage(LinearLayout coverImage) {
                this.coverImage = coverImage;
                this.isCoverImage = true;
            }

            protected Bitmap doInBackground(String... urls) {
                String urldisplay = urls[0];
                Bitmap mIcon11 = null;
                try {
                    InputStream in = new java.net.URL(urldisplay).openStream();
                    mIcon11 = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
                return mIcon11;
            }

            protected void onPostExecute(Bitmap result) {
                if(isCoverImage){
                    Drawable background = new BitmapDrawable(result);
                    coverImage.setBackground(background);
                }else {
                    bmImage.setImageBitmap(roundImageHelper.getRoundedCornerBitmap(result, Constants.PROFILE_PIC_RADIUS));
                }
            }
        }
    }

}