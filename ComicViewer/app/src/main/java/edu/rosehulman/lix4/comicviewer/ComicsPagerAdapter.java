package edu.rosehulman.lix4.comicviewer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;


import java.util.ArrayList;
import java.util.List;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class ComicsPagerAdapter extends FragmentStatePagerAdapter {
    private List<ComicWrapper> mWrappers;
    private List<Integer> mColors;
    private List<String> mJokes;
    private int colorCounter = 0;


    public ComicsPagerAdapter(FragmentManager fm) {
        super(fm);
        mWrappers = new ArrayList<>();
        mColors = new ArrayList<>();
        mColors.add(android.R.color.holo_green_light);
        mColors.add(android.R.color.holo_blue_light);
        mColors.add(android.R.color.holo_orange_light);
        mColors.add(android.R.color.holo_red_light);
        mJokes = new ArrayList<>();
        mJokes.add("-What do you call bears with no ears? -B");
        mJokes.add("I went in to a pet shop. I said, “Can I buy a goldfish?” The guy said, “Do you want an aquarium?”");
        mJokes.add("What is red and smells like blue paint?\n" +
                "\n" +
                "Red paint.");

        for (int i = 0; i < 5; i++) {
            ComicWrapper cw = new ComicWrapper();
            cw.setColor(mColors.get(colorCounter % 4));
            cw.setXkcdIssue(Utils.getRandomCleanIssue());
            cw.setJoke(mJokes.get(colorCounter % 3));
            mWrappers.add(cw);
            colorCounter++;
        }
    }

    public void addWrapper() {
        ComicWrapper cw = new ComicWrapper();
        cw.setXkcdIssue(Utils.getRandomCleanIssue());
        cw.setColor(mColors.get(colorCounter % 4));
        cw.setJoke(mJokes.get(colorCounter % 3));
        colorCounter++;
        mWrappers.add(cw);
        notifyDataSetChanged();
    }


    public ComicWrapper getWrapper(int position) {
        return mWrappers.get(position);
    }


    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a ComicFragment (defined as a static inner class below).
//            return ComicFragment.newInstance(position + 1);
        return ComicFragment.newInstance(mWrappers.get(position));
    }

    @Override
    public int getCount() {
        // Show 5 total pages.
        return mWrappers.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Issue " + mWrappers.get(position).getXkcdIssue();
    }
}