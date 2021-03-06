package com.netanel.talk2me.activities;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.FirebaseApp;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.netanel.talk2me.auth.AuthActivity;
import com.netanel.talk2me.main.MainActivity;
import com.netanel.talk2me.R;

public class SplashScreenActivity extends AppCompatActivity {
    GoogleSignInAccount signInAccount;
    ImageView splashScreenIv;
    TextView splashScreenTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        FirebaseApp.initializeApp(this);
        signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        setupViews();
        getUserPermission();
    }

    private void setupViews() {
        splashScreenIv = findViewById(R.id.splash_screen_image);
        splashScreenTv = findViewById(R.id.splash_screen_app_name);
    }

    private void getUserPermission() {
        Dexter.withContext(this).withPermission(Manifest.permission.READ_CONTACTS).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                setUpAnim();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Toast.makeText(SplashScreenActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                finishAndRemoveTask();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();

            }
        }).check();
    }

    // TODO: 07/01/2021 get contacts from phone ans save them to arrayList & save locally with room 


    //fade animation
    private void setUpAnim() {

        Animation aniFadeIn = AnimationUtils.loadAnimation(SplashScreenActivity.this, R.anim.fade_in);
        splashScreenIv.startAnimation(aniFadeIn);
        splashScreenTv.startAnimation(aniFadeIn);
        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            Animation aniFadeOut = AnimationUtils.loadAnimation(SplashScreenActivity.this, R.anim.fade_out);
            splashScreenIv.startAnimation(aniFadeOut);
            splashScreenTv.startAnimation(aniFadeOut);
            handler.postDelayed(() -> {
                if (signInAccount != null){
                    startActivity(new Intent(this , MainActivity.class));
                }else {
                   startActivity(new Intent(this, AuthActivity.class));
                }
                finish();
            }, 2100);
            splashScreenIv.setVisibility(View.INVISIBLE);
            splashScreenTv.setVisibility(View.INVISIBLE);
        }, 2000);
    }


}