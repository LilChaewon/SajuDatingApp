package com.example.sajudatingapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sajudatingapp.R;
import com.example.sajudatingapp.activity.ChatActivity;
import com.example.sajudatingapp.adapter.ChatListAdapter;
import com.example.sajudatingapp.manager.ChatManager;
import com.example.sajudatingapp.model.ChatRoom;
import com.example.sajudatingapp.util.StubDataUtil;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

    private RecyclerView recyclerView;
    private ChatListAdapter adapter;
    private List<ChatRoom> chatRoomList;
    private ChatManager chatManager;
    private Handler mainHandler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainHandler = new Handler(Looper.getMainLooper());
        chatRoomList = new ArrayList<>();

        recyclerView = view.findViewById(R.id.chat_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ChatListAdapter(chatRoomList);
        adapter.setOnChatRoomClickListener(chatRoom -> {
            Intent intent = new Intent(getContext(), ChatActivity.class);
            intent.putExtra("CHAT_ROOM_ID", chatRoom.getId());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        // UI 확인용: 더미 데이터만 사용 (ChatManager 초기화 제거)
        loadDummyDataFirst();
        
        // TODO: 백엔드 서버가 준비되면 아래 코드를 활성화
        /*
        // Context를 미리 저장
        final android.content.Context context = getContext();
        if (context == null) {
            return; // Fragment가 detach된 경우
        }
        
        // ChatManager 초기화를 메인 스레드에서 시도 (Room DB는 메인 스레드에서 초기화 가능)
        try {
            chatManager = ChatManager.getInstance(context);
            // 초기화 성공 시 백그라운드에서 데이터 로드
            new Thread(() -> {
                try {
                    mainHandler.post(() -> loadChatRooms());
                } catch (Exception e) {
                    android.util.Log.e("ChatFragment", "데이터 로드 실패", e);
                }
            }).start();
        } catch (Exception e) {
            android.util.Log.e("ChatFragment", "ChatManager 초기화 실패", e);
            e.printStackTrace();
            // 초기화 실패 시 더미 데이터만 사용
        }
        */
    }
    
    // UI 확인용: 더미 데이터 먼저 표시
    private void loadDummyDataFirst() {
        chatRoomList.clear();
        List<ChatRoom> dummyRooms = StubDataUtil.getChatRooms();
        chatRoomList.addAll(dummyRooms);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        // 더미 데이터만 사용 (ChatManager 비활성화)
        loadDummyDataFirst();
        
        // TODO: 백엔드 서버가 준비되면 아래 코드를 활성화
        /*
        // ChatManager가 초기화되었을 때만 로드
        if (chatManager != null) {
            loadChatRooms();
        } else {
            // 초기화되지 않았으면 더미 데이터만 표시
            loadDummyDataFirst();
        }
        */
    }

    private void loadChatRooms() {
        // 더미 데이터만 사용 (ChatManager 비활성화)
        loadDummyDataFirst();
        
        // TODO: 백엔드 서버가 준비되면 아래 코드를 활성화
        /*
        // ChatManager가 아직 초기화되지 않았으면 더미 데이터만 표시
        if (chatManager == null) {
            loadDummyDataFirst();
            return;
        }
        
        // TODO: 실제 사용자 ID를 가져와야 함 (SharedPreferences 등에서)
        Long currentUserId = 1L; // 임시 값
        try {
            chatManager.setCurrentUserId(currentUserId);
        } catch (Exception e) {
            android.util.Log.e("ChatFragment", "setCurrentUserId 실패", e);
            loadDummyDataFirst();
            return;
        }

        chatManager.getChatRooms(new ChatManager.GetChatRoomsCallback() {
            @Override
            public void onSuccess(List<ChatRoom> chatRooms) {
                mainHandler.post(() -> {
                    chatRoomList.clear();
                    chatRoomList.addAll(chatRooms);
                    adapter.notifyDataSetChanged();
                });
            }

            @Override
            public void onError(String error) {
                // 서버 연결 실패 시 더미 데이터 사용 (UI 확인용)
                mainHandler.post(() -> {
                    chatRoomList.clear();
                    List<ChatRoom> dummyRooms = StubDataUtil.getChatRooms();
                    chatRoomList.addAll(dummyRooms);
                    adapter.notifyDataSetChanged();
                });
            }
        });
        */
    }
}
