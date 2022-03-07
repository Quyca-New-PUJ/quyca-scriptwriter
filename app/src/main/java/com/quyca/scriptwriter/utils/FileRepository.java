package com.quyca.scriptwriter.utils;

import android.net.Uri;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;

import com.quyca.scriptwriter.MainActivity;
import com.quyca.scriptwriter.R;
import com.quyca.scriptwriter.model.PlayCharacter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FileRepository {
    private static DocumentFile charactersDir;
    private static DocumentFile charDir;
    private static DocumentFile charScenesDir;
    private static DocumentFile sceneDir;
    private static DocumentFile tempDir;
    private static DocumentFile macrosDir;
    private static String currentMacroName;
    private static String tempDirName;
    private static String macrosDirName;
    private static String charScenesDirName;
    private static Uri startUri;
    private static Map<PlayCharacter,DocumentFile> charRel;

    public static DocumentFile getCharactersDir() {
        return charactersDir;
    }

    public static void setCharactersDir(DocumentFile charactersDir) {
        FileRepository.charactersDir = charactersDir;
    }

    public static DocumentFile getCharDir() {
        return charDir;
    }

    public static void setCharDir(DocumentFile charDir) {
        FileRepository.charDir = charDir;
    }

    public static DocumentFile getSceneDir() {
        return sceneDir;
    }

    public static void setSceneDir(DocumentFile sceneDir) {
        FileRepository.sceneDir = sceneDir;
    }

    public static String getCurrentMacroName() {
        return currentMacroName;
    }

    public static void setCurrentMacroName(String currentMacroName) {
        FileRepository.currentMacroName = currentMacroName;
    }

    public static DocumentFile getCharScenesDir() {
        if(charScenesDir==null){
            charScenesDir=charDir.findFile(charScenesDirName);
        }
        return charScenesDir;
    }

    public static DocumentFile getTempDir() {
        if(tempDir==null){
            tempDir=charDir.findFile(tempDirName);
        }
        return tempDir;
    }

    public static DocumentFile getMacrosDir() {
        if(macrosDir==null){
            macrosDir=sceneDir.findFile(macrosDirName);
        }
        return macrosDir;
    }

    public static void setMacrosDirName(String macrosDirName) {
        FileRepository.macrosDirName = macrosDirName;
    }

    public static void setTempDirName(String tempDirName) {
        FileRepository.tempDirName = tempDirName;
    }

    public static void setCharScenesDirName(String charScenesDirName) {
        FileRepository.tempDirName = charScenesDirName;
    }

    public static void setUp(AppCompatActivity activity) {
        FileRepository.setMacrosDirName(activity.getResources().getString(R.string.macro_dir));
        FileRepository.setCharScenesDirName(activity.getResources().getString(R.string.scenes_dir));
        FileRepository.setTempDirName(activity.getResources().getString(R.string.temp_dir));
    }

    public static DocumentFile getCharDirectory(PlayCharacter character){
        charRel.forEach((playCharacter, documentFile) -> {
            Log.i("DEBUGFILE", playCharacter.getName());
            Log.i("DEBUGFILE", documentFile.getName());

        });
        return charRel.get(character);
    }

    public static void addCharDirectory(PlayCharacter character,DocumentFile doc){
        if(charRel==null){
            FileRepository.charRel=new HashMap<>();
        }
        charRel.put(character,doc);
    }

    public static Uri getStartUri() {
        return startUri;
    }

    public static void setStartUri(Uri startUri) {
        FileRepository.startUri = startUri;
    }
}
