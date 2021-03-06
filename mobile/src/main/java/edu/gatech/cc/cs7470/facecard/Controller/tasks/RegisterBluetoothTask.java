package edu.gatech.cc.cs7470.facecard.Controller.tasks;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import edu.gatech.cc.cs7470.facecard.Constants;

/**
 * Created by miseonpark on 3/9/15.
 */
public class RegisterBluetoothTask extends AsyncTask<String, String, String> {

    private static final String TAG = "FaceCard RegisterBluetoothTask";

    /* Register Bluetooth */
    @Override
    protected String doInBackground(String... params) {

        String accountId = params[0];
        String bluetoothId = params[1];
        String name = params[2];
        String imageLink = params[3];
        String tag = params[4];

        //create string
        String rest = "?bluetooth_id=" + bluetoothId + "&google_account=" + accountId
                + "&name=" + name + "&major=" + imageLink
                + "&personal_tags=" + tag;

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

            while((bufferedStrChunk = bufferedReader.readLine()) != null){
                stringBuilder.append(bufferedStrChunk);
            }

            Log.d(TAG, stringBuilder.toString());

            return stringBuilder.toString();

        } catch (ClientProtocolException e) {
            Log.d(TAG, e.toString());
        } catch (IOException e) {
            Log.d(TAG, e.toString());
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        if(result!=null){
            Log.d(TAG, result);
        }

//        if(result.equals("true")){
//            Log.d(TAG, "successfully registered bluetooth id");
//        }else{
//            Log.d(TAG, "bluetooth id registration failed");
//        }
    }

}
