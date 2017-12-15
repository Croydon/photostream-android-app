package h_da.fbi.khami.photostream;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import hochschuledarmstadt.photostream_tools.IPhotoStreamClient;
import hochschuledarmstadt.photostream_tools.PhotoStreamActivity;

public class AddCommentActivity extends PhotoStreamActivity
{
    IPhotoStreamClient photoStreamClient;
    int photoID;
    EditText userInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);


        userInput = findViewById(R.id.comment_editText);


        photoID = getIntent().getExtras().getInt("PhotoID");

    }

    @Override
    protected void onPhotoStreamServiceConnected(IPhotoStreamClient photoStreamClient, Bundle savedInstanceState) {

    }

    @Override
    protected void onPhotoStreamServiceDisconnected(IPhotoStreamClient photoStreamClient) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_comment_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.post_comment_item:
                photoStreamClient = getPhotoStreamClient();
                photoStreamClient.uploadComment(photoID,userInput.getText().toString());
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
