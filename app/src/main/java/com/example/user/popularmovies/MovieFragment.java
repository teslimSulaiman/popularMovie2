package com.example.user.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.host;

/**
 * Created by USER on 2/15/2017.
 */

public class MovieFragment extends Fragment {

    private MovieAdapter  movieAdapter;
    private ArrayList<Movie> movies = new ArrayList<Movie>();

    public MovieFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        movies = new ArrayList<Movie>();
        movieAdapter = new MovieAdapter(getContext(),movies);
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        GridView gridView = (GridView) rootView.findViewById(R.id.grid);
        gridView.setAdapter(movieAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Movie movie = movieAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("sampleObject", movie);
                startActivity(intent);
            }
        });

        return rootView;
    }

    private void updateMovieList() {
        FetchMovieTask movieTask = new FetchMovieTask();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String movieType = prefs.getString(getString(R.string.pref_movie_key),
                getString(R.string.pref_movie_default));
        movieTask.execute(movieType);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovieList();
    }

    public class FetchMovieTask extends AsyncTask<String, Void, List<Movie>> {
        private  final String LOG_TAG = FetchMovieTask.class.getName() ;

        @Override
        protected List<Movie> doInBackground(String... strings) {
            final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie/";
            final String APPID_PARAM = "api_key";

            if (strings.length == 0) {
                return null;
            }

            Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendPath(strings[0])
                        .appendQueryParameter(APPID_PARAM, BuildConfig.MOVIE_API_KEY)
                        .build();
                Log.v(LOG_TAG, "Built URI " + builtUri.toString());
            String jsonResponse = "";
                try {
                       URL url = new URL(builtUri.toString());
                    jsonResponse = makeHttpRequest(url);
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error connecting to the server", e);
                }
             return extractFeatureFromJson(jsonResponse);

        }

        @Override
        protected void onPostExecute(List<Movie> movieList) {
            if (movieList == null) {
                return;
            }
            updateUi(movieList);
        }

        private void updateUi(List<Movie> movieList) {

            movieAdapter.clear();
            movieAdapter.addAll(movieList);
            movieAdapter.notifyDataSetChanged();

        }

        private List<Movie> extractFeatureFromJson(String movieJson) {
            final String results = "results";
            final String title = "title";
            final String movieImage = "poster_path";
            final String overview = "overview";
            final String vote_average = "vote_average";
            final String release_date = "release_date";
            //if json string is empty or null return early
            if (TextUtils.isEmpty(movieJson)) {
                return null;
            }

            List moviesList = new ArrayList();
            try {
                JSONObject baseJsonResponse = new JSONObject(movieJson);
                JSONArray itemsArray = baseJsonResponse.getJSONArray(results);

                for (int i = 0; i < itemsArray.length(); i++) {
                    JSONObject movieData = itemsArray.getJSONObject(i);
                    // Extract out the title, url, overview, release date and average vote of movie
                    String movieTitle = movieData.getString(title);
                    String unFormatMovieImageUrl = movieData.getString(movieImage);
                    String imageUrl = formatMovieImagePath(unFormatMovieImageUrl);
                    String movieOverview = movieData.getString(overview);
                    String movieReleaseDate = movieData.getString(release_date);
                    double movieAverageVote = movieData.getDouble(vote_average);

                    // Create a new {@link Movie} object
                    moviesList.add(new Movie(movieTitle, imageUrl,movieOverview,movieAverageVote,movieReleaseDate));
                    Log.v(LOG_TAG, "Movie title : " + imageUrl);
                }
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Problem parsing the movie JSON results", e);
            }

            return moviesList;

        }

        private String formatMovieImagePath(String pathFragment) {
            final String MOVIE_IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
            final String MOVIE_IMAGE_SIZE_OPTION = "w185/";
            return MOVIE_IMAGE_BASE_URL + MOVIE_IMAGE_SIZE_OPTION + pathFragment;
        }

        private String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";
            //if url is null, then return early
            if (url == null) {
                return jsonResponse;
            }
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.connect();
                //if the request was successful (response code 200);
                //then read the input stream and parse the response
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromStream(inputStream);

                } else {
                    Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "Problem receiving JSON results", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    // function must handle java.io.IOException here
                    inputStream.close();
                }
            }
            return jsonResponse;
        }

        /**
         * Convert the {@link InputStream} into a String which contains the
         * whole JSON response from the server.
         */
        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }
    }

}
