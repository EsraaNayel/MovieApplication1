package com.example.nayle.movieapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.nayle.movieapplication.adapter.MainAdapter;
import com.example.nayle.movieapplication.data.Result;
import com.example.nayle.movieapplication.database.DBHelper;

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
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {

    private ArrayList<Result> moviemodellist;
    static boolean mTwoPane;
    MainAdapter adapter;
    GridView gridView;
    int mPosition;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MainFragment() {
        // Required empty public constructor
    }


    public interface OnMovieSelectedListener {
        void onMovieSelected(String response);
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d("rotate","orintation changed");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if( savedInstanceState != null ) {
            Toast.makeText(getActivity(), savedInstanceState .getString("message"), Toast.LENGTH_LONG).show();
        }
        setHasOptionsMenu(true);



        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle onSaveInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_main, container, false);


        gridView = (GridView) view.findViewById(R.id.main_gridview);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Result result =  adapter.list.get(position);
                Log.i("movie id", String.valueOf(result.getId()));
                ((Callback) getActivity()).onItemSelected(result);


//                Intent i = new Intent(getActivity(), DetailActivity.class);
//                Result result = moviemodellist.get(position);
//                i.putExtra("movie", result);
//                startActivity(i);
            }
        });


        if (mTwoPane) {
            if (onSaveInstanceState != null && onSaveInstanceState.containsKey("pos")) {
                 mPosition = onSaveInstanceState.getInt("pos");
                String mID = onSaveInstanceState.getString("mID");
                if (mID != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("ID", mID);
                    DetailActivityFragment fragment = new DetailActivityFragment();
                    fragment.setArguments(bundle);
                    // In two-pane mode, show the detail view in this activity by
                    // adding or replacing the detail fragment using a
                    // fragment transaction.
                    getFragmentManager().beginTransaction()
                            .replace(R.id.leftFrame, fragment).commit();
                }
            }
        } else {
            if (onSaveInstanceState != null && onSaveInstanceState.containsKey("scroll_pos")) {
                int scroll_pos = onSaveInstanceState.getInt("scroll_pos");
            }
//            else {
//                 SaveInstanceState.getParcelableArrayList("pos");}
        }
//         onSaveInstanceState();


        new JSONTask().execute("http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=c6260798b95be3f1643000b02177cf03");

        return view;

    }

    public void onSaveInstanceState (Bundle outState){
        outState.putParcelableArrayList("key", null);
        super.onSaveInstanceState(outState);
    }


    class JSONTask extends AsyncTask<String, String, List<Result>> {
        @Override
        protected List<Result> doInBackground(String... params) {
            HttpURLConnection connection;
            BufferedReader reader;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
               // StringBuffer buffer = new StringBuffer();
                if (stream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }


                String FinalJson = buffer.toString();
                System.out.println("yaraaaaab" + FinalJson);

                JSONObject jsono = new JSONObject(FinalJson);
                JSONArray jsona = jsono.getJSONArray("results");

//<<<<<<<<<<<<<<<<<<<<<<<<<< Parsing JSON >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


                moviemodellist = new ArrayList<>();
                for (int i = 0; i < jsona.length(); i++) {
                    JSONObject FinalObject = jsona.getJSONObject(i);
                    Result moviemodel = new Result();

                    moviemodel.setPosterPath(FinalObject.getString("poster_path"));
                    moviemodel.setId(FinalObject.getInt("id"));
                    moviemodel.setOverview(FinalObject.getString("overview"));
                    moviemodel.setOriginalTitle(FinalObject.getString("original_title"));
                    moviemodel.setReleaseDate(FinalObject.getString("release_date"));
                    moviemodel.setTitle(FinalObject.getString("title"));
                    moviemodel.setPopularity(FinalObject.getDouble("popularity"));
                    //moviemodel.setVoteCount(FinalObject.getInt("vote_count"));
                    moviemodel.setVoteAverage((float) FinalObject.getDouble("vote_average"));

                    moviemodellist.add(moviemodel);
                }
                return moviemodellist;

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
        public void onPostExecute(List<Result> result) {
            super.onPostExecute(result);
            if(result == null) return;

            adapter = new MainAdapter(getActivity(), result);
            gridView.setAdapter(adapter);


//            isOnline(getContext());
        }

        public boolean isOnline(Context context) {
            ConnectivityManager cm =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();

            return isConnected;
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        getActivity().getMenuInflater().inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.highest_rated) {

            JSONTask MovieTask = new JSONTask();
            MovieTask.execute("http://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&api_key=36238a089d7b9497ccba2af9e2b8cc06");
             return true;
        }

        if (id == R.id.most_popular) {

            JSONTask MovieTask = new JSONTask();
            MovieTask.execute("http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=c6260798b95be3f1643000b02177cf03");
                return true;
        }

        if (id == R.id.favorit_List) {

            DBHelper dbHelper = new DBHelper(getActivity());
            List<Result> movieMainList = dbHelper.getAllMovie();

            adapter = new MainAdapter(getActivity(), movieMainList);
            gridView.setAdapter(adapter);
                   return true;
        }
        return super.onOptionsItemSelected(item);
    }



}