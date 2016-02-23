package com.example.nayle.movieapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.nayle.movieapplication.DataMovie.Trailer;
import com.example.nayle.movieapplication.R;

import java.util.List;

/**
 * Created by Nayle on 2/23/2016.
 */
public class TrailerAdapter extends BaseAdapter {
    Context context;
    public LayoutInflater inflater;
    public List<Trailer> trailerList;
    Trailer t;


    public TrailerAdapter(Context context, List<Trailer> trailer){
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        trailerList = trailer;
    }

    @Override
    public int getCount() {
        return trailerList.size();
    }

    @Override
    public Object getItem(int position) {
        return trailerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_trailer, null);
        }

        t = trailerList.get(position);

        TextView trailerName = (TextView) convertView.findViewById(R.id.trailerName);
        trailerName.setText(t.getName());

        ImageButton ivTrailer = (ImageButton) convertView.findViewById(R.id.trailerIcon);
        ivTrailer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://www.youtube.com/watch?v=" + t.getUrl()));
                context.startActivity(intent);
            }
        });
        return convertView;

    }
}
