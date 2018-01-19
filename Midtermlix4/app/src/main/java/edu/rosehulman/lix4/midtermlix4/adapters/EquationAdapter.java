package edu.rosehulman.lix4.midtermlix4.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import edu.rosehulman.lix4.midtermlix4.R;
import edu.rosehulman.lix4.midtermlix4.model.MathProblem;
import edu.rosehulman.lix4.midtermlix4.model.MathProblemSet;

/**
 * Created by phillee on 7/2/2017.
 */

public class EquationAdapter extends RecyclerView.Adapter<EquationAdapter.ViewHolder> {
    private Context mContext;
    private List<MathProblem> mMathProblems;
    private MathProblemSet.Type mCurrentType;
    private MathProblemSet mMathProblemSet;


    public EquationAdapter(Context context, MathProblemSet mathProblemSet) {
        mContext = context;
        mMathProblemSet = mathProblemSet;
        mMathProblems = mMathProblemSet.getmMathProblems();
        mCurrentType = mathProblemSet.getmCurrentType();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View equationView = LayoutInflater.from(parent.getContext()).inflate(R.layout.equation_view, parent, false);
        return new ViewHolder(equationView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d("onBindViewHolder: ", mMathProblems.get(position).getProblemString());
        holder.equationTextView.setText(mMathProblems.get(position).getProblemString());
    }

    @Override
    public int getItemCount() {
        return mMathProblems.size();
    }

    public void reset() {
//        MathProblemSet mathProblemSet = new MathProblemSet(1, 5, getmCurrentType());
//        mMathProblems.clear();
//        for (int i = 0; i < mathProblemSet.getNumProblems(); i++) {
//            mMathProblems.add(mathProblemSet.getProblem(i));
//        }
        mMathProblemSet.setCurrentType(mCurrentType);
        mMathProblemSet.reset();
        mMathProblems = mMathProblemSet.getmMathProblems();
        notifyDataSetChanged();
    }

    public MathProblemSet.Type getmCurrentType() {
        return mCurrentType;
    }

    public void setmCurrentType(MathProblemSet.Type mCurrentType) {
        this.mCurrentType = mCurrentType;
    }

    public MathProblemSet getmMathProblemSet() {
        return mMathProblemSet;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView equationTextView;

        public ViewHolder(View equationView) {
            super(equationView);
            equationView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(v, mMathProblems.get(getAdapterPosition()).getProblemWithAnswerString() + "(Swipe if got it)", Snackbar.LENGTH_INDEFINITE)
                            .setAction("OOPS!", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //Nothing happens here.
                                }
                            })
                            .addCallback(new Snackbar.Callback() {
                                @Override
                                public void onDismissed(Snackbar snackbar, int event) {
                                    //see Snackbar.Callback docs for event details
                                    if (event == 0) {
                                        int position = getAdapterPosition();
                                        remove(position);
                                        if (mMathProblems.size() == 0) {
                                            final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                            builder.setTitle(mContext.getResources().getString(R.string.dialog_win));
                                            builder.setNegativeButton(android.R.string.cancel, null);
                                            builder.show();
                                        }
                                    }
                                }
                            })
                            .show();
                }
            });
            equationTextView = (TextView) equationView.findViewById(R.id.equation_name_view);

        }
    }

    private void remove(int position) {
        mMathProblemSet.remove(position);
        mMathProblems = mMathProblemSet.getmMathProblems();
        notifyItemRemoved(position);
    }
}
