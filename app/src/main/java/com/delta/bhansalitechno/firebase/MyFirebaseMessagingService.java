package com.delta.bhansalitechno.firebase;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;


import com.delta.bhansalitechno.R;
import com.delta.bhansalitechno.activity.DashboardActivityNew;
import com.delta.bhansalitechno.utils.PrefManager;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMessagingServ";

    NotificationManager notifManager;
    //private PrefManager prefManager;

    //int NotiCount = 0;

    String name = "my_package_channel_iShop";
    String id = "my_package_channel_id_iShop";
    String description = "my_package_channel_desc_iShop";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "onMessageReceived From : " + remoteMessage.getFrom());
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            showNotification(remoteMessage.getData());
        }
    }

    //Showing Notification
    @SuppressLint("LongLogTag")
    private void showNotification(Map<String, String> data) {
        try {
            //prefManager = new PrefManager(getApplicationContext());
            Log.d(TAG, "showNotification: " + data);
            if (data != null) {
                try {
                    Intent intent = null;
                    if (Objects.requireNonNull(data.get("Types")).equalsIgnoreCase("Test")) {
                        intent = new Intent(getApplicationContext(), DashboardActivityNew.class);
                    } else {
                        intent = new Intent(getApplicationContext(), DashboardActivityNew.class);
                    }
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), generateRandom(), intent, 0);

                    Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                    //For Oreo
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        try {
                            if (notifManager == null) {
                                notifManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                            }
                            if (notifManager != null) {
                                int importance = NotificationManager.IMPORTANCE_HIGH;
                                NotificationChannel notificationChannel = notifManager.getNotificationChannel(id);
                                if (notificationChannel == null) {
                                    notificationChannel = new NotificationChannel(id, name, importance);
                                    notificationChannel.setDescription(description);
                                    notificationChannel.enableVibration(true);
                                    notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                                    notifManager.createNotificationChannel(notificationChannel);
                                }

                                String contentText = "";
                                String photo = "";

                                if (data.containsKey("message")) {
                                    contentText = data.get("message");
                                }

                                if (data.containsKey("photo")) {
                                    photo = data.get("photo");
                                    Log.d("tag", photo);
                                }

                                Bitmap bitmap = null;

                                if (!photo.equalsIgnoreCase("")) {
                                    bitmap = getBitmapFromURL(photo);
                                    bitmap = getCircularBitmap(bitmap);
                                } else {
                                    //bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_bhansali_techno_logo_1);
                                    bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                                    bitmap = getCircularBitmap(bitmap);
                                }

                                NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
                                bigTextStyle.bigText(contentText);

                                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
                                        //.setSmallIcon(R.drawable.ic_bhansali_techno_logo_1)
                                        .setSmallIcon(R.mipmap.ic_launcher)
                                        .setLargeIcon(bitmap)
                                        .setContentTitle(data.get("Title").replace("%20", " "))
                                        .setContentText(data.get("message"))
                                        .setStyle(bigTextStyle)
                                        .setDefaults(Notification.DEFAULT_ALL)
                                        .setAutoCancel(true)
                                        .setSound(defaultSoundUri)
                                        .setChannelId(id)
                                        .setContentIntent(pendingIntent);

                                /*NotificationCompat.WearableExtender extender =
                                        new NotificationCompat.WearableExtender();
                                Bitmap bg = BitmapFactory.decodeResource(context.getResources(),
                                        R.drawable.background);
                                extender.setBackground(bg);
                                builder.extend(extender);*/

                                notifManager.notify(generateRandom(), builder.build());
                            }
                        } catch (Exception e) {
                            Log.d(TAG, "Exception is " + e.getMessage());
                            FirebaseCrashlytics.getInstance().recordException(e);
                        }

                    }

                    //For Other Version
                    else {
                        try {

                            if (notifManager == null) {
                                notifManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                            }

                            if (notifManager != null) {

                                String contentText = data.get("message");

                                String photo = "";

                                if (data.containsKey("photo")) {
                                    photo = data.get("photo");
                                }

                                Bitmap bitmap = null;

                                if (!photo.equalsIgnoreCase("")) {
                                    bitmap = getBitmapFromURL(photo);
                                    bitmap = getCircularBitmap(bitmap);
                                } else {
                                    //bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_bhansali_techno_logo_1);
                                    bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                                    bitmap = getCircularBitmap(bitmap);
                                }

                                NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
                                bigTextStyle.bigText(contentText);

                                NotificationCompat.Builder notificationBuilderForfees = new NotificationCompat.Builder(getApplicationContext())
                                        //.setSmallIcon(R.drawable.ic_bhansali_techno_logo_1)
                                        .setSmallIcon(R.mipmap.ic_launcher)
                                        .setLargeIcon(bitmap)
                                        .setContentTitle(data.get("Title").replace("%20", " "))
                                        .setStyle(bigTextStyle)
                                        .setSound(defaultSoundUri)
                                        .setContentText(data.get("message"))
                                        .setAutoCancel(true)
                                        .setChannelId(id)
                                        .setDefaults(Notification.DEFAULT_ALL)
                                        .setContentIntent(pendingIntent);

                                notifManager.notify(generateRandom(), notificationBuilderForfees.build());
                            }

                        } catch (Exception e) {
                            Log.d(TAG, "Exception is " + e.getMessage());
                            FirebaseCrashlytics.getInstance().recordException(e);
                        }

                    }

                    /*if (!prefManager.getNotiFicationCount().equals("")) {
                        NotiCount = Integer.parseInt(prefManager.getNotiFicationCount());
                    }
                    NotiCount++;
                    prefManager.setNotiFicationCount(NotiCount);*/
                } catch (Exception e) {
                    Log.d(TAG, "showNotification: " + e.getLocalizedMessage());
                    FirebaseCrashlytics.getInstance().recordException(e);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.d(TAG, "showNotification: " + e.getLocalizedMessage());
        }
    }

    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
            return null;
        }
    }

    //Generate Random Number To Set Channel Id
    public int generateRandom() {
        return new Random().nextInt(9999 - 1000) + 1000;
    }

    protected Bitmap getCircularBitmap(Bitmap srcBitmap) {

        int squareBitmapWidth = Math.min(srcBitmap.getWidth(), srcBitmap.getHeight());

        // Initialize a new instance of Bitmap
        Bitmap dstBitmap = Bitmap.createBitmap(
                squareBitmapWidth, // Width
                squareBitmapWidth, // Height
                Bitmap.Config.ARGB_8888 // Config
        );

        Canvas canvas = new Canvas(dstBitmap);

        // Initialize a new Paint instance
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        Rect rect = new Rect(0, 0, squareBitmapWidth, squareBitmapWidth);

        RectF rectF = new RectF(rect);

        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        // Calculate the left and top of copied bitmap
        float left = (squareBitmapWidth - srcBitmap.getWidth()) / 2;
        float top = (squareBitmapWidth - srcBitmap.getHeight()) / 2;

        /*
            public void drawBitmap (Bitmap bitmap, float left, float top, Paint paint)
                Draw the specified bitmap, with its top/left corner at (x,y), using the specified
                paint, transformed by the current matrix.

                Note: if the paint contains a maskfilter that generates a mask which extends beyond
                the bitmap's original width/height (e.g. BlurMaskFilter), then the bitmap will be
                drawn as if it were in a Shader with CLAMP mode. Thus the color outside of the

                original width/height will be the edge color replicated.

                If the bitmap and canvas have different densities, this function will take care of
                automatically scaling the bitmap to draw at the same density as the canvas.

            Parameters
                bitmap : The bitmap to be drawn
                left : The position of the left side of the bitmap being drawn
                top : The position of the top side of the bitmap being drawn
                paint : The paint used to draw the bitmap (may be null)
        */
        // Make a rounded image by copying at the exact center position of source image
        canvas.drawBitmap(srcBitmap, left, top, paint);

        // Free the native object associated with this bitmap.
        srcBitmap.recycle();

        // Return the circular bitmap
        return dstBitmap;
    }
}
