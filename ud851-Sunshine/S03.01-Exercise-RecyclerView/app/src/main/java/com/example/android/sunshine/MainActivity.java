/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.sunshine;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.sunshine.data.SunshinePreferences;
import com.example.android.sunshine.utilities.NetworkUtils;
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    // Within forecast_list_item.xml //////////////////////////////////////////////////////////////
    // TODO (5) Add a layout for an item in the list called forecast_list_item.xml    DONE
    // TODO (6) Make the root of the layout a vertical LinearLayout    DONE
    // TODO (7) Set the width of the LinearLayout to match_parent and the height to wrap_content    DONE

    // TODO (8) Add a TextView with an id @+id/tv_weather_data    DONE
    // TODO (9) Set the text size to 22sp    DONE
    // TODO (10) Make the width and height wrap_content    DONE
    // TODO (11) Give the TextView 16dp of padding    DONE

    // TODO (12) Add a View to the layout with a width of match_parent and a height of 1dp    DONE
    // TODO (13) Set the background color to #dadada    DONE
    // TODO (14) Set the left and right margins to 8dp    DONE
    // Within forecast_list_item.xml //////////////////////////////////////////////////////////////


    // Within ForecastAdapter.java /////////////////////////////////////////////////////////////////
    // TODO (15) Add a class file called ForecastAdapter    DONE
    // TODO (22) Extend RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder>    DONE

    // TODO (23) Create a private string array called mWeatherData     DONE

    // TODO (47) Create the default constructor (we will pass in parameters in a later lesson)

    // TODO (16) Create a class within ForecastAdapter called ForecastAdapterViewHolder    DONE
    // TODO (17) Extend RecyclerView.ViewHolder    DONE

    // Within ForecastAdapterViewHolder ///////////////////////////////////////////////////////////
    // TODO (18) Create a public final TextView variable called mWeatherTextView     DONE

    // TODO (19) Create a constructor for this class that accepts a View as a parameter     DONE
    // TODO (20) Call super(view) within the constructor for ForecastAdapterViewHolder     DONE
    // TODO (21) Using view.findViewById, get a reference to this layout's TextView and save it to mWeatherTextView      DONE
    // Within ForecastAdapterViewHolder ///////////////////////////////////////////////////////////


    // TODO (24) Override onCreateViewHolder     DONE
    // TODO (25) Within onCreateViewHolder, inflate the list item xml into a view     DONE
    // TODO (26) Within onCreateViewHolder, return a new ForecastAdapterViewHolder with the above view passed in as a parameter     DONE

    // TODO (27) Override onBindViewHolder     DONE
    // TODO (28) Set the text of the TextView to the weather for this list item's position     DONE

    // TODO (29) Override getItemCount     DONE
    // TODO (30) Return 0 if mWeatherData is null, or the size of mWeatherData if it is not null     DONE

    // TODO (31) Create a setWeatherData method that saves the weatherData to mWeatherData      DONE
    // TODO (32) After you save mWeatherData, call notifyDataSetChanged     DONE
    // Within ForecastAdapter.java /////////////////////////////////////////////////////////////////


    // TODO (33) Delete mWeatherTextView     DONE

    // TODO (34) Add a private RecyclerView variable called mRecyclerView     DONE
    // TODO (35) Add a private ForecastAdapter variable called mForecastAdapter     DONE
    private RecyclerView mRecyclerView;
    private ForecastAdapter mForecastAdapter;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        // TODO (36) Delete the line where you get a reference to mWeatherTextView     DONE
        /*
         * Using findViewById, we get a reference to our TextView from xml. This allows us to
         * do things like set the text of the TextView.
         */

        // TODO (37) Use findViewById to get a reference to the RecyclerView     DONE
        mRecyclerView = findViewById(R.id.recyclerview_forecast);
        /* This TextView is used to display errors and will be hidden if there are no errors */
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        // TODO (38) Create layoutManager, a LinearLayoutManager with VERTICAL orientation and shouldReverseLayout == false      DONE
        LinearLayoutManager manager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,
                false);
        // TODO (39) Set the layoutManager on mRecyclerView     DONE
        mRecyclerView.setLayoutManager(manager);
        // TODO (40) Use setHasFixedSize(true) on mRecyclerView to designate that all items in the list will have the same size     DONE
        mRecyclerView.setHasFixedSize(true);
        // TODO (41) set mForecastAdapter equal to a new ForecastAdapter     DONE
        mForecastAdapter = new ForecastAdapter();
        // TODO (42) Use mRecyclerView.setAdapter and pass in mForecastAdapter     DONE
        mRecyclerView.setAdapter(mForecastAdapter);
        /*
         * The ProgressBar that will indicate to the user that we are loading data. It will be
         * hidden when no data is loading.
         *
         * Please note: This so called "ProgressBar" isn't a bar by default. It is more of a
         * circle. We didn't make the rules (or the names of Views), we just follow them.
         */
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        /* Once all of our views are setup, we can load the weather data. */
        loadWeatherData();
    }

    /**
     * This method will get the user's preferred location for weather, and then tell some
     * background method to get the weather data in the background.
     */
    private void loadWeatherData() {
        showWeatherDataView();

        String location = SunshinePreferences.getPreferredWeatherLocation(this);
        new FetchWeatherTask().execute(location);
    }

    /**
     * This method will make the View for the weather data visible and
     * hide the error message.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showWeatherDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        // TODO (43) Show mRecyclerView, not mWeatherTextView     DONE
        /* Then, make sure the weather data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the error message visible and hide the weather
     * View.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showErrorMessage() {
        // TODO (44) Hide mRecyclerView, not mWeatherTextView     DONE
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    public class FetchWeatherTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(String... params) {

            /* If there's no zip code, there's nothing to look up. */
            if (params.length == 0) {
                return null;
            }

            String location = params[0];
            URL weatherRequestUrl = NetworkUtils.buildUrl(location);

            try {
                String jsonWeatherResponse = NetworkUtils
                        .getResponseFromHttpUrl(weatherRequestUrl);

                String[] simpleJsonWeatherData = OpenWeatherJsonUtils
                        .getSimpleWeatherStringsFromJson(MainActivity.this, jsonWeatherResponse);

                return simpleJsonWeatherData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] weatherData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (weatherData != null) {
                showWeatherDataView();
                // TODO (45) Instead of iterating through every string, use mForecastAdapter.setWeatherData and pass in the weather data       DONE
                /*
                 * Iterate through the array and append the Strings to the TextView. The reason why we add
                 * the "\n\n\n" after the String is to give visual separation between each String in the
                 * TextView. Later, we'll learn about a better way to display lists of data.
                 */
                mForecastAdapter.setWeatherData(weatherData);
            } else {
                showErrorMessage();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.forecast, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            // TODO (46) Instead of setting the text to "", set the adapter to null before refreshing     DONE
            mForecastAdapter = null;
            loadWeatherData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}