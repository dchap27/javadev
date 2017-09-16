package com.example.android.jdevlagos;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import static android.R.attr.resource;

/**
 * Created by Ahmad on 9/15/2017.
 */

public class DevProfileAdapter extends ArrayAdapter<DevProfile> {

    /** Tag for log messages */
    private static final String LOG_TAG = DevProfileAdapter.class.getName();

    /**
     *@param context The current context (activity)used to inflate the layout file.
     *
     */
    public DevProfileAdapter(Context context, ArrayList<DevProfile> profile) {
        super(context,0, profile);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Get the {@link Word} object located at this position in the list
        DevProfile currentDeveloper = getItem(position);

        // Find the TextView in the list_item.xml layout with the username
        TextView usernameView = (TextView) listItemView.findViewById(R.id.dev_username_view);
        // set this text on the username TextView
        usernameView.setText(currentDeveloper.getUsername());

        // Find the Imageview in the list_item.xml layout with the image
        ImageView devImage = (ImageView) listItemView.findViewById(R.id.list_item_image);

        String imageUrl = currentDeveloper.getImageResource();
        // set this view using picasso
        Picasso.with(getContext()).load(imageUrl).placeholder(R.drawable.image_holder).into(devImage);

        return listItemView;
    }

}
