package com.example.vdtea.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vdtea.R;
import com.example.vdtea.activity.RatingActivity;
import com.example.vdtea.model.Booking;
import com.example.vdtea.model.Comment;
import com.example.vdtea.model.Drinks;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import static android.content.ContentValues.TAG;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BookingAdapter extends FirebaseRecyclerAdapter<Booking,BookingAdapter.bookingViewHolder> {

    private Context context;
    long count;

    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public BookingAdapter(@NonNull FirebaseRecyclerOptions<Booking> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull bookingViewHolder holder, int position, @NonNull Booking booking) {
        holder.name.setText(booking.getUser_name());
        holder.phone_number.setText(booking.getPhone_numer());
        holder.address.setText(booking.getDelivery_address());
        MenuAdapter menuAdapter = new MenuAdapter(booking.getMenu());
        holder.menu.setAdapter(menuAdapter);
        holder.time_delivery.setText(booking.getOrder_date());
        holder.amount.setText(String.valueOf(booking.getTotal_amount()) + ",000đ");
        holder.banking_method.setText(booking.getPayment());
        if(booking.getStatus() == 0){
            holder.btn_reviews.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogPlus dialogPlus = DialogPlus.newDialog(context)
                            .setContentHolder(new ViewHolder(R.layout.activity_rating))
                            .create();
                    dialogPlus.show();
//                    UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
//                            .setDisplayName("TunzTunzz")
//                            .build();
//                    user.updateProfile(profileUpdate)
//                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful()) {
//                                        Log.d(TAG, "Update user thanh cong.");
//                                    }
//                                }
//                            });
                    View ratingView = dialogPlus.getHolderView();
                    EditText reviewText = ratingView.findViewById(R.id.rating_edtReview);
                    RatingBar ratingBar = ratingView.findViewById(R.id.ratingbarStar);
                    Button btnsubmit = ratingView.findViewById(R.id.rating_btnSubmit);
                    btnsubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            float test = ratingBar.getRating();
                            Toast.makeText(context,String.valueOf(test),Toast.LENGTH_SHORT).show();
                            String review = String.valueOf(reviewText.getText());
                            List<Booking.Menu> menuList = booking.getMenu();
                            for (Booking.Menu menu : menuList){
                                Log.d(TAG, "onClick: " + menu.getDrinks_id());
                                FirebaseDatabase.getInstance().getReference().child("drinks").child(menu.getDrinks_id())
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                Drinks drinks = snapshot.getValue(Drinks.class);
                                                Log.d(TAG, "Name: "+ drinks.getDrinks_name());
                                                Log.d(TAG, "Rating: "+ drinks.getRating());
                                                count = drinks.getSold_count() +1;
                                                double new_rating = Math.round(test);
                                                double danggia = ((drinks.getSold_count() * drinks.getRating()) + new_rating) / (drinks.getSold_count() +1);
                                                String formattedValue = String.format(Locale.US, "%.2f", danggia);
                                                double final_rating = Double.parseDouble(formattedValue);
                                                Log.d(TAG, "Sold: " + count);
                                                Log.d(TAG, "Rating original: "+ final_rating);
                                                Log.d(TAG, "Rating_last : " + drinks.getDrinks_name() + ": " + formattedValue);
                                                Log.d(TAG, "Username: " + user.getDisplayName());
                                                Log.d(TAG, "Binh luan: " + review);
                                                updateCount(menu.getDrinks_id(), count);
                                                updateRating(menu.getDrinks_id(),final_rating);
                                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                                                String currentDateAndTime = sdf.format(new Date());
                                                Comment comment = new Comment(user.getDisplayName(),review,currentDateAndTime);
                                                FirebaseDatabase.getInstance().getReference().child("comment")
                                                        .child(menu.getDrinks_id())
                                                        .push()
                                                        .setValue(comment)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                Log.d(TAG, "Da them binh luan vao db ");
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.d(TAG, "Lai loi roiiii");
                                                            }
                                                        });

                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                            }

                            FirebaseDatabase.getInstance().getReference()
                                    .child("booking")
                                    .child(user.getUid())
                                    .child(getRef(position).getKey())
                                    .child("status")
                                    .setValue(1)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d(TAG, "Value đã được thay đổi");
                                        }
                                    });
                            dialogPlus.dismiss();
                        }
                    });
                }
            });
        }else{
            holder.btn_reviews.setVisibility(View.GONE);
        }
    }



    @NonNull
    @Override
    public bookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_history,parent,false);
        return new bookingViewHolder(view);
    }

    public void updateCount(String id_drinks,long count){
        FirebaseDatabase.getInstance().getReference()
                .child("drinks")
                .child(id_drinks)
                .child("sold_count")
                .setValue(count).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "Đã sửa drinkscount ");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Lỗi cái qq gì rồi!" );
                    }
                });
    }
    public void updateRating(String id_drinks,double rating){
        FirebaseDatabase.getInstance().getReference()
                .child("drinks")
                .child(id_drinks)
                .child("rating")
                .setValue(rating).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "Đã sửa drinksrating ");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Lỗi cái qq gì rồi!" );
                    }
                });
    }

//    public void updateStatus(String userID,double rating){
//        FirebaseDatabase.getInstance().getReference()
//                .child("booking")
//                .child(userID)
//                .child("rating")
//                .setValue(rating).addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        Log.d(TAG, "Đã sửa drinksrating ");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.e(TAG, "Lỗi cái qq gì rồi!" );
//                    }
//                });
//    }

    class bookingViewHolder extends RecyclerView.ViewHolder{

        TextView name, phone_number, address, time_delivery, amount,banking_method;
        RecyclerView menu;
        Button btn_reviews;

        public bookingViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.history_txtname);
            phone_number = (TextView) itemView.findViewById(R.id.history_txtphone);
            address = (TextView) itemView.findViewById(R.id.history_txtaddress);
            menu  = (RecyclerView) itemView.findViewById(R.id.history_listmenu);
            menu.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            time_delivery = (TextView) itemView.findViewById(R.id.history_txtorderdate);
            amount = (TextView) itemView.findViewById(R.id.history_txtamount);
            btn_reviews  = (Button) itemView.findViewById(R.id.history_btnstatus);
            banking_method = (TextView) itemView.findViewById(R.id.history_txtbanking);
        }
    }
}
