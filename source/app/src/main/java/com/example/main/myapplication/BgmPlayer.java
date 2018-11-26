package com.example.main.myapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class BgmPlayer extends Service {

    public Player player;
    private final IBinder iBinder = new serviceLocalBinder();

    public class serviceLocalBinder extends Binder {
        BgmPlayer getService(){
            return BgmPlayer.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if(player == null) {
            player = new Player();
        }

        player.play("/storage/emulated/0/Music/136Hz_20061122_349.mp3");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("userTag" , "onStartCommand");
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    @Override
    public void onDestroy() {
        Log.i("userTag" , "BgmPlayer Service is Destoryed");
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }
}
