package com.example.vdtea;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.vdtea.activity.BookingActivity;
import com.example.vdtea.activity.CartActivity;
import com.example.vdtea.activity.DrinksDetailActivity;
import com.example.vdtea.activity.LoginActivity;
import com.example.vdtea.activity.RegisterActivity;
import com.example.vdtea.adapter.MainAdapter;
import com.example.vdtea.model.Drinks;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MainAdapter mainAdapter;

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.rcv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<Drinks> options =
                new FirebaseRecyclerOptions.Builder<Drinks>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("drinks"), Drinks.class)
                        .build();

        mainAdapter = new MainAdapter(options);
        mainAdapter.startListening();
        recyclerView.setAdapter(mainAdapter);

        bottom_navigation();


    }

    @Override
    protected void onStart() {
        super.onStart();
        mainAdapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        mainAdapter.stopListening();
    }
    private void bottom_navigation() {
        LinearLayout homeBtn = findViewById(R.id.homeBtn);
        LinearLayout cartBtn = findViewById(R.id.cartBtn);
        LinearLayout orderBtn = findViewById(R.id.orderBtn);
        LinearLayout profileBtn = findViewById(R.id.profileBtn);

        homeBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, MainActivity.class)));
        cartBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, CartActivity.class)));
        orderBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, BookingActivity.class)));
        profileBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, RegisterActivity.class)));
    }
}