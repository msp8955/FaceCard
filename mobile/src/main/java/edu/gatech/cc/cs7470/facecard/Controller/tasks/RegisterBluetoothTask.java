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

        Log.d(TAG, "accountId: " + accountId + " bluetoothId: " + bluetoothId);

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(Constants.URL);

        BasicNameValuePair accountPair = new BasicNameValuePair("accountId", accountId);
        BasicNameValuePair bluetoothPair = new BasicNameValuePair("bluetoothId", bluetoothId);
        List<NameValuePair> nameValuePairList = new ArrayList<>();
        nameValuePairList.add(accountPair);
        nameValuePairList.add(bluetoothPair);

        try {
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);
            httpPost.setEntity(urlEncodedFormEntity);

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

                return stringBuilder.toString();

            } catch (ClientProtocolException e) {
                Log.d(TAG, e.toString());
            } catch (IOException e) {
                Log.d(TAG, e.toString());
            }

        } catch (UnsupportedEncodingException e) {
            Log.d(TAG, e.toString());
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        if(result.equals("true")){
            Log.d(TAG, "successfully registered bluetooth id");
        }else{
            Log.d(TAG, "bluetooth id registration failed");
        }
    }

}
