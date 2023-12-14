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
import com.example.vdtea.event.SugarCheckboxListenner;
import com.example.vdtea.model.Sugar;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import static android.content.ContentValues.TAG;
public class SugarAdapter extends FirebaseRecyclerAdapter<Sugar, SugarAdapter.sugarViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */


    SugarCheckboxListenner sugarCheckboxListenner;
    public SugarAdapter(@NonNull FirebaseRecyclerOptions<Sugar> options, SugarCheckboxListenner listenner) {
        super(options);
        this.sugarCheckboxListenner = listenner;

    }

    @Override
    protected void onBindViewHolder(@NonNull SugarAdapter.sugarViewHolder holder, int position, @NonNull Sugar sugar) {
        holder.checkboxName.setText(sugar.getLevel());
        holder.checkboxPrice.setText(String.valueOf(sugar.getTopping_price())+"đ");
        holder.checkBox.setChecked(position == 0);
        if (holder.checkBox.isChecked()){
            sugarCheckboxListenner.onSugarCheckbox(sugar.getLevel());
        }
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checkBox.isChecked()){
                    if(sugarCheckboxListenner != null){
                        sugarCheckboxListenner.onSugarCheckbox(sugar.getLevel());
                    }
                }
                else{
                    Log.d(TAG, "Người dùng đã hủy chọn sugarcheckbox");
                }
            }
        });


    }

    @NonNull
    @Override
    public SugarAdapter.sugarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkbox_items,parent,false);
        return new SugarAdapter.sugarViewHolder(view);
    }

    class sugarViewHolder extends RecyclerView.ViewHolder{
        TextView checkboxName, checkboxPrice;
        CheckBox checkBox;
        public sugarViewHolder(@NonNull View itemView) {
            super(itemView);
            checkboxName = (TextView) itemView.findViewById(R.id.checkbox_text);
            checkboxPrice = (TextView) itemView.findViewById(R.id.checkbox_price);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
        }
    }
}
