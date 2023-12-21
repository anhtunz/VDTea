package com.example.vdtea.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vdtea.R;
import com.example.vdtea.model.Booking;

import java.util.List;


//public class MenuAdapter extends ArrayAdapter<Booking.Menu> {
//    float size = 13;
//    public MenuAdapter(@NonNull Context context, int resource, @NonNull List<Booking.Menu> objects) {
//        super(context, resource, objects);
//    }
//
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.method_item,parent,false);
//        Booking.Menu menu = getItem(position);
//        TextView menu_detail = convertView.findViewById(R.id.textview);
//        menu_detail.setTextSize(size);
//        menu_detail.setPadding(0,0,0,3);
//        menu_detail.setText(menu != null ? "(*) "+ menu.getMenu_detail() : "");
//        return convertView;
//    }
//}
public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private List<Booking.Menu> menuList;
    float size = 13;
    public MenuAdapter(List<Booking.Menu> menuList) {
        this.menuList = menuList;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.method_item, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        Booking.Menu menu = menuList.get(position);
        holder.menu_detail.setTextSize(size);
        holder.menu_detail.setPadding(0,0,0,3);
        holder.menu_detail.setText(menu != null ? "(*)" +  menu.getMenu_detail() : "");
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    static class MenuViewHolder extends RecyclerView.ViewHolder {
        TextView menu_detail;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            menu_detail = itemView.findViewById(R.id.textview);
        }
    }
}
