package com.example.sajudatingapp.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sajudatingapp.R;
import com.example.sajudatingapp.activity.ChatActivity;
import com.example.sajudatingapp.model.ChatRoom;

import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder> {

    private List<ChatRoom> chatRoomList;
    private OnChatRoomClickListener listener;

    public interface OnChatRoomClickListener {
        void onChatRoomClick(ChatRoom chatRoom);
    }

    public ChatListAdapter(List<ChatRoom> chatRoomList) {
        this.chatRoomList = chatRoomList;
    }

    public void setOnChatRoomClickListener(OnChatRoomClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_room, parent, false);
        return new ChatListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListViewHolder holder, int position) {
        ChatRoom chatRoom = chatRoomList.get(position);
        holder.bind(chatRoom, listener);
    }

    @Override
    public int getItemCount() {
        return chatRoomList.size();
    }


    static class ChatListViewHolder extends RecyclerView.ViewHolder {
        private ImageView partnerProfile;
        private TextView partnerName;
        private TextView lastMessage;
        private TextView pendingStatus;

        public ChatListViewHolder(@NonNull View itemView) {
            super(itemView);
            partnerProfile = itemView.findViewById(R.id.iv_partner_profile);
            partnerName = itemView.findViewById(R.id.tv_partner_name);
            lastMessage = itemView.findViewById(R.id.tv_last_message);
            pendingStatus = itemView.findViewById(R.id.tv_pending_status);
        }

        void bind(ChatRoom chatRoom, OnChatRoomClickListener listener) {
            partnerName.setText(chatRoom.getPartner().getName());
            lastMessage.setText(chatRoom.getLastMessage());

            if (chatRoom.isPending()) {
                pendingStatus.setVisibility(View.VISIBLE);
                lastMessage.setText("대화 요청을 보냈습니다.");
            } else {
                pendingStatus.setVisibility(View.GONE);
            }

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onChatRoomClick(chatRoom);
                }
            });
        }
    }
}
