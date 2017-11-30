package h_da.fbi.khami.photostream;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import java.io.File;

import hochschuledarmstadt.photostream_tools.IPhotoStreamClient;
import hochschuledarmstadt.photostream_tools.PhotoStreamActivity;
import hochschuledarmstadt.photostream_tools.model.Photo;

public class PhotoDetailActivity extends PhotoStreamActivity {

    private Photo photo;
    private ImageView photoImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);


        photoImageView = findViewById(R.id.photo_in_detail_imageView);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Das Photo aus dem Intent referenzieren
        photo = getIntent().getParcelableExtra("photoObject");

        // Pfad zum Photo
        final File imageFile = photo.getImageFile();


        Uri imageUri = Uri.fromFile(imageFile);
        if (imageUri != null) {
            try {
                Bitmap getPhotoFromURI = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                setPhoto(getPhotoFromURI); // Handle single image being sent
            } catch (Exception e) {
                // FIXME: Give user feedback that the picture could not be loaded
            }
        }
    }

    public void setPhoto(Bitmap newPhoto)
    {
        photoImageView.setImageBitmap(newPhoto);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_photo_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            case R.id.delete_photo_item:
                // FIXME: Delete photo.
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPhotoStreamServiceConnected(IPhotoStreamClient photoStreamClient, Bundle savedInstanceState) {

    }

    @Override
    protected void onPhotoStreamServiceDisconnected(IPhotoStreamClient photoStreamClient) {

    }
}
