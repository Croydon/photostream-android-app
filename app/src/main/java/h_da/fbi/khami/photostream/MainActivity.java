package h_da.fbi.khami.photostream;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.List;

import hochschuledarmstadt.photostream_tools.IPhotoStreamClient;
import hochschuledarmstadt.photostream_tools.PhotoStreamActivity;
import hochschuledarmstadt.photostream_tools.RequestType;
import hochschuledarmstadt.photostream_tools.callback.OnPhotosReceivedListener;
import hochschuledarmstadt.photostream_tools.callback.OnRequestListener;
import hochschuledarmstadt.photostream_tools.model.HttpError;
import hochschuledarmstadt.photostream_tools.model.Photo;
import hochschuledarmstadt.photostream_tools.model.PhotoQueryResult;

public class MainActivity extends PhotoStreamActivity implements OnPhotosReceivedListener, OnRequestListener {

    RecyclerView recyclerView;
    PhotoAdapter adapter;
    LinearLayoutManager linearLayoutManager;

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

                if (newState ==  RecyclerView.SCROLL_STATE_SETTLING)
                {
                    if (!photoStreamClient.hasOpenRequestOfType(RequestType.LOAD_PHOTOS)) {
                        // dann nächste Seite aus dem Stream laden
                        photoStreamClient.loadMorePhotos();
                    }
                }

            }
        });

        recyclerView.setAdapter(adapter);

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
        Log.d("http error",httpError.getMessage()+"  "+ httpError.getResponseCode());
    }

    @Override
    // OnPhotosReceivedListener
    public void onNoNewPhotosAvailable() {

    }

    @Override
    // OnRequestListener
    public void onRequestStarted() {

    }

    @Override
    // OnRequestListener
    public void onRequestFinished() {

    }
}
