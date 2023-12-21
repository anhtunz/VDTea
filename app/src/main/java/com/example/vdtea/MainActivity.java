package com.example.vdtea;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.example.vdtea.activity.BookingActivity;
import com.example.vdtea.activity.CartActivity;
import com.example.vdtea.activity.DrinksDetailActivity;
import com.example.vdtea.activity.LoginActivity;
import com.example.vdtea.activity.RegisterActivity;
import com.example.vdtea.activity.UsersActivity;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchmenu, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                processsearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                processsearch(s);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }



    private void bottom_navigation() {
        LinearLayout homeBtn = findViewById(R.id.homeBtn);
        LinearLayout cartBtn = findViewById(R.id.cartBtn);
        LinearLayout orderBtn = findViewById(R.id.orderBtn);
        LinearLayout profileBtn = findViewById(R.id.profileBtn);

        homeBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, MainActivity.class)));
        cartBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, CartActivity.class)));
        orderBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, BookingActivity.class)));
        profileBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, UsersActivity.class)));
    }
    private void processsearch(String s) {
        FirebaseRecyclerOptions<Drinks> options =
                new FirebaseRecyclerOptions.Builder<Drinks>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("drinks").orderByChild("drinks_name").startAt(s).endAt(s+"\uf8ff"), Drinks.class)
                        .build();
        mainAdapter = new MainAdapter(options);
        mainAdapter.startListening();
        recyclerView.setAdapter(mainAdapter);
    }
}