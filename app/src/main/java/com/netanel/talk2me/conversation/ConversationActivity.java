package com.netanel.talk2me.conversation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.netanel.talk2me.R;
import com.netanel.talk2me.main.MainActivity;
import com.netanel.talk2me.pojo.Message;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class ConversationActivity extends AppCompatActivity {
    ImageView contactPhoto;
    TextView contactName;
    String currentTime;
    String currentUser = FirebaseAuth.getInstance().getUid();
    String name, last, photo, phone, email, status, id;
    FirebaseDatabase conListRtRef;
    EditText messageEt;
    Button sendMessage;
    RecyclerView conversationRv;
    ConversationAdapter conversationAdapter;
    CollectionReference conListFsRef = FirebaseFirestore.getInstance().collection("Conversation");
    ArrayList<com.netanel.talk2me.pojo.Message> messages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        setupTime();
        setupRef();
        setupViews();
        startConversation();
        addMessage();
        setupMessage();
    }

    private void setupTime() {
        currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

    }

    private void setupRef() {
        conListRtRef = FirebaseDatabase.getInstance();

    }

    private void setupViews() {
        contactPhoto = findViewById(R.id.main_image);
        contactName = findViewById(R.id.title);
        messageEt = findViewById(R.id.input_et);
        sendMessage = findViewById(R.id.input_send);
        conversationRv = findViewById(R.id.conversationRv);

    }

    private void startConversation() {
        Bundle b = getIntent().getExtras();
        name = b.getString("name");
        last = b.getString("last");
        photo = b.getString("photo");
        phone = b.getString("phone");
        email = b.getString("email");
        status = b.getString("status");
        id = b.getString("id");

        Picasso.get().load(photo).transform(new CropCircleTransformation()).into(contactPhoto);
        contactName.setText(name + " " + last);
    }


    private void addMessage() {
        sendMessage.setOnClickListener(view -> {
            String messageInput = messageEt.getText().toString().trim().toLowerCase();
            setupTime();
            Message message = new Message(messageInput, currentUser, id , currentTime);
//            conListFsRef.document(currentUser).collection(currentTime).add(message);
            conListRtRef.getReference("messages").child(id).setValue(message);
            messageEt.setText("");
        });

    }

    private void setupMessage() {
        conversationAdapter = new ConversationAdapter(ConversationActivity.this, messages);

        conversationRv.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        conListRtRef.getReference("messages").orderByChild("toUser").equalTo(currentUser).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {

                    Message message = dataSnapshot.getValue(Message.class);
                    if (id.equals(message.getFromUser())) {
                        conversationAdapter.setMessageType(0);
                        messages.add(message);
                        conversationAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {

                    Message message = dataSnapshot.getValue(Message.class);
                    if (id.equals(message.getFromUser())) {
                        conversationAdapter.setMessageType(0);
                        messages.add(message);
                        conversationAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {


            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        conListRtRef.getReference("messages").orderByChild("toUser").equalTo(id).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {
                    Message message = dataSnapshot.getValue(Message.class);
                    if (currentUser.equals(message.getFromUser())) {
                        conversationAdapter.setMessageType(1);
                        messages.add(message);
//                        conListFsRef.document(id).collection("Messages").add(messages);
                        conversationAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {
                    Message message = dataSnapshot.getValue(Message.class);
                    if (currentUser.equals(message.getFromUser())) {
                        conversationAdapter.setMessageType(1);
                        messages.add(message);
//                        conListFsRef.document(id).collection("Messages").add(messages);
                        conversationAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


       /*
        conListFsRef.document(currentUser).collection(currentTime).orderBy("toUser").whereEqualTo("id", currentUser).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot snapshot:queryDocumentSnapshots){
                Message message = snapshot.toObject(Message.class);
                if (id.equals(message.getFromUser())){
                    conversationAdapter.setMessageType(1);
                    messages.add(message);
                    conversationAdapter.notifyDataSetChanged();
                }
            }

        }).addOnFailureListener(e -> {

        });

        conListFsRef.document(currentUser).collection(currentTime).orderBy("toUser").whereEqualTo("id", id).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot snapshot:queryDocumentSnapshots){
                Message message = snapshot.toObject(Message.class);
                if (currentUser.equals(message.getFromUser())){
                    conversationAdapter.setMessageType(0);
                    messages.add(message);
                    conversationAdapter.notifyDataSetChanged();

                }
            }

        }).addOnFailureListener(e -> {

        });
        */

        conversationRv.setAdapter(conversationAdapter);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}