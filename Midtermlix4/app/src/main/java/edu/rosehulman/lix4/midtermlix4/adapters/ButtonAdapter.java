package edu.rosehulman.lix4.midtermlix4.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import edu.rosehulman.lix4.midtermlix4.R;
import edu.rosehulman.lix4.midtermlix4.model.MathProblem;
import edu.rosehulman.lix4.midtermlix4.model.MathProblemSet;

/**
 * Created by phillee on 7/2/2017.
 */

public class ButtonAdapter extends RecyclerView.Adapter<ButtonAdapter.ViewHolder> {
    private Context mContext;
    private List<MathProblem> mMathProblems;
    private List<MathProblem> mOrderedMathProblems;
    private MathProblemSet mMathProblemSet;
    private MathProblemSet.Type mCurrentType;

    public ButtonAdapter(Context context, MathProblemSet mathProblemSet) {
        mContext = context;
        mMathProblemSet = mathProblemSet;
        mMathProblems = mMathProblemSet.getmMathProblems();
        mCurrentType = mathProblemSet.getmCurrentType();
        mOrderedMathProblems = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            int first = i + 1;
            for (int j = 0; j < 5; j++) {
                int second = j + 1;
                MathProblem newProblem = new MathProblem(i, j, first, second, mCurrentType);
                newProblem.setSolved(true);
                for (MathProblem problem : mMathProblems) {
                    if (problem.getFirst() == newProblem.getFirst()
                            && problem.getSecond() == newProblem.getSecond()) {
                        newProblem.setSolved(false);
                    }
                }
                Log.d("ButtonAdapter: ", newProblem.isSolved() + "");
                mOrderedMathProblems.add(newProblem);
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View equationView = LayoutInflater.from(parent.getContext()).inflate(R.layout.equation_button, parent, false);
        return new ViewHolder(equationView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.equationButton.setText(mOrderedMathProblems.get(position).getProblemString());
        if (mOrderedMathProblems.get(position).isSolved()) {
            Log.d("onBindViewHolder: ", "--------------------");
            Log.d("onBindViewHolder: ", position + "");
            holder.equationButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mOrderedMathProblems.size();
    }

    public void reset() {
        mMathProblemSet.setCurrentType(mCurrentType);
        mMathProblemSet.reset();
        mMathProblems = mMathProblemSet.getmMathProblems();
        mOrderedMathProblems = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            int first = i + 1;
            for (int j = 0; j < 5; j++) {
                int second = j + 1;
                MathProblem newProblem = new MathProblem(i, j, first, second, mCurrentType);
                newProblem.setSolved(false);
                mOrderedMathProblems.add(newProblem);
            }
        }
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
        private Button equationButton;

        public ViewHolder(View equationView) {
            super(equationView);
            equationView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(v, mOrderedMathProblems.get(getAdapterPosition()).getProblemWithAnswerString() + "(Swipe if got it)", Snackbar.LENGTH_INDEFINITE)
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
                                        MathProblem current = mOrderedMathProblems.get(getAdapterPosition());
                                        current.setSolved(true);
                                        for (MathProblem mp : mMathProblems) {
                                            if (mp.getFirst() == current.getFirst()
                                                    && mp.getSecond() == current.getSecond()) {
                                                mMathProblemSet.remove(mp);
                                                break;
                                            }
                                        }
                                        mMathProblems = mMathProblemSet.getmMathProblems();
                                        if (mMathProblems.size() == 0) {
                                            final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                            builder.setTitle(mContext.getResources().getString(R.string.dialog_win));
                                            builder.setNegativeButton(android.R.string.cancel, null);
                                            builder.show();
                                        }
                                        notifyDataSetChanged();
                                    }
                                }
                            })
                            .show();
                }
            });
            equationButton = (Button) equationView.findViewById(R.id.equation_button);
        }
    }
}
