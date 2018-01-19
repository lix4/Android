package edu.rosehulman.lix4.photobucket;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by phillee on 7/2/2017.
 */

public class PicAdapter extends RecyclerView.Adapter<PicAdapter.ViewHolder> {
    private Context mContext;
    private List<Pic> mPics;
    private ListFragment.CallBack mCallback;
    private DatabaseReference mPicsRef;
    private String mCurrentUid;
    private View mAttachedView;
    private PicsChildEventListener mAllPicsChildEventListener;
    private PicsChildEventListener mMyPicsChildEventListener;
    private StorageReference mStorageRef;
//    private UploadImageTask mUploadImageTask;

    public PicAdapter(Context context, ListFragment.CallBack callBack, String currentUid, View view) {
        mContext = context;
        mCallback = callBack;
        mCurrentUid = currentUid;
        mAttachedView = view;
        mPics = new ArrayList<>();
        mPicsRef = FirebaseDatabase.getInstance().getReference("photos");
        mAllPicsChildEventListener = new PicsChildEventListener();
        mMyPicsChildEventListener = new PicsChildEventListener();
        mPicsRef.addChildEventListener(mAllPicsChildEventListener);
        mStorageRef = FirebaseStorage.getInstance().getReference();
//        mUploadImageTask = new UploadImageTask();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View equationView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pic_card_view, parent, false);
        return new ViewHolder(equationView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Pic current = mPics.get(position);
        holder.captionTextView.setText(current.getCaption());
        holder.urlTextView.setText(current.getImageUrl());
        holder.mPicView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onPicSelected(current);
            }
        });
        holder.mPicView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String uid = current.getUid();
                if (!uid.equals(mCurrentUid)) {
                    Snackbar.make(mAttachedView, "This picture belongs to another user!", Snackbar.LENGTH_LONG)
                            .show();
                } else {
                    mCallback.showEditRemoveDialog(current);
                }
                return true;
            }
        });
    }

    public void showOnly(boolean switsh) {
        Query myPicsRef = mPicsRef.orderByChild("uid").equalTo(mCurrentUid);
        if (switsh) {
            myPicsRef.addChildEventListener(mMyPicsChildEventListener);
            mPicsRef.removeEventListener(mAllPicsChildEventListener);
        } else {
            mPicsRef.addChildEventListener(mAllPicsChildEventListener);
            myPicsRef.removeEventListener(mMyPicsChildEventListener);
        }
        mPics.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mPics.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView captionTextView;
        private TextView urlTextView;
        private View mPicView;

        public ViewHolder(View picView) {
            super(picView);
            mPicView = picView;
            captionTextView = (TextView) picView.findViewById(R.id.card_caption_view);
            urlTextView = (TextView) picView.findViewById(R.id.card_url_view);
        }
    }

    public void remove(Pic pic) {
        mPicsRef.child(pic.getKey()).removeValue();
    }

    public void add(Pic pic) {
        new UploadImageTask(pic).execute(pic.getImageUrl());
    }

    private class UploadImageTask extends AsyncTask<String, Void, Bitmap> {
        String imageUrl;
        Pic mPic;

        public UploadImageTask(Pic pic) {
            mPic = pic;
        }

        protected Bitmap doInBackground(String... urls) {
            imageUrl = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(imageUrl).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.d("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            result.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            StorageReference imageRef = mStorageRef.child(mPic.getCaption() + ".jpg");
            UploadTask uploadTask = imageRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    Log.d("onFailure: ", exception.getCause().getMessage());
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    mPic.setImageUrl(downloadUrl + "");
                    mPicsRef.push().setValue(mPic);
                }
            });
        }

    }

    public void update(Pic pic, String newCaption, String newUrl) {
        pic.setCaption(newCaption);
        pic.setImageUrl(newUrl);
        mPicsRef.child(pic.getKey()).setValue(pic);
    }

    private class PicsChildEventListener implements ChildEventListener {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Pic pic = dataSnapshot.getValue(Pic.class);
            pic.setKey(dataSnapshot.getKey());
            mPics.add(0, pic);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            String key = dataSnapshot.getKey();
            Pic value = dataSnapshot.getValue(Pic.class);
            for (Pic pic : mPics) {
                if (pic.getKey().equals(key)) {
                    pic.setValues(value);
                    notifyDataSetChanged();
                    return;
                }
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            String key = dataSnapshot.getKey();
            Pic value = dataSnapshot.getValue(Pic.class);
            for (Pic pic : mPics) {
                if (pic.getKey().equals(key)) {
                    mPics.remove(pic);
                    notifyDataSetChanged();
                    return;
                }
            }
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }
}
