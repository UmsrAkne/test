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

    public Player(){
        Log.i("userTag", "new Player");
    }

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
