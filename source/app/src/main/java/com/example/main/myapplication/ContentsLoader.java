package com.example.main.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.audiofx.EnvironmentalReverb;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.io.File;

public class ContentsLoader {

    private Context context = null;

    public ContentsLoader(Context applicationContenxt){
        this.context = applicationContenxt;
    }


    public File[] getFilesFromDirectory(String targetDirectoryName){
        File directory = Environment.getExternalStoragePublicDirectory(targetDirectoryName);
        File[] files = new File(directory.getPath()).listFiles();
        return files;
    }


        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
        File[] files = new File(directory.getPath()).listFiles();

        if(files == null){
            Log.i("userTag","files is null");
        }

        String[] fileNames = new String[ files.length ];
        for(int i = 0; i < files.length; i++){
            fileNames[i] = files[i].getName();
        }

        return fileNames;
    }

}
