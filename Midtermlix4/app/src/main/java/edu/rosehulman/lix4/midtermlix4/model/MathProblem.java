package edu.rosehulman.lix4.midtermlix4.model;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Represents an individual arithmetic problem
 * <p>
 * Created by Matt Boutell.
 * Rose-Hulman Institute of Technology.
 * Covered by MIT license.
 */
public class MathProblem {

    private int first;
    private int second;
    private int answer;
    private MathProblemSet.Type type;
    private String operation;
    private int row;
    private int column;
    private boolean solved;

    /**
     * Creates a math problem of the given type.
     *
     * @param row
     * @param column
     * @param first
     * @param second
     * @param type
     */
    public MathProblem(int row, int column, int first, int second, MathProblemSet.Type type) {
        this.row = row;
        this.column = column;
        switch (type) {
            case ADDITION:
                this.first = first;
                this.second = second;
                this.answer = first + second;
                this.operation = "+";
                break;
            case SUBTRACTION:
                this.first = first + second;
                this.second = second;
                this.answer = first;
                this.operation = "-";
                break;
            case MULTIPLICATION:
                this.first = first;
                this.second = second;
                this.answer = first * second;
                this.operation = "\u2715";
                break;
            case DIVISION:
                this.first = first * second;
                this.second = second;
                this.answer = first;
                this.operation = "\u00F7";
                break;
            default:
                throw new ArithmeticException("Unrecognized math operation");
        }
    }

    public void setType(MathProblemSet.Type type) {
        this.type = type;
    }


    public int getFirst() {
        return first;
    }

    public int getSecond() {
        return second;
    }

    public int getRow() {
        return this.row;
    }

    public int getColumn() {
        return this.column;
    }

    /**
     * You may choose to use this if you like. I actually didn't use this in my solution
     * - it was for another idea I'm working on beyond the scope of the exam.
     *
     * @param potentialAnswer
     * @return True if the given answer is correct, false otherwise.
     */
    public boolean isCorrect(String potentialAnswer) {
        this.solved = (Integer.parseInt(potentialAnswer) == this.answer);
        return this.solved;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    /**
     * @return The statement of the problem.
     */
    public String getProblemString() {
        return this.first + " " + this.operation + " " + this.second;
    }

    /**
     * @return The problem and answer (as the name implies)
     */
    public String getProblemWithAnswerString() {
        return getProblemString() + " = " + this.answer;
    }


    /**
     * Ignore: experimental use by Dr. B.
     *
     * @param canvas
     * @param boxWidth
     * @param boxHeight
     * @param paint
     */
    public void draw(Canvas canvas, float boxWidth, float boxHeight, Paint paint) {
        if (!solved) {
            canvas.drawRect(row * boxWidth, column * boxHeight, (row + 1) * boxWidth, (column + 1) * boxHeight, paint);
        }
    }

}
