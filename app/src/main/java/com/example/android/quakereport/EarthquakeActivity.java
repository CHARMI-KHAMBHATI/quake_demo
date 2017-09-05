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
package com.example.android.quakereport;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderCallbacks<List<Earthquake>> {

    private static final int EARTHQUAKE_LOADER_ID = 1;
    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    private static final String USGS_REQUEST_URL ="https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2016-01-01&endtime=2016-01-31&minmag=6&limit=10";

    private ListView earthquakeListView;
    private EarthquakeAdapter mAdapter;
    private ProgressBar progress;
    private TextView empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);


        empty= (TextView) findViewById(R.id.empty);
        progress= (ProgressBar) findViewById(R.id.loading_spinner);





         earthquakeListView = (ListView) findViewById(R.id.list);

        mAdapter= new EarthquakeAdapter(this, new ArrayList<Earthquake>());

        earthquakeListView.setEmptyView(findViewById(R.id.empty));
        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(mAdapter);


        earthquakeListView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Earthquake currentQuake= mAdapter.getItem(position);
                Uri quakeURI= Uri.parse(currentQuake.getURL());
                Intent i = new Intent(Intent.ACTION_VIEW,quakeURI);
                startActivity(i);

            }
        });

        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
               NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
               if (networkInfo != null && networkInfo.isConnected()){

                   LoaderManager loaderManager =getLoaderManager();
                   loaderManager.initLoader(EARTHQUAKE_LOADER_ID,null,this);


        }
        else{
                   progress.setVisibility(View.INVISIBLE);
                   empty.setText("No Internet Connection");
               }



    }

    @Override
    public Loader<List<Earthquake>> onCreateLoader(int id, Bundle bundle) {
        return new EarthquakeLoader(this, USGS_REQUEST_URL);
    }



    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, final List<Earthquake> earthquakes) {

       progress.setVisibility(View.INVISIBLE);
       mAdapter.clear();
        empty.setText("No Earthquakes found");

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (earthquakes != null && !earthquakes.isEmpty()) {

            mAdapter.addAll(earthquakes);

        }



    }



    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        // Loader reset, so we can clear out our existing data.
                mAdapter.clear();

    }



}
