package com.example.edward.api_project;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by edward on 10/10/17.
 */
public class GetURLAsync extends AsyncTask<String, Void, String[]> {
    String keyword;
    String json = null;
    String[] urlPaths;
    Activity activity;

    public GetURLAsync(String keyword , Activity activity){
        this.keyword = keyword;
        this.activity = activity;
    }
    @Override
    protected String[] doInBackground(String... strings) {
        HttpURLConnection connection = null;


        try {
            String strUrl = strings[0] + "?" +
                    "keyword=" + URLEncoder.encode(keyword, "UTF-8") + "&type=json";
            URL url = new URL(strUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                json = IOUtils.toString(connection.getInputStream(), "UTF8");
                JSONObject root = new JSONObject(json);
                JSONArray urls = root.getJSONArray("urls");
                urlPaths = new String[urls.length()];
                for(int i = 0; i<urls.length() ; i++){
                    String path = urls.getString(i);
                    Log.d("demo" , path);
                    urlPaths[i] = path;
                }

            }
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            //Close open connections and reader
        }
        return urlPaths;
    }



    @Override
    protected void onPostExecute(String[] urlPaths) {
        if (urlPaths.length==0){
            ((ImageView)activity.findViewById(R.id.imageView)).setImageResource(0);
            Toast.makeText(activity , "No Images Found" , Toast.LENGTH_SHORT).show();
        }
    }
}
