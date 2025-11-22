package com.example.sajudatingapp.websocket;

import android.util.Log;
import com.example.sajudatingapp.model.Message;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.net.URI;
import java.util.Date;

public class ChatWebSocketClient extends WebSocketClient {
    private static final String TAG = "ChatWebSocketClient";
    private MessageListener messageListener;
    
    public interface MessageListener {
        void onMessageReceived(Message message);
        void onChatRequestReceived(Long chatRoomId, Long requesterId);
        void onChatAccepted(Long chatRoomId);
        void onChatRejected(Long chatRoomId);
    }
    
    public ChatWebSocketClient(URI serverUri, MessageListener listener) {
        super(serverUri);
        this.messageListener = listener;
    }
    
    @Override
    public void onOpen(ServerHandshake handshake) {
        Log.d(TAG, "WebSocket 연결됨");
    }
    
    @Override
    public void onMessage(String message) {
        Log.d(TAG, "메시지 수신: " + message);
        try {
            Gson gson = new Gson();
            JsonObject json = gson.fromJson(message, JsonObject.class);
            String type = json.get("type").getAsString();
            
            switch (type) {
                case "message":
                    Message msg = gson.fromJson(json.get("data"), Message.class);
                    if (messageListener != null) {
                        messageListener.onMessageReceived(msg);
                    }
                    break;
                case "chat_request":
                    Long chatRoomId = json.get("data").getAsJsonObject().get("chatRoomId").getAsLong();
                    Long requesterId = json.get("data").getAsJsonObject().get("requesterId").getAsLong();
                    if (messageListener != null) {
                        messageListener.onChatRequestReceived(chatRoomId, requesterId);
                    }
                    break;
                case "chat_accepted":
                    Long acceptedChatRoomId = json.get("data").getAsJsonObject().get("chatRoomId").getAsLong();
                    if (messageListener != null) {
                        messageListener.onChatAccepted(acceptedChatRoomId);
                    }
                    break;
                case "chat_rejected":
                    Long rejectedChatRoomId = json.get("data").getAsJsonObject().get("chatRoomId").getAsLong();
                    if (messageListener != null) {
                        messageListener.onChatRejected(rejectedChatRoomId);
                    }
                    break;
            }
        } catch (Exception e) {
            Log.e(TAG, "메시지 파싱 오류", e);
        }
    }
    
    @Override
    public void onClose(int code, String reason, boolean remote) {
        Log.d(TAG, "WebSocket 연결 종료: " + reason);
    }
    
    @Override
    public void onError(Exception ex) {
        Log.e(TAG, "WebSocket 오류", ex);
    }
    
    public void sendMessage(Long chatRoomId, String content, Long senderId) {
        try {
            JsonObject json = new JsonObject();
            json.addProperty("type", "message");
            JsonObject data = new JsonObject();
            data.addProperty("chatRoomId", chatRoomId);
            data.addProperty("content", content);
            data.addProperty("senderId", senderId);
            json.add("data", data);
            
            send(json.toString());
        } catch (Exception e) {
            Log.e(TAG, "메시지 전송 오류", e);
        }
    }
}

