package edu.gatech.cc.cs7470.facecard.View.fragments;

/**
 * Created by miseonpark on 3/23/15.
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

import java.io.InputStream;

import edu.gatech.cc.cs7470.facecard.Constants;
import edu.gatech.cc.cs7470.facecard.Controller.receivers.BluetoothReceiver;
import edu.gatech.cc.cs7470.facecard.Controller.services.BackgroundService;
import edu.gatech.cc.cs7470.facecard.Controller.services.OnTaskCompleted;
import edu.gatech.cc.cs7470.facecard.Controller.tasks.BluetoothCommunicationTask;
import edu.gatech.cc.cs7470.facecard.Controller.tasks.DiscoverNearbyPeopleTask;
import edu.gatech.cc.cs7470.facecard.Controller.utils.BluetoothUtil;
import edu.gatech.cc.cs7470.facecard.Model.FaceCard;
import edu.gatech.cc.cs7470.facecard.Model.Profile;
import edu.gatech.cc.cs7470.facecard.R;
import edu.gatech.cc.cs7470.facecard.View.activities.MainActivity;
import edu.gatech.cc.cs7470.facecard.View.uihelpers.RoundImageHelper;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment implements OnTaskCompleted{

    private static String TAG = "PlaceholderFragment";

    private static MainActivity activity;
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

    private ToggleButton toggle_update;
    private ToggleButton toggle_demo;
    private TextView tv_device_detected;
    private BluetoothCommunicationTask bluetoothCommunicationTask;
    private boolean isDemo;

    //links
    private TextView tv_google_link;

    private GoogleApiClient mGoogleApiClient;

    private Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String btid = (String)msg.obj;
            new DiscoverNearbyPeopleTask(activity.getApplicationContext(), MainFragment.this, true).execute(btid);
            if(tv_device_detected.getText().length()==0){
                tv_device_detected.setText(btid);
            }else {
                tv_device_detected.setText(tv_device_detected.getText() + ", " + btid);
            }
//            Toast.makeText(activity, btid, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onTaskCompleted(FaceCard[] result){
        Log.d(TAG, "onTaskCompleted");
        if(result!=null){
            for (FaceCard card : result){
                Log.d(TAG, "card: " + card.getName());
                bluetoothCommunicationTask.sendToGlass(card);
                Toast.makeText(activity, "Found: " + card.getName(), Toast.LENGTH_SHORT).show();
            }
//            bluetoothCommunicationTask.sendToGlass(result);
//            connectToGlass();
        }
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static MainFragment newInstance() {
        MainFragment mFragment = new MainFragment();
//        Bundle args = new Bundle();
//        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//        fragment.setArguments(args);
        return mFragment;
    }

    public MainFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");

        activity = (MainActivity) this.getActivity();
        mGoogleApiClient = activity.getGoogleApiClient();

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        tv_profile_name = (TextView)rootView.findViewById(R.id.profile_name);
        tv_profile_tagline = (TextView)rootView.findViewById(R.id.profile_tagline);
        tv_profile_organization = (TextView)rootView.findViewById(R.id.profile_organization);

        iv_profile_picture = (ImageView)rootView.findViewById(R.id.profile_picture);
        ll_profile_background = (LinearLayout)rootView.findViewById(R.id.profile_cover);

        //links
        tv_google_link = (TextView)rootView.findViewById(R.id.google_link);

        //toggle button
        toggle_update = (ToggleButton)rootView.findViewById(R.id.profile_toggle_update);
        toggle_demo = (ToggleButton)rootView.findViewById(R.id.profile_toggle_demo);
        isDemo = false;

        SharedPreferences prefs = activity.getSharedPreferences(Constants.PACKAGE_NAME, activity.MODE_PRIVATE);
        if(prefs.contains(Constants.SHARED_PREFERENCES_ALARM)){
            if(prefs.getBoolean(Constants.SHARED_PREFERENCES_ALARM, false)){
                toggle_update.setChecked(true);
            }else{
                toggle_update.setChecked(false);
            }
        }else{
            toggle_update.setChecked(false);
        }

        toggle_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = activity.getSharedPreferences(Constants.PACKAGE_NAME, activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                if (toggle_update.isChecked()) {
                    if(!prefs.contains(Constants.SHARED_PREFERENCES_ALARM) || !prefs.getBoolean(Constants.SHARED_PREFERENCES_ALARM, false)){
//                    bluetoothCommunicationTask = new BluetoothCommunicationTask(getApplicationContext());
//                    bluetoothCommunicationTask.connectToGlass();
                        //start background service
                        Log.d(TAG, "setting alarm");
//                    Intent i = new Intent(getApplicationContext(), BackgroundService.class);
//                    getApplicationContext().startService(i);
//                    getApplicationContext().bindService(i, myConnection, Context.BIND_AUTO_CREATE);
                        BluetoothReceiver alarm = new BluetoothReceiver();
                        alarm.setAlarm(activity.getApplicationContext());
                        editor.putBoolean(Constants.SHARED_PREFERENCES_ALARM, true);
                        editor.commit();
                    }
                    Toast.makeText(activity, "update on", Toast.LENGTH_SHORT).show();
                } else {
                    BluetoothReceiver alarm = new BluetoothReceiver();
                    alarm.cancelAlarm(activity.getApplicationContext());
                    editor.putBoolean(Constants.SHARED_PREFERENCES_ALARM, false);
                    editor.commit();
                    Toast.makeText(activity, "update off", Toast.LENGTH_SHORT).show();
                }
            }
        });

        toggle_demo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = activity.getSharedPreferences(Constants.PACKAGE_NAME, activity.MODE_PRIVATE);
                if (toggle_demo.isChecked()) {
                    //cancel update mode
                    if(prefs.contains(Constants.SHARED_PREFERENCES_ALARM) && prefs.getBoolean(Constants.SHARED_PREFERENCES_ALARM, false)){
                        toggle_update.toggle();
                        SharedPreferences.Editor editor = prefs.edit();
                        BluetoothReceiver alarm = new BluetoothReceiver();
                        alarm.cancelAlarm(activity.getApplicationContext());
                        editor.putBoolean(Constants.SHARED_PREFERENCES_ALARM, false);
                        editor.commit();
                    }
                    if(!isDemo) {
                        bluetoothCommunicationTask = new BluetoothCommunicationTask(activity.getApplicationContext());
                        bluetoothCommunicationTask.connectToGlass();
                        isDemo = true;
                    }
                    //run demo
                    Intent i=new Intent(activity, BackgroundService.class);
                    i.putExtra(BackgroundService.EXTRA_MESSENGER, new Messenger(handler));
                    getActivity().startService(i);
                    Toast.makeText(activity, "demo on; update mode off", Toast.LENGTH_SHORT).show();
                } else {
//                    bluetoothCommunicationTask.stop();
                    getActivity().stopService(new Intent(getActivity(), BackgroundService.class));
                    Toast.makeText(activity, "demo off", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tv_device_detected = (TextView)rootView.findViewById(R.id.profile_device_detected);

        populateProfileInfo();

        return rootView;
    }



    private void populateProfileInfo(){
        try {
            if (mGoogleApiClient.isConnected() && Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Profile profile = activity.getProfile();

                String profile_picture_url = profile.getProfile_picture_url();
                String profile_cover_url = profile.getProfile_cover_url();

                tv_profile_name.setText(profile.getName());
                tv_profile_tagline.setText(profile.getTagline());
                tv_profile_organization.setText(profile.getOrganization());

                //Links
                tv_google_link.setText(profile.getGoogle_link());

                // by default the profile url gives 50x50 px image only
                // we can replace the value with whatever dimension we want by
                // replacing sz=X
                if(profile_picture_url.length()>0) {
                    profile_picture_url = profile_picture_url.substring(0,
                            profile_picture_url.length() - 2)
                            + Constants.PROFILE_PIC_SIZE;
                    new LoadProfileImage(iv_profile_picture).execute(profile_picture_url);
                }
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
            if(result != null) {
                if (isCoverImage) {
                    Drawable background = new BitmapDrawable(result);
                    coverImage.setBackground(background);
                } else {
                    bmImage.setImageBitmap(roundImageHelper.getRoundedCornerBitmap(result, Constants.PROFILE_PIC_RADIUS));
                }
            }
        }
    }
}