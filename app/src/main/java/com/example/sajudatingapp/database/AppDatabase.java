package com.example.sajudatingapp.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.sajudatingapp.database.dao.ChatRoomDao;
import com.example.sajudatingapp.database.dao.MessageDao;
import com.example.sajudatingapp.database.entity.ChatRoomEntity;
import com.example.sajudatingapp.database.entity.MessageEntity;

@Database(entities = {ChatRoomEntity.class, MessageEntity.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;
    
    public abstract ChatRoomDao chatRoomDao();
    public abstract MessageDao messageDao();
    
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "saju_dating_db"
            )
            .fallbackToDestructiveMigration() // 개발 중: 스키마 변경 시 기존 DB 삭제
            .build();
        }
        return instance;
    }
}

