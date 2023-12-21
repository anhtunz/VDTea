package com.example.vdtea.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.vdtea.R;
import com.example.vdtea.model.Booking;

import java.util.List;


public class MenuAdapter extends ArrayAdapter<Booking.Menu> {
    float size = 15;
    public MenuAdapter(@NonNull Context context, int resource, @NonNull List<Booking.Menu> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.method_item,parent,false);
        Booking.Menu menu = getItem(position);
        TextView menu_detail = convertView.findViewById(R.id.textview);
        menu_detail.setTextSize(size);
        menu_detail.setPadding(0,0,0,0);
        menu_detail.setText(menu != null ? "(*) "+ menu.getMenu_detail() : "");
        return convertView;
    }
}
