package com.example.vdtea.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.vdtea.R;

public class RatingActivity extends AppCompatActivity {

    EditText reviewText;

    RatingBar ratingBar;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        reviewText = findViewById(R.id.rating_edtReview);
        ratingBar = findViewById(R.id.ratingbarStar);
        submit = findViewById(R.id.rating_btnSubmit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float test = ratingBar.getRating();
                Toast.makeText(RatingActivity.this,String.valueOf(test),Toast.LENGTH_SHORT).show();
            }
        });
    }
}