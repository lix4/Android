package edu.rosehulman.lix4.finalexamlix4;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by phillee on 8/17/2017.
 */

public class RiddleAdapter extends RecyclerView.Adapter<RiddleAdapter.ViewHolder> {
    private final RiddleChildEventListener mRiddleChildEventListener;
    private final RiddleChildEventListener myFilteredChildEventListener;
    private List<Riddle> mRiddles;
    private Context mContext;
    private DatabaseReference mRiddlesRef;
    private RiddleChildEventListener myRiddleChildEventListener;

    public RiddleAdapter(Context context) {
        mContext = context;
        mRiddles = new ArrayList<>();
        mRiddlesRef = FirebaseDatabase.getInstance().getReference("riddles");
        mRiddleChildEventListener = new RiddleChildEventListener();
        mRiddlesRef.addChildEventListener(mRiddleChildEventListener);
        myRiddleChildEventListener = new RiddleChildEventListener();
        myFilteredChildEventListener = new RiddleChildEventListener();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View riddleView = LayoutInflater.from(parent.getContext()).inflate(R.layout.riddle_card_view, parent, false);
        return new ViewHolder(riddleView);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Riddle currentRiddle = mRiddles.get(position);
        holder.riddleTitleView.setText(currentRiddle.getQuestion());
        Map<String, Boolean> map = currentRiddle.getRecipients();
        if (map == null) {
            holder.riddleRecipientsView.setText(String.format(mContext.getResources().getString(R.string.recipients), ""));
        } else {
            holder.riddleRecipientsView.setText(String.format(mContext.getResources().getString(R.string.recipients), currentRiddle.processRecipients()));
        }
        holder.riddleOwnerView.setText(String.format(mContext.getResources().getString(R.string.owner), currentRiddle.getOwner()));
        holder.riddleExtraView.setText(currentRiddle.getExtra());
        holder.riddleLikesView.setText(String.valueOf(currentRiddle.getNumLikes()));
        holder.riddleItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, currentRiddle.getAnswer(), Snackbar.LENGTH_LONG)
                        .setAction("like", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                long currentLikes = currentRiddle.getNumLikes();
                                currentLikes++;
                                updateRiddle(currentRiddle, currentLikes);
                            }
                        })
                        .show();
            }
        });
        holder.riddleItemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (currentRiddle.getOwner().equals(Constants.currentOwner)) {
//                    removeRiddle(currentRiddle);
                    final Riddle clearedRiddle = currentRiddle;
                    mRiddles.remove(currentRiddle);
                    notifyDataSetChanged();
                    Snackbar.make(v, "Riddle deleted", Snackbar.LENGTH_LONG)
                            .setAction("UNDO", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
//                                    addRiddle(clearedRiddle);
                                    mRiddles.add(0, clearedRiddle);
                                    notifyDataSetChanged();
                                    Snackbar.make(v, "Riddle restored", Snackbar.LENGTH_LONG)
                                            .show();
                                }
                            })
                            .addCallback(new Snackbar.Callback() {
                                @Override
                                public void onDismissed(Snackbar transientBottomBar, int event) {
                                    super.onDismissed(transientBottomBar, event);
                                    if (event == 2) {
                                        removeRiddle(currentRiddle);
                                    }
                                }
                            })
                            .show();
                } else {
                    Snackbar.make(v, "You are not the owner", Snackbar.LENGTH_SHORT)
                            .show();
                }
                return true;
            }
        });
    }

    private void updateRiddle(Riddle riddle, long likes) {
        riddle.setNumLikes(likes);
        mRiddlesRef.child(riddle.getKey()).setValue(riddle);
    }

    @Override
    public int getItemCount() {
        return mRiddles.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView riddleTitleView;
        private TextView riddleRecipientsView;
        private TextView riddleOwnerView;
        private TextView riddleLikesView;
        private TextView riddleExtraView;
        private View riddleItemView;

        public ViewHolder(View itemView) {
            super(itemView);
            riddleItemView = itemView;
            riddleTitleView = (TextView) itemView.findViewById(R.id.riddle_title_text_view);
            riddleExtraView = (TextView) itemView.findViewById(R.id.riddle_extra_text_view);
            riddleRecipientsView = (TextView) itemView.findViewById(R.id.riddle_recipients_text_view);
            riddleOwnerView = (TextView) itemView.findViewById(R.id.riddle_owner_text_view);
            riddleLikesView = (TextView) itemView.findViewById(R.id.riddle_like_number_text_view);

        }
    }

    public void showOnly(int index, String name) {
        Query myRiddleRef = mRiddlesRef.orderByChild("owner").equalTo(Constants.currentOwner);
        Query filtered = mRiddlesRef.orderByChild("recipients/" + name).equalTo(true);
        if (index == 0) {
            mRiddlesRef.addChildEventListener(mRiddleChildEventListener);
            myRiddleRef.removeEventListener(myRiddleChildEventListener);
            filtered.removeEventListener(myFilteredChildEventListener);
        } else if (index == 1) {
            myRiddleRef.addChildEventListener(myRiddleChildEventListener);
            mRiddlesRef.removeEventListener(mRiddleChildEventListener);
            filtered.removeEventListener(myFilteredChildEventListener);
        } else if (index == 2) {
            filtered.addChildEventListener(myFilteredChildEventListener);
            mRiddlesRef.removeEventListener(mRiddleChildEventListener);
            myRiddleRef.removeEventListener(myRiddleChildEventListener);
        }
        mRiddles.clear();
        notifyDataSetChanged();
    }

    public void addRiddle(Riddle riddle) {
        mRiddlesRef.push().setValue(riddle);
    }

    public void removeRiddle(Riddle riddle) {
        mRiddlesRef.child(riddle.getKey()).removeValue();
    }

    private class RiddleChildEventListener implements ChildEventListener {

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Riddle riddle = dataSnapshot.getValue(Riddle.class);
            riddle.setKey(dataSnapshot.getKey());
            mRiddles.add(0, riddle);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            String key = dataSnapshot.getKey();
            Riddle value = dataSnapshot.getValue(Riddle.class);
            for (Riddle rd : mRiddles) {
                if (rd.getKey().equals(key)) {
                    rd.setValues(value);
                    notifyDataSetChanged();
                    return;
                }
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            String key = dataSnapshot.getKey();
            for (Riddle rd : mRiddles) {
                if (rd.getKey().equals(key)) {
                    mRiddles.remove(rd);
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
