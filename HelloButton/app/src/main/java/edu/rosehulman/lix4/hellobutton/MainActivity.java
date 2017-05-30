package edu.rosehulman.lix4.hellobutton;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private int mNumClicks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNumClicks = 0;

        final TextView messageTextView = (TextView) findViewById(R.id.messsage_text);
        Button incrementButtom = (Button) findViewById(R.id.button);

        incrementButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNumClicks++;
                String s = getResources().getQuantityString(R.plurals.message_format, mNumClicks, mNumClicks);
                messageTextView.setText(s);
                if (mNumClicks > 10) {
                    messageTextView.setVisibility(TextView.INVISIBLE);
                }
            }
        });
    }
}
