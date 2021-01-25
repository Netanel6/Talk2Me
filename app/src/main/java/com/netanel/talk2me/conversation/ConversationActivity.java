package com.netanel.talk2me.conversation;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
    String messageInput;
    EditText messageEt;
    FloatingActionButton sendTextMessage;
    FloatingActionButton sendVoiceMessage;
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
        sendTextMessage = findViewById(R.id.input_send_text);
        sendVoiceMessage = findViewById(R.id.input_send_voice);
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
        contactName.setText(name + "  " + last);
    }

    public void setupRecyclerView() {
        conversationRv = findViewById(R.id.conversationRv);
        conversationAdapter = new ConversationAdapter(ConversationActivity.this, messages);
        LinearLayoutManager llm = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        llm.setStackFromEnd(true);
        conversationRv.setLayoutManager(llm);
        conversationRv.setAdapter(conversationAdapter);
    }

    private void addMessage() {

        messageEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.length() == 0) {

                    sendVoiceMessage.setVisibility(View.VISIBLE);
                    sendTextMessage.setVisibility(View.GONE);

                } else {
                    sendVoiceMessage.setVisibility(View.GONE);
                    sendTextMessage.setVisibility(View.VISIBLE);
                }
            }
        });
         sendVoiceMessage.setOnLongClickListener(view -> {
             Toast.makeText(this, "Voice Message", Toast.LENGTH_SHORT).show();

             return false;
         });
        sendTextMessage.setOnClickListener(view -> {
            messageInput = messageEt.getText().toString().trim();
            setupTime();
            Message message = new Message(messageInput, currentUserID, id, currentTime);
            conListRtRef.getReference("messages").child(id).child(currentTimeWSec).setValue(message);
            conversationAdapter.notifyDataSetChanged();
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
                    conversationRv.smoothScrollToPosition(messages.size());
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
                    conversationRv.smoothScrollToPosition(messages.size());
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
                    }
                    conversationAdapter.notifyDataSetChanged();
                    conversationRv.smoothScrollToPosition(messages.size());
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {
                    Message message = dataSnapshot.getValue(Message.class);
                    if (currentUserID.equals(message.getFromUser())) {
                        conversationAdapter.setMessageType(1);
                        messages.add(message);
                    }
                    conversationAdapter.notifyDataSetChanged();
                    conversationRv.smoothScrollToPosition(messages.size());
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
        conversationAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        // TODO: 25/01/2021 add method that saves realtimeDataBase reference to FireStore.
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}