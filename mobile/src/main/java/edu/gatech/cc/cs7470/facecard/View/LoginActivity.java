package edu.gatech.cc.cs7470.facecard.View;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.SignInButton;

import edu.gatech.cc.cs7470.facecard.Constants;
import edu.gatech.cc.cs7470.facecard.R;

/**
 * Created by miseonpark on 2/24/15.
 */
public class LoginActivity extends Activity {

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


}
