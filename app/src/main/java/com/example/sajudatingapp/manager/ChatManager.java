package com.example.sajudatingapp.manager;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.example.sajudatingapp.api.ApiClient;
import com.example.sajudatingapp.api.ApiService;
import com.example.sajudatingapp.database.AppDatabase;
import com.example.sajudatingapp.database.dao.ChatRoomDao;
import com.example.sajudatingapp.database.dao.MessageDao;
import com.example.sajudatingapp.database.entity.ChatRoomEntity;
import com.example.sajudatingapp.database.entity.MessageEntity;
import com.example.sajudatingapp.model.ChatRoom;
import com.example.sajudatingapp.model.Message;
import com.example.sajudatingapp.model.User;
import com.example.sajudatingapp.util.NotificationHelper;
import com.example.sajudatingapp.websocket.ChatWebSocketClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatManager {
    private static final String TAG = "ChatManager";
    private static ChatManager instance;
    private Context context;
    private ApiService apiService;
    private AppDatabase database;
    private ChatRoomDao chatRoomDao;
    private MessageDao messageDao;
    private ChatWebSocketClient webSocketClient;
    private Long currentUserId;
    private ExecutorService executorService;
    private Handler mainHandler;
    
    private ChatManager(Context context) {
        this.context = context.getApplicationContext();
        this.apiService = ApiClient.getApiService();
        this.database = AppDatabase.getInstance(context);
        this.chatRoomDao = database.chatRoomDao();
        this.messageDao = database.messageDao();
        this.executorService = Executors.newSingleThreadExecutor();
        this.mainHandler = new Handler(Looper.getMainLooper());
        
        NotificationHelper.createNotificationChannel(context);
    }
    
    public static synchronized ChatManager getInstance(Context context) {
        if (instance == null) {
            instance = new ChatManager(context);
        }
        return instance;
    }
    
    public void setCurrentUserId(Long userId) {
        this.currentUserId = userId;
    }
    
    public void connectWebSocket(String serverUrl) {
        try {
            URI uri = new URI(serverUrl);
            webSocketClient = new ChatWebSocketClient(uri, new ChatWebSocketClient.MessageListener() {
                @Override
                public void onMessageReceived(Message message) {
                    // 메시지를 로컬 DB에 저장
                    executorService.execute(() -> {
                        MessageEntity entity = convertToMessageEntity(message);
                        messageDao.insertMessage(entity);
                    });
                }
                
                @Override
                public void onChatRequestReceived(Long chatRoomId, Long requesterId) {
                    // 대화 요청 알림 표시
                    NotificationHelper.showChatRequestNotification(
                            context, "사용자", chatRoomId);
                }
                
                @Override
                public void onChatAccepted(Long chatRoomId) {
                    // 채팅 수락 알림
                }
                
                @Override
                public void onChatRejected(Long chatRoomId) {
                    // 채팅 거절 알림
                }
            });
            webSocketClient.connect();
        } catch (Exception e) {
            Log.e(TAG, "WebSocket 연결 실패", e);
        }
    }
    
    public void requestChat(User user, RequestChatCallback callback) {
        if (currentUserId == null || user.getId() == null) {
            callback.onError("사용자 ID가 없습니다.");
            return;
        }
        
        ApiService.ChatRequestRequest request = 
                new ApiService.ChatRequestRequest(currentUserId, user.getId());
        
        apiService.requestChat(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onError("대화 요청 실패: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError("네트워크 오류: " + t.getMessage());
            }
        });
    }
    
    public void acceptChatRequest(Long chatRoomId, AcceptChatRequestCallback callback) {
        apiService.acceptChatRequest(chatRoomId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    refreshChatRooms();
                    callback.onSuccess();
                } else {
                    callback.onError("수락 실패: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError("네트워크 오류: " + t.getMessage());
            }
        });
    }
    
    public void rejectChatRequest(Long chatRoomId, RejectChatRequestCallback callback) {
        apiService.rejectChatRequest(chatRoomId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onError("거절 실패: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError("네트워크 오류: " + t.getMessage());
            }
        });
    }
    
    public void getChatRooms(GetChatRoomsCallback callback) {
        if (currentUserId == null) {
            callback.onError("사용자 ID가 없습니다.");
            return;
        }
        
        // 먼저 로컬 DB에서 로드
        executorService.execute(() -> {
            try {
                List<ChatRoomEntity> entities = chatRoomDao.getChatRoomsByUserId(currentUserId);
                List<ChatRoom> chatRooms = convertToChatRooms(entities);
                mainHandler.post(() -> callback.onSuccess(chatRooms));
            } catch (Exception e) {
                Log.e(TAG, "로컬 DB에서 채팅방 로드 실패", e);
                mainHandler.post(() -> callback.onError("로컬 DB 오류: " + e.getMessage()));
            }
        });
        
        // 서버에서 최신 데이터 가져오기
        apiService.getChatRooms(currentUserId).enqueue(new Callback<List<ChatRoom>>() {
            @Override
            public void onResponse(Call<List<ChatRoom>> call, Response<List<ChatRoom>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // 서버 데이터를 로컬 DB에 저장
                    executorService.execute(() -> {
                        List<ChatRoomEntity> entities = convertToChatRoomEntities(response.body());
                        chatRoomDao.insertChatRooms(entities);
                        
                        // UI 업데이트
                        List<ChatRoomEntity> updatedEntities = chatRoomDao.getChatRoomsByUserId(currentUserId);
                        List<ChatRoom> updatedRooms = convertToChatRooms(updatedEntities);
                        mainHandler.post(() -> callback.onSuccess(updatedRooms));
                    });
                } else {
                    callback.onError("서버 응답 오류: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<List<ChatRoom>> call, Throwable t) {
                callback.onError("네트워크 오류: " + t.getMessage());
            }
        });
    }
    
    public void getMessages(Long chatRoomId, GetMessagesCallback callback) {
        // 먼저 로컬 DB에서 로드
        executorService.execute(() -> {
            List<MessageEntity> entities = messageDao.getMessagesByChatRoomId(chatRoomId);
            List<Message> messages = convertToMessages(entities);
            mainHandler.post(() -> callback.onSuccess(messages));
        });
        
        // 서버에서 최신 메시지 가져오기
        apiService.getMessages(chatRoomId).enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // 서버 데이터를 로컬 DB에 저장
                    executorService.execute(() -> {
                        List<MessageEntity> entities = convertToMessageEntities(response.body());
                        messageDao.insertMessages(entities);
                        
                        // UI 업데이트
                        List<MessageEntity> updatedEntities = messageDao.getMessagesByChatRoomId(chatRoomId);
                        List<Message> updatedMessages = convertToMessages(updatedEntities);
                        mainHandler.post(() -> callback.onSuccess(updatedMessages));
                    });
                } else {
                    callback.onError("서버 응답 오류: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                callback.onError("네트워크 오류: " + t.getMessage());
            }
        });
    }
    
    public void sendMessage(Long chatRoomId, String content, SendMessageCallback callback) {
        if (currentUserId == null) {
            callback.onError("사용자 ID가 없습니다.");
            return;
        }
        
        ApiService.SendMessageRequest request = 
                new ApiService.SendMessageRequest(content, currentUserId);
        
        apiService.sendMessage(chatRoomId, request).enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Message message = response.body();
                    // 로컬 DB에 저장
                    executorService.execute(() -> {
                        MessageEntity entity = convertToMessageEntity(message);
                        messageDao.insertMessage(entity);
                    });
                    callback.onSuccess(message);
                } else {
                    callback.onError("메시지 전송 실패: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                callback.onError("네트워크 오류: " + t.getMessage());
            }
        });
        
        // WebSocket으로도 전송
        if (webSocketClient != null && webSocketClient.isOpen()) {
            webSocketClient.sendMessage(chatRoomId, content, currentUserId);
        }
    }
    
    public void deleteChatRoom(Long chatRoomId, DeleteChatRoomCallback callback) {
        apiService.deleteChatRoom(chatRoomId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // 로컬 DB에서도 삭제
                    executorService.execute(() -> {
                        chatRoomDao.deleteChatRoomById(chatRoomId);
                        messageDao.deleteMessagesByChatRoomId(chatRoomId);
                    });
                    callback.onSuccess();
                } else {
                    callback.onError("삭제 실패: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError("네트워크 오류: " + t.getMessage());
            }
        });
    }
    
    private void refreshChatRooms() {
        // 채팅방 목록 새로고침 (내부 사용)
    }
    
    // 변환 메서드들
    private List<ChatRoom> convertToChatRooms(List<ChatRoomEntity> entities) {
        List<ChatRoom> chatRooms = new ArrayList<>();
        for (ChatRoomEntity entity : entities) {
            ChatRoom room = new ChatRoom(
                    entity.id,
                    entity.userId,
                    entity.partnerId,
                    entity.lastMessage,
                    entity.lastMessageTime,
                    entity.isPending,
                    entity.createdAt
            );
            chatRooms.add(room);
        }
        return chatRooms;
    }
    
    private List<ChatRoomEntity> convertToChatRoomEntities(List<ChatRoom> chatRooms) {
        List<ChatRoomEntity> entities = new ArrayList<>();
        for (ChatRoom room : chatRooms) {
            ChatRoomEntity entity = new ChatRoomEntity();
            entity.id = room.getId();
            entity.userId = room.getUserId();
            entity.partnerId = room.getPartnerId();
            entity.lastMessage = room.getLastMessage();
            entity.lastMessageTime = room.getLastMessageTime();
            entity.isPending = room.isPending();
            entity.createdAt = room.getCreatedAt();
            entities.add(entity);
        }
        return entities;
    }
    
    private List<Message> convertToMessages(List<MessageEntity> entities) {
        List<Message> messages = new ArrayList<>();
        for (MessageEntity entity : entities) {
            boolean isSentByUser = entity.senderId != null && entity.senderId.equals(currentUserId);
            Message message = new Message(
                    entity.id,
                    entity.chatRoomId,
                    entity.senderId,
                    entity.content,
                    isSentByUser,
                    entity.timestamp,
                    entity.isRead
            );
            messages.add(message);
        }
        return messages;
    }
    
    private List<MessageEntity> convertToMessageEntities(List<Message> messages) {
        List<MessageEntity> entities = new ArrayList<>();
        for (Message message : messages) {
            MessageEntity entity = new MessageEntity();
            entity.id = message.getId();
            entity.chatRoomId = message.getChatRoomId();
            entity.senderId = message.getSenderId();
            entity.content = message.getContent();
            entity.timestamp = message.getTimestamp();
            entity.isRead = message.isRead();
            entities.add(entity);
        }
        return entities;
    }
    
    private MessageEntity convertToMessageEntity(Message message) {
        MessageEntity entity = new MessageEntity();
        entity.id = message.getId();
        entity.chatRoomId = message.getChatRoomId();
        entity.senderId = message.getSenderId();
        entity.content = message.getContent();
        entity.timestamp = message.getTimestamp();
        entity.isRead = message.isRead();
        return entity;
    }
    
    // Callback 인터페이스들
    public interface RequestChatCallback {
        void onSuccess();
        void onError(String error);
    }
    
    public interface AcceptChatRequestCallback {
        void onSuccess();
        void onError(String error);
    }
    
    public interface RejectChatRequestCallback {
        void onSuccess();
        void onError(String error);
    }
    
    public interface GetChatRoomsCallback {
        void onSuccess(List<ChatRoom> chatRooms);
        void onError(String error);
    }
    
    public interface GetMessagesCallback {
        void onSuccess(List<Message> messages);
        void onError(String error);
    }
    
    public interface SendMessageCallback {
        void onSuccess(Message message);
        void onError(String error);
    }
    
    public interface DeleteChatRoomCallback {
        void onSuccess();
        void onError(String error);
    }
}

