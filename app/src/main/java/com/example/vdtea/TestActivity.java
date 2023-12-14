package com.example.vdtea;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class TestActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<ModelClass>arrayList = new ArrayList<>();
    String[] data = new String[]{"100%","50%","30%"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_test);
//        recyclerView = findViewById(R.id.testrecycleview);
//        IceAdapter iceAdapter = new IceAdapter(this, getData());
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
//        recyclerView.setAdapter(iceAdapter);
    }
    private ArrayList<ModelClass> getData(){
        for (int i = 0; i < data.length; i++){
            arrayList.add(new ModelClass(data[i], false));
            Log.d(TAG, "iceData" + data[i]);
        }
        return arrayList;
    }
}