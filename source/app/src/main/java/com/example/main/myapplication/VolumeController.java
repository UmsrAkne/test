package com.example.main.myapplication;

import android.content.Context;
import android.media.AudioManager;
import android.view.Gravity;
import android.widget.Toast;

public final class VolumeController {

    private Context context;

    public VolumeController(Context context){
        this.context = context;
    }

    public void upDeviceVolume(){
        final int AMOUNT = 1;
        changeDeviceVolume( AMOUNT );
        showCurrentVolume();
    }

    public void downDeviceVolume(){
        final int AMOUNT = -1;
        changeDeviceVolume( AMOUNT );
        showCurrentVolume();
    }

    public void showCurrentVolume(){
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        String currentVolume = String.valueOf( am.getStreamVolume( AudioManager.STREAM_MUSIC ));

        Toast toast = Toast.makeText(
                context.getApplicationContext() , "volume " + currentVolume , Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER , 0 , 0);
        toast.show();
    }

    private void changeDeviceVolume( int volume ){
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        currentVolume += volume;
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,currentVolume,0);
    }
}
