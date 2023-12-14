package com.example.vdtea.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import static android.content.ContentValues.TAG;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vdtea.R;
import com.example.vdtea.event.ItemClickListener;
import com.example.vdtea.model.Size;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class SizeAdapter extends FirebaseRecyclerAdapter<Size, SizeAdapter.sizeViewHolder> {

    long price = 5;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    ItemClickListener itemClickListener;
    public SizeAdapter(@NonNull FirebaseRecyclerOptions<Size> options,ItemClickListener listener) {
        super(options);
        this.itemClickListener = listener;
    }

    @Override
    protected void onBindViewHolder(@NonNull sizeViewHolder holder, int position, @NonNull Size size) {
        holder.checkboxName.setText(size.getSize_name());
        holder.checkboxPrice.setText(String.valueOf(size.getSize_price())+",000đ");
        holder.checkBox.setChecked(position == 0);
        if(holder.checkBox.isChecked()){
            itemClickListener.onClickItem(price, size.getSize_name());
        }
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.checkBox.isChecked()){
                    price = size.getSize_price();
                    if (itemClickListener != null) {
                        itemClickListener.onClickItem(price, size.getSize_name());
                    }
                }
                else{
                    Log.d(TAG, "Người dùng đã hủy chnọn checkbox ");
                }
            }
        });

    }

    @NonNull
    @Override
    public sizeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  view = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkbox_items, parent, false);
        return new sizeViewHolder(view);
    }

    class sizeViewHolder extends RecyclerView.ViewHolder{

        TextView checkboxName, checkboxPrice;

        CheckBox checkBox;
        public sizeViewHolder(View view){
            super(view);
            checkboxName = (TextView) view.findViewById(R.id.checkbox_text);
            checkboxPrice = (TextView) view.findViewById(R.id.checkbox_price);
            checkBox = (CheckBox) view.findViewById(R.id.checkbox);
        }
    }

}
