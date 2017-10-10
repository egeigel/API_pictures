package com.example.edward.api_project;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.CharSequenceInputStream;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static android.R.attr.bitmap;

public class MainActivity extends AppCompatActivity {


    Button goButton;
    ImageButton nextButton;
    ImageButton backButton;
    TextView keywordSearch;
    ImageView imageView;
    ProgressBar progress;
    String[] urlPaths;
    CharSequence[]  keywords;
    int index=0;
    int arraySize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nextButton = (ImageButton)findViewById(R.id.next);
        backButton = (ImageButton)findViewById(R.id.back);
        imageView = (ImageView)findViewById(R.id.imageView);
        keywordSearch = (TextView)findViewById(R.id.keywordSearch);
        progress = (ProgressBar)findViewById(R.id.progressBar);
        goButton = (Button)findViewById(R.id.goButton);

        if(isConnected()) {
            try {
                keywords = new GetKeywordAsync().execute("http://dev.theappsdr.com/apis/photos/keywords.php?type=json").get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            //goButton.setEnabled(false);
            nextButton.setEnabled(false);
            backButton.setEnabled(false);
        }
        else{
            Toast.makeText(this , "No Connection" , Toast.LENGTH_SHORT).show();
        }

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isConnected()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Choose keyword to search for:");
                    builder.setItems(keywords, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            keywordSearch.setText(keywords[i]);
                            try {
                                progress.setVisibility(ProgressBar.VISIBLE);
                                urlPaths = new GetURLAsync(keywords[i].toString() , MainActivity.this).execute("http://dev.theappsdr.com/apis/photos/index.php").get();
                                imageView.setImageBitmap(new GetImageAsync(MainActivity.this).execute(urlPaths[0]).get());
                                index=0;
                                arraySize = urlPaths.length;
                                Log.d("demo" , urlPaths.toString());
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    builder.show();
                }
                    //new GetUrlsAsync(keywordSearch.getText().toString()).execute("");



                }
            });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if(imageView)
                index=index+1;
                if (index==arraySize){
                    index=0;
                }
                progress.setVisibility(ProgressBar.VISIBLE);
                new GetImageAsync(MainActivity.this).execute(urlPaths[index]);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if(imageView)
                index=index-1;
                if (index<0){
                    index=arraySize-1;
                }
                progress.setVisibility(ProgressBar.VISIBLE);
                new GetImageAsync(MainActivity.this).execute(urlPaths[index]);
            }
        });
    };








    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }







}
