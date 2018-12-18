package com.example.main.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
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

    private String PREFERENCE_FILE_NAME = "lastPlayingStatus";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("userTag" , " called onCreate()");
        runTimePermissionRequest();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_play);

        loadFileList();

        setEventListenerAtButtons();

        if(savedInstanceState == null) {
            player = new Player();
        }
    }

    private void setEventListenerAtButtons(){
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

        findViewById(R.id.reloadButton).setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                loadFileList();
            }
        });

        findViewById(R.id.playOrPauseButton).setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v){
                player.pause();
            }
        });

        findViewById(R.id.prevPlayButton).setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v){
                int nextPlayFilePosition = lastPlayedFilePosition -1;
                if(nextPlayFilePosition >= 0){
                    startSound( nextPlayFilePosition );
                }
            }
        });

        findViewById(R.id.nextPlayButton).setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v){
                int nextPlayFilePosition = lastPlayedFilePosition + 1;
                if(musicFiles.length > nextPlayFilePosition){
                    startSound( nextPlayFilePosition );
                }
            }
        });

        //at ListView musicList
        musicList = findViewById(R.id.musicList);
        musicList.setOnItemClickListener( new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                startSound( position );
            }
        });
    }

    class CompleteEventListener implements MediaPlayer.OnCompletionListener{
        @Override
        public void onCompletion(MediaPlayer mp) {
            if(musicFiles.length > lastPlayedFilePosition + 1){
                lastPlayedFilePosition += 1;
                String nextPlayFilePath = musicFiles[ lastPlayedFilePosition ].getPath();
                player.setSoundFile(nextPlayFilePath);
                player.start();
                player.setOnCompletionListener(new CompleteEventListener());
            }
        }
    }

    private void startSound(int indexInMusicFiles){
        File selectedMusicFile = musicFiles[ indexInMusicFiles ];
        player.setSoundFile( selectedMusicFile.getPath() );
        player.start();
        lastPlayedFilePosition = indexInMusicFiles;
        createTimer();
        player.setOnCompletionListener(new CompleteEventListener());

        SeekBar seekBar = findViewById(R.id.soundControlSeekBar);
        seekBar.setOnSeekBarChangeListener(new SoundControlSeekBarEventListener());
        seekBar.setMax( player.getDuration() );
    }

    class SoundControlSeekBarEventListener implements SeekBar.OnSeekBarChangeListener{
        @Override // Called when start drag.
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) { }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) { }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            player.seekTo( seekBar.getProgress() );
            seekBar.setProgress( seekBar.getProgress() );
        }
    }

    private void volumeControlButtonAction(boolean volumeMoveDirection ){
        int addition = 1;
        if(!volumeMoveDirection ) addition = -1; // If need be , Reverse the plus and minus.
        addToDeviceVolume(addition);
        String stringVolume = String.valueOf( getDeviceVolume() );
        Toast toast = Toast.makeText(getApplicationContext() , "volume " + stringVolume , Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER , 0 , 0);
        toast.show();
    }

    private void loadFileList(){
        ContentsLoader loader = new ContentsLoader(this);
        musicFiles = loader.getFilesFromDirectory(Environment.DIRECTORY_MUSIC);
        String[] fileNameList = new String[ musicFiles.length ];

        for(int i = 0; i < musicFiles.length; i++){
            fileNameList[i] = musicFiles[i].getName();
        }

        insertToList(fileNameList);
    }

    private int getDeviceVolume(){
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        return am.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    private void createTimer(){
        if(playTimeCounter != null) return;

        playTimeCounter = new Timer();
        int delay = 0;
        final int UPDATE_INTERVAL = 1000;
        playTimeCounter.schedule( new PlayingTimeCounter() , delay , UPDATE_INTERVAL );
    }

    class PlayingTimeCounter extends TimerTask {
        public void run(){
            handler.post(new Runnable() {
                public void run() {
                    final String SEPARATION = "  /  ";
                    TextView target = findViewById(R.id.playingStatuses);
                    target.setText( player.getCurrentPositionByTime() + SEPARATION);
                    target.append( player.getPlayingFileLength() + SEPARATION);
                    target.append( player.getPlayingFileName());
                    SeekBar seekBar = findViewById(R.id.soundControlSeekBar);
                    seekBar.setProgress( player.getCurrentPosition() );
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

    private void savePreference(){
        SharedPreferences preference = getSharedPreferences( PREFERENCE_FILE_NAME , MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor.putInt("lastPlayingPosition", player.getCurrentPosition());
        editor.putString("lastPlayingFilePath" , player.getPlayingFilePath());
        editor.commit();
    }

    private void readPreference(){
        SharedPreferences preference = getSharedPreferences( PREFERENCE_FILE_NAME , MODE_PRIVATE );
        String lastPlayingFilePath = preference.getString("lastPlayingFilePath" , "");
        int lastPlayingPosition = preference.getInt("lastPlayingPosition" , 0);
        player.setSoundFile( lastPlayingFilePath , lastPlayingPosition );
    }

    @Override
    protected void onPause() {
        super.onPause();
        savePreference();
        player.pause();
        player.stopAndNewPlayer();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        readPreference();
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
