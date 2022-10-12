package com.quyca.scriptwriter.utils;

import android.net.Uri;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;

import com.quyca.scriptwriter.R;
import com.quyca.scriptwriter.model.PlayCharacter;

import java.util.HashMap;
import java.util.Map;

/**
 * The type File repository manages a map that relates files to a certain action or play entity (characters, macros, actions, etc).
 */
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
    private static Map<PlayCharacter, DocumentFile> charRel;

    /**
     * Gets characters dir.
     *
     * @return the characters directory
     */
    public static DocumentFile getCharactersDir() {
        return charactersDir;
    }

    /**
     * Sets characters dir.
     *
     * @param charactersDir the characters directory
     */
    public static void setCharactersDir(DocumentFile charactersDir) {
        FileRepository.charactersDir = charactersDir;
    }

    /**
     * Gets the active character directory.
     *
     * @return the active character directory
     */
    public static DocumentFile getCharDir() {
        return charDir;
    }

    /**
     * Sets active character directory.
     *
     * @param charDir the active character directory
     */
    public static void setCharDir(DocumentFile charDir) {
        FileRepository.charDir = charDir;
    }

    /**
     * Gets active scene directory.
     *
     * @return the active scene directory
     */
    public static DocumentFile getSceneDir() {
        return sceneDir;
    }

    /**
     * Sets active scene directory
     *
     * @param sceneDir the active scene directory
     */
    public static void setSceneDir(DocumentFile sceneDir) {
        FileRepository.sceneDir = sceneDir;
    }

    /**
     * Gets current macro name.
     *
     * @return the current macro name
     */
    public static String getCurrentMacroName() {
        return currentMacroName;
    }

    /**
     * Sets current macro name.
     *
     * @param currentMacroName the current macro name
     */
    public static void setCurrentMacroName(String currentMacroName) {
        FileRepository.currentMacroName = currentMacroName;
    }

    /**
     * Gets active character scenes directory.
     *
     * @return the active character scenes directory
     */
    public static DocumentFile getCharScenesDir() {
        if (charScenesDir == null) {
            charScenesDir = charDir.findFile(charScenesDirName);
        }
        return charScenesDir;
    }

    /**
     * Gets temporary directory.
     *
     * @return the temporary directory.
     */
    public static DocumentFile getTempDir() {
        if (tempDir == null) {
            tempDir = charDir.findFile(tempDirName);
        }
        return tempDir;
    }

    /**
     * Gets the active scene macros directory.
     *
     * @return the active scene macros directory
     */
    public static DocumentFile getMacrosDir() {
        if (macrosDir == null) {
            macrosDir = sceneDir.findFile(macrosDirName);
        }
        return macrosDir;
    }

    /**
     * Sets the active scene macros directory
     *
     * @param macrosDirName the active scene macros directory
     */
    public static void setMacrosDirName(String macrosDirName) {
        FileRepository.macrosDirName = macrosDirName;
    }

    /**
     * Sets temporal directory name.
     *
     * @param tempDirName the temporal directory name
     */
    public static void setTempDirName(String tempDirName) {
        FileRepository.tempDirName = tempDirName;
    }

    /**
     * Sets the active character scenes directory name.
     *
     * @param charScenesDirName the active character scenes directory nam
     */
    public static void setCharScenesDirName(String charScenesDirName) {
        FileRepository.tempDirName = charScenesDirName;
    }

    /**
     * Sets up the file repository using the current activity.
     *
     * @param activity the activity
     */
    public static void setUp(AppCompatActivity activity) {
        FileRepository.setMacrosDirName(activity.getResources().getString(R.string.macro_dir));
        FileRepository.setCharScenesDirName(activity.getResources().getString(R.string.scenes_dir));
        FileRepository.setTempDirName(activity.getResources().getString(R.string.temp_dir));
    }

    /**
     * Gets a character directory.
     *
     * @param character the character
     * @return the character directory
     */
    public static DocumentFile getCharDirectory(PlayCharacter character) {
        return charRel.get(character);
    }

    /**
     * Adds character directory.
     *
     * @param character the character
     * @param doc       the doc
     */
    public static void addCharDirectory(PlayCharacter character, DocumentFile doc) {
        if (charRel == null) {
            FileRepository.charRel = new HashMap<>();
        }
        charRel.put(character, doc);
    }

    /**
     * Gets the start directory uri at the top of all the drectory tree.
     *
     * @return the start uri
     */
    public static Uri getStartUri() {
        return startUri;
    }

    /**
     * Sets the start directory uri at the top of all the drectory tree.
     *
     * @param startUri the start uri
     */
    public static void setStartUri(Uri startUri) {
        FileRepository.startUri = startUri;
    }
}
