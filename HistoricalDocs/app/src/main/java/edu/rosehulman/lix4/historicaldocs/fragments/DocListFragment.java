package edu.rosehulman.lix4.historicaldocs.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.rosehulman.lix4.historicaldocs.Doc;
import edu.rosehulman.lix4.historicaldocs.DocListAdapter;
import edu.rosehulman.lix4.historicaldocs.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CallBack mListe} interface
 * to handle interaction events.
 */
public class DocListFragment extends Fragment {

    private CallBack mCallBack;

    public DocListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        RecyclerView view = (RecyclerView) inflater.inflate(R.layout.fragment_doc_list, container, false);
        view.setLayoutManager(new LinearLayoutManager(getContext()));
        DocListAdapter adapter = new DocListAdapter(getContext(), mCallBack);
        view.setAdapter(adapter);
        return view;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface CallBack {
        // TODO: Update argument type and name
        void onDocSelected(Doc doc);
    }
}
