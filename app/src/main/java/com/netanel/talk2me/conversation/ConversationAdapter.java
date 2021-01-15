package com.netanel.talk2me.conversation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.netanel.talk2me.R;
import com.netanel.talk2me.pojo.Message;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ChatViewHolder> {

    private static final int VIEW_TYPE_MESSAGE_SENT = 0;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 1;

    private List<Message> messageItemList = new ArrayList<>();

    private Context context;

    int messageType;

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public ConversationAdapter(Context context, List<Message> messagesList) {
        this.context = context;
        this.messageItemList = messagesList;
    }


    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (messageType == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_in_single_cell, parent, false);
        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_out_single_cell, parent, false);

        }
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {

        Message chatMessages = messageItemList.get(position);


        String hour = chatMessages.getTimeStamp();
        holder.txtTimestamp.setText(hour);

        holder.txtMessage.setText(chatMessages.getInput());


    }


    @Override
    public int getItemCount() {
        return messageItemList == null ? 0 : messageItemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(messageItemList.get(position).getMessageType()==0)
            return R.layout.message_in_single_cell;
    else
        return R.layout.message_out_single_cell;

    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView txtMessage, txtTimestamp;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMessage = itemView.findViewById(R.id.last_sent_message);
            txtTimestamp = itemView.findViewById(R.id.time_sent_message);


        }
    }

}