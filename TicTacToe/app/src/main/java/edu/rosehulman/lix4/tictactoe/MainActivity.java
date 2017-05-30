package edu.rosehulman.lix4.tictactoe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TicTacToeGame mGame;
    private TextView mGameStateTextView;
    private Button[][] mTicTacToeButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGame = new TicTacToeGame(this);
        mGame.resetGame();

        Button newGameButton = (Button) findViewById(R.id.new_game_button);
        newGameButton.setOnClickListener(this);
        mGameStateTextView = (TextView) findViewById(R.id.game_state_text_view);

        mTicTacToeButtons = new Button[TicTacToeGame.NUM_ROWS][TicTacToeGame.NUM_COLUMNS];
        for (int i = 0; i < TicTacToeGame.NUM_ROWS; i++) {
            for (int j = 0; j < TicTacToeGame.NUM_COLUMNS; j++) {
                int id = getResources().getIdentifier("button" + i + j, "id", getPackageName());
                mTicTacToeButtons[i][j] = (Button) findViewById(id);
                mTicTacToeButtons[i][j].setOnClickListener(this);
            }
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.new_game_button) {
            mGame.resetGame();
        }

        for (int i = 0; i < TicTacToeGame.NUM_ROWS; i++) {
            for (int j = 0; j < TicTacToeGame.NUM_COLUMNS; j++) {
                if (v.getId() == mTicTacToeButtons[i][j].getId()) {
                    Log.d("TTT", "button pressed at " + i + j);
                    mGame.pressedButtonAtLocation(i, j);
                }
                mTicTacToeButtons[i][j].setText(mGame.stringForButtonAtLocation(i, j));
            }
        }


        mGameStateTextView.setText(mGame.stringForGameState());
    }
}
