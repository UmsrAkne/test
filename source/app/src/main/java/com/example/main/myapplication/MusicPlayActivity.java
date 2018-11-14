package com.example.main.myapplication;

import android.nfc.Tag;
import android.util.Log;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MusicPlayActivity extends AppCompatActivity {

    private TextView mTextMessage;

    //This is Listener. Not run even if write code on this
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_play);
        mTextMessage = findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        String[] testStrings = {"text1" , "text2" , "text3" , "text4" , "text5" , "text6" , "text7"  , "text4" , "text5" , "text6" , "text7" };
        insertToList(testStrings);
    }

    private void insertToList( String[] insertionTexts ){
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(this , android.R.layout.simple_list_item_1 , insertionTexts);
        ListView musicList = findViewById(R.id.musicList);
        musicList.setAdapter(arrayAdapter);
    }

}