package edu.gatech.cc.cs7470.facecard.View;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.glass.media.Sounds;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

import java.util.ArrayList;
import java.util.List;

import edu.gatech.cc.cs7470.facecard.Model.FaceCard;
import edu.gatech.cc.cs7470.facecard.R;

/**
 * Created by Yuanzhe on 4/7/15.
 */


public class FourCardsActivity extends BaseActivity {

    private List<View> mCards;

    private CardScrollView mCardScrollView;
    private FourCardScrollAdapter mAdapter;
    private GestureDetector mGestureDetector;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupCard();

        mCardScrollView = new CardScrollView(this);
        mAdapter = new FourCardScrollAdapter();
        mCardScrollView.setAdapter(mAdapter);
        mCardScrollView.activate();

        //adapter = new ArrayAdapter(this, CardScrollActivity.class);
        mGestureDetector = createGestureDetector(this);
        setContentView(mCardScrollView);
    }

    @Override
    public void setupCard() {
        mCards = new ArrayList<View>();
        int counter = 0;
        for(int i=0; i<faceCards.size()/4; i++){
            FaceCard[] fourCards = new FaceCard[4];
            for(int j=0; j<4; j++){
                //add facecards
                fourCards[j] = faceCards.get(counter);
                counter++;
                Log.d("facecard", counter + "");
            }
            addCards(fourCards);
        }
        if(faceCards.size()%4 != 0) {
            FaceCard[] fourCards = new FaceCard[4];
            for (int i = 0; i < 4; i++) {
                if (i < faceCards.size() % 4) {
                    //add facecards
                    fourCards[i] = faceCards.get(counter);
                }else{
                    //TODO
                    //add empty cards
                    fourCards[i] = new FaceCard("","","","");
                }
                Log.d("facecard", counter + "");
                counter++;
            }
            addCards(fourCards);
        }

    }

    @Override
    public void addCards(FaceCard[] fourCards){
        CardBuilder cb = new CardBuilder(this, CardBuilder.Layout.EMBED_INSIDE)
                .setEmbeddedLayout(R.layout.grid_four_views);
        View view = cb.getView();

        //four card
        ImageView four_img_1 = (ImageView) view.findViewById(R.id.four_image_1);
        four_img_1.setImageBitmap(fourCards[0].getProfilePicture());
        TextView four_name_1 = (TextView) view.findViewById(R.id.four_name_1);
        four_name_1.setText(fourCards[0].getName());
        TextView four_description_1 = (TextView) view.findViewById(R.id.four_description_1);
        four_description_1.setText(fourCards[0].getAccountId());
        TextView four_note_1 = (TextView) view.findViewById(R.id.four_note_1);
        four_note_1.setText(fourCards[0].getTag());

        ImageView four_img_2 = (ImageView) view.findViewById(R.id.four_image_2);
        four_img_2.setImageBitmap(fourCards[1].getProfilePicture());
        TextView four_name_2 = (TextView) view.findViewById(R.id.four_name_2);
        four_name_2.setText(fourCards[1].getName());
        TextView four_description_2 = (TextView) view.findViewById(R.id.four_description_2);
        four_description_2.setText(fourCards[1].getAccountId());
        TextView four_note_2 = (TextView) view.findViewById(R.id.four_note_2);
        four_note_2.setText(fourCards[1].getTag());

        ImageView four_img_3 = (ImageView) view.findViewById(R.id.four_image_3);
        four_img_3.setImageBitmap(fourCards[2].getProfilePicture());
        TextView four_name_3 = (TextView) view.findViewById(R.id.four_name_3);
        four_name_3.setText(fourCards[2].getName());
        TextView four_description_3 = (TextView) view.findViewById(R.id.four_description_3);
        four_description_3.setText(fourCards[2].getAccountId());
        TextView four_note_3 = (TextView) view.findViewById(R.id.four_note_3);
        four_note_3.setText(fourCards[2].getTag());

        ImageView four_img_4 = (ImageView) view.findViewById(R.id.four_image_4);
        four_img_4.setImageBitmap(fourCards[3].getProfilePicture());
        TextView four_name_4 = (TextView) view.findViewById(R.id.four_name_4);
        four_name_4.setText(fourCards[3].getName());
        TextView four_description_4 = (TextView) view.findViewById(R.id.four_description_4);
        four_description_4.setText(fourCards[3].getAccountId());
        TextView four_note_4 = (TextView) view.findViewById(R.id.four_note_4);
        four_note_4.setText(fourCards[3].getTag());

        mCards.add(view);
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
                    Intent intent = new Intent(getApplicationContext(), EightCardsActivity.class);
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

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        if (mGestureDetector != null) {
            return mGestureDetector.onMotionEvent(event);
        }
        return false;
    }

    private class FourCardScrollAdapter extends CardScrollAdapter {

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

//        @Override
//        public int getItemViewType(int position) {
//            return mCards.get(position).getItemViewType();
//        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
//            return mCards.get(position).getView(convertView, parent);
            return mCards.get(position);
        }
//        public void insertCardWithoutNotification(int position, CardBuilder card) {
//            mCards.add(position, card);
//        }

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
