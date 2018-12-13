package com.example.main.myapplication;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

public final class Player{

    private MediaPlayer mediaPlayer = new MediaPlayer();
    private String playingFileName = "not playing";
    private String playingFilePath = "";
    public final int PLUS_THIRTY_SECONDS = 30000;    //In terms of milliseconds
    public final int MINUS_THIRTY_SECONDS = -30000;  //In terms of milliseconds

    public void setOnCompletionListener(MediaPlayer.OnCompletionListener listener){
        mediaPlayer.setOnCompletionListener(listener);
    }

    //Example.
    //player.play("/storage/emulated/0/Music/fileName.mp3")
    public void setSoundFile(String soundFilePath){

        //A media player don't reuse.
        // Every time I call a method , Create a new player instance by stop().
        stopAndNewPlayer();

        try{
            mediaPlayer.setDataSource(soundFilePath);
        } catch (IOException | IllegalStateException e){
            e.printStackTrace();
        }

        try {
            mediaPlayer.prepare();
        } catch (IOException  | IllegalStateException e){
            e.printStackTrace();
        }

        playingFilePath = soundFilePath;
        playingFileName = separateFileName( soundFilePath );
    }

    public void setSoundFile(String soundFilePath , int startPosition){
        this.setSoundFile( soundFilePath );
        mediaPlayer.seekTo( startPosition );
    }

    public void start(){
        mediaPlayer.start();
    }

    public void pause(){
        if(!mediaPlayer.isPlaying()){
            mediaPlayer.start();
            return;
        }

        mediaPlayer.pause();
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

    public void stopAndNewPlayer(){
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = new MediaPlayer();
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

    public String getPlayingFilePath(){
        if(playingFilePath != null) return playingFilePath;
        else return "";
    }

    public int getCurrentPosition(){
        if(mediaPlayer != null) return mediaPlayer.getCurrentPosition();
        return 0;
    }

    public int getDuration(){
        if(mediaPlayer != null) return mediaPlayer.getDuration();
        return 0;
    }

    public void seekTo( int position ){
        if(mediaPlayer != null) mediaPlayer.seekTo( position );
    }
}
