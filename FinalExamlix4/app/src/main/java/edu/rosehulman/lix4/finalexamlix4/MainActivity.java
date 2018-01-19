package edu.rosehulman.lix4.finalexamlix4;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

import static edu.rosehulman.lix4.finalexamlix4.Constants.currentOwner;

public class MainActivity extends AppCompatActivity {

    private RiddleAdapter mAdapter;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mAdapter.showOnly(0, null);
                    return true;
                case R.id.navigation_dashboard:
                    mAdapter.showOnly(1, null);
                    return true;
                case R.id.navigation_notifications:
                    showFilterDialog();
                    return true;
            }
            return false;
        }

    };

    private void showFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.dialog_filter_username_title));
        final EditText editTextChangeUser = (EditText) getLayoutInflater().inflate(R.layout.edit_text_change_user, null);
        builder.setView(editTextChangeUser);
        builder.setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newUserName = editTextChangeUser.getText().toString();
                mAdapter.showOnly(2, newUserName);
            }
        }).setNegativeButton(android.R.string.cancel, null);
        builder.create().show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        mAdapter = new RiddleAdapter(this);
        recyclerView.setAdapter(mAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDialog();
            }
        });

        SharedPreferences prefs = getSharedPreferences(Constants.PREFS, MODE_PRIVATE);
        Constants.currentOwner = prefs.getString("name", "");

        Log.d("currentOwner: ", currentOwner);
    }


    private void showAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.dialog_new_riddle_title));
        View view = getLayoutInflater().inflate(R.layout.edit_text_add_riddle, null);
        final EditText editRiddleQuestionText = (EditText) view.findViewById(R.id.edit_text_add_riddle_question);
        final EditText editRiddleAnswerText = (EditText) view.findViewById(R.id.edit_text_add_answer);
        builder.setView(view);
        final Riddle newRiddle = new Riddle();
        builder.setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String riddleText = editRiddleQuestionText.getText().toString();
                String riddleAnswer = editRiddleAnswerText.getText().toString();
                newRiddle.setQuestion(riddleText);
                newRiddle.setAnswer(riddleAnswer);
                newRiddle.setOwner(Constants.currentOwner);
                mAdapter.addRiddle(newRiddle);
            }
        }).setNegativeButton(android.R.string.cancel, null)
                .setNeutralButton(R.string.add_new_recipients, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final Map<String, Boolean> recipientsMap = new HashMap<>();
                        AlertDialog.Builder anotherBuilder = new AlertDialog.Builder(MainActivity.this);
                        anotherBuilder.setTitle("Add Recipients")
                                .setMultiChoiceItems(Constants.CLASSMATES, new boolean[]{false, false, false, false, false, false, false, false, false, false, false, false}, new DialogInterface.OnMultiChoiceClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                        Log.d("onClick: ", which + " " + isChecked);
                                        recipientsMap.put(Constants.CLASSMATES[which], isChecked);
                                    }
                                })
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String riddleText = editRiddleQuestionText.getText().toString();
                                        String riddleAnswer = editRiddleAnswerText.getText().toString();
                                        newRiddle.setQuestion(riddleText);
                                        newRiddle.setAnswer(riddleAnswer);
                                        newRiddle.setOwner(Constants.currentOwner);
                                        newRiddle.setRecipients(recipientsMap);
                                        mAdapter.addRiddle(newRiddle);
                                    }
                                })
                                .setNegativeButton(android.R.string.cancel, null)
                                .create().show();
                    }
                })
                .create()
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                showSetOwnerDialog();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showSetOwnerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.dialog_set_owner_title));
        final EditText editSetOwner = (EditText) getLayoutInflater().inflate(R.layout.edit_text_set_owner, null);
        builder.setView(editSetOwner);
        builder.setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newOwnerName = editSetOwner.getText().toString();
                currentOwner = newOwnerName;
                SharedPreferences.Editor editor = getSharedPreferences(Constants.PREFS, MODE_PRIVATE).edit();
                editor.putString("name", newOwnerName);
                editor.apply();
            }
        }).setNegativeButton(android.R.string.cancel, null);
        builder.create().show();
    }
}
