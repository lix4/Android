package edu.rosehulman.lix4.midtermlix4.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.rosehulman.lix4.midtermlix4.R;
import edu.rosehulman.lix4.midtermlix4.adapters.EquationAdapter;
import edu.rosehulman.lix4.midtermlix4.model.MathProblemSet;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LinearFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LinearFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SET = "problemSet";

    // TODO: Rename and change types of parameters
    private MathProblemSet mMathProblemSet;

    private EquationAdapter mAdapter;

    public LinearFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param mathProblemSet Parameter 1.
     * @return A new instance of fragment LinearFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LinearFragment newInstance(MathProblemSet mathProblemSet) {
        LinearFragment fragment = new LinearFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_linear, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.linear_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        mAdapter = new EquationAdapter(getContext(), mMathProblemSet);
        recyclerView.setAdapter(mAdapter);
        return view;
    }

    public void resetAdapter() {
        mAdapter.reset();
    }

    public void setCurrentType(MathProblemSet.Type type) {
        mAdapter.setmCurrentType(type);
    }

    public MathProblemSet getMathSetFromAdapter() {
        return mAdapter.getmMathProblemSet();
    }

//    public interface CallBack {
//        void removeEquation(int position);
//    }

}
