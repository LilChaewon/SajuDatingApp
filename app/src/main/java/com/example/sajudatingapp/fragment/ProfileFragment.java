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
import com.example.sajudatingapp.model.User;
import com.example.sajudatingapp.network.ApiClient;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private TextInputEditText etName, etAge, etBirthdate, etJob, etLocation;
    private Button saveButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etName = view.findViewById(R.id.et_name);
        etAge = view.findViewById(R.id.et_age);
        etBirthdate = view.findViewById(R.id.et_birthdate);
        etJob = view.findViewById(R.id.et_job);
        etLocation = view.findViewById(R.id.et_location);
        saveButton = view.findViewById(R.id.btn_save_profile);

        fetchUserProfile();

        saveButton.setOnClickListener(v -> updateUserProfile());
    }

    private void fetchUserProfile() {
        ApiClient.getApiService().getUserProfile().enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    populateUI(response.body());
                } else {
                    Toast.makeText(getContext(), "프로필 정보를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getContext(), "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateUI(User user) {
        etName.setText(user.getName());
        etAge.setText(String.valueOf(user.getAge()));
        etBirthdate.setText(user.getBirthDate()); // Assuming birth_date is a string in YYYY-MM-DD format
        etJob.setText(user.getJob());
        etLocation.setText(user.getLocation());
    }
    
    private void updateUserProfile() {
        JsonObject userData = new JsonObject();
        userData.addProperty("name", etName.getText().toString());
        userData.addProperty("job", etJob.getText().toString());
        userData.addProperty("location", etLocation.getText().toString());
        // Bio field is missing from layout, can be added later.
        // userData.addProperty("bio", etBio.getText().toString());

        ApiClient.getApiService().updateUserProfile(userData).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "프로필이 저장되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "프로필 저장에 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getContext(), "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
