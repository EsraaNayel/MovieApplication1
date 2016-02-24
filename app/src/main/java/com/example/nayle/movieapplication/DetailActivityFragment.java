package com.example.nayle.movieapplication;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nayle.movieapplication.DataMovie.Review;
import com.example.nayle.movieapplication.DataMovie.Trailer;
import com.example.nayle.movieapplication.adapter.DetailAdapter;
import com.example.nayle.movieapplication.adapter.MainAdapter;
import com.example.nayle.movieapplication.adapter.TrailerAdapter;
import com.example.nayle.movieapplication.data.Result;
import com.example.nayle.movieapplication.database.DBHelper;
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

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private ArrayList<Trailer> trailerArrayList;
    private ArrayList<Review> ReviewArrayList;
    public List<Trailer> trailers;
    public List<Review> reviews;
//    DetailAdapter adapter = new DetailAdapter(getContext(),trailers,reviews);

    TrailerAdapter traileradapter;
    ListView LV;
    ListView LvTrailer;
    DBHelper dbHelper;
    int id;
    //boolean mTwoPane;
    String PosterPath;
    //Result result = getResultData();
    Result result;

    public DetailActivityFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        LV = (ListView) view.findViewById(R.id.listView2);
        LvTrailer = (ListView) view.findViewById(R.id.listView1);
        if (MainFragment.mTwoPane) {
            Bundle bundle = getArguments();
            if (bundle != null) {
                result = (Result) bundle.getSerializable("Movie");
            } else if (savedInstanceState != null && savedInstanceState.containsKey("Movie")) {
                result = (Result) bundle.getSerializable("Movie");
            }
        } else {
            Intent intent = getActivity().getIntent();

            if (intent != null && intent.hasExtra("Movie")) {
                result = (Result) intent.getExtras().getSerializable("Movie");
            }
        }

        if (result == null) {
            Bundle bundle = getArguments();
            result = (Result) bundle.getSerializable("Movie");
        }

      new TrailersTask().execute("http://api.themoviedb.org/3/movie/" + result.getId() + "/videos?api_key=c6260798b95be3f1643000b02177cf03");

        // Log.d("trailerrr1", trailers.size() + "");
//         new ReviewsTask().execute("http://api.themoviedb.org/3/movie/" + result.getId() + "/reviews?api_key=c6260798b95be3f1643000b02177cf03");

        Toast.makeText(getActivity(), "in fraaaagment ", Toast.LENGTH_LONG).show();

//         DetailAdapter adapter = new DetailAdapter(getActivity(),trailers,reviews);

//        DetaildActivity.this.trailers = trailers;

        // DetailActivityFragment.this.trailers;
//        DetailAdapter adapter = new DetailAdapter(getActivity(), trailerArrayList, reviews);
//        LV.setAdapter(adapter);

        addHeader(result);
//        new TrailersTask().execute("http://api.themoviedb.org/3/movie/" + result.getId() + "/videos?api_key=c6260798b95be3f1643000b02177cf03");


        return view;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d("rotate","orintation changed");
    }


    private void addHeader(Result result) {
        id = result.getId();
        String movDesc = result.getOverview();
        String imgPath = result.getPosterPath();
        PosterPath = imgPath;
        String movTitle = result.getTitle();
        String movDate = result.getReleaseDate();
        float movRate = result.getVoteAverage();

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.test_header, null);

        TextView tvName = (TextView) view.findViewById(R.id.movie_name);
        ImageView IV = (ImageView) view.findViewById(R.id.movie_thumb);
        TextView tvDesc = (TextView) view.findViewById(R.id.descrition);
        TextView tvDate = (TextView) view.findViewById(R.id.release_date);
        RatingBar rbRate = (RatingBar) view.findViewById(R.id.ratingBar);
        TextView tvRating = (TextView) view.findViewById(R.id.rating);

        rbRate.setEnabled(false);
//        LayerDrawable stars = (LayerDrawable) rbRate.getProgressDrawable();
//              stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_IN);

        Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/" + "w342" + imgPath).into(IV);//w342

        tvName.setText(movTitle);
        tvDesc.setText("Description:\n" + movDesc);
        tvDate.setText("Release Date:\n" + movDate);
        rbRate.setRating(movRate / 2);

        tvRating.setText(movRate + "");

        // btnFavorite.setOnClickListener(dbButtonListener);
        LV.addHeaderView(view);

//////////////////////////********* FAVORITE BUTTON *****///////////////////////////
        dbHelper = new DBHelper(getActivity());
        // addHeader(result);
//        ImageButton btnFavorite = (ImageButton) view.findViewById(R.drawable.star);

        final ImageButton btnFavorite = (ImageButton) view.findViewById(R.id.btn_favorite);

        btnFavorite.setBackgroundResource(R.drawable.button_normal);
        boolean check = dbHelper.checkIFexist(id, PosterPath);

        if (check == true) {

            btnFavorite.setImageResource(android.R.drawable.btn_star_big_on);
            dbHelper = new DBHelper(getActivity());
            Toast.makeText(getActivity(), " Movie already saved in  your favorite list ", Toast.LENGTH_LONG).show();
        }
        final View.OnClickListener dbButtonListener = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                boolean check = dbHelper.checkIFexist(id, PosterPath);//,null ,null, null, null,null);
                if (check == false) {

                    dbHelper.insert(id, PosterPath, null, null, null, null, null);//,null ,null, null, null,null );
                    view.setBackgroundResource(R.drawable.button_pressed);
                    Toast.makeText(getActivity(), "Movie added to favorite list", Toast.LENGTH_LONG).show();

                } else {

                    dbHelper.delete(id, PosterPath);
                    view.setBackgroundResource(R.drawable.button_normal);
                    Toast.makeText(getActivity(), "Movie deleted from favorite list ", Toast.LENGTH_LONG).show();
                }
            }
        };
        btnFavorite.setOnClickListener(dbButtonListener);
    }

//                 new TrailersTask().execute("http://api.themoviedb.org/3/movie/" + result.getId() + "/videos?api_key=c6260798b95be3f1643000b02177cf03");

//    private Result getResultData() {
//        Result result = (Result) getActivity().getIntent().getExtras().get("movie");
//        return result;
//    }

    //    public Result getResultData() {
//      //  Intent intent = getActivity().getIntent();
//       Result result = null;
//        if (mTwoPane) {
//            Bundle bundle = getArguments();
//            if (bundle != null) {
//                result = (Result) bundle.getSerializable("Movie");
//            } else  {
//                result = (Result) bundle.getSerializable("Movie");
//            }
//        } else {
//             result = (Result) getActivity().getIntent().getExtras().get("Movie");
//        }
//        return result;
//
//    }
    class TrailersTask extends AsyncTask<String, String, List<Trailer>> {
        @Override
        protected List<Trailer> doInBackground(String... params) {

            HttpURLConnection connection;
            BufferedReader reader;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                String FinalJsonTrailers = buffer.toString();
                System.out.println("Trailers  " + FinalJsonTrailers);
                JSONObject jsono = new JSONObject(FinalJsonTrailers);
                JSONArray jsona = jsono.getJSONArray("results");
                trailerArrayList = new ArrayList<>();
                for (int i = 0; i < jsona.length(); i++) {

                    JSONObject FinalObject = jsona.getJSONObject(i);
                    Trailer trailer = new Trailer();
                    trailer.setName(FinalObject.getString("name"));
                    trailer.setUrl(FinalObject.getString("key"));

                    trailerArrayList.add(trailer);
                }
                return trailerArrayList;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onPostExecute(List<Trailer> trailers) {
            super.onPostExecute(trailers);


//             new TrailersTask().execute("http://api.themoviedb.org/3/movie/" + result.getId() + "/videos?api_key=c6260798b95be3f1643000b02177cf03");
            Log.d("trailer", trailers + "");





            Toast.makeText(getActivity(), "traiiiiiiiiilerrrrrrrrrrrs ", Toast.LENGTH_LONG).show();

           // DetailAdapter detailAdapter = new DetailAdapter(getActivity(), trailers, reviews);

            traileradapter= new TrailerAdapter(getContext(), trailers);
            LvTrailer.setAdapter(traileradapter);


            Toast.makeText(getActivity(), "reveiwwwwwwwwwwwww ", Toast.LENGTH_LONG).show();

            new ReviewsTask().execute("http://api.themoviedb.org/3/movie/" + result.getId() + "/reviews?api_key=c6260798b95be3f1643000b02177cf03");
        }
    }

    class ReviewsTask extends AsyncTask<String, String, List<Review>> {

        @Override
        protected List<Review> doInBackground(String... params) {

            HttpURLConnection connection;
            BufferedReader reader;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                String FinalJsonReviews = buffer.toString();

                System.out.println("Reviews " + FinalJsonReviews);
                JSONObject jsono = new JSONObject(FinalJsonReviews);
                JSONArray jsona = jsono.getJSONArray("results");

                ReviewArrayList = new ArrayList<>();
                for (int i = 0; i < jsona.length(); i++) {
                    JSONObject FinalObject = jsona.getJSONObject(i);
                    Review review = new Review();
                    review.setAuthor(FinalObject.getString("author"));
                    review.setContent(FinalObject.getString("content"));
                    ReviewArrayList.add(review);
                }
                return ReviewArrayList;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onPostExecute(List<Review> reviews) {
            super.onPostExecute(reviews);
//            new TrailersTask().execute("http://api.themoviedb.org/3/movie/" + result.getId() + "/videos?api_key=c6260798b95be3f1643000b02177cf03");

            DetailAdapter adapter = new DetailAdapter(getContext(), trailers, reviews);
            //LV.setAdapter(trailadapter);
            LV.setAdapter(adapter);

//            if(result == null) return;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        dbHelper.openDB();
    }

    @Override
    public void onStop() {
        super.onStop();
        dbHelper.close();
    }
}
