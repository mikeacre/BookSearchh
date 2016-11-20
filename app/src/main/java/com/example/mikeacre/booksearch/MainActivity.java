package com.example.mikeacre.booksearch;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

    String query = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (checkInternet() == true) {
            setContentView(R.layout.activity_main);
            getLoaderManager().initLoader(1, null, this);
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            Button button = (Button) findViewById(R.id.search);
            button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            newSearch();
                    }
             });

        }
        else {
            setContentView(R.layout.no_internet);
        }

    }

    public void newSearch(){

        EditText editText = (EditText) findViewById(R.id.serachfield);
        String string = (String) editText.getText().toString();
        query = string;
        getLoaderManager().restartLoader(1, null, this);
    }

    public boolean checkInternet() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public void displayBooks(List<Book> books) {

        final ArrayList<Book> finBooks = (ArrayList<Book>) books;

        ListView bookListView = (ListView) findViewById(R.id.displaybooks);
        // Create a new {@link ArrayAdapter} of earthquakes
        BookAdapter adapter = new BookAdapter(this, finBooks);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        bookListView.setAdapter(adapter);

        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Book book = finBooks.get(position);
                String summary = book.getSummary();
                final TextView textView = (TextView) findViewById(R.id.summary);
                final RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.masque);
                relativeLayout.setVisibility(View.VISIBLE);
                relativeLayout.bringToFront();
                textView.setText(summary);
                textView.setVisibility(View.VISIBLE);
                textView.bringToFront();
                textView.setOnClickListener(new TextView.OnClickListener() {
                    public void onClick(View v) {
                        textView.setVisibility(View.INVISIBLE);
                        relativeLayout.setVisibility(View.INVISIBLE);
                    }
                });

            }
        });
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        RelativeLayout masque = (RelativeLayout) findViewById(R.id.masque);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.loading_spinner);
        progressBar.setVisibility(View.VISIBLE);
        masque.setVisibility(View.VISIBLE);

        return new BookLoader(this, query);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> data) {
        Log.e("Loader", "Loader complete, lets diplay");
        RelativeLayout masque = (RelativeLayout) findViewById(R.id.masque);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.loading_spinner);
        masque.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        displayBooks(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        Log.e("Loader", "Loader reset");
    }
}
