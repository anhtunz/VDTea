package com.example.vdtea.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.vdtea.R;
import com.example.vdtea.adapter.CartAdapter;
import com.example.vdtea.event.CartTouchButtonListener;
import com.example.vdtea.model.Cart;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import static android.content.ContentValues.TAG;
public class CartActivity extends AppCompatActivity implements CartTouchButtonListener {

    RecyclerView cart_rcv;
    CartAdapter cartAdapter;
    TextView totalCart_OPrice, totalCart_SPrice;
    long totalOPrice, totalSPrice;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        user = mAuth.getCurrentUser();
        cart_rcv = findViewById(R.id.cart_rcv);
        cart_rcv.setLayoutManager(new LinearLayoutManager(this));
        totalCart_OPrice = findViewById(R.id.cart_TotalOPrice);
        totalCart_SPrice = findViewById(R.id.cart_totalSPrice);
        FirebaseRecyclerOptions<Cart> options =
            new FirebaseRecyclerOptions.Builder<Cart>()
                    .setQuery(FirebaseDatabase.getInstance().getReference().child("cart").child(user.getUid()), Cart.class)
                    .build();
        cartAdapter = new CartAdapter(options, this::onCartListener);
        cartAdapter.startListening();
        cart_rcv.setAdapter(cartAdapter);
        Log.d(TAG, "TotalOPricemainAdapter: " + totalCart_OPrice);
        Log.d(TAG, "TotalSPricemainAdapter: " + totalCart_SPrice);
    }

    @Override
    protected void onStart() {
        super.onStart();
        cartAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        cartAdapter.stopListening();
    }

    @Override
    public void onCartListener(long OPrice, long SPrice) {
        totalOPrice = OPrice;
        totalSPrice = SPrice;
        Log.d(TAG, "Oprice: " + totalOPrice);
        Log.d(TAG, "SPrice: " + totalSPrice);
        if(totalSPrice == totalOPrice){
            totalCart_OPrice.setText("");
            totalCart_SPrice.setText(String.valueOf(totalOPrice)+ ",000đ");
        }
        else{
            totalCart_OPrice.setPaintFlags(totalCart_OPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            totalCart_OPrice.setText(String.valueOf(totalOPrice)+",000đ");
            totalCart_SPrice.setText(String.valueOf(totalSPrice)+ ",000đ");
        }
    }
}