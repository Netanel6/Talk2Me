package com.netanel.talk2me.main.fab;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.netanel.talk2me.R;
import com.netanel.talk2me.main.MainActivity;
import com.netanel.talk2me.pojo.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class PhonebookActivity extends AppCompatActivity {
    GoogleSignInAccount signInAccount;
    ImageView mainPhoto;
    String currentUserID;

    CollectionReference usersRef;
    ArrayList<User> users = new ArrayList<>();
    RecyclerView phonebookRv;
    PhonebookAdapter adapter;

    private BottomSheetBehavior mBottomSheetBehavior;

    CollectionReference dataRef;

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
        currentUserID = FirebaseAuth.getInstance().getUid();
        usersRef = FirebaseFirestore.getInstance().collection("AllUsers");
        dataRef = FirebaseFirestore.getInstance().collection("Data");
        adapter = new PhonebookAdapter();

    }

    private void setupViews() {
        mainPhoto = findViewById(R.id.main_image);
        Picasso
                .get()
                .load(signInAccount.getPhotoUrl())
                .transform(new CropCircleTransformation())
                .into(mainPhoto);
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
            Log.e("SetPhoneBookRv", e.getMessage());        });
    }

    private void setOnItemClick() {
        Button phonebookAddContactBtn = findViewById(R.id.phonebook_add_contact);

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

                //Add callback to the bottom sheet
                mBottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                    @Override
                    public void onStateChanged(@NonNull View bottomSheet, int newState) {
                        TextView bottomSheetName, bottomSheetEmail, bottomSheetPhone;
                        ImageView bottomSheetImage;

                        //Initiate the views when the bottom sheet state == to expended
                        if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                            bottomSheetName = findViewById(R.id.bottomsheet_name_and_last);
                            bottomSheetEmail = findViewById(R.id.bottomsheet_email);
                            bottomSheetPhone = findViewById(R.id.bottomsheet_phone);

                            bottomSheetImage = findViewById(R.id.bottomsheet_image);

                            Picasso.get().load(user.getPhoto()).transform(new CropCircleTransformation()).into(bottomSheetImage);
                            bottomSheetName.setText(user.getName() + " " + user.getLast());
                            bottomSheetEmail.setText(user.getEmail());
                            bottomSheetPhone.setText(user.getPhone());
                            String bottomSheetUserID = user.getId();

                            phonebookAddContactBtn.setOnClickListener(view -> {
                                usersRef.document(currentUserID).collection("Contacts").get().addOnSuccessListener(queryDocumentSnapshots -> {
                                    String contactUserID = " ";
                                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                                        User contactUser = snapshot.toObject(User.class);
                                        assert contactUser != null;
                                        contactUserID = contactUser.getId();
                                    }
                                    if (contactUserID.equals(bottomSheetUserID)) {

                                        Snackbar snackbar = Snackbar.make(view, user.getName() + " " + user.getLast() + " already in the list!", Snackbar.LENGTH_LONG);
                                        View snackbarLayout = snackbar.getView();
                                        TextView textView = snackbarLayout.findViewById(R.id.snackbar_text);
                                        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_error_24, 0, 0, 0);
                                        snackbar.show();
                                    } else {
                                        addContact(user, currentUserID);
                                        Snackbar snackbar = Snackbar.make(view, user.getName() + " " + user.getLast() + " added to the list", Snackbar.LENGTH_LONG);
                                        View snackbarLayout = snackbar.getView();
                                        TextView textView = snackbarLayout.findViewById(R.id.snackbar_text);
                                        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_24, 0, 0, 0);
                                        snackbar.show();
                                    }

                                }).addOnFailureListener(e -> {
                                    Toast.makeText(PhonebookActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.e("setOnItemClickAddUser", e.getMessage());                                });
                            });
                        }
                    }

                    @Override
                    public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                    }
                });
            }
        });

    }

    private void addContact(User user, String currentUserID) {
        usersRef.document(currentUserID).collection("Contacts").document(user.getId()).set(user);
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}