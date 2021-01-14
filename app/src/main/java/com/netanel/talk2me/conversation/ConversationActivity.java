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
import com.netanel.talk2me.R;
import com.netanel.talk2me.main.MainActivity;
import com.netanel.talk2me.pojo.Message;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class ConversationActivity extends AppCompatActivity {
    ImageView contactPhoto;
    TextView contactName;
    String currentUser = FirebaseAuth.getInstance().getUid();
    String name, last, photo, phone, email, status, id;
    FirebaseDatabase messagesRef;
    EditText messageEt;
    Button sendMessage;
    RecyclerView conversationRv;
    ConversationAdapter conversationAdapter;
    int type = 0;

    ArrayList<com.netanel.talk2me.pojo.Message> messages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        setupRef();
        setupViews();
        startConversation();
        addMessage();
        setupMessage();
    }

    private void setupRef() {
        messagesRef = FirebaseDatabase.getInstance();
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
            Message message = new Message(messageInput, id, currentUser, type);
            messagesRef.getReference("messages").child(id).setValue(message);
        });

    }

    private void setupMessage() {
        conversationRv.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        messagesRef.getReference("messages").orderByChild("fromUser").equalTo(id).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()){

                    Message message = dataSnapshot.getValue(Message.class);
                    if(id.equals(message.getFromUser())){
                        conversationAdapter.setMessageType(0);

                        messages.add(message);
                        conversationAdapter.notifyDataSetChanged();




                    }

                }


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

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

        messagesRef.getReference("messages").orderByChild("fromUser").equalTo(id).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()){


                    Message chatMessagestemp = dataSnapshot.getValue(Message.class);
                    if(currentUser.equals(chatMessagestemp.getFromUser())){
                        conversationAdapter.setMessageType(1);
                        messages.add(chatMessagestemp);
                        conversationAdapter.notifyDataSetChanged();




                    }


                }


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

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

        conversationAdapter = new ConversationAdapter(this, messages);
        conversationRv.setAdapter(conversationAdapter);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}