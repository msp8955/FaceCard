package edu.gatech.cc.cs7470.facecard.View;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import com.google.android.glass.media.Sounds;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import edu.gatech.cc.cs7470.facecard.View.FourCardsActivity;
import edu.gatech.cc.cs7470.facecard.Model.FaceCard;
import edu.gatech.cc.cs7470.facecard.R;

/**
 * Created by Yuanzhe on 3/25/15.
 */

public class CardScrollActivity extends BaseActivity {

    private static final String TAG = "CardScrollActivity";

    private List<CardBuilder> mCards;
    private CardScrollView mCardScrollView;
    private ExampleCardScrollAdapter mAdapter;
    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupCard();
        Log.d(TAG, mCards.toString());
        mCardScrollView = new CardScrollView(this);
        mAdapter = new ExampleCardScrollAdapter();
        mCardScrollView.setAdapter(mAdapter);
        mCardScrollView.activate();

        setupClickListener();
        setContentView(mCardScrollView);

    }

    @Override
    public void setupCard() {
        mCards = new ArrayList<CardBuilder>();
        mCards.add(new CardBuilder(this, CardBuilder.Layout.TEXT)
                .setText("Version1.1 @FaceCard")
                .setFootnote("Swiping Cards"));

        for(FaceCard fc : faceCards){
            mCards.add(new CardBuilder(this, CardBuilder.Layout.AUTHOR)
                    .setText(fc.getTag())
                    .setIcon(fc.getProfilePicture())
                    .setHeading(fc.getName())
                    .setSubheading(fc.getAccountId())
                    .setTimestamp("just now"));
        }
    }

    @Override
    public void addCard(FaceCard faceCard) {
        mCards.add(new CardBuilder(this, CardBuilder.Layout.AUTHOR)
                .setText(faceCard.getTag())
                .setIcon(faceCard.getProfilePicture())
                .setHeading(faceCard.getName())
                .setSubheading(faceCard.getAccountId())
                .setTimestamp("just now"));

    }

    private void setupClickListener() {
        mCardScrollView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                am.playSoundEffect(Sounds.TAP);
                Intent intent = new Intent(getApplicationContext(), FourCardsActivity.class);
                startActivity(intent);
            }
        });
    }



//    private GestureDetector createGestureDetector(Context context) {
//        GestureDetector gestureDetector = new GestureDetector((GestureDetector.OnGestureListener) context);
//        gestureDetector.setBaseListener( new com.google.android.glass.touchpad.GestureDetector.BaseListener() {
//            @Override
//            public boolean onGesture(Gesture gesture) {
//                if (gesture == Gesture.LONG_PRESS || gesture == Gesture.TAP) {
//                    Log.d(TAG, "Tap"); //When I tap the touch panel, I only get LONG_PRESS
//                    openOptionsMenu();
//                    return true;
//                } else if (gesture == Gesture.TWO_TAP) {
//                    Intent twoIntent = new Intent(this, FourCardsActivity.class);
//                    startActivity(twoIntent);
//                    return true;
//                } else if (gesture == Gesture.SWIPE_RIGHT) {
//                    return true;
//                } else if (gesture == Gesture.SWIPE_LEFT) {
//
//                    return true;
//                }
//                return false;
//            }
//        });
//        return gestureDetector;
//    }

    private class ExampleCardScrollAdapter extends CardScrollAdapter {

        @Override
        public int getPosition(Object item) {
            return mCards.indexOf(item);
        }

        @Override
        public int getCount() {
            return mCards.size();
        }

        @Override
        public Object getItem(int position) {
            return mCards.get(position);
        }

        @Override
        public int getViewTypeCount() {
            return CardBuilder.getViewTypeCount();
        }

        @Override
        public int getItemViewType(int position) {
            return mCards.get(position).getItemViewType();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return mCards.get(position).getView(convertView, parent);
        }
        public void insertCardWithoutNotification(int position, CardBuilder card) {
            mCards.add(position, card);
        }

    }

    private void insertNewCard(int position, CardBuilder card) {
        mAdapter.insertCardWithoutNotification(position, card);
        mCardScrollView.animate(position, CardScrollView.Animation.INSERTION);
    }
}
