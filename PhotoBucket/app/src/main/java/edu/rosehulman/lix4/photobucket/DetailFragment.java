package edu.rosehulman.lix4.photobucket;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment {
    private static final String ARG_PIC = "pic";
    private DownloadImageTask mDownloadImageTask;
    private Pic mPic;


    public DetailFragment() {
        // Required empty public constructor
    }


    public static DetailFragment newInstance(Pic pic) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PIC, pic);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPic = getArguments().getParcelable(ARG_PIC);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        mDownloadImageTask = new DownloadImageTask();
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.image_view);
        new DownloadImageTask((ImageView) view.findViewById(R.id.image_view))
                .execute(mPic.getImageUrl());
//        String imageCaption = mPic.getCaption();
//        StorageReference imageRef = FirebaseStorage.getInstance().getReference(imageCaption + ".jpg");
        TextView caption = (TextView) view.findViewById(R.id.caption_text_view);
        caption.setText(mPic.getCaption());
        return view;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}
