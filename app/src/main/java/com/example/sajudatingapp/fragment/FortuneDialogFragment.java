package com.example.sajudatingapp.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.sajudatingapp.R;
import com.example.sajudatingapp.util.StubDataUtil;

public class FortuneDialogFragment extends DialogFragment {

    public interface OnFortuneDialogDismissedListener {
        void onFortuneDialogDismissed();
    }

    private OnFortuneDialogDismissedListener listener;

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

        TextView fortuneText = view.findViewById(R.id.tv_fortune);
        Button closeButton = view.findViewById(R.id.btn_close_fortune);

        fortuneText.setText(StubDataUtil.getTodayFortune());

        closeButton.setOnClickListener(v -> dismiss());

        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (listener != null) {
            listener.onFortuneDialogDismissed();
        }
    }
}
