package com.example.vdtea.fragment;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.vdtea.activity.MainActivity;
import com.example.vdtea.R;
import com.example.vdtea.adapter.CommentAdapter;
import com.example.vdtea.adapter.IceApdapter;
import com.example.vdtea.adapter.SizeAdapter;
import com.example.vdtea.adapter.SugarAdapter;
import com.example.vdtea.event.IceCheckboxListenner;
import com.example.vdtea.event.ItemClickListener;
import com.example.vdtea.event.SugarCheckboxListenner;
import com.example.vdtea.model.Cart;
import com.example.vdtea.model.Comment;
import com.example.vdtea.model.Ice;
import com.example.vdtea.model.Size;
import com.example.vdtea.model.Sugar;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

public class DrinksDetailFragment extends Fragment implements ItemClickListener, SugarCheckboxListenner, IceCheckboxListenner {
    int count = 1, count_new;
    RecyclerView sizeRecycleView,sugarRecycleView, iceRecycleView, commentRecycleView;
    ImageView drinksDetail_Image;
    TextView drinksDetail_name, drinksDetails_description, drinksDetail_sold, drinksDetail_rating, drinksDetail_OPrice, drinksDetail_SPrice;
    TextView drinksDetail_addCart, totalPrice;
    private DatabaseReference mDatabase;
    private String name,desciption,image,data;
    long price, sale, sold_count,drinksprice, drinkscountprice;
    double rating;
    String drinksExtras_sugar= "100% Đường",drinksExtras_size= "M",drinksExtras_ice = "100% Đá" ;
    private long price_sendata = 5,toppingprice, totalprice;
    SizeAdapter sizeAdapter;
    IceApdapter iceApdapter;
    SugarAdapter sugarAdapter;
    CommentAdapter commentAdapter;
    public DrinksDetailFragment() {
        super(R.layout.fragment_drinks_detail);
    }

    FirebaseAuth mauth = FirebaseAuth.getInstance();
    FirebaseUser user = mauth.getCurrentUser();
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user = mauth.getCurrentUser();
        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Chi tiết đồ uống");
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
        });
        init();
        Bundle bundle = getArguments();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        if (bundle != null) {
            data = bundle.getString("DrinksID");
            Log.d(TAG, "Key in Fragment" + data);
            mDatabase.child("drinks").child(data).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Log.d(TAG, "onDataChange: Co ton tai");
                        name = snapshot.child("drinks_name").getValue(String.class);
                        desciption = snapshot.child("description").getValue(String.class);
                        image = snapshot.child("drinks_image").getValue(String.class);
                        price = snapshot.child("price").getValue(Long.class);
                        rating = snapshot.child("rating").getValue(Double.class);
                        sale = snapshot.child("sale").getValue(Long.class);
                        sold_count = snapshot.child("sold_count").getValue(Long.class);
                    Glide.with(drinksDetail_Image.getContext())
                            .load(image)
                            .placeholder(com.google.firebase.database.R.drawable.notification_bg_normal)
                            .centerCrop()
                            .error(com.google.firebase.database.R.drawable.notification_bg_normal)
                            .into(drinksDetail_Image);
                    drinksDetail_name.setText(name);
                    drinksDetails_description.setText(desciption);
                    drinksDetail_sold.setText(String.valueOf(sold_count)+" đã bán");
                    drinksDetail_rating.setText("Đánh giá: "+String.valueOf(rating)+"/5");
                    drinksDetail_OPrice.setText(String.valueOf(price)+",000đ");
                    if(sale == 1){
                        drinksDetail_SPrice.setText("");
                    }
                    else{
                        drinksDetail_OPrice.setPaintFlags(drinksDetail_OPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        drinksDetail_SPrice.setText(String.valueOf(price -(price*sale/100))+",000đ");
                    }
                    } else {
                        Log.d(TAG, "onDataChange: Khong tim thay " + data);
                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
            drinksDetail_addCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final DialogPlus dialogPlus = DialogPlus.newDialog(drinksDetail_Image.getContext())
                            .setContentHolder(new ViewHolder(R.layout.fragment_order))
                            .setExpanded(true,2000).create();

                    dialogPlus.show();
                    View view1 =dialogPlus.getHolderView();
                    sizeRecycleView = view1.findViewById(R.id.orderSize_recycleview);
                    sugarRecycleView = view1.findViewById(R.id.orderSugar_recycleview);
                    iceRecycleView = view1.findViewById(R.id.orderIce_recycleview);
                    TextView drinks_name = view1.findViewById(R.id.drinksName);
                    TextView drinks_sold = view1.findViewById(R.id.drinkssold);
                    TextView drinks_rating = view1.findViewById(R.id.drinksrating);
                    TextView drinks_OPrice = view1.findViewById(R.id.drinksOriginalPrice);
                    TextView drinks_SPrice = view1.findViewById(R.id.drinksSalePrice);

                    if(sale == 1){
                        drinksprice = price;
                        Log.d(TAG, "drinksprice: " + drinksprice);
                    }
                    else{
                        drinksprice = price - (price*sale/100);
                        Log.d(TAG, "drinkspriceSale: " + drinksprice);
                    }

                    ImageView drinks_image = view1.findViewById(R.id.drinksImage);
                    TextView drinks_substract = view1.findViewById(R.id.btn_subtract_drinks);
                    TextView drinks_add = view1.findViewById(R.id.btn_add_drinks);
                    TextView drinks_count = view1.findViewById(R.id.drinks_count);
                    TextView clearDialog = view1.findViewById(R.id.order_closeDialog);
                    LinearLayout addtoCart = view1.findViewById(R.id.order_AddToCart);
                    totalPrice = view1.findViewById(R.id.orderAddtoCart_price);
                    TextView dash = view1.findViewById(R.id.orderAddtoCart_dash);
                    TextView lable_addtoCart = view1.findViewById(R.id.orderAddtoCart_lable);
                    Drawable drawable = ContextCompat.getDrawable(getContext(),R.drawable.clear_icon);
                    clearDialog.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);
                    FirebaseDatabase.getInstance().getReference("cart").child(user.getUid())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot cartsnapshot : snapshot.getChildren()){
                                        Cart cart = cartsnapshot.getValue(Cart.class);
                                        Log.d(TAG, "DRinks_id " + cart.getDrinks_id());
                                        String cartid_drinksId = cart.getDrinks_id();
                                        if (cartid_drinksId != null && cartid_drinksId.equals(data)){
                                            // Set click cho cart ở đây
                                            addtoCart.setClickable(false);
                                            addtoCart.setBackgroundColor(getResources().getColor(R.color.gray, getContext().getTheme()));
                                            totalPrice.setVisibility(View.GONE);
                                            dash.setVisibility(View.GONE);
                                            lable_addtoCart.setText("Sản phẩm đã có trong giỏ hàng");
                                            break;
                                        }
                                        else{
                                            Log.d(TAG, "Không tìm thấy sản phẩm trong cart");
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Log.d(TAG, "Câu lệnh so sánh bị lỗi");
                                }
                            });
                    drinks_name.setText(name);
                    drinks_sold.setText("Đã bán: "+String.valueOf(sold_count));
                    drinks_rating.setText("Đánh giá: "+String.valueOf(rating)+"/5");
                    drinks_OPrice.setText(String.valueOf(price)+",000đ");
                    setToTalCount(drinksprice,price_sendata);
                    Glide.with(drinks_image.getContext())
                            .load(image)
                            .placeholder(com.google.firebase.database.R.drawable.notification_bg_normal)
                            .circleCrop()
                            .error(com.google.firebase.database.R.drawable.notification_bg_normal)
                            .into(drinks_image);
                    if(sale == 1){
                        drinks_SPrice.setText("");
                    }
                    else{
                        drinks_OPrice.setPaintFlags(drinks_OPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        drinks_SPrice.setText(String.valueOf(price - (price*sale/100))+",000đ");
                    }

                    drinks_count.setText(String.valueOf(count));
                    sizeRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
                    sugarRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
                    iceRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
                    FirebaseRecyclerOptions<Size> Sizeoptions =
                            new FirebaseRecyclerOptions.Builder<Size>()
                                    .setQuery(FirebaseDatabase.getInstance().getReference().child("drinks_extras").child("size"), Size.class)
                                    .build();
                    FirebaseRecyclerOptions<Ice> Iceoptions =
                            new FirebaseRecyclerOptions.Builder<Ice>()
                                    .setQuery(FirebaseDatabase.getInstance().getReference().child("drinks_extras").child("topping"), Ice.class)
                                    .build();
                    FirebaseRecyclerOptions<Sugar> Sugaroptions =
                            new FirebaseRecyclerOptions.Builder<Sugar>()
                                    .setQuery(FirebaseDatabase.getInstance().getReference().child("drinks_extras").child("topping"), Sugar.class)
                                    .build();
                    sizeAdapter = new SizeAdapter(Sizeoptions, DrinksDetailFragment.this::onClickItem);
                    sizeAdapter.startListening();
                    sizeRecycleView.setAdapter(sizeAdapter);

                    iceApdapter = new IceApdapter(Iceoptions, DrinksDetailFragment.this::onIceCheckbox);
                    iceApdapter.startListening();
                    iceRecycleView.setAdapter(iceApdapter);

                    sugarAdapter = new SugarAdapter(Sugaroptions, DrinksDetailFragment.this::onSugarCheckbox);
                    sugarAdapter.startListening();
                    sugarRecycleView.setAdapter(sugarAdapter);

                    clearDialog.setOnClickListener(v -> dialogPlus.dismiss());
                    drinks_add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            count += 1;
                            drinks_count.setText(String.valueOf(count));
                            drinkscountprice = drinksprice * count;
                            toppingprice = price_sendata * count;
                            Log.d(TAG, "pricesenddatadfromadd: " + toppingprice);
                            Log.d(TAG, "adddrinksprice:  " + drinkscountprice);
                            setToTalCount(drinkscountprice,toppingprice);
                        }
                    });
                    drinks_substract.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(count == 1){
                                return;
                            }
                            count_new = count - 1;
                            count--;
                            drinks_count.setText(String.valueOf(count_new));
                            drinkscountprice -= drinksprice;
                            toppingprice -= price_sendata;
                            setToTalCount(drinkscountprice, toppingprice);
                        }
                    });
                    addtoCart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            totalPrice.setText(String.valueOf(totalprice) + ",000đ");
                            add_to_Cart();
                            dialogPlus.dismiss();
                        }
                    });
                }
            });

            commentRecycleView = view.findViewById(R.id.DrinksDetail_CommentRCV);
            commentRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
            FirebaseRecyclerOptions<Comment> options =
                    new FirebaseRecyclerOptions.Builder<Comment>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("comment").child(data), Comment.class)
                            .build();
            commentAdapter = new CommentAdapter(options);
            commentAdapter.startListening();
            commentRecycleView.setAdapter(commentAdapter);
        } else {
            Log.d(TAG, "Chua nhan duoc data");
        }

    }

    public void init() {
        View view = getView();
        try {
                drinksDetail_Image = getView().findViewById(R.id.DrinksDetail_image);
                drinksDetail_name = getView().findViewById(R.id.DrinksDetail_name);
                drinksDetails_description = getView().findViewById(R.id.DrinksDetail_description);
                drinksDetail_sold = getView().findViewById(R.id.DrinksDetail_sold);
                drinksDetail_rating = getView().findViewById(R.id.DrinksDetail_rating);
                drinksDetail_OPrice = getView().findViewById(R.id.drinksOriginalPrice);
                drinksDetail_SPrice = getView().findViewById(R.id.drinksSalePrice);
                drinksDetail_addCart = getView().findViewById(R.id.DrinksDetail_AddToCart);
                drinksDetail_name.setText("");
                drinksDetails_description.setText("");
                drinksDetail_sold.setText("");
                drinksDetail_rating.setText("");
                drinksDetail_OPrice.setText("");
                drinksDetail_SPrice.setText("");

        } catch (Exception e) {
                Log.e(TAG, "init: " + e.getMessage());
        }
    }

    public void add_to_Cart(){
        Map<String,Object> cartmap =new HashMap<>();
        cartmap.put("drinks_ice",drinksExtras_ice);
        cartmap.put("drinks_id",data);
        cartmap.put("drinks_size", drinksExtras_size);
        cartmap.put("drinks_sugar", drinksExtras_sugar);
        cartmap.put("drinks_totalprice",totalprice);
        cartmap.put("quantity", count);

        FirebaseDatabase.getInstance().getReference().child("cart").child(user.getUid()).push()
                .setValue(cartmap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getContext(), "Thêm vào giỏ hàng thành công!", Toast.LENGTH_SHORT).show();;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Thêm vào giỏ hàng thất bại!", Toast.LENGTH_SHORT).show();;
                    }
                });


    }

    public void setToTalCount(long drinks_price, long sizeprice){
        totalprice = drinks_price + sizeprice;
        Log.d(TAG, "setToTalCount: "+ totalprice);
        totalPrice.setText(String.valueOf(totalprice) + ",000đ");
    }

    @Override
    public void onSugarCheckbox(String sugarName) {
        drinksExtras_sugar = sugarName;
        Log.d(TAG, "SugarCheckboxName: "+ drinksExtras_sugar);
    }

    @Override
    public void onClickItem(long price, String name) {
        drinksExtras_size = name;
        Log.d(TAG, "Giá được gửi đến fragment: "+ price);
        price_sendata = price * count;
        Log.d(TAG, "pricesendata cap nhat: "+ price_sendata);
        setToTalCount(drinksprice,price_sendata);
    }

    @Override
    public void onIceCheckbox(String Ice_level) {
        drinksExtras_ice = Ice_level;
        Log.d(TAG, "IceCheckboxName: "+ drinksExtras_ice);
    }
}
