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
 * The AudioRepository class serves as a centralized storage for managing audio files
 * associated with sound actions in the application. It uses a map to store sound actions and
 * their corresponding audio files, allowing easy retrieval and management of audio resources.
 */
public class AudioRepository {
    // Map to store sound actions and their corresponding audio files
    private static Map<SoundAction, DocumentFile> audioMap;
    // Another map specifically for play-related audio files
    private static Map<SoundAction, DocumentFile> audioPlayMap;

    /**
     * Retrieves the map that stores general audio files associated with sound actions.
     *
     * @return A map of sound actions and their corresponding audio files.
     */
    public static Map<SoundAction, DocumentFile> getAudioMap() {
        if (audioMap == null) {
            audioMap = new HashMap<>(); // Initialize the map if it doesn't exist
        }
        return audioMap;
    }

    /**
     * Sets the map that stores general audio files.
     *
     * @param audioMap A map of sound actions and their corresponding audio files.
     */
    public static void setAudioMap(Map<SoundAction, DocumentFile> audioMap) {
        AudioRepository.audioMap = audioMap;
    }

    /**
     * Retrieves the map that stores play-specific audio files associated with sound actions.
     *
     * @return A map of sound actions and their corresponding audio files for play actions.
     */
    public static Map<SoundAction, DocumentFile> getAudioPlayMap() {
        if (audioPlayMap == null) {
            audioPlayMap = new HashMap<>(); // Initialize the map if it doesn't exist
        }
        return audioPlayMap;
    }

    /**
     * Sets the map that stores play-specific audio files.
     *
     * @param audioPlayMap A map of sound actions and their corresponding audio files for play actions.
     */
    public static void setAudioPlayMap(Map<SoundAction, DocumentFile> audioPlayMap) {
        AudioRepository.audioPlayMap = audioPlayMap;
    }

    /**
     * Adds an audio file to the general audio map for a given sound action.
     *
     * @param sound The sound action associated with the audio file.
     * @param file  The audio file to be associated with the sound action.
     */
    public static void addAudio(SoundAction sound, DocumentFile file) {
        getAudioMap().put(sound, file);
    }

    /**
     * Adds an audio file to the play-specific audio map for a given sound action.
     *
     * @param sound The sound action associated with the audio file.
     * @param file  The audio file to be associated with the sound action.
     */
    public static void addAudioPlay(SoundAction sound, DocumentFile file) {
        getAudioPlayMap().put(sound, file);
    }

    /**
     * Retrieves the file descriptor for an audio file associated with a sound action from the general audio map.
     *
     * @param sound   The sound action whose audio file is to be retrieved.
     * @param context The application context used to resolve the file descriptor.
     * @return The file descriptor for the audio file, or null if not found.
     */
    private static FileDescriptor getAudioFile(SoundAction sound, Context context) {
        DocumentFile soundFile = getAudioMap().get(sound);
        assert soundFile != null;
        FileDescriptor descr = null;
        try {
            // Get the file descriptor for the audio file using the content resolver
            descr = context.getContentResolver().openFileDescriptor(soundFile.getUri(), "r").getFileDescriptor();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return descr;
    }

    /**
     * Retrieves the file descriptor for an audio file associated with a sound action from the play-specific audio map.
     *
     * @param sound   The sound action whose audio file is to be retrieved.
     * @param context The application context used to resolve the file descriptor.
     * @return The file descriptor for the audio file, or null if not found.
     */
    private static FileDescriptor getAudioPlayFile(SoundAction sound, Context context) {
        DocumentFile soundFile = getAudioPlayMap().get(sound);
        assert soundFile != null;
        FileDescriptor descr = null;
        try {
            // Get the file descriptor for the play-specific audio file using the content resolver
            descr = context.getContentResolver().openFileDescriptor(soundFile.getUri(), "r").getFileDescriptor();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return descr;
    }

    /**
     * Retrieves the appropriate audio file descriptor (either general or play-specific) for a given sound action.
     *
     * @param sound   The sound action whose audio file is to be retrieved.
     * @param context The application context used to resolve the file descriptor.
     * @return The file descriptor for the audio file, or null if not found.
     */
    public static FileDescriptor getAudio(SoundAction sound, Context context) {
        FileDescriptor descr = null;
        if (getAudioPlayMap().containsKey(sound)) {
            // Get the audio file from the play-specific map if available
            descr = getAudioPlayFile(sound, context);
        } else if (getAudioMap().containsKey(sound)) {
            // Otherwise, get the audio file from the general audio map
            descr = getAudioFile(sound, context);
        }
        // Log whether the audio file was found or not
        Log.i("HASAUDIO", descr == null ? "ES NULL" : "TODO EN ORDEN");
        return descr;
    }

    /**
     * Removes a sound action and its associated audio file from the general audio map.
     *
     * @param sound The sound action to be removed.
     */
    public static void removeSound(SoundAction sound) {
        getAudioMap().remove(sound);
    }

    /**
     * Replaces the audio file associated with a given sound action in the general audio map.
     *
     * @param sa    The sound action whose audio file is being replaced.
     * @param descr The new audio file to associate with the sound action.
     */
    public static void replaceSound(SoundAction sa, DocumentFile descr) {
        getAudioMap().replace(sa, descr);
    }
}
