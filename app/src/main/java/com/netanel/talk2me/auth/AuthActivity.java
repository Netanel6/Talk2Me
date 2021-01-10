package com.netanel.talk2me.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.netanel.talk2me.R;

public class AuthActivity extends AppCompatActivity {
    private final static int RC_SIGN_IN = 1;
    BottomNavigationView bottomNav;
    GoogleSignInAccount signInAccount;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        mAuth = FirebaseAuth.getInstance();
        signInAccount = GoogleSignIn.getLastSignedInAccount(this);

        setUpBottomNavigationView();

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void setUpBottomNavigationView() {
        BottomNavigationView.OnNavigationItemSelectedListener navListener =
                menuItem -> {
                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId()) {
                        case R.id.nav_signup:
                            selectedFragment = new SignupFragment();
                            menuItem.setChecked(true);
                            break;
                        case R.id.nav_login:
                            selectedFragment = new LoginFragment();
                            menuItem.setChecked(true);
                            break;
                    }
                    if (selectedFragment != null) {
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.auth_container, selectedFragment)
                                .commit();
                    }
                    return true;
                };
         bottomNav = findViewById(R.id.bottom_navigation_auth_activity);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        bottomNav.setSelectedItemId(R.id.nav_signup);


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());


                Log.d("Bundle", "onActivityResult: " + data.toString());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, e.getMessage() + "Error Code", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.auth_container, new LoginFragment())
                                    .commit();
                            bottomNav.setSelectedItemId(R.id.nav_login);
                            Toast.makeText(AuthActivity.this, "Please enter more info!", Toast.LENGTH_SHORT).show();


                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(AuthActivity.this, "Signup failed try again!", Toast.LENGTH_SHORT).show();
                        }


                    }
                });
    }
}
