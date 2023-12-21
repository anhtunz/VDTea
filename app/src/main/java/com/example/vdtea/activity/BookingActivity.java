package com.example.vdtea.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vdtea.R;
import com.example.vdtea.adapter.BookingAdapter;
import com.example.vdtea.model.Booking;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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

    }
}
