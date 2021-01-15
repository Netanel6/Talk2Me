package com.netanel.talk2me.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.netanel.talk2me.R;
import com.netanel.talk2me.conversation.ConversationActivity;
import com.netanel.talk2me.main.fab.PhonebookAdapter;
import com.netanel.talk2me.pojo.User;

import java.util.ArrayList;
import java.util.Objects;

public class ContactsFragment extends Fragment {
    RecyclerView contactRv;
    PhonebookAdapter adapter;
    String currentUserID;
    CollectionReference dataRef = FirebaseFirestore.getInstance().collection("Data");
    ArrayList<User> users = new ArrayList<>();

    public ContactsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new PhonebookAdapter();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contacts, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRef();
        setupViews(view);
        getContacts();
        setConversation();
    }


    private void setupRef() {
        currentUserID = FirebaseAuth.getInstance().getUid();
    }

    private void setupViews(View view) {
    contactRv = view.findViewById(R.id.contact_rv);
    }

    private void getContacts() {
        dataRef.document("ContactList").collection(currentUserID).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                User user = snapshot.toObject(User.class);
                users.add(user);
                contactRv.
                        setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                adapter.setUserArrayList(users);
                Log.d("usersRef", "setPhonebookRv: " + users.toString());
                contactRv.setAdapter(adapter);
                DividerItemDecoration itemDecoration2 = new DividerItemDecoration(contactRv.getContext(),
                        DividerItemDecoration.VERTICAL);
                contactRv.addItemDecoration(itemDecoration2);
                adapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void setConversation(){
        adapter.setOnItemClick((user, position) -> {
            Intent intent = new Intent(getActivity(), ConversationActivity.class);
            dataRef.document("Conversation").collection(currentUserID).add(user);
            intent.putExtra("name", user.getName());
            intent.putExtra("last", user.getLast());
            intent.putExtra("photo", user.getPhoto());
            intent.putExtra("phone", user.getPhone());
            intent.putExtra("email", user.getEmail());
            intent.putExtra("status", user.getStatus());
            intent.putExtra("id", user.getId());
            startActivity(intent);
            Objects.requireNonNull(getActivity()).finish();
        });
    }


}