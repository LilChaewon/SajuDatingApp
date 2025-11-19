package com.example.sajudatingapp.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sajudatingapp.R;
import com.example.sajudatingapp.adapter.MessageAdapter;
import com.example.sajudatingapp.model.ChatRoom;
import com.example.sajudatingapp.model.Message;
import com.example.sajudatingapp.util.StubDataUtil;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView messagesRecyclerView;
    private MessageAdapter messageAdapter;
    private List<Message> messageList;
    private EditText messageInput;
    private Button sendButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messagesRecyclerView = findViewById(R.id.messages_recycler_view);
        messageInput = findViewById(R.id.et_message_input);
        sendButton = findViewById(R.id.btn_send);

        // In a real app, you'd pass a chatRoomId and get the specific chat room
        ChatRoom dummyChatRoom = findFirstNonPendingChat(); 

        messageList = new ArrayList<>(StubDataUtil.getMessagesForChatRoom(dummyChatRoom));
        messageAdapter = new MessageAdapter(messageList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        messagesRecyclerView.setLayoutManager(layoutManager);
        messagesRecyclerView.setAdapter(messageAdapter);

        sendButton.setOnClickListener(v -> {
            String messageText = messageInput.getText().toString().trim();
            if (!messageText.isEmpty()) {
                // Add the new message as a "sent" message
                Message newMessage = new Message(messageText, true);
                messageList.add(newMessage);
                messageAdapter.notifyItemInserted(messageList.size() - 1);
                messagesRecyclerView.scrollToPosition(messageList.size() - 1);
                messageInput.setText("");
            }
        });

        // For prototype: disable chat if it's a pending one
        if (dummyChatRoom.isPending()) {
            messageInput.setEnabled(false);
            sendButton.setEnabled(false);
            messageInput.setHint("수락 대기중인 대화입니다.");
        }
    }

    private ChatRoom findFirstNonPendingChat() {
        for (ChatRoom room : StubDataUtil.getChatRooms()) {
            if (!room.isPending()) {
                return room;
            }
        }
        // Fallback to the first room if none are active
        return StubDataUtil.getChatRooms().get(0);
    }
}
