package com.example.main.myapplication;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

public final class Player{

    private MediaPlayer mediaPlayer = new MediaPlayer();
    private String playingFileName = "not playing";
    private Boolean isPausing = false;
    public final int PLUS_THIRTY_SECONDS = 30000;    //In terms of milliseconds
    public final int MINUS_THIRTY_SECONDS = -30000;  //In terms of milliseconds

    public void setOnCompletionListener(MediaPlayer.OnCompletionListener listener){
        mediaPlayer.setOnCompletionListener(listener);
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

        playingFileName = separateFileName( soundFilePath );
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

    public String getPlayingFileName(){ return playingFileName; }

    public String getPlayingFileLength(){
        int playingFileLength = mediaPlayer.getDuration();
        if(playingFileLength <= 0) playingFileLength = 0;
        return toStringTime(playingFileLength);
    }

    public String getCurrentPositionByTime(){
        int currentPositionByMilliSeconds = mediaPlayer.getCurrentPosition();
        if( currentPositionByMilliSeconds <= 0 ) currentPositionByMilliSeconds = 0;
        return toStringTime(currentPositionByMilliSeconds);
    }

    private String separateFileName( String filePath ){
        int lastIndex =  filePath.lastIndexOf("/");
        if( lastIndex <= 0 ) return "Don't playing";

        //Slash is unnecessary so I add 1 to index.
        return filePath.substring( lastIndex + 1);
    }

    //Convert to time string from milliseconds.
    private String toStringTime(int numberOfMilliSeconds){

        int numberOfSeconds = numberOfMilliSeconds /1000;

        int hours = 0;
        if(numberOfSeconds > 3600) hours = numberOfSeconds / 3600;

        int minutes = 0;
        if(numberOfSeconds > 60){
            minutes = numberOfSeconds % 3600;
            minutes = minutes / 60;
        }

        int seconds = numberOfSeconds % 60;

        final String zeroPadding = "0";

        String strHours = String.valueOf(hours);
        if(strHours.length() == 1) strHours = zeroPadding + strHours;
        strHours += ":";

        String strMinutes = String.valueOf(minutes);
        if(strMinutes.length() == 1) strMinutes =  zeroPadding + strMinutes;
        strMinutes += ":";

        String strSeconds = String.valueOf(seconds);
        if(strSeconds.length() == 1) strSeconds = zeroPadding + strSeconds;

        return strHours + strMinutes + strSeconds;
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
