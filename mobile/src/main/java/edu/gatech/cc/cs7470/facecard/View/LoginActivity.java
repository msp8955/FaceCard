package edu.gatech.cc.cs7470.facecard.View;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import edu.gatech.cc.cs7470.facecard.R;

/**
 * Created by miseonpark on 2/24/15.
 */
public class LoginActivity extends Activity {

    private Activity activity;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        activity = this;
        btnLogin = (Button) findViewById(R.id.sign_in_button);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(activity, MainActivity.class);
                startActivity(i);
            }
        });
    }


}
