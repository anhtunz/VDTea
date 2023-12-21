package com.example.vdtea.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vdtea.R;
import com.example.vdtea.activity.CartActivity;
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
    long totalPrice = 0, totalSalePrice = 0,count,count_new,drinksOPrice, drinksSPrice,sale,totalOPrice_old,totalSPrice_old;
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
                totalOPrice_old += drinks.getPrice()* cart.getQuantity();
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

                    totalSPrice_old = (drinks.getPrice()-(drinks.getPrice() * drinks.getSale()/ 100)) * cart.getQuantity();

                }
                cartTouchButtonListener.onCartListener(totalPrice,totalSalePrice);
                holder.cart_btnSub.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (cart.getQuantity() == 1) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(holder.drinksName.getContext());
                            builder.setTitle("Bạn muốn xóa sản phẩm này?");
                            builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    totalPrice -= drinks.getPrice()* cart.getQuantity();
                                    if (drinks.getSale() == 1) {
                                        totalSalePrice -= drinksOPrice * cart.getQuantity();
                                    } else {
                                        totalSalePrice -= drinksSPrice * cart.getQuantity();
                                    }
                                    Log.e(TAG, "TotalPrice " + totalPrice );
                                    Log.e(TAG, "TotalSPrice " + totalSalePrice );
                                    cartTouchButtonListener.onCartListener(totalPrice, totalSalePrice);
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("cart")
                                            .child(user.getUid())
                                            .child(getRef(position).getKey())
                                            .removeValue();
                                }
                            });
                            builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(holder.drinksName.getContext(),"Đã hủy",Toast.LENGTH_SHORT);
                                }
                            });
                            builder.show();
                        }
                        else{
                            count_new = cart.getQuantity() - 1;
                            holder.drinksCount.setText(String.valueOf(count_new));
                            FirebaseDatabase.getInstance().getReference().child("cart")
                                    .child(user.getUid())
                                    .child(getRef(position).getKey())
                                    .child("quantity")
                                    .setValue(count_new)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            totalPrice -= drinks.getPrice()* cart.getQuantity();
                                            if (drinks.getSale() == 1) {
                                                totalSalePrice -= drinksOPrice * cart.getQuantity();
                                            } else {
                                                totalSalePrice -= drinksSPrice * cart.getQuantity();
                                            }
                                            cartTouchButtonListener.onCartListener(totalPrice, totalSalePrice);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d(TAG, "Update quantity failed: " + e.getMessage());
                                        }
                                    });
                        }
                        }

                });
                holder.cart_btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        count_new = cart.getQuantity() + 1;
                        holder.drinksCount.setText(String.valueOf(count_new));
                        FirebaseDatabase.getInstance().getReference()
                                .child("cart")
                                .child(user.getUid())
                                .child(getRef(position).getKey())
                                .child("quantity")
                                .setValue(count_new)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        totalPrice = totalPrice - drinksOPrice * cart.getQuantity();
                                        if(sale == 1){
                                            totalSalePrice = totalSalePrice - drinksOPrice * cart.getQuantity();
                                        }
                                        else{
                                            totalSalePrice = totalSalePrice - drinksSPrice * cart.getQuantity();
                                        }
                                        cartTouchButtonListener.onCartListener(totalPrice,totalSalePrice);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "Update quantity failed: " + e.getMessage());
                                    }
                                });
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


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
