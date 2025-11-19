package com.example.sajudatingapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sajudatingapp.R;
import com.example.sajudatingapp.util.StubDataUtil;

public class ProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button saveButton = view.findViewById(R.id.btn_save_profile);
        saveButton.setOnClickListener(v -> {
            if (StubDataUtil.saveProfile()) {
                Toast.makeText(getContext(), "프로필이 저장되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
