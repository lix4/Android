package edu.rosehulman.lix4.comicviewer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


/**
 * A placeholder fragment containing a simple view.
 */
public class ComicFragment extends Fragment implements GetComicTask.ComicConsumer {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_COMIC_WRAPPER = "comic_wrapper";
    private TextView mTextView;
    private ComicWrapper mComicWrapper;
    private ImageView mImageView;

    public ComicFragment() {

    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ComicFragment newInstance(ComicWrapper comicWrapper) {
        ComicFragment fragment = new ComicFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_COMIC_WRAPPER, comicWrapper);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ComicWrapper cw = getArguments().getParcelable(ARG_COMIC_WRAPPER);
        mTextView = (TextView) rootView.findViewById(R.id.section_label);
        mTextView.setText(cw.getJoke());
        rootView.setBackgroundColor(getResources().getColor(cw.getColor()));
//        mImageView = (ImageView) rootView.findViewById(R.id.image_comic);
        return rootView;
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (savedInstanceState != null) {
////            mComicWrapper = getArguments().getParcelable(ARG_COMIC_WRAPPER);
////            String urlString = String.format("http://xkcd.com/%d/info.0.json", mComicWrapper.getXkcdIssue());
////            Log.d("onCreate: ", urlString);
////            new GetComicTask(this).execute(urlString);
//        }
//    }

    @Override
    public void onComicLoaded(Comic comic) {
//        Log.d("COMIC", "Comic Object\n" + comic);
//        mComicWrapper.setComic(comic);
//
//        mTextView.setText(comic.getSafe_title());
//        InputStream in = null;
//        try {
//            in = new URL(comic.getImg()).openStream();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Bitmap bitmap = BitmapFactory.decodeStream(in);
//        mImageView.setImageBitmap(bitmap);
    }

}
