package com.example.edward.api_project;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by edward on 10/10/17.
 */

public class GetKeywordAsync extends AsyncTask<String, Void , CharSequence[]> {

    String result;
    @Override
    protected CharSequence[] doInBackground(String... strings) {
        CharSequence[] keywords = new CharSequence[0];
        StringBuilder stringBuilder = new StringBuilder();
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        result = null;
        try {
            URL url = new URL(strings[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String json = IOUtils.toString(connection.getInputStream() , "UTF8");
                Log.d("demo" , json);
                JSONObject root = new JSONObject(json);
                Log.d("demo" , root.toString());
                JSONArray categories = root.getJSONArray("categories");
                keywords = new CharSequence[categories.length()];
                for(int i = 0; i<categories.length() ; i++){
                    String categoryJson = categories.getString(i);
                    Log.d("demo" , categoryJson);
                    keywords[i] = categoryJson;
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Handle the exceptions
        catch (JSONException e) {

        } finally {
            //Close open connections and reader
            if (connection != null) {
                connection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return keywords;
    }

       /* @Override
        protected void onPostExecute(Void aVoid) {
            keywords = result.split(";");
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Choose keyword to search for:");
            builder.setItems(keywords, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    keywordSearch.setText(keywords[i]);
                    new GetUrlsAsync(keywords[i].toString()).execute("");
                }
            });
            builder.show();
        }*/
}
