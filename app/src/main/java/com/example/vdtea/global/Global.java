package com.example.vdtea.global;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.example.vdtea.R;
import com.example.vdtea.activity.LoginActivity;

public class Global {

    private Activity activity;
    private AlertDialog dialog;

    public Global(Activity myActivity){
        activity = myActivity;
    }

    public void startProgressBar(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_process, null));
        builder.setCancelable(true);
        dialog = builder.create();
        dialog.show();
    }

    public void closeProgressBar(){
        dialog.dismiss();
    }
}
