package com.example.mikeacre.booksearch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by mikeacre on 11/19/2016.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    public boolean visible = false;

    public BookAdapter(Context context, ArrayList<Book> books){
        super(context, 0, books);
    }

    private Bitmap loadBitmap(String string)  throws IOException {

        try {
            URL url = new URL("string");
            return BitmapFactory.decodeStream(url.openConnection().getInputStream());
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){{

    }
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.book_item, parent, false);
        }

        Book currBook = getItem(position);

        TextView title = (TextView) listItemView.findViewById(R.id.title);
        TextView author = (TextView) listItemView.findViewById(R.id.author);
        ImageView thumbView = (ImageView) listItemView.findViewById(R.id.thumbnail);


        title.setText(currBook.getTitle());
        author.setText(currBook.getAuthor());


        String thumbString = currBook.getThumbnail();

        //Bitmap bmp = loadBitmap(currBook.getThumbnail());
        Bitmap bmp = null;

        try {
            URL url = new URL(currBook.getThumbnail());
            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        }catch (Exception e){
            e.printStackTrace();
        }


        thumbView.setImageBitmap(bmp);


        return listItemView;
    }

    /*
    Create thumbnail downloader
     */
}
