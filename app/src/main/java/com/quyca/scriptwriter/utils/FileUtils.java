package com.quyca.scriptwriter.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;

import com.quyca.scriptwriter.R;
import com.quyca.scriptwriter.config.ConfiguredRobot;
import com.quyca.scriptwriter.config.QuycaConfiguration;
import com.quyca.scriptwriter.model.Macro;
import com.quyca.scriptwriter.model.Play;
import com.quyca.scriptwriter.model.PlayCharacter;
import com.quyca.scriptwriter.model.Scene;
import com.quyca.scriptwriter.model.SoundAction;
import com.quyca.scriptwriter.ui.shared.SharedViewModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.TreeMap;

/**
 * FileUtils provides static utility methods for handling file operations within the app.
 * It handles loading characters, scenes, macros, and other related configurations from external storage.
 * The class is central to reading, parsing, and setting up the necessary data for the application to function.
 */
public class FileUtils {

    /**
     * Starts the process of requesting storage permissions if not already granted,
     * and if granted, loads the scenes associated with the selected character.
     *
     * @param context           The activity context
     * @param requestReadLauncher The launcher to request permissions
     * @param character         The character for which scenes are loaded
     * @param sceneSpinner      The spinner used to display the scenes
     * @param model             The ViewModel to store the character data
     * @throws IOException If reading the files encounters an issue
     */
    public static void startStoragePermissionProcess(Activity context, ActivityResultLauncher<String> requestReadLauncher, PlayCharacter character, Spinner sceneSpinner, SharedViewModel model) throws IOException {

        // Check if the app already has permission to read external storage
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // Load scenes directly if permission is granted
            loadScenes(context, character, sceneSpinner, model);
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            // Show a rationale message if permission was previously denied
            Toast.makeText(context, R.string.permission_read_script, Toast.LENGTH_LONG).show();
            // Request the permission again
            requestReadLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        } else {
            // Request permission if not granted and no rationale is needed
            requestReadLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    /**
     * Loads the scenes for a given character by reading the configuration files stored in external directories.
     * This method sets up the character's scenes and their related files (macros, actions).
     *
     * @param context       The context of the activity
     * @param character     The character whose scenes are being loaded
     * @param sceneSpinner  The spinner where the scenes will be displayed
     * @param model         The ViewModel to store character and scene data
     * @throws IOException If an error occurs while reading files
     */
    public static void loadScenes(Context context, PlayCharacter character, Spinner sceneSpinner, SharedViewModel model) throws IOException {
        character.setScenes(new ArrayList<>());
        // Retrieve the directory where character files are stored
        DocumentFile charsDir = FileRepository.getCharactersDir();
        assert charsDir != null;
        DocumentFile charDir = charsDir.findFile(character.getBasicUri());
        FileRepository.setCharDir(charDir);
        assert charDir != null;

        // Load the character configuration file (e.g., conf.json)
        DocumentFile confFile = charDir.findFile(context.getResources().getString(R.string.conf_file) + context.getResources().getString(R.string.json_postfix));
        if (confFile != null && confFile.exists()) {
            // Read the configuration file and parse the JSON content
            BufferedReader jsonConfReader = new BufferedReader(new InputStreamReader(context.getContentResolver().openInputStream(confFile.getUri())));
            StringBuilder jsonConfBuilder = new StringBuilder();
            for (String line; (line = jsonConfReader.readLine()) != null; ) {
                jsonConfBuilder.append(line).append("\n");
            }
            QuycaConfiguration config = QuycaConfiguration.parseJSON(jsonConfBuilder.toString());
            config = QuycaConfiguration.mergeConfWithBaseConf(config); // Merge with base configuration
            character.setConf(config); // Set the parsed configuration to the character
        }

        // Load the robot configuration file (e.g., robot_conf.json)
        DocumentFile robotFile = charDir.findFile(context.getResources().getString(R.string.robot_conf_file) + context.getResources().getString(R.string.json_postfix));
        if (robotFile != null && robotFile.exists()) {
            BufferedReader jsonConfReader = new BufferedReader(new InputStreamReader(context.getContentResolver().openInputStream(robotFile.getUri())));
            StringBuilder jsonConfBuilder = new StringBuilder();
            for (String line; (line = jsonConfReader.readLine()) != null; ) {
                jsonConfBuilder.append(line).append("\n");
            }
            ConfiguredRobot confRobot = ConfiguredRobot.parseJSON(jsonConfBuilder.toString());
            character.setRobotConf(confRobot);
        }

        // Load scenes for the character
        if (charDir.exists()) {
            DocumentFile scenesDir = charDir.findFile(context.getResources().getString(R.string.scenes_dir));
            if (scenesDir != null && scenesDir.exists()) {
                for (DocumentFile sceneDir : scenesDir.listFiles()) {
                    DocumentFile sceneFile = sceneDir.findFile(context.getResources().getString(R.string.scenes_file));
                    if (sceneFile != null && sceneFile.exists()) {
                        BufferedReader jsonConfReader = new BufferedReader(new InputStreamReader(context.getContentResolver().openInputStream(sceneFile.getUri())));
                        StringBuilder jsonConfBuilder = new StringBuilder();
                        for (String line; (line = jsonConfReader.readLine()) != null; ) {
                            jsonConfBuilder.append(line).append("\n");
                        }
                        Scene scene = Scene.parseJSON(jsonConfBuilder.toString()); // Parse scene JSON data
                        scene.setSceneDirName(sceneDir.getName());
                        processScene(context, sceneDir, scene); // Process the scene
                        character.getScenes().add(scene); // Add the scene to the character
                    }
                }
            }
        }
        model.setCharacterObservable(character); // Set the character in the ViewModel
        setUpSceneSpinner(context, character, sceneSpinner, model); // Set up the spinner to display scenes
    }

    /**
     * Processes a scene by reading macros and playable actions, then adds them to the scene object.
     * This method reads the macro files and associates them with the corresponding sound files.
     *
     * @param context  The context of the activity
     * @param sceneDir The directory containing the scene files
     * @param scene    The scene object being populated
     * @throws IOException If an error occurs while reading files
     */
    public static void processScene(Context context, DocumentFile sceneDir, Scene scene) throws IOException {
        if (sceneDir != null && sceneDir.exists()) {
            scene.setPlayables(new ArrayList<>()); // Initialize the list of playable actions
            DocumentFile macrosDir = sceneDir.findFile(context.getResources().getString(R.string.macro_dir));
            TreeMap<Integer, Macro> macroSet = new TreeMap<>();
            List<Macro> duplicates = new ArrayList<>();
            assert macrosDir != null;

            // Iterate through each macro directory and read the macro files
            for (DocumentFile macroDir : macrosDir.listFiles()) {
                if (macroDir != null && macroDir.exists()) {
                    DocumentFile macroFile = macroDir.findFile(context.getResources().getString(R.string.macro_file));
                    BufferedReader jsonConfReader = new BufferedReader(new InputStreamReader(context.getContentResolver().openInputStream(macroFile.getUri())));
                    StringBuilder jsonConfBuilder = new StringBuilder();
                    for (String line; (line = jsonConfReader.readLine()) != null; ) {
                        jsonConfBuilder.append(line).append("\n");
                    }
                    Macro macro = Macro.parseJSON(jsonConfBuilder.toString()); // Parse macro JSON data

                    // Process sound actions associated with the macro
                    DocumentFile soundDir = macroDir.findFile(context.getResources().getString(R.string.sound_dir));
                    assert soundDir != null;
                    macro.getPlayables().forEach(action -> {
                        if (action instanceof SoundAction) {
                            DocumentFile soundFile = soundDir.findFile(((SoundAction) action).getName());
                            assert soundFile != null;
                            AudioRepository.addAudio((SoundAction) action, soundFile); // Add sound action to audio repository
                        }
                    });

                    // Add the macro to the set or mark it as duplicate if position already exists
                    if (!macroSet.containsKey(macro.getPosition())) {
                        macroSet.put(macro.getPosition(), macro);
                    } else {
                        duplicates.add(macro);
                    }
                }
            }

            // Add macros to the scene in the correct order based on their positions
            macroSet.keySet().forEach(key -> {
                scene.getPlayables().add(macroSet.get(key));
            });
            scene.getPlayables().addAll(duplicates); // Add duplicates to the scene
        }
    }

    /**
     * Sets up the scene spinner with the names of the scenes associated with the character.
     * When a scene is selected, the corresponding scene data is loaded and updated in the ViewModel.
     *
     * @param context      The context of the activity
     * @param character    The character whose scenes are being displayed
     * @param sceneSpinner The spinner used to display the scenes
     * @param model        The ViewModel to store scene data
     */
    public static void setUpSceneSpinner(Context context, PlayCharacter character, Spinner sceneSpinner, SharedViewModel model) {
        // Populate the spinner with scene names
        List<String> sceneNames = new ArrayList<>();
        List<Scene> scenes = character.getScenes();
        scenes.forEach(scene -> {
            sceneNames.add(scene.getName());
        });

        // Set up the adapter for the spinner
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, sceneNames);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sceneSpinner.setAdapter(arrayAdapter);

        // Handle scene selection from the spinner
        sceneSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Scene scene = character.getScenes().get(position); // Get the selected scene
                DocumentFile confDir = FileRepository.getCharDir();
                if (confDir.exists()) {
                    // Set the scene directory for the selected scene
                    DocumentFile scenesDir = confDir.findFile(context.getResources().getString(R.string.scenes_dir));
                    assert scenesDir != null;
                    for (DocumentFile sceneDir : scenesDir.listFiles()) {
                        if (Objects.requireNonNull(sceneDir.getName()).equalsIgnoreCase(scene.getName())) {
                            FileRepository.setSceneDir(sceneDir);
                            break;
                        }
                    }
                }
                model.setSceneObservable(scene); // Update the ViewModel with the selected scene
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Set the default selection to the first item
        sceneSpinner.setSelection(0);
    }

    /**
     * Reads the play files from the selected directory, parsing the play and character data.
     * The method parses the JSON files and associates characters with their directories.
     *
     * @param context The context of the activity
     * @param uri     The URI of the selected directory
     * @return The Play object with all the loaded data
     */
    public static Play readPlayFiles(Activity context, Uri uri) {
        Play selectPlay = null;
        try {
            // Take permission for accessing the selected directory
            context.getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            FileRepository.setStartUri(uri); // Store the URI in the file repository
            DocumentFile file = DocumentFile.fromTreeUri(context, uri);
            if (file != null && file.isDirectory()) {
                String filename = context.getResources().getString(R.string.play_prefix) + context.getResources().getString(R.string.json_postfix);
                DocumentFile playFile = file.findFile(filename);
                if (playFile != null && playFile.exists()) {
                    // Read and parse the play JSON file
                    BufferedReader jsonReader = new BufferedReader(new InputStreamReader(context.getContentResolver().openInputStream(playFile.getUri())));
                    StringBuilder jsonBuilder = new StringBuilder();
                    for (String line; (line = jsonReader.readLine()) != null; ) {
                        jsonBuilder.append(line).append("\n");
                    }
                    selectPlay = Play.parseJson(jsonBuilder.toString()); // Parse play data
                    selectPlay.setCharacters(new ArrayList<>()); // Initialize character list
                    selectPlay.setUriString(uri.getPath()); // Store the URI path

                    // Load character directories
                    DocumentFile charsDir = file.findFile(context.getResources().getString(R.string.char_dir));
                    FileRepository.setCharactersDir(charsDir);
                    if (charsDir != null && charsDir.exists()) {
                        for (DocumentFile charDir : charsDir.listFiles()) {
                            // Load each character's specific file and parse the JSON content
                            DocumentFile charSpecFile = charDir.findFile(context.getResources().getString(R.string.char_spec_name));
                            if (charSpecFile != null && charSpecFile.exists()) {
                                jsonReader = new BufferedReader(new InputStreamReader(context.getContentResolver().openInputStream(charSpecFile.getUri())));
                                jsonBuilder = new StringBuilder();
                                for (String line; (line = jsonReader.readLine()) != null; ) {
                                    jsonBuilder.append(line).append("\n");
                                }
                                PlayCharacter aux = PlayCharacter.parseJson(jsonBuilder.toString()); // Parse character JSON
                                FileRepository.addCharDirectory(aux, charDir); // Map character to directory
                                aux.setBasicUri(charDir.getName()); // Set the character's basic URI
                                aux.setImageUri(Objects.requireNonNull(charDir.findFile(context.getResources().getString(R.string.char_img_name))).getUri()); // Set the character's image URI
                                selectPlay.getCharacters().add(aux); // Add character to the play
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e); // Handle any exceptions
        }
        return selectPlay; // Return the parsed play object
    }
}
