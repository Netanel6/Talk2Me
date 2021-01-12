package com.netanel.talk2me.main.main_photo;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.netanel.talk2me.R;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    GoogleSignInAccount signInAccount;
    ImageView mainPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setupRef();
        setupViews();
    }


    private void setupRef() {
        signInAccount = GoogleSignIn.getLastSignedInAccount(this);

    }

    private void setupViews() {
        mainPhoto = findViewById(R.id.main_image);
        Picasso.get().load(signInAccount.getPhotoUrl()).into(mainPhoto);
    }
}