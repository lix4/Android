package edu.rosehulman.lix4.pointofsale;

import android.support.v7.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.audiofx.BassBoost;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private TextView mNameText;
    private TextView mQuantityText;
    private TextView mDeliveryDateText;
    private Item mCurrentItem;
    private ArrayList<Item> mItems = new ArrayList<Item>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mNameText = (TextView) findViewById(R.id.name_text);
        mQuantityText = (TextView) findViewById(R.id.quantity_text);
        mDeliveryDateText = (TextView) findViewById(R.id.date_text);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEditItem(false);
//                mCurrentItem = Item.getDefaultItem();
//                showCurrentItem();
//                Toast.makeText(MainActivity.this, "Item added", Toast.LENGTH_LONG).show();
            }
        });

        registerForContextMenu(mNameText);

    }

    private void addEditItem(final boolean isEditing) {
        DialogFragment df = new DialogFragment() {
            //            @NonNull
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_add, null);
                builder.setView(view);
                final EditText nameEditText = (EditText) view.findViewById(R.id.edit_name);
                final EditText quantityEditText = (EditText) view.findViewById(R.id.edit_quantity);
                final CalendarView deliveryDateView = (CalendarView) view.findViewById(R.id.calendar_view);
                if (isEditing) {
                    nameEditText.setText(mCurrentItem.getName());
                    quantityEditText.setText(String.valueOf(mCurrentItem.getQuantity()));
                    deliveryDateView.setDate(mCurrentItem.getDeliveryDateTime());
                }
                builder.setNegativeButton(android.R.string.cancel, null);
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = nameEditText.getText().toString();
                        int quantity = Integer.parseInt(quantityEditText.getText().toString());
                        Date deliveryDate = new Date(deliveryDateView.getDate());
                        Log.d("TTT", deliveryDate.toString());
                        if (isEditing) {
                            mCurrentItem.setName(name);
                            mCurrentItem.setQuantity(quantity);
                            mCurrentItem.setDeliveryDate(deliveryDate);
                        } else {
                            mCurrentItem = new Item(name, quantity, deliveryDate);
                            mItems.add(mCurrentItem);
                        }
                        showCurrentItem();
                    }
                });
                return builder.create();
            }
        };
        df.show(getSupportFragmentManager(), "add");
    }

    private void showCurrentItem() {
        mNameText.setText(mCurrentItem.getName());
        mQuantityText.setText(getString(R.string.quantity_format, mCurrentItem.getQuantity()));
        mDeliveryDateText.setText(getString(R.string.date_format, mCurrentItem.getDeliveryDateString()));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_context_remove:
                mItems.remove(mCurrentItem);
                mCurrentItem = new Item();
                showCurrentItem();
                return true;
            case R.id.menu_context_edit:
                addEditItem(true);
                return true;
        }
        return super.onContextItemSelected(item);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_main, menu);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_reset:
                final Item mClearItem = mCurrentItem;
                mCurrentItem = new Item();
                showCurrentItem();
                Snackbar.make(findViewById(R.id.coordinate_layout), "Item cleared", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mCurrentItem = mClearItem;
                                showCurrentItem();
                                Snackbar.make(findViewById(R.id.coordinate_layout), "Item restored", Snackbar.LENGTH_LONG)
                                        .show();
                            }
                        }).show();
                return true;
            case R.id.action_settings:
                startActivity(new Intent(Settings.ACTION_SETTINGS));
                return true;
            case R.id.action_search:
                showSearchDialog();
                return true;
            case R.id.action_clearAll:
                showClearDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void showClearDialog() {
        DialogFragment df = new DialogFragment() {
            @NonNull
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.remove);
                builder.setMessage(R.string.confirmation_dialog_message);
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mItems.clear();
                        mCurrentItem = new Item();
                        showCurrentItem();
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, null);
                return builder.create();
            }
        };
        df.show(getSupportFragmentManager(), "clearAll");
    }

    private void showSearchDialog() {
        DialogFragment df = new DialogFragment() {
            @NonNull
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.search_dialog_title);
                builder.setItems(getNames(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mCurrentItem = mItems.get(which);
                        showCurrentItem();
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, null);
                return builder.create();
            }
        };
        df.show(getSupportFragmentManager(), "search");
    }

    private String[] getNames() {
        String[] names = new String[mItems.size()];
        for (int i = 0; i < mItems.size(); i++) {
            names[i] = mItems.get(i).getName();
        }
        return names;
    }
}
