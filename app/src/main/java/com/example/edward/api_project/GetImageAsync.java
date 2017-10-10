package com.example.edward.api_project;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by edward on 10/10/17.
 */

public class GetImageAsync extends AsyncTask<String, Void, Bitmap> {
    ImageView imageView;
    Bitmap bitmap;
    Activity activity;

    public GetImageAsync(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
            HttpURLConnection connection = null;
            bitmap = null;
            try {
                URL url = new URL(params[0]);
                Log.d("demo" , url.toString());
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    bitmap = BitmapFactory.decodeStream(connection.getInputStream());
                    Log.d("demo" , "finished downloading image");
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                connection.disconnect();
            }
            return bitmap;

    }


    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap != null) {
            Log.d("demo" , "in hrererererere");
            ((ImageView)activity.findViewById(R.id.imageView)).setImageBitmap(bitmap);
            ((ImageButton)activity.findViewById(R.id.next)).setEnabled(true);
            ((ImageButton)activity.findViewById(R.id.back)).setEnabled(true);
            ((ProgressBar)activity.findViewById(R.id.progressBar)).setVisibility(ProgressBar.INVISIBLE);
        }
    }
}
