package edu.rosehulman.lix4.midtermlix4.model;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Matt Boutell.
 * Rose-Hulman Institute of Technology.
 * Covered by MIT license.
 */
public class MathProblemSet implements Parcelable {

    private final int nRows;
    private final int nColumns;
    private List<MathProblem> mMathProblems;
    private Type mCurrentType = Type.MULTIPLICATION;

    protected MathProblemSet(Parcel in) {
        nRows = in.readInt();
        nColumns = in.readInt();
    }

    public static final Creator<MathProblemSet> CREATOR = new Creator<MathProblemSet>() {
        @Override
        public MathProblemSet createFromParcel(Parcel in) {
            return new MathProblemSet(in);
        }

        @Override
        public MathProblemSet[] newArray(int size) {
            return new MathProblemSet[size];
        }
    };

    public void setCurrentType(Type type) {
        mCurrentType = type;
    }

    public Type getmCurrentType() {
        return mCurrentType;
    }

    public void reset() {
        mMathProblems = new ArrayList<>();
        for (int row = 0; row < nRows; row++) {
            int first = row + 1;
            for (int column = 0; column < nColumns; column++) {
                int second = column + 1;
                mMathProblems.add(new MathProblem(row, column, first, second, mCurrentType));
            }
        }
        Collections.shuffle(mMathProblems);
    }

    public List<MathProblem> getmMathProblems() {
        return mMathProblems;
    }

    public void setmMathProblems(List<MathProblem> mMathProblems) {
        this.mMathProblems = mMathProblems;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(nRows);
        dest.writeInt(nColumns);
    }

    public enum Type {
        ADDITION, SUBTRACTION, MULTIPLICATION, DIVISION
    }

    public MathProblemSet() {
        this(1, 5, Type.MULTIPLICATION);
    }

    /**
     * Creates a set of problems in which the range of values is [min, max] for both
     * the first and second operand. That is, it assumes they can all be arranged in a square.
     *
     * @param min
     * @param max
     * @param type
     */
    public MathProblemSet(int min, int max, Type type) {
        mMathProblems = new ArrayList<>();
        nRows = max - min + 1;
        nColumns = max - min + 1;

        for (int row = 0; row < nRows; row++) {
            int first = row + min;
            for (int column = 0; column < nColumns; column++) {
                int second = column + min;
                mMathProblems.add(new MathProblem(row, column, first, second, type));
            }
        }
        Collections.shuffle(mMathProblems);
    }

    /**
     * Gets the math problem at the given position in the list.
     * Throws an exception if it is requesting a position out of bounds.
     *
     * @param position
     * @return The requested math problem
     */
    public MathProblem getProblem(int position) {
        if (position >= 0 && position < mMathProblems.size()) {
            return mMathProblems.get(position);
        } else {
            throw new ArrayIndexOutOfBoundsException("Requesting problem out of bounds");
        }
    }

    /**
     * Gets the math problem at the given position in the GRID,
     * or null if no such problem exists.
     *
     * @param row
     * @param col
     * @return The requested math problem.
     */
    public MathProblem getProblem(int row, int col) {
        for (MathProblem mathProblem : mMathProblems) {
            if (mathProblem.getRow() == row && mathProblem.getColumn() == col) {
                return mathProblem;
            }
        }
        return null;
    }

    /**
     * Removes the problem at the given position.
     *
     * @param position
     * @return True if removed, false otherwise
     */
    public boolean remove(int position) {
        if (position >= 0 && position < mMathProblems.size()) {
            mMathProblems.remove(position);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Removes the problem passed in
     *
     * @param mathProblem
     * @return True if removed, false otherwise
     */
    public boolean remove(MathProblem mathProblem) {
        return mMathProblems.remove(mathProblem);
    }

    /**
     * @return How many math problems are in the list.
     */
    public int getNumProblems() {
        return mMathProblems.size();
    }

    /**
     * Ignore: experimental use by Dr. B.
     *
     * @param canvas
     * @param bitmapWidth
     * @param bitmapHeight
     * @param paint
     */
    public void draw(Canvas canvas, int bitmapWidth, int bitmapHeight, Paint paint) {
        float boxHeight = bitmapHeight / (float) nRows;
        float boxWidth = bitmapWidth / (float) nColumns;

        for (MathProblem mp : mMathProblems) {
            // Draw
            mp.draw(canvas, boxWidth, boxHeight, paint);
        }
    }

    @Override
    public String toString() {
        return mMathProblems.size() + "";
    }

}
