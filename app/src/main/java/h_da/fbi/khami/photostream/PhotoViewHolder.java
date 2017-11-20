package h_da.fbi.khami.photostream;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class PhotoViewHolder extends RecyclerView.ViewHolder {

    public final ImageView streamPhoto;
    public final TextView user;
    public final ImageView star;


    public PhotoViewHolder(View itemView) {
        super(itemView);

        streamPhoto = (ImageView) itemView.findViewById(R.id.photo_in_stream_imageView);
        user = (TextView) itemView.findViewById(R.id.username_textView);
        star = (ImageView) itemView.findViewById(R.id.star_unchecked_imageView);
    }
}
