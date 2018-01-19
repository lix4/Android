package edu.rosehulman.lix4.foodrater;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by phillee on 6/19/2017.
 */

public class FoodRecyclerAdapter extends RecyclerView.Adapter<FoodRecyclerAdapter.ViewHolder> {
    private final Map<String, Integer> sDefaultNamesAndIds;
    private Context mContext;
    final List<Food> mFood = new ArrayList<>();
    private Random mRandom = new Random();
    private RecyclerView mRecyclerView;

    public FoodRecyclerAdapter(Context context, RecyclerView recyclerView) {
        mContext = context;
        mRecyclerView = recyclerView;

        //set up the map
        sDefaultNamesAndIds = new HashMap<>();
        sDefaultNamesAndIds.put("banana", R.drawable.banana);
        sDefaultNamesAndIds.put("broccoli", R.drawable.broccoli);
        sDefaultNamesAndIds.put("homemade bread", R.drawable.bread);
        sDefaultNamesAndIds.put("chicken", R.drawable.chicken);
        sDefaultNamesAndIds.put("chocolate", R.drawable.chocolate);
        sDefaultNamesAndIds.put("ice cream", R.drawable.icecream);
        sDefaultNamesAndIds.put("lima beans", R.drawable.limabeans);
        sDefaultNamesAndIds.put("steak", R.drawable.steak);

        mFood.add(0, new Food("banana", sDefaultNamesAndIds.get("banana"), 0));
        mFood.add(0, new Food("broccoli", sDefaultNamesAndIds.get("broccoli"), 0));
        mFood.add(0, new Food("homemade bread", sDefaultNamesAndIds.get("homemade bread"), 0));
        mFood.add(0, new Food("chicken", sDefaultNamesAndIds.get("chicken"), 0));
        mFood.add(0, new Food("chocolate", sDefaultNamesAndIds.get("chocolate"), 0));
        mFood.add(0, new Food("ice cream", sDefaultNamesAndIds.get("ice cream"), 0));
        mFood.add(0, new Food("lima beans", sDefaultNamesAndIds.get("lima beans"), 0));
        mFood.add(0, new Food("steak", sDefaultNamesAndIds.get("steak"), 0));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_view, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Food temp = mFood.get(position);
        holder.itemImageView.setImageResource(temp.getResourceId());
        holder.nameTextView.setText(temp.getName());
        holder.ratingBar.setRating(temp.getRating());
    }

    @Override
    public int getItemCount() {
        return mFood.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView;
        private ImageView itemImageView;
        private RatingBar ratingBar;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    deleteFood(getAdapterPosition());
                    return false;
                }
            });
            nameTextView = (TextView) itemView.findViewById(R.id.item_name);
            itemImageView = (ImageView) itemView.findViewById(R.id.item_image);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    Food temp = mFood.get(getAdapterPosition());
                    temp.setRating(rating);
                }
            });
        }

    }

    public String getRandomName() {
        String[] names = new String[]{
                "banana", "broccoli", "homemade bread",
                "chicken", "chocolate", "ice cream",
                "lima beans", "steak"
        };
        return names[mRandom.nextInt(names.length)];
    }

    public String addFood() {
        String randomName = getRandomName();
        int id = sDefaultNamesAndIds.get(randomName);
        mFood.add(0, new Food(randomName, id, 0));
        mRecyclerView.scrollToPosition(0);
        notifyItemInserted(0);
        return randomName;
    }


    private void deleteFood(int position) {
        mFood.remove(position);
        notifyItemRemoved(position);
    }
}
