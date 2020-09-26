package com.example.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;

interface DialogClickListener{
    void onAccept();
    void onCancel();
}

public class DeleteDialog extends Dialog {

    Context context;
    DialogClickListener listener;

    public DeleteDialog(@NonNull Context context, DialogClickListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_dialog);
        Button btnAccept, btnCancel;
        btnAccept = findViewById(R.id.btnAccept);
        btnCancel = findViewById(R.id.btnCancel);

        btnAccept.setOnClickListener(v ->{
            this.listener.onAccept();
            dismiss();
        });
        btnCancel.setOnClickListener(v-> {
            this.listener.onCancel();
            dismiss();
        });
    }
}
