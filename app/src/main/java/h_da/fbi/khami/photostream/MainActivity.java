package h_da.fbi.khami.photostream;

import android.content.ComponentName;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;

import java.util.Iterator;
import java.util.List;

import hochschuledarmstadt.photostream_tools.IPhotoStreamClient;
import hochschuledarmstadt.photostream_tools.PhotoStreamActivity;
import hochschuledarmstadt.photostream_tools.RequestType;
import hochschuledarmstadt.photostream_tools.callback.OnPhotosReceivedListener;
import hochschuledarmstadt.photostream_tools.callback.OnRequestListener;
import hochschuledarmstadt.photostream_tools.model.HttpError;
import hochschuledarmstadt.photostream_tools.model.Photo;
import hochschuledarmstadt.photostream_tools.model.PhotoQueryResult;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class MainActivity extends PhotoStreamActivity implements OnPhotosReceivedListener, OnRequestListener {

    RecyclerView recyclerView;
    PhotoAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new PhotoAdapter();

        recyclerView = (RecyclerView) findViewById(R.id.stream_recyclerView);
         linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);

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
