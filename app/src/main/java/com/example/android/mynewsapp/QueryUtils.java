package com.example.android.mynewsapp;

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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    /**
     * Return a list of {@link NewsArticle} objects that has been built up from
     * parsing a JSON response.
     */
    public static List<NewsArticle> fetchNewsArticles(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);
        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        /**
         * Return a list of {@link NewsArticle} objects that has been built up from
         * parsing the given JSON response.
         */
        // Extract relevant fields from the JSON response and create a list of {@link Earthquake}s
        List<NewsArticle> newsArticles = extractFeatureFromJson(jsonResponse);
        // Return the list of {@link Earthquake}s
        return newsArticles;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
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
            Log.e(LOG_TAG, "Problem retrieving the news article JSON results.", e);

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
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

    private static List<NewsArticle> extractFeatureFromJson(String newsJSON) {


        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsJSON)) {
            return null;

        }

        List<NewsArticle> newsArticles = new ArrayList<>();

        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(newsJSON);

            JSONObject responseJSONObject = baseJsonResponse.getJSONObject("response");

            // Extract the JSONArray associated with the key called "results",
            // which represents a list of articles.
            JSONArray newsArray = responseJSONObject.getJSONArray("results");

            // For each article in the newsArray, create a {@link NewsArticle} object
            for (int i = 0; i < newsArray.length(); i++) {

                // Get a single news article at position i within the list of news articles
                JSONObject currentNewsArticle = newsArray.getJSONObject(i);

                // For a given article, extract the JSONObject associated with the
                // key called "fields", which represents a list of all fields
                // for that article.
                JSONObject details = currentNewsArticle.getJSONObject("fields");

                // Extract the value for the key called "thumbnail"
                String thumbnail = details.getString("thumbnail");

                // Extract the value for the key called "headline"
                String header = details.getString("headline");


                // Extract the value for the key called "webUrl"
                String url = currentNewsArticle.getString("webUrl");

                // Extract the value for the key called "webPublicationDate"
                String date = currentNewsArticle.getString("webPublicationDate");

                // Extract the value for the key called "sectionName"
                String section = currentNewsArticle.getString("sectionName");

                //Get the tags array
                JSONArray tagsArray = currentNewsArticle.getJSONArray("tags");

                //Declare String variable to hold author name
                String authorName = null;

                if (tagsArray.length() == 1) {
                    JSONObject contributorTag = (JSONObject) tagsArray.get(0);
                    authorName = contributorTag.getString("webTitle");
                }


                // Create a new {@link NewsArticle} object with the thumbnail, header, section, authorName, date,
                // and url from the JSON response.
                NewsArticle article = new NewsArticle(thumbnail, header, section, authorName, date, url);

                // Add the new {@link NewsArticle} to the list of articles.
                newsArticles.add(article);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the news article JSON results", e);
        }


        // Return the list of news articles
        return newsArticles;
    }
}


