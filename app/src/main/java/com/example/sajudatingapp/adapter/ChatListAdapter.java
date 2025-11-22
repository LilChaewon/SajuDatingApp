package com.example.sajudatingapp.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sajudatingapp.R;
import com.example.sajudatingapp.activity.ChatActivity;
import com.example.sajudatingapp.model.ChatRoom;

import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder> {

    private List<ChatRoom> chatRoomList;

    public ChatListAdapter(List<ChatRoom> chatRoomList) {
        this.chatRoomList = chatRoomList;
    }
    
    public void setChatRooms(List<ChatRoom> chatRooms) {
        this.chatRoomList = chatRooms;
        notifyDataSetChanged();
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
        holder.bind(chatRoom);
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

        void bind(ChatRoom chatRoom) {
            partnerName.setText(chatRoom.getOtherUserName());
            // TODO: lastMessage needs to be added to the API response for chat rooms
            lastMessage.setText("..."); 
            
            Glide.with(itemView.getContext())
                    .load(chatRoom.getOtherUserProfilePictureUrl())
                    .placeholder(R.drawable.ic_profile)
                    .into(partnerProfile);

            if ("pending".equalsIgnoreCase(chatRoom.getStatus())) {
                pendingStatus.setVisibility(View.VISIBLE);
                lastMessage.setText("대화 요청을 보냈습니다.");
            } else {
                pendingStatus.setVisibility(View.GONE);
            }

            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(itemView.getContext(), ChatActivity.class);
                intent.putExtra("CHAT_ROOM_ID", chatRoom.getId());
                intent.putExtra("CHAT_PARTNER_NAME", chatRoom.getOtherUserName());
                itemView.getContext().startActivity(intent);
            });
        }
    }
}
