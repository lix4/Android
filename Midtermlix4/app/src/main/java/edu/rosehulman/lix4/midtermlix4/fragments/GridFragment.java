package edu.rosehulman.lix4.midtermlix4.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.rosehulman.lix4.midtermlix4.R;
import edu.rosehulman.lix4.midtermlix4.adapters.ButtonAdapter;
import edu.rosehulman.lix4.midtermlix4.model.MathProblemSet;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GridFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GridFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SET = "problemSet";

    // TODO: Rename and change types of parameters
    private MathProblemSet mMathProblemSet;

    private ButtonAdapter mAdapter;


    public GridFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param mathProblemSet Parameter 1.
     * @return A new instance of fragment GridFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GridFragment newInstance(MathProblemSet mathProblemSet) {
        GridFragment fragment = new GridFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_SET, mathProblemSet);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMathProblemSet = getArguments().getParcelable(ARG_SET);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_grid, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.grid_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 5));
        recyclerView.setHasFixedSize(true);
        Log.d("onCreateView: ", mMathProblemSet.toString());
        mAdapter = new ButtonAdapter(getActivity(), mMathProblemSet);
        recyclerView.setAdapter(mAdapter);
        return view;
    }

    public void resetAdapter() {
        mAdapter.reset();
    }

    public MathProblemSet getMathSetFromAdapter() {
        return mAdapter.getmMathProblemSet();
    }

    public void setCurrentType(MathProblemSet.Type type) {
        mAdapter.setmCurrentType(type);
    }
}
