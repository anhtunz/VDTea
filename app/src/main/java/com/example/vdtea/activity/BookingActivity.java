package com.example.vdtea.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vdtea.R;
import com.example.vdtea.adapter.BookingAdapter;
import com.example.vdtea.model.Booking;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class BookingActivity extends AppCompatActivity {

    BookingAdapter bookingAdapter;
    RecyclerView recyclerView;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        recyclerView = findViewById(R.id.bookingrcv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<Booking> bookingOptions =
                new FirebaseRecyclerOptions.Builder<Booking>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("booking").child(user.getUid()), Booking.class)
                        .build();
        bookingAdapter = new BookingAdapter(bookingOptions,this);
        bookingAdapter.startListening();
        recyclerView.setAdapter(bookingAdapter);
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Đơn hàng của tôi");
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(BookingActivity.this, MainActivity.class);
            startActivity(intent);
        });
        bottom_navigation();
    }
    private void bottom_navigation() {
        LinearLayout homeBtn = findViewById(R.id.homeBtn);
        LinearLayout cartBtn = findViewById(R.id.cartBtn);
        LinearLayout orderBtn = findViewById(R.id.orderBtn);
        LinearLayout profileBtn = findViewById(R.id.profileBtn);

        homeBtn.setOnClickListener(v -> startActivity(new Intent(BookingActivity.this, MainActivity.class)));
        cartBtn.setOnClickListener(v -> startActivity(new Intent(BookingActivity.this, CartActivity.class)));
        orderBtn.setOnClickListener(v -> startActivity(new Intent(BookingActivity.this, BookingActivity.class)));
        profileBtn.setOnClickListener(v -> startActivity(new Intent(BookingActivity.this, UsersActivity.class)));
    }
}
