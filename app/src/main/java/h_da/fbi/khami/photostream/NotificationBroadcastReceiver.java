package h_da.fbi.khami.photostream;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import hochschuledarmstadt.photostream_tools.IPhotoStreamClient;
import hochschuledarmstadt.photostream_tools.model.Photo;

public class NotificationBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (isNewPhotoIntent(intent)){
            // Das empfangene Photo ist im Intent enthalten
            Photo receivedPhoto = intent.getParcelableExtra(IPhotoStreamClient.INTENT_KEY_PHOTO);
            Toast.makeText(context, "Intent empfangen im BroadcastReceiver!", Toast.LENGTH_LONG).show();

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context,"channel")
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("New Photo Received !")
                            .setContentText("Explore Now!   ");

            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            // notificationID allows you to update the notification later on.
            mNotificationManager.notify(1, mBuilder.build());


        }
    }

    private boolean isNewPhotoIntent(Intent intent) {
        return intent.getAction().equals(IPhotoStreamClient.INTENT_ACTION_NEW_PHOTO_AVAILABLE);
    }
}