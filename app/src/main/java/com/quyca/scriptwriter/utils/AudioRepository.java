package com.quyca.scriptwriter.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;

import com.quyca.scriptwriter.R;
import com.quyca.scriptwriter.model.PlayCharacter;
import com.quyca.scriptwriter.model.SoundAction;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.net.ContentHandler;
import java.util.HashMap;
import java.util.Map;

public class AudioRepository {
    private static Map<SoundAction, DocumentFile> audioMap;
    private static Map<SoundAction, DocumentFile> audioPlayMap;



    public static Map<SoundAction, DocumentFile> getAudioMap() {
        if(audioMap==null){
            audioMap = new HashMap<>();
        }
        return audioMap;
    }


    public static void setAudioMap(Map<SoundAction, DocumentFile> audioMap) {
        AudioRepository.audioMap = audioMap;
    }

    public static Map<SoundAction, DocumentFile> getAudioPlayMap() {
        if(audioPlayMap==null){
            audioPlayMap = new HashMap<>();
        }
        return audioPlayMap;
    }

    public static void setAudioPlayMap(Map<SoundAction, DocumentFile> audioPlayMap) {
        AudioRepository.audioPlayMap = audioPlayMap;
    }

    public static void addAudio(SoundAction sound, DocumentFile file){
        getAudioMap().put(sound,file);
    }

    public static void addAudioPlay(SoundAction sound, DocumentFile file){
        getAudioPlayMap().put(sound,file);
    }


    private static FileDescriptor getAudioFile(SoundAction sound, Context context){
        DocumentFile soundFile = getAudioMap().get(sound);
        assert soundFile != null;
        FileDescriptor descr = null;
        try {
            descr = context.getContentResolver().openFileDescriptor(soundFile.getUri(), "r").getFileDescriptor();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return descr;
    }

    private static FileDescriptor getAudioPlayFile(SoundAction sound , Context context){
        DocumentFile soundFile = getAudioPlayMap().get(sound);
        assert soundFile != null;
        FileDescriptor descr = null;

        try {
            descr = context.getContentResolver().openFileDescriptor(soundFile.getUri(), "r").getFileDescriptor();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return descr;
    }


    public static FileDescriptor getAudio(SoundAction sound, Context context){
        FileDescriptor descr=null;
        if(getAudioPlayMap().containsKey(sound)){
            descr= getAudioPlayFile(sound,context);
        }else if (getAudioMap().containsKey(sound)){
            descr=getAudioFile(sound,context);
        }
        Log.i("HASAUDIO",descr==null?"ES NULL":"TODO EN ORDEN");
        return descr;
    }

    public static void removeSound(SoundAction sound){
        getAudioMap().remove(sound);
    }

    public static void replaceSound(SoundAction sa, DocumentFile descr) {
        getAudioMap().replace(sa,descr);
    }
}
