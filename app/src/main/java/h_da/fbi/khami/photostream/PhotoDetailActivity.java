package h_da.fbi.khami.photostream;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import hochschuledarmstadt.photostream_tools.IPhotoStreamClient;
import hochschuledarmstadt.photostream_tools.PhotoStreamActivity;
import hochschuledarmstadt.photostream_tools.adapter.BaseCommentAdapter;
import hochschuledarmstadt.photostream_tools.callback.OnCommentsReceivedListener;
import hochschuledarmstadt.photostream_tools.callback.OnNewCommentReceivedListener;
import hochschuledarmstadt.photostream_tools.callback.OnPhotoDeletedListener;
import hochschuledarmstadt.photostream_tools.model.Comment;
import hochschuledarmstadt.photostream_tools.model.HttpError;
import hochschuledarmstadt.photostream_tools.model.Photo;

public class PhotoDetailActivity extends PhotoStreamActivity implements OnPhotoDeletedListener, OnCommentsReceivedListener, OnNewCommentReceivedListener
{

    private Photo photo;
    private ImageView photoImageView;
    private TextView photoDescriptionTextView;
    private ImageView photoFavstarImageView;
    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;
    private LinearLayoutManager linearLayoutManager;
    private FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);


        photoImageView = findViewById(R.id.photo_in_detail_imageView);
        photoDescriptionTextView = findViewById(R.id.photo_description_in_detail_textView);
        photoFavstarImageView = findViewById(R.id.photo_in_detail_favstar_imageView);

        fab = findViewById(R.id.add_comment_fab);
        fab.setOnClickListener((View view) ->
        {
            Intent intent = new Intent(this, AddCommentActivity.class);
            intent.putExtra("PhotoID",photo.getId());
            startActivity(intent);
        });

        recyclerView = (RecyclerView) findViewById(R.id.comment_recyclerView);
        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);

        commentAdapter = new CommentAdapter();


        commentAdapter.setOnItemClickListener(R.id.comment_delete_imageView, new BaseCommentAdapter.OnItemClickListener<CommentAdapter.CommentViewHolder>() {
            @Override
            public void onItemClicked(CommentAdapter.CommentViewHolder viewHolder, View v, Comment comment) {
                if(comment.isDeleteable())
                {
                    // FIXME: Do something.
                }
            }
        });

        // Der RecyclerView abschließend den Adapter als Datenquelle zuweisen
        recyclerView.setAdapter(commentAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        photo = getIntent().getParcelableExtra("photoObject");
        photoDescriptionTextView.setText(photo.getDescription());

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

        updateFavstar();

        photoFavstarImageView.setOnClickListener((View view) ->
        {
            IPhotoStreamClient photoStreamClient = getPhotoStreamClient();

            if (photo.isFavorite()) {
                photoStreamClient.unfavoritePhoto(photo.getId());
                photo.setFavorite(false);
                updateFavstar();
            } else {
                photoStreamClient.favoritePhoto(photo.getId());
                photo.setFavorite(true);
                updateFavstar();
            }
        });
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

        if(!(photo.isDeleteable())) {

            menu.findItem(R.id.delete_photo_item).setVisible(false);

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            case R.id.delete_photo_item:

                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(this);
                }
                builder.setTitle("Delete Photo")
                        .setMessage("Are you sure you want to delete this photo?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                deletePhoto();
                                finish();  // delete activity from stack
                                startActivity(new Intent(PhotoDetailActivity.this, MainActivity.class));
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .show();


                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deletePhoto() {
        IPhotoStreamClient photoStreamClient = getPhotoStreamClient();
        photoStreamClient.deletePhoto(photo.getId());
    }

    // OnPhotoDeletedListener
    @Override
    public void onPhotoDeleted(int photoId) {
    }

    @Override
    public void onPhotoDeleteFailed(int photoId, HttpError httpError) {

    }

    @Override
    protected void onPhotoStreamServiceConnected(IPhotoStreamClient photoStreamClient, Bundle savedInstanceState) {
        photoStreamClient.addOnCommentsReceivedListener(this);
        photoStreamClient.loadComments(photo.getId());
    }

    @Override
    protected void onPhotoStreamServiceDisconnected(IPhotoStreamClient photoStreamClient) {

    }


    @Override
    public void onCommentsReceived(int photoId, java.util.List<Comment> comments) {
        if (photo.getId() == photoId)
        {
            commentAdapter.set(comments);
        }
        // holder.delete.setVisibli(

                   /*  if(photo.isDeletable()) {
            holder.delete.setImageResource(R.drawable.ic_star_white_24dp);
        } */
    }



   /* @Override
    public void onNewCommentReceived(Comment comment) {
        if (comment.getPhotoId() == photo.getId()) {
            commentAdapter.add(comment);

            // Wenn der Kommentar von diesem Gerät stammt
            if (comment.isDeleteable()) {
                // dann die Eingabe leeren
                editText.setText("");
                // und ans Ende der Liste springen
                recyclerView.scrollToPosition(adapter.getItemCount() - 1);
            }
        }
    } */

    @Override
    public void onReceiveCommentsFailed(int photoId, HttpError httpError) {
        // FIXME: Maybe display an error.
    }

    @Override
    public void onNewCommentReceived(Comment comment) {
        IPhotoStreamClient photoStreamClient = getPhotoStreamClient();
        photoStreamClient.loadComments(photo.getId());
    }

    public void updateFavstar()
    {
        if (photo.isFavorite()) {
            photoFavstarImageView.setImageResource(R.drawable.ic_star_white_24dp);
        } else {
            photoFavstarImageView.setImageResource(R.drawable.ic_star_border_white_24dp);
        }
    }
}
