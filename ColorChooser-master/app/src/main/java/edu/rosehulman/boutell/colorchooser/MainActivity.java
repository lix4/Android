package edu.rosehulman.boutell.colorchooser;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private RelativeLayout mLayout;
    private TextView mTextView;
    private String mMessage = "This is your phone. Please rescue me!";
    private int mBackgroundColor = Color.GREEN;
    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    public static final String EXTRA_COLOR = "EXTRA_COLOR";
    private static final int REQUEST_CODE_INPUT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Send an email with the message field as the body
                sendEmail();

            }
        });

        // Capture
        mLayout = (RelativeLayout) findViewById(R.id.content_main_layout);
        mTextView = (TextView) findViewById(R.id.content_main_message);

        // Set color and text
        updateUI();
    }

    private void sendEmail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, "xy@gmail.com");
        intent.putExtra(Intent.EXTRA_SUBJECT, "From me");
        intent.putExtra(Intent.EXTRA_TEXT, mMessage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_info:
                // TODO: Launch a new Info Activity that is a ScrollingActivity.
                Intent infoIntent = new Intent(this, InfoActivity.class);
                startActivity(infoIntent);
                return true;

            case R.id.action_change_color:
                // TODO: Launch the InputActivity to get a result
                Intent inputIntent = new Intent(this, InputActivity.class);
                inputIntent.putExtra(EXTRA_MESSAGE, mMessage);
                inputIntent.putExtra(EXTRA_COLOR, mBackgroundColor);
                startActivityForResult(inputIntent, REQUEST_CODE_INPUT);
                return true;
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_INPUT && resultCode == Activity.RESULT_OK) {
            mMessage = data.getStringExtra(MainActivity.EXTRA_MESSAGE);
            mBackgroundColor = data.getIntExtra(MainActivity.EXTRA_COLOR, Color.GRAY);
            updateUI();
        }
    }

    private void updateUI() {
        mTextView.setText(mMessage);
        mLayout.setBackgroundColor(mBackgroundColor);
    }


}
