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
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import hochschuledarmstadt.photostream_tools.BitmapUtils;
import hochschuledarmstadt.photostream_tools.IPhotoStreamClient;
import hochschuledarmstadt.photostream_tools.PhotoStreamActivity;
import hochschuledarmstadt.photostream_tools.RequestType;
import hochschuledarmstadt.photostream_tools.callback.OnPhotoUploadListener;
import hochschuledarmstadt.photostream_tools.model.HttpError;
import hochschuledarmstadt.photostream_tools.model.Photo;

public class UploadPhoto extends PhotoStreamActivity implements OnPhotoUploadListener {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView selectPhotoImageView;
    Bitmap selectedPhoto;
    EditText descriptionPhoto;
    MenuItem uploadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photo);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        selectPhotoImageView = (ImageView) findViewById(R.id.select_photo_imageView);
        descriptionPhoto = (EditText) findViewById(R.id.description_editText);
        uploadButton = (MenuItem) findViewById(R.id.upload_photo_item);

        selectPhotoImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });


        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                handleSingleImageIntent(intent, action, type);

            }
        }
    }

    public void handleSingleImageIntent(Intent intent, String action, String type)
    {
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            try {
                Bitmap getPhotoFromURI = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                setPhoto(getPhotoFromURI); // Handle single image being sent
            }
            catch(Exception e)
            {
                // FIXME: Give user feedback that the picture could not be loaded
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_photo_upload, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            case R.id.upload_photo_item:
                uploadSelectedPhoto();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void uploadSelectedPhoto()
    {
        IPhotoStreamClient photoStreamClient = getPhotoStreamClient();
        try {
            photoStreamClient.uploadPhoto(BitmapUtils.bitmapToBytes(selectedPhoto), descriptionPhoto.toString());
        }
        catch(Exception e)
        {
            // FIXME: Output an error to the user
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            setPhoto((Bitmap) extras.get("data"));
        }
    }

    public void setPhoto(Bitmap newPhoto)
    {
        selectedPhoto = newPhoto;
        selectPhotoImageView.setImageBitmap(selectedPhoto);
    }

    @Override
    protected void onPhotoStreamServiceConnected(IPhotoStreamClient photoStreamClient, Bundle savedInstanceState) {
        photoStreamClient.addOnPhotoUploadListener(this);
        if (savedInstanceState != null){
            boolean uploadRequestIsRunning = photoStreamClient.hasOpenRequestOfType(RequestType.UPLOAD_PHOTO);
            uploadButton.setEnabled(!uploadRequestIsRunning);
        }
    }

    @Override
    protected void onPhotoStreamServiceDisconnected(IPhotoStreamClient photoStreamClient) {
        photoStreamClient.removeOnPhotoUploadListener(this);
    }

    @Override
    public void onPhotoUploaded(Photo photo) {
        uploadButton.setEnabled(true);
        // FIXME: When photo detail page is implemented, the users should get redirected there
        startActivity(new Intent(UploadPhoto.this, MainActivity.class));
    }

    @Override
    public void onPhotoUploadFailed(HttpError httpError) {
        // FIXME: Output error somehwere
        uploadButton.setEnabled(true);
    }

}
