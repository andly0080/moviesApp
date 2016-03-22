package com.example.android.mymoiveapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.mymoiveapp.utilities.Utility;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by davidyu on 8/22/2015.
 */
public class MovieFragment extends Fragment {


    private static final String KEY_MOVES_LIST = "key_moves_list";


    GridView gridView;
    ImageAdapter myImageAdapter;
    ArrayList<MyMovie> movies_list;


    public MovieFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setHasOptionsMenu(true);

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putParcelableArrayList(KEY_MOVES_LIST, movies_list);


        super.onSaveInstanceState(outState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        gridView = (GridView) rootView.findViewById(R.id.gridView);

        myImageAdapter = new ImageAdapter(getActivity());

        gridView.setAdapter(myImageAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                MyMovie item = (MyMovie) myImageAdapter.getItem(position);

                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(DetailActivityFragment.MY_MOVIE_OBJECT, item);
                startActivity(intent);

            }
        });


        if (savedInstanceState != null) {

            movies_list = savedInstanceState.getParcelableArrayList(KEY_MOVES_LIST);

            if (movies_list == null) {

                refreshMovieList();
            } else {
                myImageAdapter.addAllItems(movies_list);

            }


        } else {

            refreshMovieList();

        }


        return rootView;
    }

    public void refreshMovieList() {
        if (Utility.isInternetConnected(getActivity())) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

            String sortType = prefs.getString(getString(R.string.pref_sort_type_key), getString(R.string.sort_type_default));

            new FetchMovieTask().execute(sortType);

        } else {

            Toast.makeText(getActivity(), "no internet connected, please try it later", LENGTH_SHORT).show();

        }
    }


    //create menu item


//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//
//        inflater.inflate(R.menu.moviefragment,menu);
//
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        int id = item.getItemId();
//        if(id ==R.id.action_refresh)
//        {
//
//        }
//
//
//        return super.onOptionsItemSelected(item);
//    }

    public class FetchMovieTask extends AsyncTask<String, Void, String> {

        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

        @Override
        protected String doInBackground(String... params) {


            Log.i("kkan", "fetch movie task is running ");


            if (params.length == 0) {

                return null;
            }


            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;


            // Will contain the raw JSON response as a string.
            String movieJsonStr = null;

            try {
                final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
                final String APIKEY = "";
                final String SORY_BY_PARAM = "sort_by";
                final String APIKEY_PARAM = "api_key";


//                URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7");
                // URL url = new URL("http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key="+APIKEY);

                Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendQueryParameter(SORY_BY_PARAM, params[0])
                        .appendQueryParameter(APIKEY_PARAM, APIKEY)
                        .build();


                URL url = new URL(builtUri.toString());

                // Create the request to  open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;

                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;

                }
                movieJsonStr = buffer.toString();


            } catch (IOException e) {
                Log.e("MoiveFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;

            }

            return movieJsonStr;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            try {
                movies_list = getMovieDataFromJson(s);

                //assign data to adapter

                myImageAdapter.addAllItems(movies_list);


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }


    public ArrayList<MyMovie> getMovieDataFromJson(String movieJsonStr) throws JSONException {

        final String OWM_LIST = "results";
        final String MOVIE_ID = "id";
        final String MOVIE_TITLE = "title";
        final String MOVIE_ORIGINAL_TITLE = "original_title";
        final String MOVIE_OVERVIEW = "overview";
        final String MOVIE_POSTER = "poster_path";
        final String MOVIE_RELEASE_DATE = "release_date";
        final String MOVIE_VOTE_AVERAGE = "vote_average";


        JSONObject movieJson = new JSONObject(movieJsonStr);
        JSONArray movieArray = movieJson.getJSONArray(OWM_LIST);
        ArrayList<MyMovie> movieList = new ArrayList<MyMovie>();

        for (int i = 0; i < movieArray.length(); i++) {
            MyMovie movie = new MyMovie();
            // each movie
            JSONObject eachMovie = movieArray.getJSONObject(i);

            String movieTitle = eachMovie.getString(MOVIE_TITLE);
            String movieOriginalTitle = eachMovie.getString(MOVIE_ORIGINAL_TITLE);
            String movieId = eachMovie.getString(MOVIE_ID);
            String moviePoster = eachMovie.getString(MOVIE_POSTER);
            String movieOverview = eachMovie.getString(MOVIE_OVERVIEW);
            String movieReleaseDate = eachMovie.getString(MOVIE_RELEASE_DATE);
            String movieVoteAverage = eachMovie.getString(MOVIE_VOTE_AVERAGE);


            movie.setId(movieId);
            movie.setOriginalTitle(movieOriginalTitle);
            movie.setTitle(movieTitle);
            movie.setPosterUrl(moviePoster);
            movie.setOverview(movieOverview);
            movie.setRelease(movieReleaseDate);
            movie.setVoteAverage(movieVoteAverage);

            movieList.add(movie);
        }

        return movieList;


    }


    public class ImageAdapter extends BaseAdapter {

        private Context mContext;
        //references to our movie image

        private List<MyMovie> movieList;
        private final String BASEPOSTERURL = "http://image.tmdb.org/t/p";


        public void addAllItems(List<MyMovie> movies) {
            movieList.clear();
            movieList.addAll(movies);
            notifyDataSetChanged();

        }


        public ImageAdapter(Context c) {
            movieList = new ArrayList<MyMovie>();
            mContext = c;
        }

        @Override
        public int getCount() {
            return movieList.size();
        }

        @Override
        public Object getItem(int i) {
            return movieList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View covertView, ViewGroup parent) {


            MyMovie myMovie = movieList.get(position);

            ImageView imageView;

            if (covertView == null) {

                imageView = new ImageView(mContext);

                imageView.setLayoutParams(new GridView.LayoutParams(GridLayout.LayoutParams.WRAP_CONTENT, GridLayout.LayoutParams.WRAP_CONTENT));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);


            } else {

                imageView = (ImageView) covertView;

            }


            String posterUrl = myMovie.getPosterUrl();
            String fullPosterUrl = BASEPOSTERURL + "/w500" + posterUrl;

            Picasso.with(mContext).load(fullPosterUrl).into(imageView);

            return imageView;

        }


    }

}
