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
 * The FileRepository class manages the file structure and access paths for the application's
 * characters, scenes, and macros. It provides static methods to initialize directories, retrieve
 * files associated with characters, and store important file-related information such as URIs.
 * This class centralizes all file management operations related to the characters and their associated
 * assets (scenes, macros, etc.), ensuring easy access and modification throughout the app.
 */
public class FileRepository {
    // Static variables for different directories used in the app for storing files related to characters, scenes, macros, etc.
    private static DocumentFile charactersDir; // Directory holding character files
    private static DocumentFile charDir; // Directory for a specific character
    private static DocumentFile charScenesDir; // Directory for a specific character's scenes
    private static DocumentFile sceneDir; // Directory for a specific scene
    private static DocumentFile tempDir; // Temporary directory used for caching or intermediate storage
    private static DocumentFile macrosDir; // Directory for storing macros (predefined sets of actions)
    private static String currentMacroName; // Name of the currently selected macro
    private static String tempDirName; // Name of the temporary directory
    private static String macrosDirName; // Name of the macros directory
    private static String charScenesDirName; // Name of the directory for character scenes
    private static Uri startUri; // Starting URI for file access
    // Map to relate characters with their respective directories
    private static Map<PlayCharacter, DocumentFile> charRel;

    // Getter for the main directory where character files are stored
    public static DocumentFile getCharactersDir() {
        return charactersDir;
    }

    // Setter for the characters directory
    public static void setCharactersDir(DocumentFile charactersDir) {
        FileRepository.charactersDir = charactersDir;
    }

    // Getter for the directory of a specific character
    public static DocumentFile getCharDir() {
        return charDir;
    }

    // Setter for the character's directory
    public static void setCharDir(DocumentFile charDir) {
        FileRepository.charDir = charDir;
    }

    // Getter for the directory of a specific scene
    public static DocumentFile getSceneDir() {
        return sceneDir;
    }

    // Setter for the scene directory
    public static void setSceneDir(DocumentFile sceneDir) {
        FileRepository.sceneDir = sceneDir;
    }

    // Getter for the currently active macro name
    public static String getCurrentMacroName() {
        return currentMacroName;
    }

    // Setter for the current macro name
    public static void setCurrentMacroName(String currentMacroName) {
        FileRepository.currentMacroName = currentMacroName;
    }

    // Retrieves the directory for a character's scenes, initializing it if necessary
    public static DocumentFile getCharScenesDir() {
        if (charScenesDir == null) {
            // If the directory is not already set, find it under the character's directory
            charScenesDir = charDir.findFile(charScenesDirName);
        }
        return charScenesDir;
    }

    // Retrieves the temporary directory, initializing it if necessary
    public static DocumentFile getTempDir() {
        if (tempDir == null) {
            // If the temp directory is not already set, find it under the character's directory
            tempDir = charDir.findFile(tempDirName);
        }
        return tempDir;
    }

    // Retrieves the macros directory, initializing it if necessary
    public static DocumentFile getMacrosDir() {
        if (macrosDir == null) {
            // If the macros directory is not already set, find it under the scene's directory
            macrosDir = sceneDir.findFile(macrosDirName);
        }
        return macrosDir;
    }

    // Setter for the name of the macros directory
    public static void setMacrosDirName(String macrosDirName) {
        FileRepository.macrosDirName = macrosDirName;
    }

    // Setter for the name of the temporary directory
    public static void setTempDirName(String tempDirName) {
        FileRepository.tempDirName = tempDirName;
    }

    // Setter for the name of the character scenes directory
    public static void setCharScenesDirName(String charScenesDirName) {
        FileRepository.tempDirName = charScenesDirName;
    }

    // Sets up the repository by initializing directory names based on the app's resources
    public static void setUp(AppCompatActivity activity) {
        // Fetch directory names from string resources
        FileRepository.setMacrosDirName(activity.getResources().getString(R.string.macro_dir));
        FileRepository.setCharScenesDirName(activity.getResources().getString(R.string.scenes_dir));
        FileRepository.setTempDirName(activity.getResources().getString(R.string.temp_dir));
    }

    // Retrieves the directory associated with a specific character
    public static DocumentFile getCharDirectory(PlayCharacter character) {
        // Logs the character's name and directory for debugging purposes
        charRel.forEach((playCharacter, documentFile) -> {
            Log.i("DEBUGFILE", playCharacter.getName()); // Log character name
            Log.i("DEBUGFILE", documentFile.getName()); // Log associated directory name
        });
        return charRel.get(character); // Return the directory for the given character
    }

    // Adds a new entry to the map linking a character to their directory
    public static void addCharDirectory(PlayCharacter character, DocumentFile doc) {
        // If the map is not yet initialized, create a new one
        if (charRel == null) {
            FileRepository.charRel = new HashMap<>();
        }
        // Add the character and their corresponding directory to the map
        charRel.put(character, doc);
    }

    // Getter for the start URI used to access the file system
    public static Uri getStartUri() {
        return startUri;
    }

    // Setter for the start URI
    public static void setStartUri(Uri startUri) {
        FileRepository.startUri = startUri;
    }
}
