package com.example.nayle.movieapplication.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.nayle.movieapplication.R;
import com.example.nayle.movieapplication.data.Result;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Nayle on 2/5/2016.
 */
public class MainAdapter extends BaseAdapter {

   public List<Result> list;
    Context context;
    private LayoutInflater inflater ;



    public MainAdapter(Context context, List<Result> object ) {
        this.list = object;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    @Override
    public int getCount() {
        return list.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.movie_item, null);
        }
        Result result = list.get(position);
        String url = result.getPosterPath();

        final ImageView Iv= (ImageView) convertView.findViewById(R.id.main_ImageView);
        Log.e("testtest", " http://image.tmdb.org/t/p/" + "w185" + list.get(position).getPosterPath());
        Picasso.with(context).load("http://image.tmdb.org/t/p/"+"w185"+list.get(position).getPosterPath()).into(Iv);
        return convertView;
    }

}