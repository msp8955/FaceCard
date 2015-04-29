package edu.gatech.cc.cs7470.facecard;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.glass.app.Card;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import edu.gatech.cc.cs7470.facecard.Model.FaceCard;

/**
 * Created by miseonpark on 3/31/15.
 */
public class FaceCardMainActivity extends Activity {

    String TAG = "Glass Main Activity";

    /* variables for UI */
    private List<Card> mCards;
    private CardScrollView mCardScrollView;
    GestureDetector mGestureDetector;
    private ArrayList<FaceCard> faceCards;
    public static final int UI_STATE_DEFAULT = 0;
    public static final int UI_STATE_FOUR = 1;
    public static final int UI_STATE_EIGHT = 2;
    public static final int MAX_FACE_CARD = 8;
    private int CURRENT_UI_STATE=0;

    /* variables for One Card UI */
    private ImageView one_image;
    private TextView one_name;
    private TextView one_description;
    private TextView one_note;

    /* variables for Four Card UI */
    private ImageView four_image_1;
    private TextView four_name_1;
    private TextView four_description_1;
    private TextView four_note_1;
    private ImageView four_image_2;
    private TextView four_name_2;
    private TextView four_description_2;
    private TextView four_note_2;
    private ImageView four_image_3;
    private TextView four_name_3;
    private TextView four_description_3;
    private TextView four_note_3;
    private ImageView four_image_4;
    private TextView four_name_4;
    private TextView four_description_4;
    private TextView four_note_4;

    /* variables for Eight Card UI */


    /* variables for Bluetooth connection */
    public static String msgToSend;
    public static final int STATE_CONNECTION_STARTED = 0;
    public static final int STATE_CONNECTION_LOST = 1;
    public static final int READY_TO_CONN = 2;
    public static final int READ_FROM_CONNECTION = 3;
    private ConnectedThread mConnectedThread;
    private BluetoothAdapter mBluetoothAdapter;
    public final String NAME =" BLE";
    private Handler handler;
    private ArrayList<BluetoothSocket> mSockets = new ArrayList<BluetoothSocket>();
    // list of addresses for devices we've connected to
    private ArrayList<String> mDeviceAddresses = new ArrayList<String>();
    private UUID[] uuids = new UUID[2];
    private final String uuid1 = "00001101-0000-1000-8000-00805F9B34FB";
    private final String uuid2 = "c2911cd0-5c3c-11e3-949a-0800200c9a66";
    private int REQUEST_ENABLE_BT = 1;
    private AcceptThread accThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.face_card_main);

        //initialize an array list of card object, which works the same as the list view..
        mCards = new ArrayList<Card>();
        mGestureDetector = this.createGestureDetector(this);
        faceCards = new ArrayList<FaceCard>();

        uuids[0] = UUID.fromString(uuid1);
        uuids[1] = UUID.fromString(uuid2);

        handler = new Handler(Looper.getMainLooper()) {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    // if connection is built.....
                    case STATE_CONNECTION_STARTED:
                        setContentView(R.layout.face_card_main);
                        Toast.makeText(getApplicationContext(), "bluetooth connection started", Toast.LENGTH_SHORT).show();
                        break;
                    case STATE_CONNECTION_LOST:
                        Toast.makeText(getApplicationContext(), "bluetooth connection lost", Toast.LENGTH_LONG).show();
                        // if the connection is broken, listening the device again
                        startListening();
                        break;
                    case READY_TO_CONN:
                        // if the connection is ready to go, start listening the
                        // device
                        Toast.makeText(getApplicationContext(), "bluetooth ready to connect", Toast.LENGTH_LONG).show();
                        startListening();
                        break;
                    case READ_FROM_CONNECTION:
                        byte[] readBuf = (byte[]) msg.obj;
                        FaceCard faceCard = null;
                        try{
                            faceCard = deserialize(readBuf);
                            Log.d(TAG, "received the card" + faceCard.getName());
                            if(!faceCards.contains(faceCard)){
                                faceCards.add(faceCard);
                                Log.d(TAG, "added card to list");
                            }
                        } catch(ClassNotFoundException | IOException e){
                            Log.d(TAG, "deserialize fail " + e.getMessage());
                        }
                        if(faceCard!=null) {
                            Toast.makeText(getApplicationContext(), "read from connection: " + faceCard.getName(), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                        break;
                }
            }

        };
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        Card card = new Card(getApplicationContext());
        card.setText("searching for nearby people");
        //add the card to the array list
        mCards.add(card);
        setupScrollView();

        // run the "go get em" thread..
        accThread = new AcceptThread();
        accThread.start();
    }

    private void addCardToUI(FaceCard faceCard){
        boolean doesExist = false;
        for(FaceCard card : faceCards){
            if(card.equals(faceCard)){
                doesExist = true;
            }
        }
        if(!doesExist){
            //only add if it doesn't exist
            faceCards.add(faceCard);
            updateUI();
        }

    }

    /* update glass UI upon receiving new face card */
    private void updateUI(){

    }

    public void startListening() {
        if (accThread != null) {
            accThread.cancel();
        } else if (mConnectedThread != null) {
            mConnectedThread.cancel();
        } else {
            accThread = new AcceptThread();
            accThread.start();
        }
    }

    public static class HostBroadRec extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle b = intent.getExtras();
            String vals = "";
            for (String key : b.keySet()) {
                vals += key + "&" + b.getString(key) + "Z";
            }
            FaceCardMainActivity.setMsg(vals);
        }
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {

            Log.d(TAG, "create ConnectedThread");
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "temp sockets not created", e);
            }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            Log.i(TAG, "BEGIN mConnectedThread");

            byte[] buffer;  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    buffer = new byte[1024];

                    bytes = mmInStream.read(buffer);
                    // Send the obtained bytes to the UI activity
                    handler.obtainMessage(READ_FROM_CONNECTION, buffer)
//                    handler.obtainMessage(READ_FROM_CONNECTION, bytes, -1, buffer)
                            .sendToTarget();
                    Log.i(TAG, "message received!");

                } catch (IOException e) {
                    Log.i(TAG, "message failed!");
                    break;
                }
            }
        }

        public void connectionLost() {
            Message msg = handler.obtainMessage(STATE_CONNECTION_LOST);
            handler.sendMessage(msg);
        }

        /**
         * Write to the connected OutStream.
         *
         * @param buffer
         *            The bytes to write
         */
        public void write(byte[] buffer) {
            try {
                // Toast.makeText(getApplicationContext(), "write buffer!",
                // 1).show();
                mmOutStream.write(buffer);
            } catch (IOException e) {
                Log.e(TAG, "Exception during write", e);
                connectionLost();
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
                Message msg = handler.obtainMessage(READY_TO_CONN);
                handler.sendMessage(msg);
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }

    public static synchronized void setMsg(String newMsg) {
        msgToSend = newMsg;
    }

    private class AcceptThread extends Thread {
        private BluetoothServerSocket mmServerSocket;
        BluetoothServerSocket tmp;

        public AcceptThread() {
            BluetoothServerSocket tmp = null;
            try {
                tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, uuids[0]);
            } catch (IOException e) {
            }
            mmServerSocket = tmp;
        }

        public void run() {
            Log.e(TAG, "Running");
            BluetoothSocket socket = null;
            // Keep listening until exception occurs or a socket is returned
            while (true) {

                try {
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
                // If a connection was accepted

                if (socket != null) {
                    // if the connection has been built, then close the server
                    // socket..
                    try {
                        mmServerSocket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    // Do work to manage the connection (in a separate thread)
                    manageConnectedSocket(socket);
                    break;
                }
            }
        }

        /** Will cancel the listening socket, and cause the thread to finish */
        public void cancel() {
            try {
                mmServerSocket.close();
                Message msg = handler.obtainMessage(READY_TO_CONN);
                handler.sendMessage(msg);

            } catch (IOException e) {
            }
        }
    }

    private void manageConnectedSocket(BluetoothSocket socket) {
        // start our connection thread
        mConnectedThread = new ConnectedThread(socket);
        mConnectedThread.start();
        // Send the name of the connected device back to the UI Activity
        // so the HH can show you it's working and stuff...
        String devs = "";
        for (BluetoothSocket sock : mSockets) {
            devs += sock.getRemoteDevice().getName() + "\n";
        }
        // pass it to the pool....
        Message msg = handler.obtainMessage(STATE_CONNECTION_STARTED);
        Bundle bundle = new Bundle();
        bundle.putString("NAMES", devs);
        msg.setData(bundle);
        handler.sendMessage(msg);
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        unregisterReceiver(mReceiver);
//    }

    private void setupScrollView(){
        mCardScrollView = new CardScrollView(this){
            @Override
            public final boolean dispatchGenericFocusedEvent(MotionEvent event) {
                if (mGestureDetector.onMotionEvent(event)) {
                    return true;
                }
                return super.dispatchGenericFocusedEvent(event);
            }
        };

        List<FaceCard[]> faceCardsToSend = new ArrayList<FaceCard[]>();
        int div = 0;
        switch(CURRENT_UI_STATE){
            case UI_STATE_DEFAULT: div = 1; break;
            case UI_STATE_FOUR: div = 4; break;
            case UI_STATE_EIGHT: div = 8; break;
        }
        for(int i=0; i<MAX_FACE_CARD/div; i++){
            FaceCard[] cards = new FaceCard[div];
            for(int j=0; j<div; j++){
                try {
                    cards[j]=faceCards.get(i*div+j);
                }catch(Exception e){
                    Bitmap.Config conf = Bitmap.Config.ARGB_8888;
//                    cards[j]= new FaceCard("","","","",Bitmap.createBitmap(100, 100, conf));
                }
            }
            faceCardsToSend.add(cards);
        }

        FaceCardScrollAdapter adapter = new FaceCardScrollAdapter(this, faceCardsToSend, CURRENT_UI_STATE);
        mCardScrollView.setAdapter(adapter);
        mCardScrollView.activate();
        setContentView(mCardScrollView);
    }

    private GestureDetector createGestureDetector(final Context context) {

        GestureDetector mGestureDetector = new GestureDetector(context);
        mGestureDetector.setBaseListener(new GestureDetector.BaseListener() {

            @Override
            public boolean onGesture(Gesture gesture) {
                if (gesture == Gesture.TAP) {
                    // do something on tap
                    int index = mCardScrollView.getSelectedItemPosition();
                    Log.d(TAG, Integer.toString(index));
                    Log.i(TAG, "clicked");
                    return true;
                }
                return false;
            }
        });
        return mGestureDetector;
    }

    private class ExampleCardScrollAdapter extends CardScrollAdapter {
        //the same as implementing a list view

        public int findIdPosition(Object id) {
            return -1;
        }

        public int findItemPosition(Object item) {
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
        public View getView(int position, View convertView, ViewGroup parent) {
            return mCards.get(position).getView();
        }

        @Override
        public int getPosition(Object arg0) {
            // TODO Auto-generated method stub
            return 0;
        }
    }

    public static FaceCard deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream b = new ByteArrayInputStream(bytes);
        ObjectInputStream o = new ObjectInputStream(b);
        return (FaceCard) o.readObject();
    }
}
