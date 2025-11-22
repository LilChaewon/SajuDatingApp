package com.example.sajudatingapp.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.sajudatingapp.R;
import com.example.sajudatingapp.model.FortuneResponse;
import com.example.sajudatingapp.network.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FortuneDialogFragment extends DialogFragment {

    public interface OnFortuneDialogDismissedListener {
        void onFortuneDialogDismissed();
    }

    private OnFortuneDialogDismissedListener listener;
    private TextView fortuneText;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (OnFortuneDialogDismissedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnFortuneDialogDismissedListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_fortune_dialog, null);

        fortuneText = view.findViewById(R.id.tv_fortune);
        Button closeButton = view.findViewById(R.id.btn_close_fortune);

        fetchFortune();

        closeButton.setOnClickListener(v -> dismiss());

        builder.setView(view);
        return builder.create();
    }

    private void fetchFortune() {
        fortuneText.setText("오늘의 운세를 불러오는 중...");
        ApiClient.getApiService().getFortune().enqueue(new Callback<FortuneResponse>() {
            @Override
            public void onResponse(Call<FortuneResponse> call, Response<FortuneResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    fortuneText.setText(response.body().getGeneralFortune());
                } else {
                    fortuneText.setText("운세를 불러오는데 실패했습니다.");
                }
            }

            @Override
            public void onFailure(Call<FortuneResponse> call, Throwable t) {
                fortuneText.setText("네트워크 오류가 발생했습니다.");
            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (listener != null) {
            listener.onFortuneDialogDismissed();
        }
    }
}
