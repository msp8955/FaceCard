package edu.gatech.cc.cs7470.facecard.Controller.tasks;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import edu.gatech.cc.cs7470.facecard.Constants;
import edu.gatech.cc.cs7470.facecard.Controller.services.BackgroundService;
import edu.gatech.cc.cs7470.facecard.Controller.services.OnTaskCompleted;
import edu.gatech.cc.cs7470.facecard.Model.Bluetooth;
import edu.gatech.cc.cs7470.facecard.Model.FaceCard;
import edu.gatech.cc.cs7470.facecard.Model.Profile;

/**
 * Created by miseonpark on 3/24/15.
 */
public class DiscoverNearbyPeopleTask extends AsyncTask<String, String, String> {

    private OnTaskCompleted listener;

    private static final String TAG = "FaceCard DiscoverNearbyPeopleTask";
    private FaceCard[] faceCards;
    private Context context;

    public DiscoverNearbyPeopleTask(Context c, OnTaskCompleted listener){
        context = c;
        this.listener = listener;
    }

    /* Get Info for Facecard */
    @Override
    protected String doInBackground(String... params) {

        String inputBluetoothId = params[0];

        //create string
        String rest = "?bluetooth_id=" + inputBluetoothId;

        rest = rest.replace(" ", "%20");
        Log.d(TAG, rest);



        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(Constants.DISCOVER_ACCOUNT_URL + rest);

        try { //get response
            HttpResponse httpResponse = httpClient.execute(httpPost);

            InputStream inputStream = httpResponse.getEntity().getContent();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder stringBuilder = new StringBuilder();
            String bufferedStrChunk = null;

            while((bufferedStrChunk = bufferedReader.readLine()) != null){
                stringBuilder.append(bufferedStrChunk + "\n");
            }
            String result = stringBuilder.toString();
            if(result.charAt(9)==','){
                result = result.substring(0,9) + result.substring(10);
            }
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            faceCards = new FaceCard[jsonArray.length()];
            for(int i=0; i< jsonArray.length(); i++){
                JSONObject currentObject = jsonArray.getJSONObject(i);
                faceCards[i] = new FaceCard(
                        currentObject.getString("bluetooth_id"),
                        currentObject.getString("google_account"),
                        currentObject.getString("name"),
                        currentObject.getString("personal_tags"));
            }

            Log.d(TAG, stringBuilder.toString());
            return "successful";

        } catch (ClientProtocolException e) {
            Log.d(TAG, e.toString());
        } catch (IOException e) {
            Log.d(TAG, e.toString());
        } catch (JSONException e){
            Log.d(TAG, e.toString());
        }


        return "unsuccessful"; //account not found for bluetooth id
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        Log.d(TAG, "onPostExecute");

        if(result!=null){
            Log.d(TAG, result);
        }

        FaceCard[] faceCardForDebugging = new FaceCard[2];
        faceCardForDebugging[0] = new FaceCard("btid1", "gmail1", "name1", "tag1");
        faceCardForDebugging[1] = new FaceCard("btid2", "gmail2", "name2", "tag2");
        if(result.equals("successful") && faceCards!= null && faceCards.length>0){
            Log.d(TAG, "discovered neighbors: " + faceCards.length);
//            Intent i = new Intent(context, BackgroundService.class);
//            i.putExtra("FaceCards", faceCards);
//            context.startService(i);

            listener.onTaskCompleted(faceCards);


//            BluetoothCommunicationTask task = new BluetoothCommunicationTask(context);
////            task.connectToGlass();
//            task.sendToGlass(faceCards);
        }else{
            listener.onTaskCompleted(faceCardForDebugging);
        }
    }
}
