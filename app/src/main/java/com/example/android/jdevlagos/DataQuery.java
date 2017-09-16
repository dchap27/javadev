package com.example.android.jdevlagos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

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
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static android.webkit.ConsoleMessage.MessageLevel.LOG;
import static java.lang.Long.getLong;

/**
 * Created by Ahmad on 9/15/2017.
 */

public class DataQuery {

    /**
     * Create a private constructor because no one should ever create a {@link DataQuery} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name DataQuery
     */
    private DataQuery() {
    }

    /** Tag for the log messages */
    public static final String LOG_TAG = DataQuery.class.getSimpleName();

    /**
     * Query the Github dataset and return an {@link DevProfile} object to represent a single developer.
     */
    public static ArrayList<DevProfile> fetchDevelopersData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }
        Log.v(LOG_TAG, "finished fetching data, Now extracting and processing");

        // Extract relevant fields from the JSON response and create an {@link Event} object
        ArrayList<DevProfile> developer = extractDataFromJson(jsonResponse);

        // Return the {@link DevProfile}
        return developer;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
            return null;
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        // if the url is null, return the response early;
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code " + urlConnection.getResponseCode());
            }

        } catch (IOException e) {
            // TODO: Handle the exception
            Log.e(LOG_TAG, "Problem retrieving from the url", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
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
            InputStreamReader inputStreamReader = new InputStreamReader(
                    inputStream, Charset.forName("UTF-8"));
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
     * Return an {@link DevProfile} object by parsing out information
     * about the first developer from the input developerJSON string.
     */
    public static ArrayList<DevProfile> extractDataFromJson(String developerJSON) {
        // if the JSON string is empty or null, return early
        if (TextUtils.isEmpty(developerJSON)){
            return null;
        }
        // Create an empty ArrayList that we can start adding developers to
        ArrayList<DevProfile> profiles = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(developerJSON);
            JSONArray itemsArray = baseJsonResponse.getJSONArray("items");

            // If there are results in the features array
            if (itemsArray.length() > 0) {
                for (int i = 0; i < itemsArray.length(); i++) {
                    // Extract out the items array
                    JSONObject items = itemsArray.getJSONObject(i);
                    // Extract out the username, profile url and photo url
                    String username = items.getString("login");
                    String profilePage = items.getString("html_url");
                    String imageUrl = items.getString("avatar_url");

                    /**
                     * create http connection for image url and download image
                     */
                    // Bitmap devImage = downloadImage(imageUrl);

                    // Create a new {@link DevProfile} object
                    profiles.add(new DevProfile(username, imageUrl, profilePage));
                }
            } return profiles;
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the developer JSON results", e);
        }
        return null;
    }

    // Creates Bitmap from InputStream and returns it
    private static Bitmap downloadImage(String url) {
        Bitmap bitmap = null;
        InputStream stream = null;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inSampleSize = 1;

        try {
            stream = getHttpConnection(url);
            bitmap = BitmapFactory.
                    decodeStream(stream, null, bmOptions);
            stream.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return bitmap;
    }

    // Makes HttpURLConnection and returns InputStream
    private static InputStream getHttpConnection(String urlString)
            throws IOException {
        InputStream stream = null;
        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();

        try {
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            httpConnection.setRequestMethod("GET");
            httpConnection.connect();

            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                stream = httpConnection.getInputStream();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return stream;
    }

}