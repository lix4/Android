package edu.rosehulman.lix4.lightsout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private LightsOutGame mGame;
    private Button[] mButtons;
    private Button mResetButton;
    private TextView mGameStateView;
    private boolean win = false;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("NUM_PRESS", mGame.getNumPresses());
        outState.putIntArray("MY_BUTTONS", mGame.getValues());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mGame = new LightsOutGame();
        mGameStateView = (TextView) findViewById(R.id.game_status_display_view);
        if (savedInstanceState != null) {
            Log.d("here", "here!!!");
            mGame.setAllValues(savedInstanceState.getIntArray("MY_BUTTONS"));
            mGame.setNumPresses(savedInstanceState.getInt("NUM_PRESS"));
            if (mGame.getNumPresses() != 0) {
                String s = getResources().getQuantityString(R.plurals.message_format, mGame.getNumPresses(), mGame.getNumPresses());
                mGameStateView.setText(s);
            } else {
                mGameStateView.setText(R.string.message_start);
            }
        }

        mButtons = new Button[7];
        for (int i = 0; i < mButtons.length; i++) {
            int id = getResources().getIdentifier("button" + i, "id", getPackageName());
            int value = mGame.getValueAtIndex(i);
            mButtons[i] = (Button) findViewById(id);
            mButtons[i].setOnClickListener(this);
            mButtons[i].setText(value + "");
        }

        win = mGame.checkForWin();
        if (win) {
            mGameStateView.setText(R.string.message_win);
            for (Button b : mButtons) {
                b.setEnabled(false);
            }
        }

        mResetButton = (Button) findViewById(R.id.reset_button);
        mResetButton.setOnClickListener(this);

    }

    private void resetGame() {
        mGame = new LightsOutGame();
        for (int i = 0; i < mButtons.length; i++) {
            int value = mGame.getValueAtIndex(i);
            mButtons[i].setEnabled(true);
            mButtons[i].setText(value + "");
        }
        win = mGame.checkForWin();
        mGameStateView.setText(R.string.message_start);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == mResetButton.getId()) {
            resetGame();
        }

        for (int i = 0; i < mButtons.length; i++) {
            if (v.getId() == mButtons[i].getId()) {
                win = mGame.pressedButtonAtIndex(i);
            }
        }

        for (int i = 0; i < mButtons.length; i++) {
            mButtons[i].setText(mGame.getValueAtIndex(i) + "");
        }

        int numPress = mGame.getNumPresses();
        if (numPress != 0) {
            String s = getResources().getQuantityString(R.plurals.message_format, mGame.getNumPresses(), mGame.getNumPresses());
            mGameStateView.setText(s);
        }

        if (win) {
            mGameStateView.setText(R.string.message_win);
            for (Button b : mButtons) {
                b.setEnabled(false);
            }
        }
    }
}
