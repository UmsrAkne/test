package com.example.main.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.io.File;


public class MusicPlayActivity extends AppCompatActivity {

    private Player player;
    private ListView musicList;
    private File[] musicFiles;
    private int lastPlayedFilePosition;
    private Boolean isExecuted = false;

    //This is Listener. Not run even if write code on this
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            int nextPlayFilePosition = lastPlayedFilePosition;
            Log.i("userTag",Integer.toString(lastPlayedFilePosition));

            switch (item.getItemId()) {
                case R.id.navigation_Prev:
                    nextPlayFilePosition -= 1;
                    if(nextPlayFilePosition >= 0){
                        String nextPlayFilePath = musicFiles[ nextPlayFilePosition ].getPath();
                        lastPlayedFilePosition -= 1;
                        player.play( nextPlayFilePath );
                    }
                    return true;

                case R.id.navigation_Play:
                    player.pause();
                    return true;

                case R.id.navigation_Next:
                    nextPlayFilePosition += 1;
                    if(musicFiles.length > nextPlayFilePosition){
                        String nextPlayFilePath = musicFiles[ nextPlayFilePosition ].getPath();
                        lastPlayedFilePosition += 1;
                        player.play( nextPlayFilePath );
                    }
                    return true;
            }
            return false;
        }
    };

    protected void onRestart(){
        super.onRestart();
        Log.i("userTag","restert");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("userTag" , " called onCreate()");
        runTimePermissionRequest();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_play);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

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

        final int VOLUME_PLUS_VALUE = 1;
        final int VOLUME_MINUS_VALUE = -1;

        findViewById(R.id.volumeDownButton).setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                addToDeviceVolume(VOLUME_MINUS_VALUE);
            }
        });

        findViewById(R.id.volumeUpButton).setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                addToDeviceVolume(VOLUME_PLUS_VALUE);
            }
        });

        //if it don't write this, created multiple player instances.
        if(savedInstanceState == null) {
            player = new Player();
        }

        onSaveInstanceState(new Bundle());

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

    private void insertToList( File[] files){
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(this , android.R.layout.simple_list_item_1 , files);
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
