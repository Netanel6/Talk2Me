package com.netanel.talk2me.main;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.netanel.talk2me.R;
import com.netanel.talk2me.pojo.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

import static android.content.ContentValues.TAG;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ViewHolder> {
    private static OnItemClick onItemClick;
    ArrayList<User> userArrayList = new ArrayList<>();

    public void setUserArrayList(ArrayList<User> userArrayList) {
        this.userArrayList = userArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.phonebook_single_cell, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userArrayList.get(position);
        holder.name.setText(user.getName() + " " + user.getLast());
        holder.status.setText(user.getStatus());
        Picasso
                .get()
                .load(user.getPhoto())
                .transform(new CropCircleTransformation())
                .into(holder.photo);

        holder.itemView.setOnClickListener(view -> {
            if (onItemClick != null && position != RecyclerView.NO_POSITION) {
                onItemClick.onShortClick(user, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        try {
            return userArrayList.size();
        } catch (Exception e) {
            Log.d(TAG, "getItemCount: " + e.getMessage());
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView photo;
        TextView name, status;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.phonebook_image);
            name = itemView.findViewById(R.id.phonebook_name);
            status = itemView.findViewById(R.id.phonebook_status);
        }
    }

    public interface OnItemClick {
        void onShortClick(User user, int position);
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        ChatsAdapter.onItemClick = onItemClick;
    }
}
