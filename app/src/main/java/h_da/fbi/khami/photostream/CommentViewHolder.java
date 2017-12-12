package h_da.fbi.khami.photostream;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class CommentViewHolder extends RecyclerView.ViewHolder {

    public final TextView textComment;
    public final ImageView delete;

    public CommentViewHolder(View itemView) {
        super(itemView);

        textComment = itemView.findViewById(R.id.comment_textView);
        delete = itemView.findViewById(R.id.delete_imageView);
    }
}
