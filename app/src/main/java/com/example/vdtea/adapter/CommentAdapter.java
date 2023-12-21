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
                .load("https://scontent.fhan20-1.fna.fbcdn.net/v/t39.30808-6/324259259_565764695569197_5052157878188652179_n.jpg?_nc_cat=110&ccb=1-7&_nc_sid=efb6e6&_nc_eui2=AeF03-2u94VrZi4IUeB7DdVUnBsSXSmDX7KcGxJdKYNfskYBEOIh7s3NjGwn5iu6bMMxfy6Xfkie5DMaJQVfd58R&_nc_ohc=s8-lSjcVuz0AX966YZg&_nc_ht=scontent.fhan20-1.fna&oh=00_AfCr9BWsgYG55XEEzXj-7-mdY97Y6CbnA0ITQT1fEQCD1w&oe=658A4E65")
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
