package com.netanel.talk2me.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.netanel.talk2me.R;
import com.netanel.talk2me.main.fab.PhonebookActivity;
import com.netanel.talk2me.main.main_photo.ProfileActivity;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    GoogleSignInAccount signInAccount;
    ImageView mainPhoto;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupTabLayouts();
        setupRef();
        setupViews();

        setupOnPhotoClick();
    }


    private void setupTabLayouts() {
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.main_container);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PhonebookActivity.class));
                /*
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                */

            }
        });

    }

    private void setupRef() {
        signInAccount = GoogleSignIn.getLastSignedInAccount(this);

    }

    private void setupViews() {
        mainPhoto = findViewById(R.id.main_image);
        Picasso.get().load(signInAccount.getPhotoUrl()).into(mainPhoto);
    }

    private void setupOnPhotoClick() {
        mainPhoto.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
        });
    }

    @Override
    public void onBackPressed() {

    }

  /*  private ArrayList<User> getContacts() {
        Toast.makeText(this, "getContacts", Toast.LENGTH_SHORT).show();
        ArrayList<User> list = new ArrayList<>();
        //we will find the contacts here
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if (!cur.moveToFirst()) return list;
        while (cur.moveToNext()) {
            int namePosition = cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            String name = cur.getString(namePosition);

            int hasPhonePosition = cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
            boolean hasPhone = cur.getInt(hasPhonePosition) > 0;
            User user = new User(name, "No phone");
            if(hasPhone){
                int phonePosition = cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                if (phonePosition > 0) {
                    String phone = cur.getString(phonePosition);
                    user.setPhone(phone);
                }
            }
            list.add(user);
        }

        return list;
    }*/
}