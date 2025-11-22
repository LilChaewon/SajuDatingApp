package com.example.sajudatingapp.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import com.example.sajudatingapp.database.converter.DateConverter;
import java.util.Date;

@Entity(tableName = "chat_rooms")
@TypeConverters(DateConverter.class)
public class ChatRoomEntity {
    @PrimaryKey
    public Long id;
    
    public Long userId;
    public Long partnerId;
    public String lastMessage;
    public Date lastMessageTime;
    public boolean isPending;
    public Date createdAt;
}

