package com.example.android.jdevlagos;

import android.content.Context;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;

import static android.R.attr.data;

/**
 * Created by Ahmad on 9/15/2017.
 */

public class DevelopersLoader extends AsyncTaskLoader<ArrayList<DevProfile>> {

    // Cached the data
    private ArrayList<DevProfile> cachedData;

    /** Tag for log messages */
    private static final String LOG_TAG = DevelopersLoader.class.getName();

    /** Query URL */
    private String mUrl;
    /**
     * Constructs a new {@link DevelopersLoader}.
     *  @param context of the activity
     * @param url to load data from
     */
    public DevelopersLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        if (cachedData == null){
            forceLoad();
        } else {
            super.deliverResult(cachedData);
        }
    }

    @Override
    public ArrayList<DevProfile> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        // create a number arrays of words
        final ArrayList<DevProfile> result = DataQuery.fetchDevelopersData(mUrl);

        return result;
    }

    @Override
    public void deliverResult(ArrayList<DevProfile> data) {
        cachedData = data;
        super.deliverResult(data);
    }
}
