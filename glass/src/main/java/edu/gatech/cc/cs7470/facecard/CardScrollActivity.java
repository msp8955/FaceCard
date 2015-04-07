package edu.gatech.cc.cs7470.facecard;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import com.google.android.glass.media.Sounds;
import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

/**
 * Created by Yuanzhe on 3/25/15.
 */

public class CardScrollActivity extends Activity {

    private static final String TAG = "CardScrollActivity";

    private List<CardBuilder> mCards;
    private CardScrollView mCardScrollView;
    private ExampleCardScrollAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createCards();
        Log.d(TAG, mCards.toString());
        mCardScrollView = new CardScrollView(this);
        mAdapter = new ExampleCardScrollAdapter();
        mCardScrollView.setAdapter(mAdapter);
        mCardScrollView.activate();

        setupClickListener();
        setContentView(mCardScrollView);
    }

    private void setupClickListener() {
        mCardScrollView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                am.playSoundEffect(Sounds.TAP);
            }
        });
    }

    private void createCards() {
        mCards = new ArrayList<CardBuilder>();

//        List<Record> records = getReords();
//
//        for (Record r : records) {
//            mCards.add(new CardBuilder(this, CardBuilder.Layout.AUTHOR)
//                .setText(r.get("first_name")));
//        }


        mCards.add(new CardBuilder(this, CardBuilder.Layout.TEXT)
                .setText("Version1.1 @FaceCard")
                .setFootnote("Swiping Cards"));

        mCards.add(new CardBuilder(this, CardBuilder.Layout.CAPTION)
                .setText("College of Computing, Career Fair")
                .setFootnote("Remainder Lists.")
                .addImage(R.drawable.name_profile_thad));

        mCards.add(new CardBuilder(this, CardBuilder.Layout.AUTHOR)
                .setText("This card has a puppy background image.")
                .setIcon(R.drawable.name_profile_thad)
                .setHeading("Thad Starner")
                .setSubheading("Wearable Computing, Georgia Tech")
                .setFootnote("This is the footnote")
                .setTimestamp("just now"));

        mCards.add(new CardBuilder(this, CardBuilder.Layout.AUTHOR)
                .setText("Interested in Machine Learning")
                .setIcon(R.drawable.name_profile_weiren)
                .setHeading("Weiren Wang")
                .setSubheading("Phd, Georgia Tech")
                .setFootnote("Interested in Machine Learning")
                .setTimestamp("just now"));
        mCards.add(new CardBuilder(this, CardBuilder.Layout.AUTHOR)
                .setText("This card has a puppy background image.")
                .setIcon(R.drawable.name_profile_john)
                .setHeading("John Stasko")
                .setSubheading("Professor, Georgia Tech")
                .setFootnote("Information Visualization")
                .setTimestamp("just now"));


    }

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
        public int getItemViewType(int position){
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
