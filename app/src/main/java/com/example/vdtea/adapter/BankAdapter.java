package com.example.vdtea.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.vdtea.R;
import com.example.vdtea.model.Bank;

import java.util.List;

public class BankAdapter extends ArrayAdapter<Bank.Data> {
    public BankAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<Bank.Data> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.dropdown_item, parent,false);
        }
        Bank.Data data = getItem(position);
        if(data != null){
            TextView BankName = convertView.findViewById(R.id.bankName);
            BankName.setText(data.getBankName()+ " (" + data.getBankShortName() + ")");
            ImageView BankLogo = convertView.findViewById(R.id.bankImg);
            String image = data.getBankLogo();
            Glide.with(BankLogo.getContext())
                    .load(image)
                    .placeholder(com.google.firebase.database.R.drawable.notification_bg_normal)
                    .error(com.google.firebase.database.R.drawable.notification_bg_normal)
                    .into(BankLogo);
        }
        return convertView;
    }

}
