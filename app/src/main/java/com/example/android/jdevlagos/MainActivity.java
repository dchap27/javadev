package com.example.android.jdevlagos;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.android.jdevlagos.DataQuery.LOG_TAG;

public class MainActivity extends AppCompatActivity {

    private SwipeRefreshLayout refresh;

    /**
     * URL for developers data from the github
     */
    private static final String GITHUB_REQUEST_URL = "https://api.github.com/search/users?q=location:lagos+language:java";

    /**
     * get the current item
     */
    public static DevProfile currentDeveloper;

    /**
     * Constant value for the profile loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int DEVELOPERS_LOADER_ID = 1;

    private DevProfileAdapter adapter;
    private TextView mEmptyView;
    private ProgressBar mProgressBar;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
//
//            @Override
//            public void onRefresh() {
//                loader.forceLoad();
//            }
//        });

        /** variable to test for network connectivity */

        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();

        mEmptyView = (TextView) findViewById(R.id.empty_state_view);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar_loading_spinner);

        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list, which is declared in the
        // activity_main file.
        listView = (ListView) findViewById(R.id.list);

        listView.setEmptyView(mEmptyView);


        // Create an {@link ArrayAdapter}, whose data source is a list of DevProfile objects.
        adapter = new DevProfileAdapter(this, new ArrayList<DevProfile>());

        // Make the {@link ListView} use the {@link ArrayAdapter} we created above, so that the
        // {@link ListView} will display list items for each word in the list of words.
        // Do this by calling the setAdapter method on the {@link ListView} object and pass in
        // 1 argument, which is the {@link ArrayAdapter} with the variable name itemsAdapter.

        listView.setAdapter(adapter);

        // set up click listener on Item click

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Get position of the list item
                currentDeveloper = adapter.getItem(i);

                // Create a new intent to view the developer details
                Intent websiteIntent = new Intent(MainActivity.this, ProfileScreenActivity.class);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        // This kickoff the Loader running after checking network connectivity
        // Initialize the loader. Pass in the int ID constant defined above and pass in null for
        // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
        // because this activity implements the LoaderCallbacks interface).
        if (isConnected) {
            getSupportLoaderManager().initLoader(DEVELOPERS_LOADER_ID, null, loaderCallbacks);
            Log.i(LOG_TAG, "the initialization of loader started");
        } else {
            mProgressBar.setVisibility(View.GONE);
            mEmptyView.setText(getString(R.string.no_internet));
        }


    }

    private LoaderManager.LoaderCallbacks<ArrayList<DevProfile>> loaderCallbacks =
            new LoaderManager.LoaderCallbacks<ArrayList<DevProfile>>() {
        @Override
        public Loader<ArrayList<DevProfile>> onCreateLoader(int id, Bundle args) {
            Uri baseUri = Uri.parse(GITHUB_REQUEST_URL);
            Uri.Builder uriBuilder = baseUri.buildUpon();
            return new DevelopersLoader(getApplicationContext(), uriBuilder.toString());
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<DevProfile>> loader, ArrayList<DevProfile> data) {
            mProgressBar.setVisibility(View.GONE);
            mEmptyView.setText(R.string.no_earthquake);
            // If there is no result, do nothing.
            if (data == null) {
                return;
            }

//        // Clear the adapter of previous earthquake data
//        adapter.clear();

            // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
            // data set. This will trigger the ListView to update.
            if (data != null && !data.isEmpty()) {
                adapter.addAll(data);
            }
            Log.i(LOG_TAG, "Now add the data to adapter still onloadfinished");
            adapter.notifyDataSetChanged();

        }

        @Override
        public void onLoaderReset(Loader<ArrayList<DevProfile>> loader) {
            // Clear the adapter of previous earthquake data
            adapter.clear();
        }
    };
}
//    @Override
//    protected void onResume() {
//        super.onResume();
//        listView.setAdapter(adapter);
//    }

