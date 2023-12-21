package com.example.vdtea.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vdtea.R;
import com.example.vdtea.adapter.BankAdapter;
import com.example.vdtea.adapter.CartAdapter;
import com.example.vdtea.event.ApiService;
import com.example.vdtea.event.ApiServiceManager;
import com.example.vdtea.event.CartTouchButtonListener;
import com.example.vdtea.model.Bank;
import com.example.vdtea.model.Booking;
import com.example.vdtea.model.Cart;
import com.example.vdtea.model.Drinks;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import static android.content.ContentValues.TAG;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity implements CartTouchButtonListener {

    RecyclerView cart_rcv;
    CartAdapter cartAdapter;
    TextView totalCart_OPrice, totalCart_SPrice,btn_CartDelivery;
    long totalOPrice, totalSPrice;
    int dialogheight = 2200;
    AutoCompleteTextView paymentMethod,bankChoose;
    ArrayAdapter<String> paymentChoose;
    List<Booking.Menu> drinksIds = new ArrayList<>();
    private ApiService apiService;
    BankAdapter bankAdapter;
    boolean choosen = false;
    String[] choose = {"Thanh toán khi nhận hàng","Chuyển khoản qua ngân hàng"};
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        user = mAuth.getCurrentUser();
        apiService = ApiServiceManager.getApiService();
        cart_rcv = findViewById(R.id.cart_rcv);
        cart_rcv.setLayoutManager(new LinearLayoutManager(this));
        totalCart_OPrice = findViewById(R.id.cart_TotalOPrice);
        totalCart_SPrice = findViewById(R.id.cart_totalSPrice);
        btn_CartDelivery = findViewById(R.id.cart_btnDelivery);
        FirebaseRecyclerOptions<Cart> options =
            new FirebaseRecyclerOptions.Builder<Cart>()
                    .setQuery(FirebaseDatabase.getInstance().getReference().child("cart").child(user.getUid()), Cart.class)
                    .build();
        cartAdapter = new CartAdapter(options, this::onCartListener);
        cartAdapter.startListening();
        cart_rcv.setAdapter(cartAdapter);
        Log.d(TAG, "TotalOPricemainAdapter: " + totalCart_OPrice);
        Log.d(TAG, "TotalSPricemainAdapter: " + totalCart_SPrice);

        btn_CartDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(CartActivity.this)
                        .setContentHolder(new ViewHolder(R.layout.confirm_delivery))
                        .create();
                dialogPlus.show();
                View dialogView = dialogPlus.getHolderView();
                TextView totalPrice = dialogView.findViewById(R.id.delivery_title_price);
                totalPrice.setText(String.valueOf(totalSPrice)+ ",000đ");
                EditText edt_Name = dialogView.findViewById(R.id.edt_name);
                EditText edt_Phone = dialogView.findViewById(R.id.edt_phone);
                EditText edt_Address = dialogView.findViewById(R.id.edt_address);
                EditText edt_bankNumber = dialogView.findViewById(R.id.delivery_BankNumber);
                TextView btn_cancel = dialogView.findViewById(R.id.delivery_cancel);
                TextView btn_confirmDelivery = dialogView.findViewById(R.id.delivery_confirm);
                LinearLayout bankLinear = dialogView.findViewById(R.id.delivery_BankLinear);
                bankLinear.setVisibility(View.GONE);
                bankChoose = dialogView.findViewById(R.id.delivery_bankName);
                paymentMethod = dialogView.findViewById(R.id.delivery_bankSelect);
                paymentChoose = new ArrayAdapter<String>(CartActivity.this, R.layout.method_item,choose);
                paymentMethod.setAdapter(paymentChoose);
                paymentMethod.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.d(TAG, "onItemClick: " + parent.getItemAtPosition(position));
                        if ( parent.getItemAtPosition(position) == "Chuyển khoản qua ngân hàng"){
                        bankLinear.setVisibility(View.VISIBLE);
                        }
                        else{
                            bankLinear.setVisibility(View.GONE);
                        }
                    }
                });

                Call<Bank> call = apiService.getBank();
                call.enqueue(new Callback<Bank>() {
                    @Override
                    public void onResponse(Call<Bank> call, Response<Bank> response) {
                        if (response.isSuccessful()) {
                            Bank bank = response.body();
                            List<Bank.Data> dataList = bank.getData();
                            bankAdapter = new BankAdapter(CartActivity.this, R.layout.dropdown_item,R.id.delivery_bankName,dataList);
                            bankChoose.setAdapter(bankAdapter);
                            bankChoose.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (choosen == true){
                                        bankChoose.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                String selectedBankName = "";
                                                Bank.Data selectedBank = dataList.get(position);
                                                selectedBankName = selectedBank.getBankName();
                                                Log.d(TAG, "Selected Bank Name: " + selectedBankName);
                                                bankChoose.setText(selectedBankName);
                                                choosen = false;
                                            }
                                        });
                                    }
                                    else{
                                        choosen = true;
                                        bankChoose.setText("");
                                    }

                                }
                            });
                        } else {
                            Log.e(TAG, "API call failed with code: " + response.code());
                        }
                    }
                    @Override
                    public void onFailure(Call<Bank> call, Throwable t) {
                        Log.e(TAG, "API call failed: " + t.getMessage());
                    }
                });

                getListDrinks();
                btn_cancel.setOnClickListener(v1 -> dialogPlus.dismiss());
                btn_confirmDelivery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = String.valueOf(edt_Name.getText());
                        String phone_number = String.valueOf(edt_Phone.getText());
                        String address = String.valueOf(edt_Address.getText());
                        String bankNumber = String.valueOf(edt_bankNumber.getText());
                        String bankchoose = String.valueOf(paymentMethod.getText());
                        String price = String.valueOf(totalSPrice);
                        if (TextUtils.isEmpty(name)|| TextUtils.isEmpty(phone_number) || TextUtils.isEmpty(address) || TextUtils.isEmpty(bankchoose)){
                            Toast.makeText(getApplicationContext(), "Hãy điền đầy đủ thông tin!",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else if(TextUtils.isEmpty(bankNumber) && bankchoose == "Chuyển khoản qua ngân hàng"){
                            Log.e(TAG, "daasdasdas: Da goi ham nayu");
                            Toast.makeText(getApplicationContext(), "Vui lòng chọn ngân hàng muốn chuyển khoản!",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        CompletableFuture<Void> getListDrinksFuture = CompletableFuture.runAsync(() -> {
                            getListDrinks();
                        });
                        getListDrinksFuture.join();
                            Log.e(TAG, "onClick: "+bankchoose );
                        Log.d(TAG, "onClick: " + address);
                        Log.d(TAG, "onClick: "+ phone_number);
                        Log.d(TAG, "name: " + name);
                        Log.d(TAG, "onClick: "+ user.getUid());
                        Log.d(TAG, "tieenf: "+ price);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                        String currentDateAndTime = sdf.format(new Date());
                        Log.d(TAG, "onDataChange: " + currentDateAndTime);
                        Log.d(TAG, "onDataChange2: "+ drinksIds.size());
                        Booking booking = new Booking(address,drinksIds,currentDateAndTime,phone_number,0,Long.parseLong(price),name,bankchoose);
                        FirebaseDatabase.getInstance().getReference().child("booking")
                                .child(user.getUid())
                                .push()
                                .setValue(booking)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getApplicationContext(),"Đơn hàng đang trên được giao đến bạn", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(),"Đơn hàng bị lỗi. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
            }
        });
    }
    public void getListDrinks(){
        FirebaseDatabase.getInstance().getReference().child("cart")
                .child(user.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot cartSnapshot : snapshot.getChildren()) {
                            Cart cart = cartSnapshot.getValue(Cart.class);
                            String drinksID = cart.getDrinks_id();
                            FirebaseDatabase.getInstance().getReference().child("drinks")
                                    .child(cart.getDrinks_id()).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            Drinks drinks = snapshot.getValue(Drinks.class);

                                            String drinksFull = drinks.getDrinks_name() + " - " + cart.getDrinks_size() + " - SL: " + cart.getQuantity();
                                            Booking.Menu menu = new Booking.Menu(drinksFull,drinksID);
                                            Log.d(TAG, "abcdef: "+ menu.getMenu_detail());
                                            Log.d(TAG, "anbcdef: " + menu.getDrinks_id());
                                            drinksIds.add(menu);
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
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

    public String callRestfulApi(){
        HttpURLConnection urlConnection = null;
        String stResponse = "";
        try {
            URL url = new URL("https://api.vietqr.io/v2/banks");
            urlConnection = (HttpURLConnection) url.openConnection();

            int code = urlConnection.getResponseCode();
            if (code !=  200) {
                throw new IOException("Invalid response from server: " + code);
            }

            BufferedReader rd = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                stResponse+=line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return stResponse;
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
//        cartAdapter.notifyDataSetChanged();
    }
}