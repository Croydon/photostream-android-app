package h_da.fbi.khami.photostream;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.List;

import hochschuledarmstadt.photostream_tools.IPhotoStreamClient;
import hochschuledarmstadt.photostream_tools.PhotoStreamActivity;
import hochschuledarmstadt.photostream_tools.RequestType;
import hochschuledarmstadt.photostream_tools.adapter.BasePhotoAdapter;
import hochschuledarmstadt.photostream_tools.callback.OnPhotosReceivedListener;
import hochschuledarmstadt.photostream_tools.callback.OnRequestListener;
import hochschuledarmstadt.photostream_tools.model.HttpError;
import hochschuledarmstadt.photostream_tools.model.Photo;
import hochschuledarmstadt.photostream_tools.model.PhotoQueryResult;

public class MainActivity extends PhotoStreamActivity implements OnPhotosReceivedListener, OnRequestListener {

    RecyclerView recyclerView;
    PhotoAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    FloatingActionButton fab;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        

        adapter = new PhotoAdapter();

        recyclerView = (RecyclerView) findViewById(R.id.stream_recyclerView);
        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                IPhotoStreamClient photoStreamClient = getPhotoStreamClient();

                if(linearLayoutManager.findLastVisibleItemPosition() == recyclerView.getAdapter().getItemCount()-1){
                    //findViewById(R.id.photo_loading_progressBar).setVisibility(View.VISIBLE);
                    if (!photoStreamClient.hasOpenRequestOfType(RequestType.LOAD_PHOTOS)) {
                        // load next page of photos
                        photoStreamClient.loadMorePhotos();
                    }
                }
            }

        });


        // OnItemClickListener für die ImageView mit der id "imageView" setzen.
        adapter.setOnItemClickListener(R.id.photo_in_stream_imageView, (BasePhotoAdapter.OnItemClickListener<PhotoViewHolder>) (viewHolder, v, photo) -> {
            // Wenn auf die ImageView ein Klick ausgelöst wurde, dann die FullscreenActivity starten,
            // um das Photo im Vollbild anzuzeigen
            Intent intent = new Intent(MainActivity.this, PhotoDetailActivity.class);
            intent.putExtra("photoObject", photo);
            startActivity(intent);
        });

        adapter.setOnItemClickListener(R.id.favorite_imageView, (BasePhotoAdapter.OnItemClickListener<PhotoViewHolder>) (viewHolder, v, photo) -> {
            // Wenn auf die ImageView ein Klick ausgelöst wurde, dann die FullscreenActivity starten,
            // um das Photo im Vollbild anzuzeigen
            Intent intent = new Intent(MainActivity.this, PhotoDetailActivity.class);
            intent.putExtra("photoObject", photo);
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);

        fab = findViewById(R.id.add_photo_fab);

        fab.setOnClickListener((View view) ->
        {
            startActivity(new Intent(MainActivity.this, UploadPhoto.class));
        });
    }


    @Override
    protected void onPhotoStreamServiceConnected(IPhotoStreamClient photoStreamClient, Bundle savedInstanceState) {

        photoStreamClient.addOnRequestListener(this, RequestType.LOAD_PHOTOS);
        photoStreamClient.addOnPhotosReceivedListener(this);

        if(savedInstanceState == null)
        {
            photoStreamClient.loadPhotos();
        }
    }


    @Override
    protected void onPhotoStreamServiceDisconnected(IPhotoStreamClient photoStreamClient) {
        photoStreamClient.removeOnRequestListener(this);
        photoStreamClient.removeOnPhotosReceivedListener(this);
    }


    @Override
    // OnPhotosReceivedListener
    public void onPhotosReceived(PhotoQueryResult result) {
        List<Photo> receivedPhotos = result.getPhotos();
        adapter.addAll(receivedPhotos);
    }


    @Override
    // OnPhotosReceivedListener
    public void onReceivePhotosFailed(HttpError httpError) {
        Log.d("http error",httpError.getResponseCode()+":  "+ httpError.getMessage());
    }


    @Override
    // OnPhotosReceivedListener
    public void onNoNewPhotosAvailable() {
        // KEEP ME
    }


    @Override
    // OnRequestListener
    public void onRequestStarted() {
        Log.d("in onrequeststarted", "true");
        findViewById(R.id.photo_loading_progressBar).setVisibility(View.VISIBLE);
    }


    @Override
    // OnRequestListener
    public void onRequestFinished() {
        findViewById(R.id.photo_loading_progressBar).setVisibility(View.GONE);
    }
}
