package com.example.sajudatingapp.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.example.sajudatingapp.R;
import com.example.sajudatingapp.manager.ChatManager;

public class ChatRequestDialogFragment extends DialogFragment {
    private static final String ARG_CHAT_ROOM_ID = "chatRoomId";
    private static final String ARG_REQUESTER_NAME = "requesterName";
    
    private Long chatRoomId;
    private String requesterName;
    private ChatManager chatManager;
    
    public static ChatRequestDialogFragment newInstance(Long chatRoomId, String requesterName) {
        ChatRequestDialogFragment fragment = new ChatRequestDialogFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_CHAT_ROOM_ID, chatRoomId);
        args.putString(ARG_REQUESTER_NAME, requesterName);
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            chatRoomId = getArguments().getLong(ARG_CHAT_ROOM_ID);
            requesterName = getArguments().getString(ARG_REQUESTER_NAME);
        }
        chatManager = ChatManager.getInstance(requireContext());
    }
    
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_chat_request, null);
        
        TextView requesterNameText = view.findViewById(R.id.tv_requester_name);
        requesterNameText.setText(requesterName + "님이 대화를 요청했습니다.");
        
        builder.setView(view)
                .setPositiveButton("수락", (dialog, which) -> {
                    chatManager.acceptChatRequest(chatRoomId, new ChatManager.AcceptChatRequestCallback() {
                        @Override
                        public void onSuccess() {
                            dismiss();
                        }
                        
                        @Override
                        public void onError(String error) {
                            // 에러 처리
                        }
                    });
                })
                .setNegativeButton("거절", (dialog, which) -> {
                    chatManager.rejectChatRequest(chatRoomId, new ChatManager.RejectChatRequestCallback() {
                        @Override
                        public void onSuccess() {
                            dismiss();
                        }
                        
                        @Override
                        public void onError(String error) {
                            // 에러 처리
                        }
                    });
                });
        
        return builder.create();
    }
}

