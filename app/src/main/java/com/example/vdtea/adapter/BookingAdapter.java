package com.example.vdtea.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vdtea.R;
import com.example.vdtea.model.Booking;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class BookingAdapter extends FirebaseRecyclerAdapter<Booking,BookingAdapter.bookingViewHolder> {

    private Context context;

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
