package h_da.fbi.khami.photostream;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import hochschuledarmstadt.photostream_tools.adapter.BaseCommentAdapter;
import hochschuledarmstadt.photostream_tools.model.Comment;

public class CommentAdapter extends BaseCommentAdapter<CommentAdapter.CommentViewHolder> {

    public CommentAdapter() { }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ImageView deleteImageView;

        public CommentViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.comment_textView);
            deleteImageView = itemView.findViewById(R.id.comment_delete_imageView);
        }
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.comment_item, parent, false);
        CommentViewHolder viewHolder = new CommentViewHolder(v);
        return viewHolder;
    }

    /*
        Wird intern von der RecyclerView aufgerufen, um den Inhalt einer View,
        mit Informationen aus dem Element an der "position", zu aktualisieren
     */
    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        Comment comment = getItemAtPosition(position);
        holder.textView.setText(comment.getMessage());

        if(comment.isDeleteable())
        {
            holder.deleteImageView.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.deleteImageView.setVisibility(View.INVISIBLE);
        }
    }

}
