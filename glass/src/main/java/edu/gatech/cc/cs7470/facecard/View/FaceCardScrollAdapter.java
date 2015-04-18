package edu.gatech.cc.cs7470.facecard.View;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.glass.app.Card;
import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollAdapter;

import java.util.List;

import edu.gatech.cc.cs7470.facecard.Model.FaceCard;
import edu.gatech.cc.cs7470.facecard.R;

/**
 * Created by miseonpark on 4/16/15.
 */
public class FaceCardScrollAdapter extends CardScrollAdapter {

    private List<FaceCard[]> cards;
    private Context context;
    private int current_ui_state;

    public FaceCardScrollAdapter(Context context, List<FaceCard[]> cards, int current_ui_state){
        this.context = context;
        this.cards = cards;
        this.current_ui_state = current_ui_state;
    }

    public void setNum_face_cards(int num_face_cards){
        this.current_ui_state = num_face_cards;
    }

    public int findItemPosition(Object item) {
        return cards.indexOf(item);
    }

    @Override
    public int getCount() {
        return (cards.size()+current_ui_state-1)/current_ui_state;
    }

    @Override
    public Object getItem(int position) {
        return cards.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        Card card = new Card(context);
        View view = new CardBuilder(context, CardBuilder.Layout.EMBED_INSIDE)
                .setEmbeddedLayout(R.layout.face_card_main)
                .getView();
        FaceCard[] faceCards = cards.get(position);

        switch(current_ui_state){
            case 0:
                //default one card
                ImageView one_img = (ImageView) view.findViewById(R.id.one_image);
                one_img.setImageBitmap(faceCards[0].getProfilePicture());
                TextView one_name = (TextView) view.findViewById(R.id.one_name);
                one_name.setText(faceCards[0].getName());
                TextView one_description = (TextView) view.findViewById(R.id.one_description);
                one_description.setText(faceCards[0].getAccountId());
                TextView one_note = (TextView) view.findViewById(R.id.one_note);
                one_note.setText(faceCards[0].getTag());
            case 1:
                view = new CardBuilder(context, CardBuilder.Layout.EMBED_INSIDE)
                        .setEmbeddedLayout(R.layout.grid_four_views)
                        .getView();
                //four card
                ImageView four_img_1 = (ImageView) view.findViewById(R.id.four_image_1);
                four_img_1.setImageBitmap(faceCards[0].getProfilePicture());
                TextView four_name_1 = (TextView) view.findViewById(R.id.four_name_1);
                four_name_1.setText(faceCards[0].getName());
                TextView four_description_1 = (TextView) view.findViewById(R.id.four_description_1);
                four_description_1.setText(faceCards[0].getAccountId());
                TextView four_note_1 = (TextView) view.findViewById(R.id.four_note_1);
                four_note_1.setText(faceCards[0].getTag());

                ImageView four_img_2 = (ImageView) view.findViewById(R.id.four_image_2);
                four_img_2.setImageBitmap(faceCards[1].getProfilePicture());
                TextView four_name_2 = (TextView) view.findViewById(R.id.four_name_2);
                four_name_2.setText(faceCards[1].getName());
                TextView four_description_2 = (TextView) view.findViewById(R.id.four_description_2);
                four_description_2.setText(faceCards[1].getAccountId());
                TextView four_note_2 = (TextView) view.findViewById(R.id.four_note_2);
                four_note_2.setText(faceCards[1].getTag());

                ImageView four_img_3 = (ImageView) view.findViewById(R.id.four_image_3);
                four_img_3.setImageBitmap(faceCards[2].getProfilePicture());
                TextView four_name_3 = (TextView) view.findViewById(R.id.four_name_3);
                four_name_3.setText(faceCards[2].getName());
                TextView four_description_3 = (TextView) view.findViewById(R.id.four_description_3);
                four_description_3.setText(faceCards[2].getAccountId());
                TextView four_note_3 = (TextView) view.findViewById(R.id.four_note_3);
                four_note_3.setText(faceCards[2].getTag());

                ImageView four_img_4 = (ImageView) view.findViewById(R.id.four_image_4);
                four_img_4.setImageBitmap(faceCards[3].getProfilePicture());
                TextView four_name_4 = (TextView) view.findViewById(R.id.four_name_4);
                four_name_4.setText(faceCards[3].getName());
                TextView four_description_4 = (TextView) view.findViewById(R.id.four_description_4);
                four_description_4.setText(faceCards[3].getAccountId());
                TextView four_note_4 = (TextView) view.findViewById(R.id.four_note_4);
                four_note_4.setText(faceCards[3].getTag());
                break;
            case 2:
                //eight card
                view = new CardBuilder(context, CardBuilder.Layout.EMBED_INSIDE)
                        .setEmbeddedLayout(R.layout.grid_eight_views)
                        .getView();
                //eight card
                ImageView eight_img_1 = (ImageView) view.findViewById(R.id.eight_image_1);
                eight_img_1.setImageBitmap(faceCards[0].getProfilePicture());
                TextView eight_name_1 = (TextView) view.findViewById(R.id.eight_name_1);
                eight_name_1.setText(faceCards[0].getName());

                ImageView eight_img_2 = (ImageView) view.findViewById(R.id.eight_image_2);
                eight_img_2.setImageBitmap(faceCards[1].getProfilePicture());
                TextView eight_name_2 = (TextView) view.findViewById(R.id.eight_name_2);
                eight_name_2.setText(faceCards[1].getName());

                ImageView eight_img_3 = (ImageView) view.findViewById(R.id.eight_image_3);
                eight_img_3.setImageBitmap(faceCards[2].getProfilePicture());
                TextView eight_name_3 = (TextView) view.findViewById(R.id.eight_name_3);
                eight_name_3.setText(faceCards[2].getName());

                ImageView eight_img_4 = (ImageView) view.findViewById(R.id.eight_image_4);
                eight_img_4.setImageBitmap(faceCards[3].getProfilePicture());
                TextView eight_name_4 = (TextView) view.findViewById(R.id.eight_name_4);
                eight_name_4.setText(faceCards[3].getName());

                ImageView eight_img_5 = (ImageView) view.findViewById(R.id.eight_image_5);
                eight_img_5.setImageBitmap(faceCards[4].getProfilePicture());
                TextView eight_name_5 = (TextView) view.findViewById(R.id.eight_name_5);
                eight_name_5.setText(faceCards[4].getName());

                ImageView eight_img_6 = (ImageView) view.findViewById(R.id.eight_image_6);
                eight_img_6.setImageBitmap(faceCards[5].getProfilePicture());
                TextView eight_name_6 = (TextView) view.findViewById(R.id.eight_name_6);
                eight_name_6.setText(faceCards[5].getName());

                ImageView eight_img_7 = (ImageView) view.findViewById(R.id.eight_image_7);
                eight_img_7.setImageBitmap(faceCards[6].getProfilePicture());
                TextView eight_name_7 = (TextView) view.findViewById(R.id.eight_name_7);
                eight_name_7.setText(faceCards[6].getName());

                ImageView eight_img_8 = (ImageView) view.findViewById(R.id.eight_image_8);
                eight_img_8.setImageBitmap(faceCards[7].getProfilePicture());
                TextView eight_name_8 = (TextView) view.findViewById(R.id.eight_name_8);
                eight_name_8.setText(faceCards[7].getName());
                break;
            default:
                break;
        }
        return view;
    }

    @Override
    public int getPosition(Object item) {
        return cards.indexOf(item);
    }
}
