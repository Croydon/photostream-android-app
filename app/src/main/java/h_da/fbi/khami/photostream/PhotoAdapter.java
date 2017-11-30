package h_da.fbi.khami.photostream;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import hochschuledarmstadt.photostream_tools.adapter.BasePhotoAdapter;
import hochschuledarmstadt.photostream_tools.callback.OnPhotoDeletedListener;
import hochschuledarmstadt.photostream_tools.model.HttpError;
import hochschuledarmstadt.photostream_tools.model.Photo;

/**
 *
 */

public class PhotoAdapter extends BasePhotoAdapter<PhotoViewHolder> implements OnPhotoDeletedListener {
    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        // Ein neues Layout für ein Element erzeugen
        View itemView = layoutInflater.inflate(R.layout.card_item, parent, false);
        // ViewHolder erzeugen und dabei das erzeugte Layout übergeben
        return new PhotoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PhotoViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        // Photo an der angegebenen Position referenzieren
        final Photo photo = getItemAtPosition(position);
        // Bitmap anhand des Photo Objekts laden und in der ImageView setzen
        loadBitmapIntoImageViewAsync(holder, holder.streamPhoto, photo);
        holder.user.setText(photo.getDescription());
    }

    @Override
    // Bei Zeit und Lust implementieren bspw fuer animationen
    protected void onBitmapLoadedIntoImageView(ImageView imageView) {

    }

    @Override
    public void onPhotoDeleted(int photoId) {
      remove(photoId);

    }

    @Override
    public void onPhotoDeleteFailed(int photoId, HttpError httpError) {

    }
}
