package com.example.main.myapplication;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

public final class Player {

    private MediaPlayer mediaPlayer = new MediaPlayer();

    //Example.
    //player.play("/storage/emulated/0/Music/fileName.mp3")
    public void play(String soundFilePath){
        try{
            mediaPlayer.setDataSource(soundFilePath);
        } catch (IOException e){
            Log.e("userTag" , "Error");
            e.printStackTrace();
        }

        try {
            mediaPlayer.prepare();
        }
            catch (IOException e){
                Log.e("userTag" , "Error");
            e.printStackTrace();
        }

        mediaPlayer.start();

    }

    public void pause(){

    }

    public void stop(){

    }

    public void playNext(){

    }

    public void playPrev(){

    }
}
