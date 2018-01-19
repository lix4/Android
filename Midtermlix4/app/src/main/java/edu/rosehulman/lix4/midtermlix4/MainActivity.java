package edu.rosehulman.lix4.midtermlix4;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import edu.rosehulman.lix4.midtermlix4.fragments.GridFragment;
import edu.rosehulman.lix4.midtermlix4.fragments.LinearFragment;
import edu.rosehulman.lix4.midtermlix4.model.MathProblemSet;

public class MainActivity extends AppCompatActivity {

    private boolean toggle;
    private LinearFragment mLinearFragment = null;
    private GridFragment mGridFragment = null;
    private MathProblemSet mMathProblemSet;
    private MathProblemSet.Type mCurrentType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);

        mMathProblemSet = new MathProblemSet();
        mCurrentType = MathProblemSet.Type.MULTIPLICATION;

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        mLinearFragment = LinearFragment.newInstance(mMathProblemSet);
        ft.add(R.id.fragment_container, mLinearFragment);
        ft.commit();

        toggle = true;
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

        switch (id) {
            case R.id.action_settings:
                showSettingDialog();
                break;
            case R.id.action_revert:
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getResources().getString(R.string.dialog_reset));
                builder.setMessage(getResources().getString(R.string.dialog_reset_message));
                builder.setNegativeButton(android.R.string.cancel, null);
                builder.setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (toggle) {
                            mLinearFragment.setCurrentType(mCurrentType);
                            mLinearFragment.resetAdapter();
                            mMathProblemSet = mLinearFragment.getMathSetFromAdapter();
                        } else {
                            mGridFragment.setCurrentType(mCurrentType);
                            mGridFragment.resetAdapter();
                            mMathProblemSet = mGridFragment.getMathSetFromAdapter();
                        }
                    }
                });
                builder.show();
                break;
            case R.id.action_toggle:
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                if (toggle) {
                    mMathProblemSet = mLinearFragment.getMathSetFromAdapter();
                    mGridFragment = GridFragment.newInstance(mMathProblemSet);
                    ft.replace(R.id.fragment_container, mGridFragment);
                } else {
                    mMathProblemSet = mGridFragment.getMathSetFromAdapter();
                    mLinearFragment = LinearFragment.newInstance(mMathProblemSet);
                    ft.replace(R.id.fragment_container, mLinearFragment);
                }
                toggle = !toggle;
                ft.commit();
                break;
        }
        return true;
    }

    private void showSettingDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.dialog_title));
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setItems(new String[]{
                "Addition",
                "Substraction",
                "Multiplication",
                "Division"
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        mCurrentType = MathProblemSet.Type.ADDITION;
                        break;
                    case 1:
                        mCurrentType = MathProblemSet.Type.SUBTRACTION;
                        break;
                    case 2:
                        mCurrentType = MathProblemSet.Type.MULTIPLICATION;
                        break;
                    case 3:
                        mCurrentType = MathProblemSet.Type.DIVISION;
                        break;
                }

                if (toggle) {
                    mLinearFragment.setCurrentType(mCurrentType);
                    mLinearFragment.resetAdapter();
                    mMathProblemSet = mLinearFragment.getMathSetFromAdapter();
                } else {
                    mGridFragment.setCurrentType(mCurrentType);
                    mGridFragment.resetAdapter();
                    mMathProblemSet = mGridFragment.getMathSetFromAdapter();
                }

                dialog.dismiss();
            }
        });
        builder.show();
    }
}
