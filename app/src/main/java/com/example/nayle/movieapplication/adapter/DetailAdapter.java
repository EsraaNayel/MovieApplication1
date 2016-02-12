package com.example.nayle.movieapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nayle.movieapplication.DataMovie.Review;
import com.example.nayle.movieapplication.DataMovie.Trailer;
import com.example.nayle.movieapplication.R;
import com.example.nayle.movieapplication.data.Result;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by Nayle on 12/10/2015.
 */
public class DetailAdapter extends BaseAdapter {

    Result result;

    public DetailAdapter(Context context, List<Trailer> trailer, List<Review> reviews) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        trailerList = trailer;
        this.reviews = reviews;
    }

    Context context;
    private LayoutInflater inflater;
    public List<Trailer> trailerList;
    List<Review> reviews;


    @Override
    public int getCount() {
        // 1 + result.reviews.size() + result.trailerList.size();
        int count = 0;
        if (trailerList != null) {
            count += trailerList.size();
        }
        if (reviews != null) {
            count += reviews.size();
        }

        return count;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {



        View view = null;

        String type = getType(position);
        position = getExactPosition(position);


        if (type.equals("trailers")) {

            view = inflater.inflate(R.layout.row_trailer, null);
            final Trailer t = trailerList.get(position);

            TextView trailerName = (TextView) view.findViewById(R.id.trailerName);
            trailerName.setText(t.getName());

            ImageButton ivTrailer = (ImageButton) view.findViewById(R.id.trailerIcon);
            ivTrailer.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://www.youtube.com/watch?v=" + t.getUrl()));
                    context.startActivity(intent);
                }
            });

        } else if (type.equals("review")) {
            view = inflater.inflate(R.layout.row_review, null);
            Review r = reviews.get(position);
            TextView name = (TextView) view.findViewById(R.id.authorName);
            TextView content = (TextView) view.findViewById(R.id.reviewContent);

            name.setText("Name of Author: " + r.getAuthor());
            content.setText("Content: " + r.getContent());

//           name.setText(" The Author Name is: \n" + name + "\n" + " Review content is : \n " + content);

        }
        return view;
    }

        String getType(int position) {
        int TrailerCount = 0;
        if (trailerList != null) {
            TrailerCount = trailerList.size();
        }
        int ReviewCount = 0;
        if (reviews != null) {
            ReviewCount = reviews.size();
        }

        if (position < TrailerCount)
            return "trailers";
        else if (position < ReviewCount) {
            return "review";
        }
        return null;
    }

//
//    String getType(int position) {
//        if (trailerList != null)
//            return "details";
//        else if (position <= result.trailerList.size())
//            return "trailer";
//        else
//            return "review";
//
//    }

    private int getExactPosition(int position) {
        int TrailerCount = 0;
        if (trailerList != null) {
            TrailerCount = trailerList.size();
        }
        int ReviewCount = 0;
        if (reviews != null) {
            ReviewCount = reviews.size();
        }
        if (position < TrailerCount) {
            return position;
        } else {
            return position - TrailerCount;
        }

    }
}