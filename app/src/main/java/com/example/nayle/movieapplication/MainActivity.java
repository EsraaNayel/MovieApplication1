package com.example.nayle.movieapplication;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.nayle.movieapplication.data.Result;

public class MainActivity extends AppCompatActivity implements Callback {
    boolean mTwoPane;
    // private String DFTAG = DetailFragment.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putString("Type", "tablet ");

        MainFragment mainFragment = new MainFragment();
        mainFragment.setArguments(bundle);

        fragmentManager.beginTransaction().replace(R.id.leftFrame, mainFragment).commit();


        if (findViewById(R.id.frame_details) == null) {
            if (savedInstanceState == null) {

                mTwoPane = false;
                 getSupportFragmentManager().beginTransaction().add(R.id.frame_details, new MainFragment());

            }
        } else {
            mTwoPane = true;
        }
    }

    public void onItemSelected(Result result) {
        //this function gets called when a user clicks on an item,
        if (mTwoPane) {
            //if in two pane mode, contact the fragment directly and replace the right frame with it.
            DetailActivityFragment fragment = new DetailActivityFragment();

            Bundle bundle = new Bundle();

            bundle.putSerializable("Movie", result);
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_details, fragment).commit();
        } else {
            //if in one pane mode, open the detailsActivity which will contact the fragment.
            Intent intent = new Intent(this, DetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("Movie",result);
            intent.putExtras(bundle);

            startActivity(intent);
        }
    }
}
