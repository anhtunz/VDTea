package com.example.vdtea.adapter;

import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vdtea.R;
import com.example.vdtea.event.CartTouchButtonListener;
import com.example.vdtea.model.Cart;
import com.example.vdtea.model.Drinks;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import static android.content.ContentValues.TAG;

import java.util.HashMap;
import java.util.Map;

public class CartAdapter extends FirebaseRecyclerAdapter<Cart, CartAdapter.cartViewHolder> {
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    FirebaseUser user;
    long totalPrice = 0, totalSalePrice = 0,count,count_new,drinksOPrice, drinksSPrice,sale;
    CartTouchButtonListener cartTouchButtonListener;
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public CartAdapter(@NonNull FirebaseRecyclerOptions<Cart> options, CartTouchButtonListener listener) {
        super(options);
        this.cartTouchButtonListener = listener;
    }

    @Override
    protected void onBindViewHolder(@NonNull cartViewHolder holder, int position, @NonNull Cart cart) {
        user = mAuth.getCurrentUser();
        String drinksID = cart.getDrinks_id();
        holder.drinksIce.setText(cart.getDrinks_ice());
        holder.drinksSize.setText(cart.getDrinks_size());
        holder.drinksSugar.setText(cart.getDrinks_sugar());
        holder.drinksCount.setText(String.valueOf(cart.getQuantity()));
        FirebaseDatabase.getInstance().getReference().child("drinks").child(drinksID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Drinks drinks = snapshot.getValue(Drinks.class);
                holder.drinksName.setText(drinks.getDrinks_name());
                Glide.with(holder.image.getContext())
                        .load(drinks.getDrinks_image())
                        .placeholder(com.google.firebase.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                        .centerCrop()
                        .error(com.google.firebase.appcheck.interop.R.drawable.notification_tile_bg)
                        .into(holder.image);
                holder.drinksOPrice.setText(String.valueOf(drinks.getPrice()) + ",000đ");

                drinksOPrice = drinks.getPrice() ;
                totalPrice += drinks.getPrice()* cart.getQuantity();
                Log.d(TAG, "DrinksOPrice " + totalPrice);
                Log.d(TAG, "DrinksSPrice " + totalSalePrice);
                sale = drinks.getSale();
                Log.d(TAG, "sale " + sale);
                if(drinks.getSale() == 1){
                    holder.drinksSPrice.setText("");
                    long price = drinks.getPrice()* cart.getQuantity();
                    totalSalePrice += price;
                }
                else{
                    holder.drinksOPrice.setPaintFlags(holder.drinksOPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.drinksOPrice.setText(String.valueOf(drinksOPrice)+ ",000đ");
                    holder.drinksSPrice.setText(String.valueOf( (drinks.getPrice()-(drinks.getPrice() * drinks.getSale()/ 100) ) + ",000đ"));
                    totalSalePrice += (drinks.getPrice()-(drinks.getPrice() * drinks.getSale()/ 100)) * cart.getQuantity();
                    drinksSPrice = drinks.getPrice()-(drinks.getPrice() * drinks.getSale()/ 100);
                    Log.d(TAG, "totalSalePrice: " + totalSalePrice);
                }
                cartTouchButtonListener.onCartListener(totalPrice,totalSalePrice);

                holder.cart_btnSub.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "Đã click vào nút trừ");
                        if (cart.getQuantity() == 1){
                            return;
                        }
                        count_new = cart.getQuantity() - 1;
                        Log.d(TAG, "Countnew: " + count_new);
                        holder.drinksCount.setText(String.valueOf(count_new));
//                        Map<String, Object> map = new HashMap<>();
//                        map.put("quantity",Long.parseLong(String.valueOf(count_new)));
//                        FirebaseDatabase.getInstance().getReference().child("cart")
//                                .child(user.getUid())
//                                .child(getRef(position).getKey()).updateChildren(map);
                        if(drinks.getSale() == 1){
                            totalPrice -= drinksOPrice;
                            totalSalePrice -= drinksOPrice;
                            cartTouchButtonListener.onCartListener(totalPrice,totalSalePrice);
                        }
                        else{
                            totalPrice -= drinksOPrice;
                            totalSalePrice -= drinksSPrice;
                            cartTouchButtonListener.onCartListener(totalPrice,totalSalePrice);
                        }

                    };
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

//        holder.cart_btnAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                count ++;
//                holder.drinksCount.setText(String.valueOf(count));
//
//            }
//        });
//        Log.d(TAG, "totalPriceAdapter: "+ totalPrice);
//        Log.d(TAG, "totalSalePriceAdapter: " + totalSalePrice);

    }

    @NonNull
    @Override
    public cartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item,parent,false);
        return new CartAdapter.cartViewHolder(view);
    }

    class cartViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView drinksName, drinksIce,drinksSize,drinksSugar,drinksCount,drinksOPrice, drinksSPrice;

        TextView cart_btnSub, cart_btnAdd;
        public cartViewHolder(@NonNull View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.cart_drinksImage);
            drinksName = (TextView) itemView.findViewById(R.id.cart_drinksName);
            drinksIce = (TextView) itemView.findViewById(R.id.cart_drinksIce);
            drinksSize = (TextView) itemView.findViewById(R.id.cart_drinksSize);
            drinksSugar = (TextView) itemView.findViewById(R.id.cart_drinksSugar);
            drinksCount = (TextView) itemView.findViewById(R.id.cart_drinksCount);
            drinksOPrice = (TextView) itemView.findViewById(R.id.cart_drinksOriginalPrice);
            drinksSPrice = (TextView) itemView.findViewById(R.id.cart_drinksSalePrice);
            cart_btnSub = (TextView) itemView.findViewById(R.id.cart_drinksSub);
            cart_btnAdd = (TextView) itemView.findViewById(R.id.cart_drinksAdd);
        }
    }
}
