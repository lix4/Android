package edu.rosehulman.lix4.namerecycleradapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by phillee on 6/17/2017.
 */

public class NameAdapter extends RecyclerView.Adapter<NameAdapter.ViewHolder> {
    private Context mContext;
    final ArrayList<String> mNames = new ArrayList<>();
    private Random mRandom = new Random();

    public NameAdapter(Context context) {
        mContext = context;
        for (int i = 0; i < 5; i++) {
            mNames.add(getRandomName());
        }
    }

    private String getRandomName() {
        String[] names = new String[]{
                "Hannah", "Emily", "Sarah", "Madison", "Brianna",
                "Kaylee", "Kaitlyn", "Hailey", "Alexis",
                "Elizabeth", "Michael", "Jacob", "Mathew", "Nicholas",
                "Christopher", "Joseph", "Zachary", "Joshua", "Andrew",
                "William"
        };
        return names[mRandom.nextInt(names.length)];
    }

    @Override
    public int getItemCount() {
        return mNames.size();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.name_view, parent, false);
        return new ViewHolder(itemView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView positionTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.name_view);
            positionTextView = (TextView) itemView.findViewById(R.id.position_view);

        }


    }

    @Override
    public void onBindViewHolder(NameAdapter.ViewHolder holder, int position) {

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.name_view, parent, false);
        } else {
            view = convertView;
        }
        String currentName = mNames.get(position);
        TextView nameTextView = (TextView) view.findViewById(R.id.name_view);
        TextView numberTextView = (TextView) view.findViewById(R.id.position_view);
        nameTextView.setText(currentName);
        numberTextView.setText(String.format("I am on #%d", (position + 1)));
        return view;
    }

    public void addName() {
        mNames.add(0, getRandomName());
        notifyDataSetChanged();
    }

    public void deleteName(int position) {
        mNames.remove(position);
        notifyDataSetChanged();
    }

}
