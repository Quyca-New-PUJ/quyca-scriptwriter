package com.quyca.scriptwriter.utils;

import android.content.Context;
import android.util.Log;

import androidx.documentfile.provider.DocumentFile;

import com.quyca.scriptwriter.model.SoundAction;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Audio repository manages a map that relates audio files to a certain action.
 */
public class AudioRepository {
    private static Map<SoundAction, DocumentFile> audioMap;
    private static Map<SoundAction, DocumentFile> audioPlayMap;


    /**
     * Gets audio map.
     *
     * @return the audio map
     */
    public static Map<SoundAction, DocumentFile> getAudioMap() {
        if (audioMap == null) {
            audioMap = new HashMap<>();
        }
        return audioMap;
    }


    /**
     * Sets audio map.
     *
     * @param audioMap the audio map
     */
    public static void setAudioMap(Map<SoundAction, DocumentFile> audioMap) {
        AudioRepository.audioMap = audioMap;
    }

    /**
     * Gets audio play map.
     *
     * @return the audio play map
     */
    public static Map<SoundAction, DocumentFile> getAudioPlayMap() {
        if (audioPlayMap == null) {
            audioPlayMap = new HashMap<>();
        }
        return audioPlayMap;
    }

    /**
     * Sets audio play map.
     *
     * @param audioPlayMap the audio play map
     */
    public static void setAudioPlayMap(Map<SoundAction, DocumentFile> audioPlayMap) {
        AudioRepository.audioPlayMap = audioPlayMap;
    }

    /**
     * Adds audio to the map.
     *
     * @param sound the sound
     * @param file  the file
     */
    public static void addAudio(SoundAction sound, DocumentFile file) {
        getAudioMap().put(sound, file);
    }

    /**
     * Add audio to the play map.
     *
     * @param sound the sound
     * @param file  the file
     */
    public static void addAudioPlay(SoundAction sound, DocumentFile file) {
        getAudioPlayMap().put(sound, file);
    }


    private static FileDescriptor getAudioFile(SoundAction sound, Context context) {
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

    private static FileDescriptor getAudioPlayFile(SoundAction sound, Context context) {
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


    /**
     * Gets audio either form the play  or the temp map.
     *
     * @param sound   the SoundAction
     * @param context the context
     * @return the audio filedescriptor.
     */
    public static FileDescriptor getAudio(SoundAction sound, Context context) {
        FileDescriptor descr = null;
        if (getAudioPlayMap().containsKey(sound)) {
            descr = getAudioPlayFile(sound, context);
        } else if (getAudioMap().containsKey(sound)) {
            descr = getAudioFile(sound, context);
        }
        Log.i("HASAUDIO", descr == null ? "ES NULL" : "TODO EN ORDEN");
        return descr;
    }

    /**
     * Remove sound.
     *
     * @param sound the sound
     */
    public static void removeSound(SoundAction sound) {
        getAudioMap().remove(sound);
    }

    /**
     * Replace sound.
     *
     * @param sa    the sa
     * @param descr the descr
     */
    public static void replaceSound(SoundAction sa, DocumentFile descr) {
        getAudioMap().replace(sa, descr);
    }
}
