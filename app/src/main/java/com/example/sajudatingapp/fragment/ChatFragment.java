package com.example.sajudatingapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sajudatingapp.R;
import com.example.sajudatingapp.adapter.ChatListAdapter;
import com.example.sajudatingapp.model.ChatRoom;
import com.example.sajudatingapp.util.StubDataUtil;

import java.util.List;

public class ChatFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.chat_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<ChatRoom> chatRoomList = StubDataUtil.getChatRooms();
        ChatListAdapter adapter = new ChatListAdapter(chatRoomList);
        recyclerView.setAdapter(adapter);
    }
}
