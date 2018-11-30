package com.example.main.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;


public class MusicPlayActivity extends AppCompatActivity {

    private Player player;
    private ListView musicList;
    private File[] musicFiles;
    private int lastPlayedFilePosition;
    private Handler handler = new Handler();
    private Timer playTimeCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("userTag" , " called onCreate()");
        runTimePermissionRequest();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_play);

        ContentsLoader loader = new ContentsLoader(this);

        musicFiles = loader.getFilesFromDirectory(Environment.DIRECTORY_MUSIC);
        String[] fileNameList = new String[ musicFiles.length ];

        for(int i = 0; i < musicFiles.length; i++){
            fileNameList[i] = musicFiles[i].getName();
        }

        insertToList(fileNameList);
        setMusicListOnItemClickEvent();

        findViewById(R.id.minus30sButton).setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                player.seekFromCurrentPosition( player.MINUS_THIRTY_SECONDS );
            }
        });

        findViewById(R.id.plus30sButton).setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                player.seekFromCurrentPosition( player.PLUS_THIRTY_SECONDS );
            }
        });


        findViewById(R.id.volumeDownButton).setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                volumeControlButtonAction(false);
            }
        });

        findViewById(R.id.volumeUpButton).setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                volumeControlButtonAction(true);
            }
        });

        //if it don't write this, created multiple player instances.
        if(savedInstanceState == null) {
            player = new Player();
        }

        createTimer();
    }

    private void volumeControlButtonAction(boolean volumeMoveDirection ){
        int addition = 1;
        if(!volumeMoveDirection ) addition = -1;
        addToDeviceVolume(addition);
        String stringVolume = String.valueOf( getDeviceVolume() );
        Toast toast = Toast.makeText(getApplicationContext() , "volume" + stringVolume , Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER , 0 , 0);
        toast.show();
    }

    private int getDeviceVolume(){
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        return am.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    private void createTimer(){
        Timer timer = new Timer();
        int delay = 0;
        int period = 1000;
        timer.schedule( new PlayingTimeCounter() , delay , period );
    }

    class PlayingTimeCounter extends TimerTask {
        public void run(){
            handler.post(new Runnable() {
                public void run() {
                    TextView target = findViewById(R.id.playingStatuses);
                    target.setText( player.getCurrentPositionByTime() + "  /  ");
                    target.append( player.getPlayingFileLength());
                }
            });
        }
    }

    private void addToDeviceVolume(int value){
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int musicVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        musicVolume += value;
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,musicVolume,0);
    }

    private void setMusicListOnItemClickEvent(){
        musicList = findViewById(R.id.musicList);
        musicList.setOnItemClickListener( new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                String item = (String)musicList.getItemAtPosition(position);
                File selectedMusicFile = musicFiles[ position ];
                player.play( selectedMusicFile.getPath() );
                lastPlayedFilePosition = position;
            }
        });
    }

    private void runTimePermissionRequest(){
        String[] PERMISSIONS_STORAGE = { Manifest.permission.READ_EXTERNAL_STORAGE };
        final int REQUEST_EXTERNAL_STORAGE = 1;
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if(permission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE , REQUEST_EXTERNAL_STORAGE);
        }
    }

    private void insertToList( String[] insertionTexts ){
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(this , android.R.layout.simple_list_item_1 ,insertionTexts);
        this.musicList = findViewById(R.id.musicList);
        musicList.setAdapter(arrayAdapter);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //If , Received keyCode is back key , change behavior.
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }


}
