package h_da.fbi.khami.photostream;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
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
                            .setContentTitle("New Photo Received!")
                            .setContentText("Explore Now!");


            // Creates an explicit intent for an Activity in your app
            Intent resultIntent = new Intent(context, MainActivity.class);

            // The stack builder object will contain an artificial back stack for the
            // started Activity.
            // This ensures that navigating backward from the Activity leads out of
            // your app to the Home screen.
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            // Adds the back stack for the Intent (but not the Intent itself)
            stackBuilder.addParentStack(MainActivity.class);
            // Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );

            mBuilder.setContentIntent(resultPendingIntent);


            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            // notificationID allows you to update the notification later on.
            mNotificationManager.notify(1, mBuilder.build());


        }
    }

    private boolean isNewPhotoIntent(Intent intent) {
        return intent.getAction().equals(IPhotoStreamClient.INTENT_ACTION_NEW_PHOTO_AVAILABLE);
    }
}
