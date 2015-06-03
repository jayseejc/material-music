package com.jayseeofficial.materialmusic;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.graphics.Palette;
import android.util.TypedValue;
import android.view.WindowManager;
import android.widget.RemoteViews;

import com.jayseeofficial.materialmusic.activity.LibraryViewActivity;
import com.jayseeofficial.materialmusic.activity.NotificationActivity;
import com.jayseeofficial.materialmusic.domain.Album;
import com.jayseeofficial.materialmusic.domain.Song;
import com.jayseeofficial.materialmusic.event.PlaybackEvent;
import com.jayseeofficial.materialmusic.event.PlaybackFinishedEvent;

import de.greenrobot.event.EventBus;

/**
 * Created by jon on 02/06/15.
 */
public class NotificationManager {

    private static final int NOTIFICATION_ID = 89745;

    private static NotificationManager instance;

    public static NotificationManager getInstance(Context context) {
        if (instance == null) instance = new NotificationManager(context);
        return instance;
    }

    public static NotificationManager getInstance() {
        if (instance == null)
            throw new IllegalStateException("NotificationManager not initialized!");
        return instance;
    }

    public static void init(Context context) {
        instance = new NotificationManager(context);
    }

    private Context context;
    private android.app.NotificationManager notificationManager;
    private NotificationCompat.Action prevAction, toggleAction, nextAction;

    private NotificationManager(Context context) {
        this.context = context.getApplicationContext();
        notificationManager =
                (android.app.NotificationManager) this.context.getSystemService(Context.NOTIFICATION_SERVICE);
        prevAction = new NotificationCompat.Action.Builder(R.drawable.ic_skip_previous_black_36dp, "Previous", null)
                .build();
        nextAction = new NotificationCompat.Action.Builder(R.drawable.ic_skip_next_black_36dp, "Next", null)
                .build();
        EventBus.getDefault().register(this);
    }

    private void showNotification() {
        Song currentSong = SongPlayer.getInstance(context).getCurrentSong();
        if (currentSong != null) {
            if (SongPlayer.getInstance(context).isPlaying())
                toggleAction = new NotificationCompat.Action.Builder(R.drawable.ic_pause_black_48dp, "Pause", null).build();
            else
                toggleAction = new NotificationCompat.Action.Builder(R.drawable.ic_play_arrow_black_48dp, "Play", null).build();

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addNextIntent(new Intent(context, LibraryViewActivity.class));
            PendingIntent mainIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            RemoteViews remoteViewsBig = new RemoteViews(context.getPackageName(), R.layout.notification_main_big);
            RemoteViews remoteViewsSmall = new RemoteViews(context.getPackageName(), R.layout.notification_main);

            Album album = SongManager.getInstance(context).getAlbum(currentSong);

            remoteViewsBig.setTextViewText(R.id.txt_title, currentSong.getTitle());
            remoteViewsSmall.setTextViewText(R.id.txt_title, currentSong.getTitle());
            remoteViewsBig.setTextViewText(R.id.txt_artist, currentSong.getArtist());
            remoteViewsSmall.setTextViewText(R.id.txt_artist, currentSong.getArtist());

            // Set up buttons
            Intent intent = new Intent(context, NotificationActivity.class);

            intent.putExtra(NotificationActivity.ACTION_KEY, NotificationActivity.Action.PREV);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            remoteViewsBig.setOnClickPendingIntent(R.id.btn_prev, pendingIntent);
            remoteViewsSmall.setOnClickPendingIntent(R.id.btn_prev, pendingIntent);

            intent.putExtra(NotificationActivity.ACTION_KEY, NotificationActivity.Action.TOGGLE);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            remoteViewsBig.setOnClickPendingIntent(R.id.btn_play, pendingIntent);
            remoteViewsSmall.setOnClickPendingIntent(R.id.btn_play, pendingIntent);

            intent.putExtra(NotificationActivity.ACTION_KEY, NotificationActivity.Action.NEXT);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            remoteViewsBig.setOnClickPendingIntent(R.id.btn_next, pendingIntent);
            remoteViewsSmall.setOnClickPendingIntent(R.id.btn_next, pendingIntent);

            // Calculate the maximum dimentions of the notification.
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Point point = new Point();
            wm.getDefaultDisplay().getSize(point);
            int width = point.x + 1;
            Resources res = context.getResources();
            int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 256, res.getDisplayMetrics()) + 1;

            if (album != null) {
                remoteViewsBig.setTextViewText(R.id.txt_album, album.getTitle());
                String path = album.getAlbumArtPath();
                if (path != null) {
                    Bitmap bmp = BitmapFactory.decodeFile(path);
                    remoteViewsBig.setImageViewBitmap(R.id.img_album_art, bmp);
                    remoteViewsSmall.setImageViewBitmap(R.id.img_album_art, bmp);

                    Palette palette = Palette.from(bmp).generate();
                    Bitmap backgrounds = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                    backgrounds.eraseColor(palette.getVibrantColor(res.getColor(R.color.primary)));

                    remoteViewsBig.setImageViewBitmap(R.id.img_background, backgrounds);
                    remoteViewsSmall.setImageViewBitmap(R.id.img_background, backgrounds);
                } else {
                    remoteViewsBig.setImageViewResource(R.id.img_album_art, R.drawable.ic_default_artwork);
                    remoteViewsSmall.setImageViewResource(R.id.img_album_art, R.drawable.ic_default_artwork);

                    Bitmap background = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                    background.eraseColor(res.getColor(R.color.primary));

                    remoteViewsBig.setImageViewBitmap(R.id.img_background, background);
                    remoteViewsSmall.setImageViewBitmap(R.id.img_album_art, background);
                }
            }

            Notification notification = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_pause_black_48dp)
                    .setContentIntent(mainIntent)
                    .setOngoing(SongPlayer.getInstance(context).isPlaying())
                    .setCategory(NotificationCompat.CATEGORY_SERVICE)
                    .setContentTitle(currentSong.getTitle())
                    .setContentText(currentSong.getArtist())
                    .setContent(remoteViewsSmall)
                    .setSubText(SongManager.getInstance().getAlbum(currentSong).getTitle())
                    .addAction(prevAction)
                    .addAction(toggleAction)
                    .addAction(nextAction)
                    .build();
            notification.bigContentView = remoteViewsBig;

            notificationManager.notify(NOTIFICATION_ID, notification);
        }
    }

    private void hideNotification() {
        notificationManager.cancel(NOTIFICATION_ID);
    }

    private void updateNotification() {
    }

    public void onEventBackgroundThread(PlaybackEvent event) {
        showNotification();
        if (event.getEventType().equals(PlaybackFinishedEvent.EVENT_TYPE)) {
            if (((PlaybackFinishedEvent) event).getReason() == PlaybackFinishedEvent.Reason.END_OF_TRACK) {
                hideNotification();
            }
        }
    }

}
