package com.example.sajudatingapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.sajudatingapp.R;
import com.example.sajudatingapp.adapter.MatchCardAdapter;
import com.example.sajudatingapp.model.User;
import com.example.sajudatingapp.util.StubDataUtil;

import java.util.List;

public class MainFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPager2 viewPager = view.findViewById(R.id.match_card_view_pager);
        List<User> userList = StubDataUtil.getTodayMatchCandidates();
        MatchCardAdapter adapter = new MatchCardAdapter(userList);
        viewPager.setAdapter(adapter);
    }
}
