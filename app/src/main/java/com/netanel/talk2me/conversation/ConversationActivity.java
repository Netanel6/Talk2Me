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
    String currentTimeWSec;
    String currentUserID = FirebaseAuth.getInstance().getUid();
    String name, last, photo, phone, email, status, id;
    EditText messageEt;
    Button sendMessage;
    RecyclerView conversationRv;


    FirebaseDatabase conListRtRef;
    ConversationAdapter conversationAdapter;


    CollectionReference usersRef;
    CollectionReference dataRef;

    ArrayList<Message> messages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        setupTime();
        setupRef();
        setupViews();
        setupRecyclerView();
        startConversation();
        setupMessage();
        addMessage();
    }

    private void setupTime() {
        currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        currentTimeWSec = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

    }

    private void setupRef() {
        usersRef = FirebaseFirestore.getInstance().collection("AllUsers");
        dataRef = FirebaseFirestore.getInstance().collection("Data");
        conListRtRef = FirebaseDatabase.getInstance();


    }

    private void setupViews() {
        contactPhoto = findViewById(R.id.main_image);
        contactName = findViewById(R.id.title);
        messageEt = findViewById(R.id.input_et);
        sendMessage = findViewById(R.id.input_send);


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

    public void setupRecyclerView() {
        conversationRv = findViewById(R.id.conversationRv);
        conversationAdapter = new ConversationAdapter(ConversationActivity.this, messages);
        LinearLayoutManager llm = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        conversationRv.setLayoutManager(llm);
        conversationRv.setAdapter(conversationAdapter);
        conversationAdapter.setOnSend(new ConversationAdapter.OnSend() {
            @Override
            public void getPosition(int position) {
                conversationRv.smoothScrollToPosition(position);
            }
        });
    }

    private void addMessage() {
        sendMessage.setOnClickListener(view -> {
            String messageInput = messageEt.getText().toString().trim().toLowerCase();
            setupTime();
            Message message = new Message(messageInput, currentUserID, id, currentTime);
            /*final int min = 20;
            final int max = 80;
            final int random = new Random().nextInt((max - min) + 1) + min;
            Toast.makeText(this, String.valueOf(random), Toast.LENGTH_SHORT).show();*/
            conListRtRef.getReference("messages").child(id).child(currentTimeWSec).setValue(message);

            conversationAdapter.notifyDataSetChanged();
            conversationRv.smoothScrollToPosition(messages.size() - 1);

            messageEt.setText("");
        });

    }

    private void setupMessage() {

        conListRtRef.getReference("messages").child(currentUserID).orderByChild("timeStamp").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {


                    Message message = dataSnapshot.getValue(Message.class);
                    if (id.equals(message.getFromUser())) {
                        conversationAdapter.setMessageType(0);
                        messages.add(message);

                    }
                    conversationAdapter.notifyDataSetChanged();
                    conversationRv.smoothScrollToPosition(messages.size() - 1);

                  /*  conversationAdapter.setOnSend(new ConversationAdapter.OnSend() {
                        @Override
                        public void getPosition(int position) {
                            conversationRv.smoothScrollToPosition(position);
                        }
                    });*/
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {

                    Message message = dataSnapshot.getValue(Message.class);
                    if (id.equals(message.getFromUser())) {
                        conversationAdapter.setMessageType(0);
                        messages.add(message);


                    }
                    conversationAdapter.notifyDataSetChanged();
                    conversationRv.smoothScrollToPosition(messages.size() - 1);
/*
                    conversationAdapter.setOnSend(new ConversationAdapter.OnSend() {
                        @Override
                        public void getPosition(int position) {
                            conversationRv.smoothScrollToPosition(position);
                        }
                    });*/
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

        conListRtRef.getReference("messages").child(id).orderByChild("timeStamp").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {

                    Message message = dataSnapshot.getValue(Message.class);
                    if (currentUserID.equals(message.getFromUser())) {
                        conversationAdapter.setMessageType(1);
                        messages.add(message);
//                        conListFsRef.document(id).collection("Messages").add(messages);

                    }
                    conversationAdapter.notifyDataSetChanged();
                    conversationRv.smoothScrollToPosition(messages.size() - 1);
                    /*conversationAdapter.setOnSend(new ConversationAdapter.OnSend() {
                        @Override
                        public void getPosition(int position) {
                            conversationRv.smoothScrollToPosition(position);
                        }
                    });
*/

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {
/*

                    conversationAdapter.notifyDataSetChanged();
*/

                    Message message = dataSnapshot.getValue(Message.class);
                    if (currentUserID.equals(message.getFromUser())) {
                        conversationAdapter.setMessageType(1);
                        messages.add(message);
//                        conListFsRef.document(id).collection("Messages").add(messages);

                    }
                    conversationAdapter.notifyDataSetChanged();
                    conversationRv.smoothScrollToPosition(messages.size() - 1);

                   /* conversationAdapter.setOnSend(new ConversationAdapter.OnSend() {
                        @Override
                        public void getPosition(int position) {
                            conversationRv.smoothScrollToPosition(position);
                        }
                    });*/

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

        /*usersRef.document(currentUserID).collection("Contacts").document(id).collection("Chat")
                .orderBy("id", Query.Direction.ASCENDING).whereEqualTo("toUser", currentUserID).orderBy("timeStamp")
                .get().addOnSuccessListener(queryDocumentSnapshots -> {

            for (QueryDocumentSnapshot snapshot:queryDocumentSnapshots){
                Message message = snapshot.toObject(Message.class);
                if (id.equals(message.getFromUser())){
                    conversationAdapter.setMessageType(0);
                    messages.add(message);
                    conversationRv.setAdapter(conversationAdapter);
                    conversationAdapter.notifyDataSetChanged();
                }
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("error", e.getMessage());

        });

         usersRef.document(currentUserID).collection("Contacts").document(id).collection("Chat")
                .orderBy("id", Query.Direction.ASCENDING).whereEqualTo("toUser", id).orderBy("timeStamp")
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
             for (QueryDocumentSnapshot snapshot:queryDocumentSnapshots){
                 Message message = snapshot.toObject(Message.class);
                 if (currentUserID.equals(message.getFromUser())){
                     conversationAdapter.setMessageType(1);
                     messages.add(message);
                     conversationRv.setAdapter(conversationAdapter);
                     conversationAdapter.notifyDataSetChanged();
                 }
             }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
             Log.e("error", e.getMessage());
        });*/

        /*
        usersRef.document(currentUserID).collection("Contacts").document(id).collection("Chat")
                .orderBy("toUser").whereEqualTo("id", currentUserID)
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                for (QueryDocumentSnapshot snapshot: task.getResult()){
                    Message message = snapshot.toObject(Message.class);
                    if (id.equals(message.getFromUser())) {
                        conversationAdapter.setMessageType(0);
                        messages.add(message);
                        conversationAdapter.notifyDataSetChanged();
                    }
                }
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });

        usersRef.document(currentUserID).collection("Contacts").document(id).collection("Chat")
                .orderBy("toUser").whereEqualTo("id", id)
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                for (QueryDocumentSnapshot snapshot: task.getResult()){
                    Message message = snapshot.toObject(Message.class);
                    if (currentUserID.equals(message.getFromUser())) {
                        conversationAdapter.setMessageType(1);
                        messages.add(message);
                        conversationAdapter.notifyDataSetChanged();
                    }
                }
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });
*/

        /*       dataRef.document("ContactList").collection(currentUserID).document(id).collection("Messages").orderBy("toUser").whereEqualTo("id", currentUserID).get().addOnSuccessListener(queryDocumentSnapshots -> {
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

        dataRef.document("ContactList").collection(currentUserID).document(id).collection("Messages").orderBy("toUser").whereEqualTo("id", id).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot snapshot:queryDocumentSnapshots){
                Message message = snapshot.toObject(Message.class);
                if (currentUserID.equals(message.getFromUser())){
                    conversationAdapter.setMessageType(0);
                    messages.add(message);
                    conversationAdapter.notifyDataSetChanged();

                }
            }

        }).addOnFailureListener(e -> {

        });

  */

        conversationAdapter.notifyDataSetChanged();


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}