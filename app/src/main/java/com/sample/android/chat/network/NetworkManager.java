package com.sample.android.chat.network;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by sa on 6/30/16.
 */
public class NetworkManager {

    private static final String TAG = NetworkManager.class.getSimpleName();

    public static String getDataFromNetwork(Context context, String urlString){
        String response = "";

        try {
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();

            InputStream in = con.getInputStream();

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String line;
                Reader reader = new InputStreamReader(con.getInputStream());

                BufferedReader br = new BufferedReader(reader);
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            }
            else {
                response="";
                Log.e(TAG,"HTTPs request failed on: " + urlString + " With error code: "+ responseCode);
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i(TAG,"Response: "+response);

        return response;
    }
}
