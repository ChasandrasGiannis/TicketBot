package com.example.ticketbot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Message> messageList;
    private Context context;

    public MessageAdapter(Context context, List<Message> messageList) {
        this.context = context;
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messageList.get(position);

        if (message.isSent()) {
            holder.rightChatBubble.setVisibility(View.VISIBLE);
            holder.leftChatBubble.setVisibility(View.GONE);
            holder.textViewRightMessage.setText(message.getText());
        } else {
            holder.leftChatBubble.setVisibility(View.VISIBLE);
            holder.rightChatBubble.setVisibility(View.GONE);
            holder.textViewLeftMessage.setText(message.getText());
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewRightMessage;
        public TextView textViewLeftMessage;
        public LinearLayout rightChatBubble;
        public LinearLayout leftChatBubble;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewRightMessage = itemView.findViewById(R.id.textViewRightMessage);
            textViewLeftMessage = itemView.findViewById(R.id.textViewLeftMessage);
            rightChatBubble = itemView.findViewById(R.id.rightChatBubble);
            leftChatBubble = itemView.findViewById(R.id.leftChatBubble);
        }
    }
}
