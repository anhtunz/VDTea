package com.example.vdtea.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.vdtea.R;

public class DeliveryActivity extends AppCompatActivity {

    LottieAnimationView lottie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
        lottie = findViewById(R.id.delivery_splash);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(DeliveryActivity.this,"Đơn hàng vận chuyển thành công!",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DeliveryActivity.this,BookingActivity.class);
                startActivity(intent);
                finish();
            }
        }, 6000);
    }
}