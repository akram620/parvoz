package rebus.llc.parvoz;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;
import android.os.Build;

import rebus.llc.parvoz.models.ObjavlenijaModel;

public class Parvoz extends Application {

    // This is the Notification Channel ID. More about this in the next section
    public static final String NOTIFICATION_CHANNEL_ID = "channel_id";
    //User visible Channel Name
    public static final String CHANNEL_NAME = "Notification Channel";
    // Importance applicable to all the notifications in this Channel
    int importance = NotificationManager.IMPORTANCE_DEFAULT;


    @Override
    public void onCreate() {

        super.onCreate();
    }

    public void createNotification(ObjavlenijaModel model){
        //Notification channel should only be created for devices running Android 26
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Boolean value to set if lights are enabled for Notifications from this Channel
                NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, model.getZagolovok(), importance);
            //notificationChannel.setSound(defaultSoundUri,att);
            notificationChannel.setDescription(model.getOpisanie());
            notificationChannel.enableLights(true);
            //Boolean value to set if vibration are enabled for Notifications from this Channel
            notificationChannel.enableVibration(true);
            //Sets the color of Notification Light
            notificationChannel.setLightColor(Color.GREEN);

            //Set the vibration pattern for notifications. Pattern is in milliseconds with the format {delay,play,sleep,play,sleep...}
            notificationChannel.setVibrationPattern(new long[] {
                    500,
                    500,
                    500,
                    500,
                    500
            });
            //Sets whether notifications from these Channel should be visible on Lockscreen or not
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}
