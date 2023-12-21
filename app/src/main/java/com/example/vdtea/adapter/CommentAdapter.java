package com.example.vdtea.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vdtea.R;
import com.example.vdtea.model.Comment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class CommentAdapter extends FirebaseRecyclerAdapter<Comment,CommentAdapter.commentViewHolder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public CommentAdapter(@NonNull FirebaseRecyclerOptions<Comment> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull commentViewHolder holder, int position, @NonNull Comment comment) {
        holder.username.setText(comment.getUsername());
        holder.content.setText(comment.getContent());
        holder.time.setText(comment.getTime());
        Glide.with(holder.avatar.getContext())
                .load("lalalala")
                .placeholder(R.drawable.avatar)
                .circleCrop()
                .error(R.drawable.avatar)
                .into(holder.avatar);
    }

    @NonNull
    @Override
    public commentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment,parent,false);
        return new commentViewHolder(view);
    }

    public class commentViewHolder extends RecyclerView.ViewHolder{

        ImageView avatar;
        TextView username, content,time;
        public commentViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = (ImageView) itemView.findViewById(R.id.comment_avatar);
            username = (TextView) itemView.findViewById(R.id.comment_username);
            content = (TextView) itemView.findViewById(R.id.comment_content);
            time = (TextView) itemView.findViewById(R.id.comment_time);
        }
    }
}
