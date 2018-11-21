package com.example.main.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
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

import java.util.ArrayList;

public class MusicPlayActivity extends AppCompatActivity {

    private Player player = new Player();
    private ListView musicList;
    private File[] musicFiles;
    private int lastPlayedFilePosition;

    //This is Listener. Not run even if write code on this
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            int nextPlayFilePosition = lastPlayedFilePosition;
            Log.i("userTag",Integer.toString(nextPlayFilePosition));

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

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

}
