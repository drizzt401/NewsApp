package com.example.android.mynewsapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsArticle>> {

    private static final int EARTHQUAKE_LOADER_ID = 1;
    private static final String GUARDIAN_REQUEST_URL =
            "https://content.guardianapis.com/search";
    private NewsAdapter Adapter;
    private TextView mEmptyStateTextView;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();


        if (isConnected) {

            LoaderManager loaderManager = getSupportLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
        } else {
            mProgressBar = findViewById(R.id.loading_spinner);
            mProgressBar.setVisibility(View.INVISIBLE);
            mEmptyStateTextView = findViewById(R.id.empty_view);
            mEmptyStateTextView.setText(getString(R.string.no_internet));

        }
        // Find a reference to the {@link ListView} in the layout
        ListView newsArticleListView = findViewById(R.id.list);

        mProgressBar = findViewById(R.id.loading_spinner);
        // Create a new adapter that takes an empty list of earthquakes as input
        Adapter = new NewsAdapter(this, new ArrayList<NewsArticle>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        newsArticleListView.setAdapter(Adapter);

        mEmptyStateTextView = findViewById(R.id.empty_view);
        newsArticleListView.setEmptyView(mEmptyStateTextView);


        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected earthquake.
        newsArticleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current earthquake that was clicked on
                NewsArticle currentNewsArticle = Adapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri earthquakeUri = Uri.parse(currentNewsArticle.getUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });


    }

    @Override
    // onCreateLoader instantiates and returns a new Loader for the given ID
    public android.support.v4.content.Loader<List<NewsArticle>> onCreateLoader(int i, Bundle bundle) {

        String apiKey = getString(R.string.apiKey);
        String orderBy = getString(R.string.order);
        String fields = getString(R.string.fields);
        String tags = getString(R.string.tags);

        // parse breaks apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(GUARDIAN_REQUEST_URL);

        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // Append query parameter and its value.
        uriBuilder.appendQueryParameter("api-key", apiKey);
        uriBuilder.appendQueryParameter("order-by", orderBy);
        uriBuilder.appendQueryParameter("show-tags", tags);
        uriBuilder.appendQueryParameter("show-fields", fields);


        // Return the completed uri `https://content.guardianapis.com/search?api-key=b9b4bf54-2edf-4e5c-aa02-982779134a55&order-by=newest&show-tags=contributor&show-fields=thumbnail,headline
        return new NewsLoader(this, uriBuilder.toString());

    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<List<NewsArticle>> loader, List<NewsArticle> newsArticles) {
        Adapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (newsArticles != null && !newsArticles.isEmpty()) {
            Adapter.addAll(newsArticles);
        }

        // Set empty state text to display "No earthquakes found."
        mEmptyStateTextView.setText(R.string.no_articles);

        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<List<NewsArticle>> loader) {
        Adapter.clear();


    }


}
