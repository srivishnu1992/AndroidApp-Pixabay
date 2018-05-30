package com.example.srivi.pixabay;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    OkHttpClient okHttpClient = new OkHttpClient();
    EditText etKeyword;
    Button btnSearch;
    String keyword="";
    String link = "https://pixabay.com/api/?key=8642262-dfcda692c25acca3b9c6a7d79&q=";
    ArrayList<ImageInfo.hits> hits = new ArrayList<>(  );
    ArrayList<String> imageUrls = new ArrayList<>(  );
    ImageView imageView;
    int index=0;
    GestureDetector gestureDetector;

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        imageView.setImageResource( 0 );
        etKeyword.setText( "" );
        hits = new ArrayList<>(  );
        imageUrls = new ArrayList<>(  );
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );


        etKeyword = findViewById( R.id.etKeyword );
        btnSearch = findViewById( R.id.btnSearch );
        imageView = findViewById( R.id.imageView );
        imageView.setOnTouchListener( new OnSwipeTouchListener( MainActivity.this ) {
            public void onSwipeRight() {
                index++;
                if(imageUrls.size()==0) {}
                else if(index>imageUrls.size())
                    index=0;
                else
                    Picasso.get().load( imageUrls.get( index ) ).into( imageView );
            }

            public void onSwipeLeft() {
                index--;
                if(imageUrls.size()==0) {}
                else if(index<0)
                    index = imageUrls.size()-1;
                else
                    Picasso.get().load( imageUrls.get( index ) ).into( imageView );
            }
        } );
        btnSearch.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyword = etKeyword.getText().toString();
                if(keyword.equals( "" ))
                    etKeyword.setError( "Please enter a value" );
                else {
                    getData(link+keyword);
                }
            }
        } );
    }
    public void getData(String link) {
        Log.d( "link", link );
        Request request = new Request.Builder()
                .url( link )
                .build();
        okHttpClient.newCall( request ).enqueue( new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread( new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText( MainActivity.this, "Request Failed", Toast.LENGTH_SHORT ).show();
                    }
                } );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                Log.d("Request", responseBody);
                Gson gson = new Gson();
                ImageInfo imageInfo = gson.fromJson( responseBody, ImageInfo.class );
                Log.d("Request", imageInfo.toString());
                if(imageInfo.total == 0) {
                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText( MainActivity.this, "No images retrieved", Toast.LENGTH_SHORT ).show();
                        }
                    } );
                } else {
                    Log.d( "Request", imageInfo.toString() );
                    hits = imageInfo.hits;
                    for(int i=0;i<hits.size();i++)
                        imageUrls.add( hits.get( i ).largeImageURL );
                    final String imageLink = imageUrls.get( index );
                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            Picasso.get().load(imageLink).into(imageView);
                        }
                    } );
                }
            }
        } );
    }
}
