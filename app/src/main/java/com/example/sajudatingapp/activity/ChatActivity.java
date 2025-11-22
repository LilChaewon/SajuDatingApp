package com.example.sajudatingapp.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sajudatingapp.R;
import com.example.sajudatingapp.adapter.MessageAdapter;
import com.example.sajudatingapp.model.ChatRoom;
import com.example.sajudatingapp.model.Message;
import com.example.sajudatingapp.network.ApiClient;
import com.example.sajudatingapp.util.StubDataUtil;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView messagesRecyclerView;
    private MessageAdapter messageAdapter;
    private List<Message> messageList;
    private EditText messageInput;
    private Button sendButton;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("상대방 이름 (채팅)");
        }

        messagesRecyclerView = findViewById(R.id.messages_recycler_view);
        messageInput = findViewById(R.id.et_message_input);
        sendButton = findViewById(R.id.btn_send);

        ChatRoom dummyChatRoom = findFirstNonPendingChat();

        messageList = new ArrayList<>(StubDataUtil.getMessagesForChatRoom(dummyChatRoom));
        messageAdapter = new MessageAdapter(messageList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        messagesRecyclerView.setLayoutManager(layoutManager);
        messagesRecyclerView.setAdapter(messageAdapter);

        sendButton.setOnClickListener(v -> {
            String messageText = messageInput.getText().toString().trim();
            if (!messageText.isEmpty()) {
                Message newMessage = new Message(messageText, true);
                messageList.add(newMessage);
                messageAdapter.notifyItemInserted(messageList.size() - 1);
                messagesRecyclerView.scrollToPosition(messageList.size() - 1);
                messageInput.setText("");
            }
        });

        if (dummyChatRoom.isPending()) {
            messageInput.setEnabled(false);
            sendButton.setEnabled(false);
            messageInput.setHint("수락 대기중인 대화입니다.");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_report) {
            showReportDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showReportDialog() {
        new AlertDialog.Builder(this)
                .setTitle("사용자 신고")
                .setMessage("이 사용자를 신고하시겠습니까?")
                .setPositiveButton("신고", (dialog, which) -> {
                    // TODO: Replace with the actual ID of the user being reported
                    int reportedUserId = 1;

                    JsonObject reportData = new JsonObject();
                    reportData.addProperty("reportedUserId", reportedUserId);
                    reportData.addProperty("reason", "Inappropriate behavior in chat.");

                    // This assumes a token has been set in ApiClient after login.
                    // For now, let's assume a token is available.
                    // ApiClient.setAuthToken("your_dummy_jwt");
                    
                    ApiClient.getApiService().createReport(reportData).enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(ChatActivity.this, "신고가 접수되었습니다.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ChatActivity.this, "신고 접수에 실패했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            Toast.makeText(ChatActivity.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("취소", null)
                .show();
    }

    private ChatRoom findFirstNonPendingChat() {
        for (ChatRoom room : StubDataUtil.getChatRooms()) {
            if (!room.isPending()) {
                return room;
            }
        }
        return StubDataUtil.getChatRooms().get(0);
    }
}