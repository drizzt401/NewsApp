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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NewsAdapter extends ArrayAdapter<NewsArticle> {


    /**
     * Constructs a new {@link NewsAdapter}.
     *
     * @param context      of the app
     * @param newsArticles is the list of articles, which is the data source of the adapter
     */
    public NewsAdapter(Context context, List<NewsArticle> newsArticles) {
        super(context, 0, newsArticles);
    }

    /**
     * Returns a list item view that displays information about the news article at the given position
     * in the list of news articles.
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

        // Find the article at the given position in the list of news articles
        NewsArticle currentNewsArticle = getItem(position);

        // Find the ImageView with view ID thumbnail
        ImageView thumbnailView = listItemView.findViewById(R.id.thumbnail);
        // Display the image associated with the current article in that ImageView
        if (currentNewsArticle != null) {
            new DownloadImageTask(thumbnailView).execute(currentNewsArticle.getThumbnailUrl());
        }
        // Find the TextView with view ID headline
        TextView headlineTextView = listItemView.findViewById(R.id.headline);
        // Display the header of the current article in that TextView
        headlineTextView.setText(currentNewsArticle.getHeader());

        //Find the TextView with view ID Section
        TextView sectionTextView = listItemView.findViewById(R.id.Section);
        // Display the section of the current article in that TextView
        sectionTextView.setText(currentNewsArticle.getSection());

        // Find the TextView with view ID Author
        TextView authorTextView = listItemView.findViewById(R.id.Author);
        // Display the author of the current article in that TextView
        authorTextView.setText(currentNewsArticle.getAuthor());

        // Create a new Date object from the time the article was published
        String dateObject = currentNewsArticle.getDatePublished();

        /** SimpleDateFormat format = new SimpleDateFormat("M-dd-yyyy HH:mm:ss"); // your format
         * Date date;
         *
         {
         try {
         date = format.parse(dateObject);
         SimpleDateFormat formatter = new SimpleDateFormat("M-dd-yyyy HH:mm:ss");
         String datestring = formatter.format(date)
         } catch (ParseException e) {
         e.printStackTrace();
         }

         }
         **/
        // Find the TextView with view ID date
        TextView dateView = listItemView.findViewById(R.id.date);

        // Display the date the current article was published in that TextView
        dateView.setText(dateObject);

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
