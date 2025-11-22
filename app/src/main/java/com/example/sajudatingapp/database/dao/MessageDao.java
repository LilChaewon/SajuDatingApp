package com.example.sajudatingapp.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.sajudatingapp.database.entity.MessageEntity;
import java.util.List;

@Dao
public interface MessageDao {
    @Query("SELECT * FROM messages WHERE chatRoomId = :chatRoomId ORDER BY timestamp ASC")
    List<MessageEntity> getMessagesByChatRoomId(Long chatRoomId);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMessage(MessageEntity message);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMessages(List<MessageEntity> messages);
    
    @Query("DELETE FROM messages WHERE chatRoomId = :chatRoomId")
    void deleteMessagesByChatRoomId(Long chatRoomId);
}

