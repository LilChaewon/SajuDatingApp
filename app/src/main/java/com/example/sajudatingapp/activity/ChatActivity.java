package com.example.sajudatingapp.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sajudatingapp.R;
import com.example.sajudatingapp.adapter.MessageAdapter;
import com.example.sajudatingapp.manager.ChatManager;
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
    private Toolbar toolbar;
    private TextView matchingSuccessText;
    private Long chatRoomId;
    private ChatManager chatManager;
    private Handler mainHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.util.Log.d("ChatActivity", "onCreate 시작");
        
        try {
            setContentView(R.layout.activity_chat);
            android.util.Log.d("ChatActivity", "setContentView 완료");

            chatRoomId = getIntent().getLongExtra("CHAT_ROOM_ID", -1L);
            android.util.Log.d("ChatActivity", "chatRoomId: " + chatRoomId);
            
            mainHandler = new Handler(Looper.getMainLooper());
            
            // 먼저 UI 초기화 (더미 데이터로)
            initializeViews();
            
            // TODO: 백엔드 서버가 준비되면 아래 코드를 활성화
            /*
            // ChatManager 초기화를 백그라운드로 이동
            new Thread(() -> {
                try {
                    chatManager = ChatManager.getInstance(ChatActivity.this);
                    mainHandler.post(() -> {
                        android.util.Log.d("ChatActivity", "ChatManager 초기화 완료");
                        // ChatManager 초기화 후 실제 데이터 로드 시도
                        if (chatRoomId != null && chatRoomId > 0) {
                            loadChatRoom();
                            loadMessages();
                        }
                    });
                } catch (Exception e) {
                    android.util.Log.e("ChatActivity", "ChatManager 초기화 실패", e);
                    // 실패해도 더미 데이터는 이미 표시됨
                }
            }).start();
            */
        } catch (Exception e) {
            android.util.Log.e("ChatActivity", "onCreate 오류", e);
            e.printStackTrace();
        }
    }
    
    private void initializeViews() {

        try {
            toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }

            messagesRecyclerView = findViewById(R.id.messages_recycler_view);
            messageInput = findViewById(R.id.et_message_input);
            sendButton = findViewById(R.id.btn_send);
            matchingSuccessText = findViewById(R.id.tv_matching_success);

            messageList = new ArrayList<>();
            messageAdapter = new MessageAdapter(messageList);

            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setStackFromEnd(true);
            messagesRecyclerView.setLayoutManager(layoutManager);
            messagesRecyclerView.setAdapter(messageAdapter);

            // UI 확인용: 더미 데이터만 사용 (ChatManager 비활성화)
            android.util.Log.d("ChatActivity", "더미 데이터 로드");
            loadDummyData();
            
            // TODO: 백엔드 서버가 준비되면 아래 코드를 활성화
            /*
            if (chatRoomId == null || chatRoomId <= 0) {
                android.util.Log.d("ChatActivity", "더미 데이터 로드");
                loadDummyData();
            } else {
                android.util.Log.d("ChatActivity", "실제 채팅방 로드");
                if (chatManager != null) {
                    loadChatRoom();
                    loadMessages();
                } else {
                    loadDummyData();
                }
            }
            */

            sendButton.setOnClickListener(v -> {
                String messageText = messageInput.getText().toString().trim();
                if (!messageText.isEmpty()) {
                    // 더미 모드: 메시지를 리스트에만 추가
                    Message dummyMessage = new Message(messageText, true);
                    messageList.add(dummyMessage);
                    messageAdapter.notifyItemInserted(messageList.size() - 1);
                    messagesRecyclerView.scrollToPosition(messageList.size() - 1);
                    messageInput.setText("");
                    
                    // TODO: 백엔드 서버가 준비되면 아래 코드를 활성화
                    /*
                    if (chatRoomId != null && chatRoomId > 0 && chatManager != null) {
                        sendMessage(messageText);
                    } else {
                        // 더미 모드: 메시지를 리스트에만 추가
                        Message dummyMessage = new Message(messageText, true);
                        messageList.add(dummyMessage);
                        messageAdapter.notifyItemInserted(messageList.size() - 1);
                        messagesRecyclerView.scrollToPosition(messageList.size() - 1);
                        messageInput.setText("");
                    }
                    */
                }
            });
        } catch (Exception e) {
            android.util.Log.e("ChatActivity", "initializeViews 오류", e);
            e.printStackTrace();
        }
    }

    // UI 확인용 더미 데이터 로드
    private void loadDummyData() {
        List<ChatRoom> dummyRooms = StubDataUtil.getChatRooms();
        if (!dummyRooms.isEmpty()) {
            // 수락된 채팅방을 우선적으로 찾기
            ChatRoom dummyRoom = null;
            for (ChatRoom room : dummyRooms) {
                if (!room.isPending()) {
                    dummyRoom = room;
                    break;
                }
            }
            // 수락된 채팅방이 없으면 첫 번째 채팅방 사용
            if (dummyRoom == null) {
                dummyRoom = dummyRooms.get(0);
            }
            
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(
                    dummyRoom.getPartner() != null ? 
                    dummyRoom.getPartner().getName() + " (채팅)" : "채팅"
                );
            }

            if (!dummyRoom.isPending() && matchingSuccessText != null) {
                matchingSuccessText.setVisibility(View.VISIBLE);
                matchingSuccessText.setText("매칭을 성공했습니다");
            }

            if (dummyRoom.isPending()) {
                messageInput.setEnabled(false);
                sendButton.setEnabled(false);
                messageInput.setHint("수락 대기중인 대화입니다.");
            } else {
                // 수락된 채팅방이면 입력 가능하도록 설정
                messageInput.setEnabled(true);
                sendButton.setEnabled(true);
                messageInput.setHint("메시지를 입력하세요...");
            }

            // 더미 메시지 로드
            List<Message> dummyMessages = StubDataUtil.getMessagesForChatRoom(dummyRoom);
            messageList.clear();
            messageList.addAll(dummyMessages);
            messageAdapter.notifyDataSetChanged();
            if (!messageList.isEmpty()) {
                messagesRecyclerView.scrollToPosition(messageList.size() - 1);
            }
        }
    }

    private void loadChatRoom() {
        if (chatRoomId == null || chatRoomId <= 0) {
            // 서버 연결 실패 시 더미 데이터 사용
            loadDummyData();
            return;
        }

        if (chatManager == null) {
            loadDummyData();
            return;
        }

        chatManager.getChatRooms(new ChatManager.GetChatRoomsCallback() {
            @Override
            public void onSuccess(List<ChatRoom> chatRooms) {
                boolean found = false;
                for (ChatRoom room : chatRooms) {
                    if (room.getId() != null && room.getId().equals(chatRoomId)) {
                        found = true;
                        mainHandler.post(() -> {
                            if (getSupportActionBar() != null) {
                                getSupportActionBar().setTitle(room.getPartner() != null ? 
                                    room.getPartner().getName() + " (채팅)" : "채팅");
                            }

                            if (!room.isPending() && matchingSuccessText != null) {
                                matchingSuccessText.setVisibility(View.VISIBLE);
                                matchingSuccessText.setText("매칭을 성공했습니다");
                            }

                            if (room.isPending()) {
                                messageInput.setEnabled(false);
                                sendButton.setEnabled(false);
                                messageInput.setHint("수락 대기중인 대화입니다.");
                            }
                        });
                        break;
                    }
                }
                if (!found) {
                    // 채팅방을 찾지 못한 경우 더미 데이터 사용
                    mainHandler.post(() -> loadDummyData());
                }
            }

            @Override
            public void onError(String error) {
                // 서버 연결 실패 시 더미 데이터 사용
                mainHandler.post(() -> loadDummyData());
            }
        });
    }

    private void loadMessages() {
        if (chatRoomId == null || chatRoomId <= 0) {
            // 더미 데이터 사용
            loadDummyData();
            return;
        }

        if (chatManager == null) {
            loadDummyData();
            return;
        }

        chatManager.getMessages(chatRoomId, new ChatManager.GetMessagesCallback() {
            @Override
            public void onSuccess(List<Message> messages) {
                mainHandler.post(() -> {
                    messageList.clear();
                    messageList.addAll(messages);
                    messageAdapter.notifyDataSetChanged();
                    if (!messageList.isEmpty()) {
                        messagesRecyclerView.scrollToPosition(messageList.size() - 1);
                    }
                });
            }

            @Override
            public void onError(String error) {
                // 서버 연결 실패 시 더미 데이터 사용
                mainHandler.post(() -> {
                    List<ChatRoom> dummyRooms = StubDataUtil.getChatRooms();
                    if (!dummyRooms.isEmpty()) {
                        List<Message> dummyMessages = StubDataUtil.getMessagesForChatRoom(dummyRooms.get(0));
                        messageList.clear();
                        messageList.addAll(dummyMessages);
                        messageAdapter.notifyDataSetChanged();
                        if (!messageList.isEmpty()) {
                            messagesRecyclerView.scrollToPosition(messageList.size() - 1);
                        }
                    }
                });
            }
        });
    }

    private void sendMessage(String content) {
        if (chatManager == null) {
            // 더미 모드: 메시지를 리스트에만 추가
            Message dummyMessage = new Message(content, true);
            messageList.add(dummyMessage);
            messageAdapter.notifyItemInserted(messageList.size() - 1);
            messagesRecyclerView.scrollToPosition(messageList.size() - 1);
            messageInput.setText("");
            return;
        }

        chatManager.sendMessage(chatRoomId, content, new ChatManager.SendMessageCallback() {
            @Override
            public void onSuccess(Message message) {
                mainHandler.post(() -> {
                    messageList.add(message);
                    messageAdapter.notifyItemInserted(messageList.size() - 1);
                    messagesRecyclerView.scrollToPosition(messageList.size() - 1);
                    messageInput.setText("");
                });
            }

            @Override
            public void onError(String error) {
                mainHandler.post(() -> {
                    Toast.makeText(ChatActivity.this, "메시지 전송 실패: " + error, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 더미 데이터만 사용 (ChatManager 비활성화)
        // TODO: 백엔드 서버가 준비되면 아래 코드를 활성화
        /*
        // 주기적으로 메시지 새로고침 (WebSocket으로 대체 가능)
        refreshMessages();
        */
    }

    private void refreshMessages() {
        // 더미 데이터만 사용 (ChatManager 비활성화)
        // TODO: 백엔드 서버가 준비되면 아래 코드를 활성화
        /*
        if (chatRoomId != null && chatRoomId > 0) {
            loadMessages();
        }
        */
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // Handle back button click
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat_menu, menu); // Inflate the chat menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_report) {
            Toast.makeText(this, "신고하기 버튼 클릭됨", Toast.LENGTH_SHORT).show();
            return true;
        } else if (item.getItemId() == R.id.action_delete_chat) {
            deleteChatRoom();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteChatRoom() {
        if (chatRoomId == null || chatRoomId <= 0) return;

        if (chatManager == null) {
            Toast.makeText(this, "채팅방 삭제 기능을 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        new android.app.AlertDialog.Builder(this)
                .setTitle("채팅방 삭제")
                .setMessage("정말 이 채팅방을 삭제하시겠습니까?")
                .setPositiveButton("삭제", (dialog, which) -> {
                    chatManager.deleteChatRoom(chatRoomId, new ChatManager.DeleteChatRoomCallback() {
                        @Override
                        public void onSuccess() {
                            mainHandler.post(() -> {
                                Toast.makeText(ChatActivity.this, "채팅방이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                finish();
                            });
                        }

                        @Override
                        public void onError(String error) {
                            mainHandler.post(() -> {
                                Toast.makeText(ChatActivity.this, "삭제 실패: " + error, Toast.LENGTH_SHORT).show();
                            });
                        }
                    });
                })
                .setNegativeButton("취소", null)
                .show();
    }
}

