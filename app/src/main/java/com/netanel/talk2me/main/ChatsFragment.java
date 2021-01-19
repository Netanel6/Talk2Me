package com.netanel.talk2me.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.netanel.talk2me.R;
import com.netanel.talk2me.conversation.ConversationActivity;
import com.netanel.talk2me.main.fab.PhonebookAdapter;
import com.netanel.talk2me.pojo.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class ChatsFragment extends Fragment {
    ImageView contactPhoto;
    TextView contactName;
    int hours,minutes;
    ArrayList<User> users = new ArrayList<>();
    String currentUser = FirebaseAuth.getInstance().getUid();
    String name, last, photo, phone, email, status, id;
    CollectionReference conversationListRef = FirebaseFirestore.getInstance().collection("Data");
    RecyclerView conversationListRv;
    ChatsAdapter adapter;

    public ChatsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ChatsAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chats, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupTime();
        setupRef();
        setupViews(view);
        getConversationList();
        setConversation();

    }

    private void setupTime() {
        Calendar rightNow = Calendar.getInstance();
        hours = rightNow.get(Calendar.HOUR_OF_DAY); // return the hour in 24 hrs format (ranging from 0-23)
        minutes = rightNow.get(Calendar.MINUTE); // return the minutes
    }

    private void setupRef() {
    }

    private void setupViews(View view) {
        contactPhoto = view.findViewById(R.id.main_image);
        contactName = view.findViewById(R.id.title);
        conversationListRv = view.findViewById(R.id.conversationListRv);

    }

    private void getConversationList(){
        conversationListRef.document("Conversation").collection(currentUser).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot snapshot :queryDocumentSnapshots){
                User user = snapshot.toObject(User.class);
                users.add(user);
                conversationListRv.
                        setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                adapter.setUserArrayList(users);
                Log.d("usersRef", "setPhonebookRv: " + users.toString());
                conversationListRv.setAdapter(adapter);
                DividerItemDecoration itemDecoration2 = new DividerItemDecoration(conversationListRv.getContext(),
                        DividerItemDecoration.VERTICAL);
                conversationListRv.addItemDecoration(itemDecoration2);
                adapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
    private void setConversation(){
        adapter.setOnItemClick((user, position) -> {
            Intent intent = new Intent(getActivity(), ConversationActivity.class);
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


