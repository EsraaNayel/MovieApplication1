package com.example.nayle.movieapplication;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.nayle.movieapplication.DataMovie.Review;
import com.example.nayle.movieapplication.DataMovie.Trailer;
import com.example.nayle.movieapplication.adapter.DetailAdapter;
import com.example.nayle.movieapplication.data.Result;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    ListView listview;
    Result result;
    public ArrayList<Review> ReviewArrayList;
    public ArrayList<Trailer> trailerArrayList;
    public List<Trailer> trailers;
    String MovieID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);



        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putString("Type", "tablet ");

        DetailActivityFragment  detailActivityFragment = new DetailActivityFragment();
        detailActivityFragment.setArguments(bundle);

        fragmentManager.beginTransaction().replace(R.id.Frame,detailActivityFragment).commit();


    }

}
