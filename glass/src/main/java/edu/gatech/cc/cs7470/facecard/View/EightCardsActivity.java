package edu.gatech.cc.cs7470.facecard.View;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
public class EightCardsActivity extends BaseActivity {

    private static final String TAG = "EightCardsActivity";

    private List<View> mCards;

    private CardScrollView mCardScrollView;
    private EightCardScrollAdapter mAdapter;
    private GestureDetector mGestureDetector;


    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setupCard();

        mCardScrollView = new CardScrollView(this);
        mAdapter = new EightCardScrollAdapter();
        mCardScrollView.setAdapter(mAdapter);
        mCardScrollView.activate();

        mGestureDetector = createGestureDetector(this);
        setContentView(mCardScrollView);
    }

    @Override
    public void setupCard() {
        Log.d(TAG, "setupCard");
        Log.d(TAG, "faceCards length: " + faceCards.size());
        mCards = new ArrayList<View>();
        int counter = 0;
        for(int i=0; i<faceCards.size()/8; i++){
            FaceCard[] eightCards = new FaceCard[8];
            Bitmap[] images = new Bitmap[8];
            for(int j=0; j<8; j++){
                //add facecards
                eightCards[j] = faceCards.get(counter);
                images[j] = faceCardImages.get(counter);
                counter++;
                Log.d("facecard", counter + "");
            }
            addCards(eightCards, images);
        }
        if(faceCards.size()%8 != 0) {
            FaceCard[] eightCards = new FaceCard[8];
            Bitmap[] images = new Bitmap[8];
            for (int i = 0; i < 8; i++) {
                if (i < faceCards.size() % 8) {
                    //add facecards
                    eightCards[i] = faceCards.get(counter);
                    images[i] = faceCardImages.get(counter);
                }else{
                    //add empty cards
                    eightCards[i] = new FaceCard("","","","","");
                    Bitmap.Config conf = Bitmap.Config.ARGB_8888;
                    images[i] = Bitmap.createBitmap(100, 100, conf);
                }
                Log.d("facecard", counter + "");
                counter++;
            }
            addCards(eightCards, images);
        }

    }

    @Override
    public void addCards(FaceCard[] eightCards, Bitmap[] images){
        Log.d(TAG, "addCards");

        //eight card
        CardBuilder cb = new CardBuilder(this, CardBuilder.Layout.EMBED_INSIDE)
                .setEmbeddedLayout(R.layout.grid_eight_views);
        View view = cb.getView();

        //eight card
        ImageView eight_img_1 = (ImageView) view.findViewById(R.id.eight_image_1);
        eight_img_1.setImageBitmap(images[0]);
        TextView eight_name_1 = (TextView) view.findViewById(R.id.eight_name_1);
        eight_name_1.setText(eightCards[0].getName());

        ImageView eight_img_2 = (ImageView) view.findViewById(R.id.eight_image_2);
        eight_img_2.setImageBitmap(images[1]);
        TextView eight_name_2 = (TextView) view.findViewById(R.id.eight_name_2);
        eight_name_2.setText(eightCards[1].getName());

        ImageView eight_img_3 = (ImageView) view.findViewById(R.id.eight_image_3);
        eight_img_3.setImageBitmap(images[2]);
        TextView eight_name_3 = (TextView) view.findViewById(R.id.eight_name_3);
        eight_name_3.setText(eightCards[2].getName());

        ImageView eight_img_4 = (ImageView) view.findViewById(R.id.eight_image_4);
        eight_img_4.setImageBitmap(images[3]);
        TextView eight_name_4 = (TextView) view.findViewById(R.id.eight_name_4);
        eight_name_4.setText(eightCards[3].getName());

        ImageView eight_img_5 = (ImageView) view.findViewById(R.id.eight_image_5);
        eight_img_5.setImageBitmap(images[4]);
        TextView eight_name_5 = (TextView) view.findViewById(R.id.eight_name_5);
        eight_name_5.setText(eightCards[4].getName());

        ImageView eight_img_6 = (ImageView) view.findViewById(R.id.eight_image_6);
        eight_img_6.setImageBitmap(images[5]);
        TextView eight_name_6 = (TextView) view.findViewById(R.id.eight_name_6);
        eight_name_6.setText(eightCards[5].getName());

        ImageView eight_img_7 = (ImageView) view.findViewById(R.id.eight_image_7);
        eight_img_7.setImageBitmap(images[6]);
        TextView eight_name_7 = (TextView) view.findViewById(R.id.eight_name_7);
        eight_name_7.setText(eightCards[6].getName());

        ImageView eight_img_8 = (ImageView) view.findViewById(R.id.eight_image_8);
        eight_img_8.setImageBitmap(images[7]);
        TextView eight_name_8 = (TextView) view.findViewById(R.id.eight_name_8);
        eight_name_8.setText(eightCards[7].getName());

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
                    finish();
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

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        if (mGestureDetector != null) {
            return mGestureDetector.onMotionEvent(event);
        }
        return false;
    }

    protected void onStart() {
        super.onStart();

    }

    private class EightCardScrollAdapter extends CardScrollAdapter {

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
    }

    protected void onRestart() {
        super.onRestart();
    }

    protected void onResume() {
        super.onResume();
    }

}
