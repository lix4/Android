package edu.rosehulman.passwordkeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

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

import edu.rosehulman.rosefire.Rosefire;
import edu.rosehulman.rosefire.RosefireResult;


public class MainActivity extends AppCompatActivity implements LoginFragment.OnLoginListener, PasswordFragment.OnLogoutListener, GoogleApiClient.OnConnectionFailedListener {
    private static final int RC_SIGN_IN = 1;
    private static final int RC_ROSEFIRE_LOGIN = 2;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private OnCompleteListener mOnCompleteListener;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        initilizeListeners();
        initilizeGoogle();
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

    private void initilizeListeners() {
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                Log.d(Constants.TAG, "User: " + user);
                if (user != null) {
                    switchToPasswordFragment("users/" + user.getUid());
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
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
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

        if (requestCode == RC_ROSEFIRE_LOGIN) {
            RosefireResult result = Rosefire.getSignInResultFromIntent(data);
            if (result.isSuccessful()) {
                mAuth.signInWithCustomToken(result.getToken())
                        .addOnCompleteListener(this, mOnCompleteListener);
            } else {
                showLoginError("Rosefire Login Error!");
            }
        }

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(Constants.TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, mOnCompleteListener);
    }


    @Override
    public void onRosefireLogin() {
        Intent signInIntent = Rosefire.getSignInIntent(this, getString(R.string.rosefire_key));
        startActivityForResult(signInIntent, RC_ROSEFIRE_LOGIN);
    }

    @Override
    public void onLogout() {
        mAuth.signOut();
    }

    // MARK: Provided Helper Methods
    private void switchToLoginFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment, new LoginFragment(), "Login");
        ft.commit();
    }

    private void switchToPasswordFragment(String path) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment passwordFragment = new PasswordFragment();
        Bundle args = new Bundle();
        args.putString(Constants.FIREBASE_PATH, path);
        passwordFragment.setArguments(args);
        ft.replace(R.id.fragment, passwordFragment, "Passwords");
        ft.commit();
    }

    private void showLoginError(String message) {
        LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager().findFragmentByTag("Login");
        loginFragment.onLoginError(message);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        showLoginError("Google Connection Failed!");
    }
}
