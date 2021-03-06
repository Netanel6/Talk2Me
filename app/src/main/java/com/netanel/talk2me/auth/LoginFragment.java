package com.netanel.talk2me.auth;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.netanel.talk2me.R;
import com.netanel.talk2me.main.MainActivity;
import com.netanel.talk2me.pojo.User;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class LoginFragment extends Fragment {

    GoogleSignInAccount signInAccount;
    ImageView ivPhoto;
    EditText etNameLast, etEmail, etPhone;
    String nameStr, lastStr, emailStr, photoUrl;
    boolean installed;
    User user;
    SignInButton btnLogin;
    CollectionReference usersRef = FirebaseFirestore.getInstance().collection("AllUsers");

    public LoginFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRef();
        setupViews(view);
    }

    private void setupRef() {
        signInAccount = GoogleSignIn.getLastSignedInAccount(getActivity());

    }

    private void setupViews(View view) {

        ivPhoto = view.findViewById(R.id.iv_photo);
        photoUrl = signInAccount.getPhotoUrl().toString();
        Picasso
                .get()
                .load(photoUrl)
                .transform(new CropCircleTransformation())
                .into(ivPhoto);

        etNameLast = view.findViewById(R.id.et_name_last);
        nameStr = signInAccount.getGivenName();
        lastStr = signInAccount.getFamilyName();
        etNameLast.setText(nameStr + " " + lastStr);
        etEmail = view.findViewById(R.id.et_email);
        emailStr = signInAccount.getEmail();
        etEmail.setText(emailStr);
        etPhone = view.findViewById(R.id.et_phone);
        btnLogin = view.findViewById(R.id.btn_signin);
        saveUserToDatabase();
    }


    private void isAppInstalled() {
        PackageManager pm = getActivity().getPackageManager();
        try {
            pm.getPackageInfo("com.netanel.talk2me", PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
    }


    private void saveUserToDatabase() {
        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        isAppInstalled();
        etPhone.setError("Enter your phone number");
        String status = getString(R.string.status);

        btnLogin.setOnClickListener(view -> {
            String phoneStr = etPhone.getText().toString().trim();
            user = new User(id, nameStr, lastStr, photoUrl, emailStr, phoneStr, status, installed);
            usersRef.document(id).set(user).addOnFailureListener(e -> {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("SaveUserToDatabase", e.getMessage());
            });
            startActivity(new Intent(getActivity(), MainActivity.class));
        });
    }

    public static void toast(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
        /*  Snackbar snackbar = Snackbar.make(view, "You are one of us now!", Snackbar.LENGTH_LONG);
            View snackbarLayout = snackbar.getView();
            TextView textView = snackbarLayout.findViewById(R.id.snackbar_text);
            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add_24, 0, 0, 0);
            snackbar.show();
            */
    }
}