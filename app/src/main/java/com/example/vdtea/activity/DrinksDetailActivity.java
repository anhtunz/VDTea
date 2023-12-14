package com.example.vdtea.activity;

import static android.content.ContentValues.TAG;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.vdtea.R;
import com.example.vdtea.adapter.SizeAdapter;
import com.example.vdtea.databinding.ActivityDrinksDetailBinding;
import com.example.vdtea.databinding.ActivityMainBinding;
import com.example.vdtea.fragment.DrinksDetailFragment;

public class DrinksDetailActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    private ActivityDrinksDetailBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityDrinksDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        NavController navController = Navigation.findNavController(this, R.id.DrinksDetail_Fragment);
//        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        Intent intent = getIntent();
        String key = intent.getStringExtra("KEY_DATA");
        if (savedInstanceState == null) {
            Bundle bundle = new Bundle();
            bundle.putString("DrinksID",key);
            Log.d(TAG, "Bundle in activity: "+ bundle);
            DrinksDetailFragment fragment = new DrinksDetailFragment();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.DrinksDetail_Fragment, DrinksDetailFragment.class,bundle)
                    .commit();
        }


//        String receivedData = getIntent().getStringExtra("KEY_DATA");
//        Log.d(TAG, "KEY_DATA: "+ receivedData);


    }


}