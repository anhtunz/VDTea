package com.example.vdtea.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.vdtea.R;
import com.example.vdtea.adapter.MainAdapter;
import com.example.vdtea.model.Drinks;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import static android.content.ContentValues.TAG;
public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MainAdapter mainAdapter;

    EditText search;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(user == null){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            String email = user.getEmail();
            int atIndex = email.indexOf('@');
            if (atIndex != -1) {
                String username = email.substring(0,atIndex);

                UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                        .setDisplayName(username)
                        .build();
                user.updateProfile(profileUpdate)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "Update username thanh cong.");
                                }
                            }
                        });

            } else {
                System.out.println("Không có ký tự '@' trong chuỗi.");
            }
        }


        recyclerView = findViewById(R.id.rcv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<Drinks> options =
                new FirebaseRecyclerOptions.Builder<Drinks>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("drinks"), Drinks.class)
                        .build();

        mainAdapter = new MainAdapter(options);
        mainAdapter.startListening();
        recyclerView.setAdapter(mainAdapter);
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Danh sách đồ uống");
        toolbar.setTitleCentered(true);
        bottom_navigation();
        search = findViewById(R.id.edt_search_name);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                processsearch(s.toString());
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                processsearch(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
                processsearch(s.toString());
            }
        });

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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.searchmenu, menu);
//        MenuItem item = menu.findItem(R.id.search);
//        SearchView searchView = (SearchView)item.getActionView();
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                processsearch(s);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                processsearch(s);
//                return false;
//            }
//        });
//        return super.onCreateOptionsMenu(menu);
//    }



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