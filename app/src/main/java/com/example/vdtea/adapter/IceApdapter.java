package com.example.vdtea.adapter;

import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vdtea.R;
import com.example.vdtea.event.IceCheckboxListenner;
import com.example.vdtea.model.Ice;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import static android.content.ContentValues.TAG;

public class IceApdapter extends FirebaseRecyclerAdapter<Ice, IceApdapter.iceViewHolder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    IceCheckboxListenner iceCheckboxListenner;
    public IceApdapter(@NonNull FirebaseRecyclerOptions<Ice> options, IceCheckboxListenner listenner) {

        super(options);
        this.iceCheckboxListenner = listenner;
    }

    @Override
    protected void onBindViewHolder(@NonNull iceViewHolder holder, int position, @NonNull Ice ice) {
        holder.checkboxName.setText(ice.getLevel());
        holder.checkboxPrice.setText(String.valueOf(ice.getTopping_price())+"đ");
        holder.checkBox.setChecked(position == 0);
        if (holder.checkBox.isChecked()){
            iceCheckboxListenner.onIceCheckbox(ice.getLevel());
        }
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checkBox.isChecked()){
                    if (iceCheckboxListenner != null){
                        iceCheckboxListenner.onIceCheckbox(ice.getLevel());
                    }
                }
            }
        });
    }

    @NonNull
    @Override
    public iceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkbox_items,parent,false);
        return new iceViewHolder(view);
    }

    class iceViewHolder extends RecyclerView.ViewHolder{
        TextView checkboxName, checkboxPrice;
        CheckBox checkBox;
        public iceViewHolder(@NonNull View itemView) {
            super(itemView);
            checkboxName = (TextView) itemView.findViewById(R.id.checkbox_text);
            checkboxPrice = (TextView) itemView.findViewById(R.id.checkbox_price);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
        }
    }
}
