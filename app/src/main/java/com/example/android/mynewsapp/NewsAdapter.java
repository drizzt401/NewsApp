package com.example.android.mynewsapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.AsyncTask;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

import java.util.Date;
import java.util.List;

public class NewsAdapter extends ArrayAdapter<NewsArticle> {


    private static final String LOCATION_SEPARATOR = " of ";

    /**
     * Constructs a new {@link NewsAdapter}.
     *
     * @param context of the app
     * @param newsArticles is the list of earthquakes, which is the data source of the adapter
     */
    public NewsAdapter(Context context, List<NewsArticle> newsArticles) {
        super(context, 0, newsArticles);
    }

    /**
     * Returns a list item view that displays information about the earthquake at the given position
     * in the list of earthquakes.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Find the earthquake at the given position in the list of earthquakes
        NewsArticle currentNewsArticle = getItem(position);

        // Find the TextView with view ID magnitude
        ImageView thumbnailView = (ImageView) listItemView.findViewById(R.id.thumbnail);
        // Display the magnitude of the current earthquake in that TextView
        if (currentNewsArticle != null) {
            new DownloadImageTask(thumbnailView).execute(currentNewsArticle.getThumbnailUrl());
        }
        // Find the TextView with view ID location
        TextView headlineTextView = (TextView) listItemView.findViewById(R.id.headline);
        // Display the location of the current earthquake in that TextView
        headlineTextView.setText(currentNewsArticle.getHeader());

        // Find the TextView with view ID location offset
        TextView authorTextView = (TextView) listItemView.findViewById(R.id.Author);
        // Display the location offset of the current earthquake in that TextView
        authorTextView.setText(currentNewsArticle.getAuthor());

        // Create a new Date object from the time in milliseconds of the earthquake
        Date dateObject = new Date(currentNewsArticle.getDatePublished());

        // Find the TextView with view ID date
        TextView dateView = (TextView) listItemView.findViewById(R.id.date);

        // Display the date of the current earthquake in that TextView
        dateView.setText(currentNewsArticle.getDatePublished());

        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urlDisplay = urls[0];
            Bitmap bmp = null;
            try {
                InputStream in = new java.net.URL(urlDisplay).openStream();
                bmp = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bmp;
        }
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
                    }
    }





}
