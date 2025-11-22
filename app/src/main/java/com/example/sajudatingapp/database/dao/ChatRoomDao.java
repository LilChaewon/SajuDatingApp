package com.example.sajudatingapp.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.sajudatingapp.database.entity.ChatRoomEntity;
import java.util.List;

@Dao
public interface ChatRoomDao {
    @Query("SELECT * FROM chat_rooms WHERE userId = :userId ORDER BY lastMessageTime DESC")
    List<ChatRoomEntity> getChatRoomsByUserId(Long userId);
    
    @Query("SELECT * FROM chat_rooms WHERE id = :id")
    ChatRoomEntity getChatRoomById(Long id);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertChatRoom(ChatRoomEntity chatRoom);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertChatRooms(List<ChatRoomEntity> chatRooms);
    
    @Delete
    void deleteChatRoom(ChatRoomEntity chatRoom);
    
    @Query("DELETE FROM chat_rooms WHERE id = :id")
    void deleteChatRoomById(Long id);
}

