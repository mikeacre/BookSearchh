package com.example.mikeacre.booksearch;

import android.text.TextUtils;
import android.util.Log;
import com.example.mikeacre.booksearch.Book;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public final class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getSimpleName();
    public static final String MAX_RESULTS = "20";


    public static ArrayList<Book> fetchBookData(String query) {
        String requestUrl = "https://www.googleapis.com/books/v1/volumes?q="+query+"&maxResults="+MAX_RESULTS;
        URL url = createUrl(requestUrl);
        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        ArrayList<Book> books = extractBooks(jsonResponse);

        return books;
    }


    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

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

    public static ArrayList<Book> extractBooks(String jsonresponse) {

        ArrayList<Book> books = new ArrayList<>();

        try {

            JSONObject root = new JSONObject(jsonresponse);
            JSONArray features = root.getJSONArray("items");
            for (int i = 0; i < features.length(); i++) {
                try {
                    JSONObject book = features.getJSONObject(i);
                    String volumeInfo = book.getString("volumeInfo");
                    JSONObject infoObject = new JSONObject(volumeInfo);
                    JSONArray authorObject = infoObject.getJSONArray("authors");
                    JSONObject imageLink = infoObject.getJSONObject("imageLinks");
                    String thumbnail = imageLink.getString("thumbnail");

                    JSONObject properties = book.getJSONObject("volumeInfo");
                    String title = properties.getString("title");
                    String author = "By: "+authorObject.getString(0);
                    String summary = properties.getString("description");

                    Log.e("url:", thumbnail);

                    books.add(new Book(title, author, summary, thumbnail));
                }
                catch (Exception e){
                    Log.e("missing variable","skip book");
                }

            }

        } catch (JSONException e) {

            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        Log.e("List", "There are "+books.size()+" books beign returned");
        return books;
    }

}
