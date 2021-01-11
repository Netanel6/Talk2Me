package com.netanel.talk2me.main.fab;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.netanel.talk2me.R;
import com.netanel.talk2me.pojo.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PhonebookActivity extends AppCompatActivity {
    GoogleSignInAccount signInAccount;
    ImageView mainPhoto;

    CollectionReference usersRef;
    ArrayList<User> users = new ArrayList<>();
    RecyclerView phonebookRv;
    PhonebookAdapter adapter;

    private BottomSheetBehavior mBottomSheetBehavior;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonebook);
        setupRef();
        setupViews();
        setPhonebookRv();
        setOnItemClick();

    }


    private void setupRef() {
        signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        usersRef = FirebaseFirestore.getInstance().collection("AllUsers");
        adapter = new PhonebookAdapter();

    }

    private void setupViews() {
        mainPhoto = findViewById(R.id.main_image);
        Picasso.get().load(signInAccount.getPhotoUrl()).into(mainPhoto);
        phonebookRv = findViewById(R.id.phonebook_rv);
    }

    private void setPhonebookRv() {
        usersRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                User user = snapshot.toObject(User.class);
                users.add(user);
                phonebookRv.
                        setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
                adapter.setUserArrayList(users);
                Log.d("usersRef", "setPhonebookRv: " + users.toString());
                phonebookRv.setAdapter(adapter);
                DividerItemDecoration itemDecoration2 = new DividerItemDecoration(phonebookRv.getContext(),
                        DividerItemDecoration.VERTICAL);
                phonebookRv.addItemDecoration(itemDecoration2);
                adapter.notifyDataSetChanged();

            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void setOnItemClick() {

        adapter.setOnItemClick(new PhonebookAdapter.OnItemClick() {
            @Override
            public void onShortClick(User user, int position) {
                LinearLayout mCustomBottomSheet = findViewById(R.id.custom_bottom_sheet);
                mBottomSheetBehavior = BottomSheetBehavior.from(mCustomBottomSheet);

                if (mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }

                //Add callbacck to the bottom sheet
                mBottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                    @Override
                    public void onStateChanged(@NonNull View bottomSheet, int newState) {
                        TextView bottomSheetName, bottomSheetLast, bottomSheetEmail, bottomSheetPhone;
                        ImageView bottomSheetImage;

                        //Initiate the views when the bottom sheet state == to expended
                        if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                            bottomSheetName = findViewById(R.id.bottomsheet_name_and_last);
                            bottomSheetEmail = findViewById(R.id.bottomsheet_email);
                            bottomSheetPhone = findViewById(R.id.bottomsheet_phone);

                            bottomSheetImage = findViewById(R.id.bottomsheet_image);

                            Picasso.get().load(user.getPhoto()).fit().into(bottomSheetImage);
                            bottomSheetName.setText(user.getName()+" "+user.getLast());
                            bottomSheetEmail.setText(user.getEmail());
                            bottomSheetPhone.setText(user.getPhone());


                        }
                    }

                    @Override
                    public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                    }
                });            }
        });
    }


}