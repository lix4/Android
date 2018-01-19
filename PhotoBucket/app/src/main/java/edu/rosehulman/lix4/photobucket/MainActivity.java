package edu.rosehulman.lix4.photobucket;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

public class MainActivity extends AppCompatActivity implements ListFragment.CallBack, LoginFragment.OnLoginListener, GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 1;
    private DatabaseReference mPicsRef;
    private Toolbar mToolbar;
    private ListFragment mListFragment;
    private FloatingActionButton mFab;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private OnCompleteListener mOnCompleteListener;
    private GoogleApiClient mGoogleApiClient;
    private boolean mineOrAll;
    private MenuItem mShowOnlyButton;
    private String mCurrentUid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mPicsRef = FirebaseDatabase.getInstance().getReference("title");
        mPicsRef.addValueEventListener(new TitleValueEventListener());

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDialog(null);
            }
        });


        mAuth = FirebaseAuth.getInstance();
        initilizeListener();
        initilizeGoogle();


        mListFragment = new ListFragment();
        mineOrAll = true;

    }

    private void initilizeListener() {
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    mCurrentUid = user.getUid();
                    Log.d("Current uid: ", user.getUid() + "");
                    switchToListFragment(mCurrentUid);
                } else {
                    switchToLoginFragment();
                }
            }
        };
        mOnCompleteListener = new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (!task.isSuccessful()) {
                    Log.d("onComplete: ", task.getException().toString());
                    showLoginError("Login Failed!");
                }
            }
        };
    }

    @Override
    public void onLogin(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(mOnCompleteListener);
    }

    @Override
    public void onGoogleLogin() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void showLoginError(String message) {
        LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager().findFragmentByTag("Login");
        loginFragment.onLoginError(message);
    }


    private void initilizeGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        showLoginError("Google Connection Failed!");
    }

    private class TitleValueEventListener implements ValueEventListener {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            mToolbar.setTitle((String) dataSnapshot.getValue());
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            //nothing
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }


    public void signOut() {
        mAuth.signOut();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                showLoginError("Google Signin Failed!");
            }
        }

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(Constants.TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, mOnCompleteListener);
    }


    private void showAddDialog(final Pic pic) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View view = getLayoutInflater().inflate(R.layout.dialog_pic, null);
        final EditText captionEdit = (EditText) view.findViewById(R.id.edit_pic_caption);
        final EditText urlEdit = (EditText) view.findViewById(R.id.edit_pic_url);
        if (pic == null) {
            builder.setTitle(R.string.dialog_title_add);
            urlEdit.setText(Util.randomImageUrl());
        } else {
            builder.setTitle(R.string.dialog_title_edit);
            captionEdit.setText(pic.getCaption());
            urlEdit.setText(pic.getImageUrl());
        }
        builder.setView(view);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String caption = captionEdit.getText().toString();
                String url = urlEdit.getText().toString();
                if (pic == null) {
                    Pic newPic = new Pic();
                    newPic.setCaption(caption);
                    newPic.setImageUrl(url);
                    newPic.setUid(mCurrentUid);
                    mListFragment.addToList(newPic);
                } else {
                    mListFragment.updateInList(pic, caption, url);
                }
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        if (pic != null) {
            builder.setNeutralButton(R.string.delete, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mListFragment.removeFromList(pic);
                }
            });
        }
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mShowOnlyButton = menu.findItem(R.id.action_show_only);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_logout:
                signOut();
                switchToLoginFragment();
                break;
            case R.id.action_show_only:
                //TODO: show only my posts
                if (mineOrAll) {
                    mShowOnlyButton.setTitle(R.string.action_show_all);
                    mListFragment.showOnly(true);
                } else {
                    mShowOnlyButton.setTitle(R.string.action_show_mine);
                    mListFragment.showOnly(false);
                }
                mineOrAll = !mineOrAll;
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mFab.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPicSelected(Pic pic) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment detailFragment = DetailFragment.newInstance(pic);
        ft.replace(R.id.container, detailFragment);
        ft.addToBackStack("detail");
        ft.commit();
        mFab.setVisibility(View.INVISIBLE);
    }

    private void switchToLoginFragment() {
        mFab.setVisibility(View.INVISIBLE);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, new LoginFragment(), "Login");
        ft.commit();
    }

    private void switchToListFragment(String uid) {
        mFab.setVisibility(View.VISIBLE);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        mListFragment = new ListFragment();
        Bundle args = new Bundle();
        args.putString(Constants.FIREBASE_UID, uid);
        mListFragment.setArguments(args);
        ft.replace(R.id.container, mListFragment);
        ft.commit();
    }

    @Override
    public void showEditRemoveDialog(Pic pic) {
        showAddDialog(pic);
    }
}
