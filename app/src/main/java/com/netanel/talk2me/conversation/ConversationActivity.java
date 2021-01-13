package com.netanel.talk2me.conversation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.netanel.talk2me.R;
import com.netanel.talk2me.main.MainActivity;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class ConversationActivity extends AppCompatActivity {
    ImageView contactPhoto;
    TextView contactName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        setupViews();
        startConversation();
    }

    private void setupViews(){
        contactPhoto = findViewById(R.id.main_image);
        contactName =findViewById(R.id.title);
    }

    private void startConversation() {
        Bundle b = getIntent().getExtras();
        String name = b.getString("name");
        String last = b.getString("last");
        String photo = b.getString("photo");
        String phone = b.getString("phone");
        String email = b.getString("email");
        String status = b.getString("status");

        Picasso.get().load(photo).transform(new CropCircleTransformation()).into(contactPhoto);
        contactName.setText(name+" "+last);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}