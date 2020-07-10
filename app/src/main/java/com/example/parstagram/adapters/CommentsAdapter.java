package com.example.parstagram.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parstagram.R;
import com.example.parstagram.models.Comment;
import com.example.parstagram.models.Post;


import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {


    private Context context;
    private List<Comment> comments;

    public CommentsAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);


        return new CommentsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsAdapter.ViewHolder holder, int position) {

        Log.i("CommentsAdapter", "onBindViewHolder: " + comments.get(0).getMessage());


        Comment comment = comments.get(position);

        holder.bind(comment);

    }

    @Override
    public int getItemCount() {
        return comments.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvUsername;
        private TextView tvComment;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUsername = itemView.findViewById(R.id.tvCmtAuthor);
            tvComment = itemView.findViewById(R.id.tvCmtMsg);

        }


        public  void bind(Comment comment){

            tvComment.setText(comment.getMessage());
            tvUsername.setText("@" + comment.getUser().getUsername());
        }


    }
}
