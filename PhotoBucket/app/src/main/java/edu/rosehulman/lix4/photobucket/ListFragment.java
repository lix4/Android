package edu.rosehulman.lix4.photobucket;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {
    private PicAdapter mAdapter;
    private CallBack mCallBack;
    private String mCurrentUid;

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mCurrentUid = getArguments().getString(Constants.FIREBASE_UID);
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.linear_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        mAdapter = new PicAdapter(getContext(), mCallBack, mCurrentUid, view);
        recyclerView.setAdapter(mAdapter);
        return view;
    }

    public void addToList(Pic pic) {
        mAdapter.add(pic);
    }

    public void showOnly(boolean switsh) {
        mAdapter.showOnly(switsh);
    }

    public void removeFromList(Pic pic) {
        mAdapter.remove(pic);
    }

    public void updateInList(Pic pic, String newCaption, String newUrl) {
        mAdapter.update(pic, newCaption, newUrl);
    }

    public interface CallBack {
        void onPicSelected(Pic pic);

        void showEditRemoveDialog(Pic pic);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CallBack) {
            mCallBack = (CallBack) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement CallBack");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallBack = null;
    }

}
