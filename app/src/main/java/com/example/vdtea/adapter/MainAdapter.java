package com.example.vdtea.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vdtea.R;
import com.example.vdtea.activity.DrinksDetailActivity;
import com.example.vdtea.event.ItemClickListener;
import com.example.vdtea.fragment.DrinksDetailFragment;
import com.example.vdtea.model.Drinks;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import static android.content.ContentValues.TAG;
public class MainAdapter extends FirebaseRecyclerAdapter<Drinks, MainAdapter.myViewHolder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MainAdapter(@NonNull FirebaseRecyclerOptions<Drinks> options) {
        super(options);
    }
    @Override
    protected void onBindViewHolder(@NonNull MainAdapter.myViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull Drinks model) {
        String itemID = getRef(position).getKey();
        holder.name.setText(model.getDrinks_name());
        holder.soldCount.setText("Đã bán: "+String.valueOf(model.getSold_count()));
        holder.rating.setText("Đánh giá: " + String.valueOf(model.getRating())+"/5");
        holder.priceOriginal.setText(String.valueOf(model.getPrice()+",000đ"));
        holder.txt_add.setVisibility(View.GONE);
        holder.txt_count.setVisibility(View.GONE);
        holder.txt_sub.setVisibility(View.GONE);
        if(model.getSale() == 1){
            holder.priceSale.setText("");
        }
        else{
            holder.priceOriginal.setPaintFlags(holder.priceOriginal.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.priceSale.setText(String.valueOf( (model.getPrice()-(model.getPrice() * model .getSale()/ 100) ) + ",000đ"));
        }
        Log.d(TAG, "onBindViewHolder: "+ model.getDrinks_image());
        Glide.with(holder.img.getContext())
                .load(model.getDrinks_image())
                .placeholder(com.google.firebase.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .centerCrop()
                .error(com.google.firebase.appcheck.interop.R.drawable.notification_tile_bg)
                .into(holder.img);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Đã goi hàm omclixk");
                String data = getRef(position).getKey();
                Intent intent = new Intent(v.getContext(), DrinksDetailActivity.class);
                intent.putExtra("KEY_DATA",data);
                v.getContext().startActivity(intent);
                Log.d(TAG, "Data in MainAdapter: "+data);
            }
        });
    }

    @NonNull
    @Override
    public MainAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drinks_items,parent,false);
       return new myViewHolder(view);
    }
//    public void setItemClickListener(ItemClickListener itemClickListener) {
//        this.itemClickListener = itemClickListener;
//    }
//
//        public interface ItemClickListener {
//        void onClickItem(Drinks drinks);
//
//    }
    class myViewHolder extends RecyclerView.ViewHolder{

        ImageView img;

        TextView name, soldCount, rating, priceOriginal, priceSale,txt_add, txt_sub,txt_count;

        CardView cardView;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            img = (ImageView)itemView.findViewById(R.id.drinksImage);
            name = (TextView) itemView.findViewById(R.id.drinksName);
            soldCount = (TextView) itemView.findViewById(R.id.drinkssold);
            rating = (TextView) itemView.findViewById(R.id.drinksrating);
            priceOriginal = (TextView) itemView.findViewById(R.id.drinksOriginalPrice);
            priceSale = (TextView) itemView.findViewById(R.id.drinksSalePrice);
            txt_add = (TextView) itemView.findViewById(R.id.btn_add_drinks);
            txt_count = (TextView) itemView.findViewById(R.id.drinks_count);
            txt_sub = (TextView) itemView.findViewById(R.id.btn_subtract_drinks);
            cardView = (CardView) itemView.findViewById(R.id.drinksitem);

        }
    }

}
