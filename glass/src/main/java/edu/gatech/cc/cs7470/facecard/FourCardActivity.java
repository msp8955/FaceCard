package edu.gatech.cc.cs7470.facecard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.View;

import com.google.android.glass.touchpad.Gesture;

/**
 * Created by Yuanzhe on 3/28/15.
 */
public class FourCardActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_four_views);

    }

    public void changGridView(View view) {
        Intent intent = new Intent(this, EightCardActivity.class);
        startActivity(intent);
    }

}
