package com.example.android.quakereport;

import android.text.TextUtils;
import android.util.Log;

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
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by ADMIN on 6/7/2017.
 */

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


    /**
     * Utility class with methods to help perform the HTTP request and
     * parse the response.
     */
    public final class Utils {

        /** Tag for the log messages */
        public static final String LOG_TAG = Utils.class.getSimpleName();

        /**
         * Query the USGS dataset and return an {@link Earthquake} object to represent a single earthquake.
         */
        public static ArrayList<Earthquake> fetchEarthquakeData(String requestUrl) {
            // Create URL object
            URL url = createUrl(requestUrl);

            // Perform HTTP request to the URL and receive a JSON response back
            String jsonResponse = null;
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error closing input stream", e);
            }

            // Extract relevant fields from the JSON response and create an {@link Earthquake} object
            ArrayList<Earthquake> earthquake = extractFeatureFromJson(jsonResponse);

            // Return the {@link Earthquake}
            return earthquake;
        }

        /**
         * Returns new URL object from the given string URL.
         */
        private static URL createUrl(String stringUrl) {
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException e) {
                Log.e(LOG_TAG, "Error with creating URL ", e);
            }
            return url;
        }

        /**
         * Make an HTTP request to the given URL and return a String as the response.
         */
        private static String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";

            // If the URL is null, then return early.
            if (url == null) {
                return jsonResponse;
            }

            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // If the request was successful (response code 200),
                // then read the input stream and parse the response.
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromStream(inputStream);
                } else {
                    Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            }


            return jsonResponse;
        }

        /**
         * Convert the {@link InputStream} into a String which contains the
         * whole JSON response from the server.
         */
        private static String readFromStream(InputStream inputStream) throws IOException {
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

        /**
         * Return an {@link Earthquake} object by parsing out information
         * about the first earthquake from the input earthquakeJSON string.
         */
        private static ArrayList<Earthquake>  extractFeatureFromJson(String earthquakeJSON) {
            // If the JSON string is empty or null, then return early.
            if (TextUtils.isEmpty(earthquakeJSON)) {

                //Toast.makeText(this , "null json string", Toast.LENGTH_SHORT).show();
                return null;
            }
            ArrayList<Earthquake> earthquakes = new ArrayList<>();

            try {
                JSONObject baseJsonResponse = new JSONObject(earthquakeJSON);
                JSONArray featureArray = baseJsonResponse.getJSONArray("features");

                for(int i=0;i<featureArray.length();i++)
                {
                    JSONObject c = featureArray.getJSONObject(i);
                    JSONObject properties= c.getJSONObject("properties");

                    double magnitude= properties.getDouble("mag");
                    String location = properties.getString("place");
                    long time= properties.getLong("time");

                    String timeT= getDate(time, "dd/MM/yyyy hh:mm:ss" );

                    String url=properties.getString("url");
                    earthquakes.add(new Earthquake(magnitude, location,timeT, url));
                }

            } catch (JSONException e) {
                Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
            }
            return earthquakes;
        }
        private static String getDate(long milliSeconds, String dateFormat)
        {
            // Create a DateFormatter object for displaying date in specified format.
            DateFormat formatter = new SimpleDateFormat(dateFormat);

            // Create a calendar object that will convert the date and time value in milliseconds to date.
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(milliSeconds);
            return formatter.format(calendar.getTime());
        }

    }


