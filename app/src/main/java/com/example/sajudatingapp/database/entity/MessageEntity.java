package com.example.sajudatingapp.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import com.example.sajudatingapp.database.converter.DateConverter;
import java.util.Date;

@Entity(tableName = "messages")
@TypeConverters(DateConverter.class)
public class MessageEntity {
    @PrimaryKey
    public Long id;
    
    public Long chatRoomId;
    public Long senderId;
    public String content;
    public Date timestamp;
    public boolean isRead;
}

