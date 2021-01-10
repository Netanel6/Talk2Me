package com.netanel.talk2me.auth;

import android.accounts.Account;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.netanel.talk2me.R;
import com.squareup.picasso.Picasso;

public class LoginFragment extends Fragment {
    GoogleSignInAccount signInAccount;
    TextView name, last, email, account;
    ImageView photo;
    public LoginFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signInAccount = GoogleSignIn.getLastSignedInAccount(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViews(view);
    }


    private void setupViews(View view){
        name = view.findViewById(R.id.name);
        String nameStr = signInAccount.getGivenName();
        name.setText(nameStr);
        last = view.findViewById(R.id.last);
        String lastStr = signInAccount.getFamilyName();
        last.setText(lastStr);
        photo = view.findViewById(R.id.photo);
        String photoUrl = signInAccount.getPhotoUrl().toString();
        Picasso.get().load(photoUrl).into(photo);
        email = view.findViewById(R.id.email);
        String emailStr = signInAccount.getEmail();
        email.setText(emailStr);
    }

}