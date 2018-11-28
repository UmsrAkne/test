package com.example.main.myapplication;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

public final class Player {

    private MediaPlayer mediaPlayer = new MediaPlayer();
    private Boolean isPausing = false;
    public final int PLUS_THIRTY_SECONDS = 30000;    //In terms of milliseconds
    public final int MINUS_THIRTY_SECONDS = -30000;  //In terms of milliseconds

    //Example.
    //player.play("/storage/emulated/0/Music/fileName.mp3")
    public void play(String soundFilePath){

        mediaPlayer.reset();

        //TODO: Please correct error handling.
        try{
            mediaPlayer.setDataSource(soundFilePath);
        } catch (IOException e){
            Log.e("userTag" , "throw error when mediaplayer.setDataSource");
            e.printStackTrace();
        }

        try {
            mediaPlayer.prepare();
        } catch (IOException e){
            Log.e("userTag" , "throw error when mediaplayer.setDataSource");
            e.printStackTrace();
        }

        mediaPlayer.start();
    }

    public void pause(){
        if(isPausing){
            mediaPlayer.start();
            isPausing = false;
            return;
        }

        mediaPlayer.pause();
        isPausing = true;
    }

    public String getCurrentPositionByTime(){
        int currentPositionByMilliSeconds = mediaPlayer.getCurrentPosition();
        int crPos = currentPositionByMilliSeconds / 1000;

        int hours = 0;
        if(crPos > 3600) hours = crPos / 3600;

        int minutes = 0;
        if(crPos > 60){
            minutes = crPos % 3600;
            minutes = minutes / 60;
        }

        int seconds = crPos % 60;

        final String zeroPadding = "0";

        String strHours = String.valueOf(hours);
        if(strHours.length() == 1) strHours = zeroPadding + strHours;

        String strMinutes = String.valueOf(minutes);
        if(strMinutes.length() == 1) strMinutes =  zeroPadding + strMinutes;

        String strSeconds = String.valueOf(seconds);
        if(strSeconds.length() == 1) strSeconds = zeroPadding + strSeconds;

        String playTime = strHours;
        playTime += strMinutes;
        playTime += strSeconds;
        return playTime;
    }

    public int getCurrentPositionBySeconds(){
        int currentPositionByMilliSeconds = mediaPlayer.getCurrentPosition();
        return currentPositionByMilliSeconds / 1000;
    }

    public void stop(){
        mediaPlayer.stop();
    }

    public void playNext(){

    }

    public void playPrev(){

    }

    public void seekFromCurrentPosition(int seekTime){
        int currentPosition = mediaPlayer.getCurrentPosition();
        if(currentPosition > 0){
            mediaPlayer.seekTo( currentPosition + seekTime );
        }
    }
}
