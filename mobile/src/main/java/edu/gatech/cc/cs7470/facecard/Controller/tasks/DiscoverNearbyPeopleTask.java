package edu.gatech.cc.cs7470.facecard.Controller.tasks;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import edu.gatech.cc.cs7470.facecard.Constants;
import edu.gatech.cc.cs7470.facecard.Model.Bluetooth;
import edu.gatech.cc.cs7470.facecard.Model.FaceCard;
import edu.gatech.cc.cs7470.facecard.Model.Profile;

/**
 * Created by miseonpark on 3/24/15.
 */
public class DiscoverNearbyPeopleTask extends AsyncTask<String, String, String> {

    private static final String TAG = "FaceCard DiscoverNearbyPeopleTask";
    private ArrayList<FaceCard> faceCard;

    /* Get Info for Facecard */
    @Override
    protected String doInBackground(String... params){

        faceCard = new ArrayList<FaceCard>();

        for(String b : params){
            //create string
            String rest = "?bluetooth_id=" + b;

            rest = rest.replace(" ", "%20");
            Log.d(TAG, rest);

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(Constants.REGISTER_ACCOUNT_URL + rest);

            try { //get response
                HttpResponse httpResponse = httpClient.execute(httpPost);

                InputStream inputStream = httpResponse.getEntity().getContent();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder stringBuilder = new StringBuilder();
                String bufferedStrChunk = null;

                String bluetoothId="";
                String accountId="";
                String name="";
                String tag="";

                int counter = 0;
                while((bufferedStrChunk = bufferedReader.readLine()) != null){
                    switch(counter){
                        case 1:
                            bluetoothId = bufferedStrChunk;
                            break;
                        case 2:
                            accountId = bufferedStrChunk;
                            break;
                        case 3:
                            name = bufferedStrChunk;
                            break;
                        case 4:
                            tag = bufferedStrChunk;
                            faceCard.add(new FaceCard(bluetoothId, accountId, name, tag));
                            break;
                        default:
                            break;
                    }
                    counter++;
                }

                Log.d(TAG, stringBuilder.toString());

            } catch (ClientProtocolException e) {
                Log.d(TAG, e.toString());
            } catch (IOException e) {
                Log.d(TAG, e.toString());
            }
        }
        return "successful";

    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        if(result!=null){
            Log.d(TAG, result);
        }

        for(FaceCard f : faceCard){
            Log.d(TAG, "FaceCard onPostExecute: " + f.getName());
        }
//        if(result.equals("successful")){
//            Log.d(TAG, "successful");
//        }else{
//            Log.d(TAG, "failed");
//        }
    }
}
