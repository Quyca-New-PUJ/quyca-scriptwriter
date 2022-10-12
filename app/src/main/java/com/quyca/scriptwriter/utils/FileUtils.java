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
 * The type File utils implements utility methods for play parsing and set up.
 */
public class FileUtils {

    /**
     * Start storage permission process.
     *
     * @param context             the context
     * @param requestReadLauncher the request read permission launcher
     * @param character           the character
     * @param sceneSpinner        the scene spinner
     * @param model               the model
     * @throws IOException the io exception
     */
    public static void startStoragePermissionProcess(Activity context, ActivityResultLauncher<String> requestReadLauncher, PlayCharacter character, Spinner sceneSpinner, SharedViewModel model) throws IOException {

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            loadScenes(context, character, sceneSpinner, model);
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(context, R.string.permission_read_script, Toast.LENGTH_LONG).show();
            requestReadLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        } else {
            requestReadLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    /**
     * Load scenes.
     *
     * @param context      the context
     * @param character    the character
     * @param sceneSpinner the scene spinner
     * @param model        the model
     * @throws IOException the io exception
     */
    public static void loadScenes(Context context, PlayCharacter character, Spinner sceneSpinner, SharedViewModel model) throws IOException {
        character.setScenes(new ArrayList<>());
        DocumentFile charsDir = FileRepository.getCharactersDir();
        assert charsDir != null;
        DocumentFile charDir = charsDir.findFile(character.getBasicUri());
        FileRepository.setCharDir(charDir);
        assert charDir != null;
        DocumentFile confFile = charDir.findFile(context.getResources().getString(R.string.conf_file) + context.getResources().getString(R.string.json_postfix));

        if (confFile != null && confFile.exists()) {

            BufferedReader jsonConfReader = new BufferedReader(new InputStreamReader(context.getContentResolver().openInputStream(confFile.getUri())));
            StringBuilder jsonConfBuilder = new StringBuilder();
            for (String line; (line = jsonConfReader.readLine()) != null; ) {
                jsonConfBuilder.append(line).append("\n");
            }
            QuycaConfiguration config = QuycaConfiguration.parseJSON(jsonConfBuilder.toString());
            config = QuycaConfiguration.mergeConfWithBaseConf(config);
            character.setConf(config);
        }
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
                        Scene scene = Scene.parseJSON(jsonConfBuilder.toString());
                        scene.setSceneDirName(sceneDir.getName());
                        processScene(context, sceneDir, scene);
                        character.getScenes().add(scene);
                    }

                }
            }
        }
        model.setCharacterObservable(character);
        setUpSceneSpinner(context, character, sceneSpinner, model);
    }

    /**
     * Process scene.
     *
     * @param context  the context
     * @param sceneDir the scene dir
     * @param scene    the scene
     * @throws IOException the io exception
     */
    public static void processScene(Context context, DocumentFile sceneDir, Scene scene) throws IOException {
        if (sceneDir != null && sceneDir.exists()) {
            scene.setPlayables(new ArrayList<>());
            DocumentFile macrosDir = sceneDir.findFile(context.getResources().getString(R.string.macro_dir));
            TreeMap<Integer, Macro> macroSet = new TreeMap<>();
            List<Macro> duplicates = new ArrayList<>();
            assert macrosDir != null;
            for (DocumentFile macroDir : macrosDir.listFiles()) {
                if (macroDir != null && macroDir.exists()) {
                    DocumentFile macroFile = macroDir.findFile(context.getResources().getString(R.string.macro_file));
                    BufferedReader jsonConfReader = new BufferedReader(new InputStreamReader(context.getContentResolver().openInputStream(macroFile.getUri())));
                    StringBuilder jsonConfBuilder = new StringBuilder();
                    for (String line; (line = jsonConfReader.readLine()) != null; ) {
                        jsonConfBuilder.append(line).append("\n");
                    }
                    Macro macro = Macro.parseJSON(jsonConfBuilder.toString());
                    DocumentFile soundDir = macroDir.findFile(context.getResources().getString(R.string.sound_dir));
                    assert soundDir != null;
                    macro.getPlayables().forEach(action -> {
                        if (action instanceof SoundAction) {
                            DocumentFile soundFile = soundDir.findFile(((SoundAction) action).getName());
                            assert soundFile != null;
                            AudioRepository.addAudio((SoundAction) action, soundFile);

                        }

                    });
                    if (!macroSet.containsKey(macro.getPosition())) {
                        macroSet.put(macro.getPosition(), macro);
                    } else {
                        duplicates.add(macro);
                    }
                }
            }
            macroSet.keySet().forEach(key -> {
                scene.getPlayables().add(macroSet.get(key));
            });
            scene.getPlayables().addAll(duplicates);
        }

    }

    /**
     * Sets up scene spinner.
     *
     * @param context      the context
     * @param character    the character
     * @param sceneSpinner the scene spinner
     * @param model        the model
     */
    public static void setUpSceneSpinner(Context context, PlayCharacter character, Spinner sceneSpinner, SharedViewModel model) {
        List<String> sceneNames = new ArrayList<>();
        List<Scene> scenes = character.getScenes();
        scenes.forEach(scene -> {
            sceneNames.add(scene.getName());
        });
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, sceneNames);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sceneSpinner.setAdapter(arrayAdapter);
        sceneSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Scene scene = character.getScenes().get(position);
                DocumentFile confDir = FileRepository.getCharDir();
                if (confDir.exists()) {
                    DocumentFile scenesDir = confDir.findFile(context.getResources().getString(R.string.scenes_dir));
                    assert scenesDir != null;
                    for (DocumentFile sceneDir : scenesDir.listFiles()) {
                        if (Objects.requireNonNull(sceneDir.getName()).equalsIgnoreCase(scene.getName())) {
                            FileRepository.setSceneDir(sceneDir);
                            break;
                        }
                    }
                }
                model.setSceneObservable(scene);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        sceneSpinner.setSelection(0);
    }

    /**
     * Read play files play.
     *
     * @param context the context
     * @param uri     the uri
     * @return the play
     */
    public static Play readPlayFiles(Activity context, Uri uri) {
        Play selectPlay = null;
        try {
            context.getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            FileRepository.setStartUri(uri);
            DocumentFile file = DocumentFile.fromTreeUri(context, uri);
            if (file != null) {
                if (file.isDirectory()) {
                    String filename = context.getResources().getString(R.string.play_prefix) + context.getResources().getString(R.string.json_postfix);
                    DocumentFile playFile = file.findFile(filename);
                    if (playFile != null && playFile.exists()) {
                        BufferedReader jsonReader = new BufferedReader(new InputStreamReader(context.getContentResolver().openInputStream(playFile.getUri())));
                        StringBuilder jsonBuilder = new StringBuilder();
                        for (String line; (line = jsonReader.readLine()) != null; ) {
                            jsonBuilder.append(line).append("\n");
                        }
                        selectPlay = Play.parseJson(jsonBuilder.toString());
                        selectPlay.setCharacters(new ArrayList<>());
                        selectPlay.setUriString(uri.getPath());
                        DocumentFile charsDir = file.findFile(context.getResources().getString(R.string.char_dir));
                        FileRepository.setCharactersDir(charsDir);
                        if (charsDir != null && charsDir.exists()) {
                            for (DocumentFile charDir : charsDir.listFiles()) {
                                DocumentFile charSpecFile = charDir.findFile(context.getResources().getString(R.string.char_spec_name));
                                if (charSpecFile != null && charSpecFile.exists()) {
                                    jsonReader = new BufferedReader(new InputStreamReader(context.getContentResolver().openInputStream(charSpecFile.getUri())));
                                    jsonBuilder = new StringBuilder();
                                    for (String line; (line = jsonReader.readLine()) != null; ) {
                                        jsonBuilder.append(line).append("\n");
                                    }
                                    PlayCharacter aux = PlayCharacter.parseJson(jsonBuilder.toString());
                                    FileRepository.addCharDirectory(aux, charDir);
                                    aux.setBasicUri(charDir.getName());
                                    aux.setImageUri(Objects.requireNonNull(charDir.findFile(context.getResources().getString(R.string.char_img_name))).getUri());
                                    selectPlay.getCharacters().add(aux);
                                }
                            }

                        }
                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return selectPlay;
    }
}

