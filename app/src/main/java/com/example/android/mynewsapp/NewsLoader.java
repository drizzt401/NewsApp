package com.example.android.mynewsapp;

import android.content.Context;

import java.util.List;

public class NewsLoader extends android.support.v4.content.AsyncTaskLoader<List<NewsArticle>> {
    private  String mUrl;

    public NewsLoader(Context context, String url){
        super(context);
        // TODO: Finish implementing this constructor
        mUrl= url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
    @Override
    public List<NewsArticle> loadInBackground() {
        // Don't perform the request if there are no URLs, or the first URL is null.
        if (mUrl == null) {
            return null;
        }

        List<NewsArticle> result = QueryUtils.fetchEarthquakeData(mUrl);
        return result;
    }
}
