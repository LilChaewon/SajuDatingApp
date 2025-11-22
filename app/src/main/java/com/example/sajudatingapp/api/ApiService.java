package com.example.sajudatingapp.api;

import com.example.sajudatingapp.model.ChatRoom;
import com.example.sajudatingapp.model.Message;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Body;
import retrofit2.http.Query;

public interface ApiService {
    
    @POST("/api/chat/request")
    Call<Void> requestChat(@Body ChatRequestRequest request);
    
    @POST("/api/chat/{chatRoomId}/accept")
    Call<Void> acceptChatRequest(@Path("chatRoomId") Long chatRoomId);
    
    @POST("/api/chat/{chatRoomId}/reject")
    Call<Void> rejectChatRequest(@Path("chatRoomId") Long chatRoomId);
    
    @GET("/api/chat/rooms")
    Call<List<ChatRoom>> getChatRooms(@Query("userId") Long userId);
    
    @GET("/api/chat/{chatRoomId}/messages")
    Call<List<Message>> getMessages(@Path("chatRoomId") Long chatRoomId);
    
    @POST("/api/chat/{chatRoomId}/messages")
    Call<Message> sendMessage(@Path("chatRoomId") Long chatRoomId, @Body SendMessageRequest request);
    
    @DELETE("/api/chat/{chatRoomId}")
    Call<Void> deleteChatRoom(@Path("chatRoomId") Long chatRoomId);
    
    class ChatRequestRequest {
        public Long requesterId;
        public Long receiverId;
        
        public ChatRequestRequest(Long requesterId, Long receiverId) {
            this.requesterId = requesterId;
            this.receiverId = receiverId;
        }
    }
    
    class SendMessageRequest {
        public String content;
        public Long senderId;
        
        public SendMessageRequest(String content, Long senderId) {
            this.content = content;
            this.senderId = senderId;
        }
    }
}

