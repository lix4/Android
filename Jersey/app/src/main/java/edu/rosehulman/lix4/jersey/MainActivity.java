package edu.rosehulman.lix4.jersey;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private final static String PREFS = "PREFS";
    private static final String KEY_JERSEY_NAME = "KEY_JERSEY_NAME";
    private Jersey mJersey;
    private TextView mCurrentNameView;
    private TextView mCurrentNumberView;
    private ImageView mCurrentImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        String name = prefs.getString(KEY_JERSEY_NAME, getString(R.string.default_jersey_name));
//        int number=prefs.getInt(,get)
        // Get the other fields. Then use them all

        mJersey = new Jersey();

        mCurrentImageView = (ImageView) findViewById(R.id.image_view);
        mCurrentNameView = (TextView) findViewById(R.id.player_name_view);
        mCurrentNumberView = (TextView) findViewById(R.id.player_number_view);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateItem();
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    private void showCurrentJersey() {
        mCurrentNameView.setText(this.mJersey.getPlayerName());
        mCurrentNumberView.setText(String.valueOf(mJersey.getPlayerNumber()));
        if (mJersey.isRed()) {
            mCurrentImageView.setImageResource(R.drawable.red_jersey);
        } else {
            mCurrentImageView.setImageResource(R.drawable.blue_jersey);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_JERSEY_NAME, mJersey.getPlayerName());
//        editor.putInt();
//        editor.putBoolean();
        // Put the other fields into the editor
        editor.commit();

    }

    private void updateItem() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View view = getLayoutInflater().inflate(R.layout.dialog_add, null);
        builder.setView(view);
        final EditText playerNameEdit = (EditText) view.findViewById(R.id.edit_player_name);
        final EditText playerNumberEdit = (EditText) view.findViewById(R.id.edit_player_number);
        final ToggleButton toggleButton = (ToggleButton) view.findViewById(R.id.toggleButton);
        playerNameEdit.setText(mJersey.getPlayerName());
        playerNumberEdit.setText(String.valueOf(mJersey.getPlayerNumber()));
        toggleButton.setChecked(mJersey.isRed());
        builder.setTitle(R.string.edit_dialog_title);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String playerName = playerNameEdit.getText().toString();
                int playNumber = Integer.parseInt(playerNumberEdit.getText().toString());
                boolean isRed = toggleButton.isChecked();
                mJersey.setPlayerName(playerName);
                mJersey.setPlayerNumber(playNumber);
                mJersey.setISRed(isRed);
                showCurrentJersey();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.action_reset:
                return true;
            case R.id.action_settings:
                startActivity(new Intent(Settings.ACTION_LOCALE_SETTINGS));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
