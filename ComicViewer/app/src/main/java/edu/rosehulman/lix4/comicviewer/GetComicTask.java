package edu.rosehulman.lix4.comicviewer;

import android.os.AsyncTask;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;

/**
 * Created by phillee on 6/28/2017.
 */

public class GetComicTask extends AsyncTask<String, Void, Comic> {
    private ComicConsumer mComicConsumer;

    public GetComicTask(ComicConsumer activity) {
        mComicConsumer = activity;
    }

    @Override
    protected Comic doInBackground(String... params) {
        String urlString = params[0];
        Comic comic = null;
        Log.d("T", "A");
        try {
            comic = new ObjectMapper().readValue(new URL(urlString), Comic.class);
        } catch (IOException e) {
            Log.d("TTT", "ERROR: " + e.getMessage());
        }
        return comic;
    }

    @Override
    protected void onPostExecute(Comic comic) {
        super.onPostExecute(comic);
        mComicConsumer.onComicLoaded(comic);
    }


    public interface ComicConsumer {
        public void onComicLoaded(Comic comic);
    }
}
