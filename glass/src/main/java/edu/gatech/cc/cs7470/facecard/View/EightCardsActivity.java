package edu.gatech.cc.cs7470.facecard.View;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.glass.media.Sounds;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.google.android.glass.widget.CardBuilder;

import java.util.ArrayList;
import java.util.List;

import edu.gatech.cc.cs7470.facecard.Model.FaceCard;
import edu.gatech.cc.cs7470.facecard.R;

/**
 * Created by Yuanzhe on 4/7/15.
 */
public class EightCardsActivity extends BaseActivity {

    private ListView listView;
    private GestureDetector mGestureDetector;
    private List<CardBuilder> mCards;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_eight_views);
        mGestureDetector = createGestureDetector(this);
        //listView = new ListView(this);
        //setupClickListener();
    }

    @Override
    public void setupCard() {
        mCards = new ArrayList<CardBuilder>();
        int counter = 0;
        //Outer-> Number of eightCards to add
        for(int i=0; i<faceCards.size()/8; i++) {
            FaceCard[] eightCards = new FaceCard[8];
            for(int j=0; j<8; j++){
                //add facecards
                eightCards[j] = faceCards.get(counter);
                counter++;
            }
            addCards(eightCards);
        }
        if(faceCards.size() % 8 != 0) {
            FaceCard[] eightCards = new FaceCard[8];
            for (int i = 0; i < 8; i++) {
                if (i < faceCards.size() % 8) {
                    //add facecards
                    eightCards[i] = faceCards.get(counter);
                } else {
                    //TODO
                    //add empty cards
                }
                counter++;
            }
            addCards(eightCards);
        }
    }



    private void addCards(FaceCard[] eightCards){
        CardBuilder cb = new CardBuilder(this, CardBuilder.Layout.EMBED_INSIDE)
                .setEmbeddedLayout(R.layout.grid_eight_views);
        View view = cb.getView();

        //four card
        ImageView eight_image_1 = (ImageView) view.findViewById(R.id.eight_image_1);
        eight_image_1.setImageBitmap(eightCards[0].getProfilePicture());
        TextView eight_name_1 = (TextView) view.findViewById(R.id.eight_name_1);
        eight_name_1.setText(eightCards[0].getName());

        ImageView eight_image_2 = (ImageView) view.findViewById(R.id.eight_image_2);
        eight_image_2.setImageBitmap(eightCards[0].getProfilePicture());
        TextView eight_name_2 = (TextView) view.findViewById(R.id.eight_name_2);
        eight_name_2.setText(eightCards[0].getName());

        ImageView eight_image_3 = (ImageView) view.findViewById(R.id.eight_image_3);
        eight_image_3.setImageBitmap(eightCards[0].getProfilePicture());
        TextView eight_name_3 = (TextView) view.findViewById(R.id.eight_name_3);
        eight_name_3.setText(eightCards[0].getName());

        ImageView eight_image_4 = (ImageView) view.findViewById(R.id.eight_image_4);
        eight_image_4.setImageBitmap(eightCards[0].getProfilePicture());
        TextView eight_name_4 = (TextView) view.findViewById(R.id.eight_name_4);
        eight_name_4.setText(eightCards[0].getName());

        ImageView eight_image_5 = (ImageView) view.findViewById(R.id.eight_image_5);
        eight_image_5.setImageBitmap(eightCards[0].getProfilePicture());
        TextView eight_name_5 = (TextView) view.findViewById(R.id.eight_name_5);
        eight_name_5.setText(eightCards[0].getName());

        ImageView eight_image_6 = (ImageView) view.findViewById(R.id.eight_image_6);
        eight_image_6.setImageBitmap(eightCards[0].getProfilePicture());
        TextView eight_name_6 = (TextView) view.findViewById(R.id.eight_name_6);
        eight_name_6.setText(eightCards[0].getName());

        ImageView eight_image_7 = (ImageView) view.findViewById(R.id.eight_image_7);
        eight_image_7.setImageBitmap(eightCards[0].getProfilePicture());
        TextView eight_name_7 = (TextView) view.findViewById(R.id.eight_name_7);
        eight_name_7.setText(eightCards[0].getName());

        ImageView eight_image_8 = (ImageView) view.findViewById(R.id.eight_image_8);
        eight_image_8.setImageBitmap(eightCards[0].getProfilePicture());
        TextView eight_name_8 = (TextView) view.findViewById(R.id.eight_name_8);
        eight_name_8.setText(eightCards[0].getName());

        mCards.add(cb);

    }

    @Override
    public void addCard(FaceCard faceCard) {

    }

    private GestureDetector createGestureDetector(Context context) {
        GestureDetector gestureDetector = new GestureDetector(context);
        //Create a base listener for generic gestures
        gestureDetector.setBaseListener( new GestureDetector.BaseListener() {
            @Override
            public boolean onGesture(Gesture gesture) {
                if (gesture == Gesture.TAP) {
                    // do something on
                    Log.d("test", "click");
                    AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    am.playSoundEffect(Sounds.TAP);
                    //finish();
                    Intent intent = new Intent(getApplicationContext(), CardScrollActivity.class);
                    startActivity(intent);

                    return true;
                } else if (gesture == Gesture.TWO_TAP) {
                    // do something on two finger tap
                    return true;
                } else if (gesture == Gesture.SWIPE_RIGHT) {
                    // do something on right (forward) swipe
                    return true;
                } else if (gesture == Gesture.SWIPE_LEFT) {
                    // do something on left (backwards) swipe
                    return true;
                }
                return false;
            }
        });
        gestureDetector.setFingerListener(new GestureDetector.FingerListener() {
            @Override
            public void onFingerCountChanged(int previousCount, int currentCount) {
                // do something on finger count changes
            }
        });
        gestureDetector.setScrollListener(new GestureDetector.ScrollListener() {
            @Override
            public boolean onScroll(float displacement, float delta, float velocity) {
                // do something on scrolling
                return false;
            }
        });
        return gestureDetector;
    }

    private void setupClickListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                am.playSoundEffect(Sounds.TAP);
                finish();
                Intent intent = new Intent(getApplicationContext(), CardScrollActivity.class);
                startActivity(intent);
            }
        });
    }

    protected void onStart() {
        super.onStart();

    }

    protected void onRestart() {
        super.onRestart();
    }

    protected void onResume() {
        super.onResume();
    }

}
