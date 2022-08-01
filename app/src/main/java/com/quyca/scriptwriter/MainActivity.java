package com.quyca.scriptwriter;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.quyca.scriptwriter.config.ConfiguredRobot;
import com.quyca.scriptwriter.config.QuycaConfiguration;
import com.quyca.scriptwriter.databinding.ActivityMainBinding;
import com.quyca.scriptwriter.integ.model.QuycaMessage;
import com.quyca.scriptwriter.integ.network.TestQuycaSender;
import com.quyca.scriptwriter.model.Macro;
import com.quyca.scriptwriter.model.Play;
import com.quyca.scriptwriter.model.PlayCharacter;
import com.quyca.scriptwriter.model.Scene;
import com.quyca.scriptwriter.model.Script;
import com.quyca.scriptwriter.model.SoundAction;
import com.quyca.scriptwriter.ui.scripteditor.ScriptEditorFragment;
import com.quyca.scriptwriter.ui.shared.SharedViewModel;
import com.quyca.scriptwriter.utils.AudioRepository;
import com.quyca.scriptwriter.utils.FileRepository;

import java.io.BufferedReader;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Script activeScript;
    private QuycaConfiguration conf;
    private SharedViewModel model;
    private Spinner sceneSpinner;
    private Play play;
    private Button calibrate;
    private int pos;
    private PlayCharacter character;
    private ActivityResultLauncher<String> requestReadLauncher;
    private ImageView charView;
    private Button backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(this).get(SharedViewModel.class);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        charView = findViewById(R.id.char_view);
        sceneSpinner = findViewById(R.id.scene_spinner);
        calibrate = findViewById(R.id.calibrate);
        FileRepository.setUp(this);
        backButton= findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> onBackPressed());
        calibrate.setOnClickListener(v -> {
            Activity helper = this;
            Runnable mRunnable = () -> {
                QuycaMessage actMsg = new QuycaMessage(0);
                List<QuycaMessage> list = new ArrayList<>();
                actMsg.setAlias(character.getRobotConf().getAlias());
                actMsg.setActionId("calibration");
                list.add(actMsg);
                int port = getResources().getInteger(R.integer.port_value);
                String ip = character.getIp();
                TestQuycaSender sender = new TestQuycaSender(ip, port);
                sender.send(list);

                helper.runOnUiThread(() -> {Toast.makeText(helper, "Calibrado!", Toast.LENGTH_LONG).show();});
            };
            new Thread(mRunnable).start();

        });
        requestReadLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        try {
                            loadScenes();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(this, "Sin permisos es imposible cargar la obra", Toast.LENGTH_SHORT).show();
                    }
                });

        model.getCharacterObservable().observe(this, playCharacter -> {
            if (playCharacter != null) {
                character = playCharacter;
                setUpSceneSpinner();
            } else {
                if (getIntent().getExtras() != null) {

                    Bundle bundle = getIntent().getExtras();
                    pos = bundle.getInt("pos");
                    play = (Play) bundle.getSerializable("play");
                    model.setPlayObservable(play);
                    character = play.getCharacters().get(pos);
                    try {
                        startStoragePermissionProcess();
                        getSupportFragmentManager().popBackStack();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            charView.setImageURI(character.getImageUri());
        });


        //BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_script_viewer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        //NavigationUI.setupWithNavController(binding.navView, navController);

    }

    private void loadScenes() throws IOException {
        character.setScenes(new ArrayList<>());
        DocumentFile charsDir = FileRepository.getCharactersDir();
        assert charsDir != null;
        DocumentFile charDir = charsDir.findFile(character.getBasicUri());
        FileRepository.setCharDir(charDir);
        assert charDir != null;
        DocumentFile confFile = charDir.findFile(getResources().getString(R.string.conf_file) + getResources().getString(R.string.json_postfix));

        if (confFile != null && confFile.exists()) {

            BufferedReader jsonConfReader = new BufferedReader(new InputStreamReader(getContentResolver().openInputStream(confFile.getUri())));
            StringBuilder jsonConfBuilder = new StringBuilder();
            for (String line; (line = jsonConfReader.readLine()) != null; ) {
                jsonConfBuilder.append(line).append("\n");
            }
            QuycaConfiguration config = QuycaConfiguration.parseJSON(jsonConfBuilder.toString());
            config = QuycaConfiguration.mergeConfWithBaseConf(config);
            character.setConf(config);
        }
        DocumentFile robotFile = charDir.findFile(getResources().getString(R.string.robot_conf_file) + getResources().getString(R.string.json_postfix));

        if (robotFile != null && robotFile.exists()) {

            BufferedReader jsonConfReader = new BufferedReader(new InputStreamReader(getContentResolver().openInputStream(robotFile.getUri())));
            StringBuilder jsonConfBuilder = new StringBuilder();
            for (String line; (line = jsonConfReader.readLine()) != null; ) {
                jsonConfBuilder.append(line).append("\n");
            }
            ConfiguredRobot confRobot = ConfiguredRobot.parseJSON(jsonConfBuilder.toString());
            character.setRobotConf(confRobot);

        }
        if (charDir.exists()) {
            DocumentFile scenesDir = charDir.findFile(getResources().getString(R.string.scenes_dir));
            if (scenesDir != null && scenesDir.exists()) {
                for (DocumentFile sceneDir : scenesDir.listFiles()) {
                    DocumentFile sceneFile = sceneDir.findFile(getResources().getString(R.string.scenes_file));
                    if (sceneFile != null && sceneFile.exists()) {
                        BufferedReader jsonConfReader = new BufferedReader(new InputStreamReader(getContentResolver().openInputStream(sceneFile.getUri())));
                        StringBuilder jsonConfBuilder = new StringBuilder();
                        for (String line; (line = jsonConfReader.readLine()) != null; ) {
                            jsonConfBuilder.append(line).append("\n");
                        }
                        Scene scene = Scene.parseJSON(jsonConfBuilder.toString());
                        scene.setSceneDirName(sceneDir.getName());
                        processScene(sceneDir, scene);
                        character.getScenes().add(scene);
                    }

                }
            }
        }
        model.setCharacterObservable(character);
        setUpSceneSpinner();
    }

    private void processScene(DocumentFile sceneDir, Scene scene) throws IOException {
        if (sceneDir != null && sceneDir.exists()) {
            scene.setMacros(new ArrayList<>());
            DocumentFile macrosDir = sceneDir.findFile(getResources().getString(R.string.macro_dir));
            TreeMap<Integer,Macro> macroSet = new TreeMap<>();
            List<Macro> duplicates= new ArrayList<>();
            assert macrosDir != null;
            for (DocumentFile macroDir : macrosDir.listFiles()) {
                if (macroDir != null && macroDir.exists()) {
                    DocumentFile macroFile = macroDir.findFile(getResources().getString(R.string.macro_file));
                    BufferedReader jsonConfReader = new BufferedReader(new InputStreamReader(getContentResolver().openInputStream(macroFile.getUri())));
                    StringBuilder jsonConfBuilder = new StringBuilder();
                    for (String line; (line = jsonConfReader.readLine()) != null; ) {
                        jsonConfBuilder.append(line).append("\n");
                    }
                    Macro macro = Macro.parseJSON(jsonConfBuilder.toString());
                    DocumentFile soundDir = macroDir.findFile(getResources().getString(R.string.sound_dir));
                    assert soundDir!=null;
                    macro.getActions().forEach(action -> {
                        if(action instanceof SoundAction){
                                DocumentFile soundFile = soundDir.findFile(((SoundAction) action).getName());
                                assert soundFile!=null;
                                AudioRepository.addAudio((SoundAction) action,soundFile);

                        }

                    });
                    if(!macroSet.containsKey(macro.getPosition())){
                        macroSet.put(macro.getPosition(),macro);
                    }else{
                        duplicates.add(macro);
                    }
                }
            }
            macroSet.keySet().forEach(key -> {
                scene.getMacros().add(macroSet.get(key));
            });
            scene.getMacros().addAll(duplicates);
        }

    }

    private void setUpSceneSpinner() {
        List<String> sceneNames = new ArrayList<>();
        List<Scene> scenes = character.getScenes();
        scenes.forEach(scene -> {
            sceneNames.add(scene.getName());
        });
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sceneNames);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sceneSpinner.setAdapter(arrayAdapter);
        sceneSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Scene scene = character.getScenes().get(position);
                DocumentFile confDir = FileRepository.getCharDir();
                if (confDir.exists()) {
                    DocumentFile scenesDir = confDir.findFile(getResources().getString(R.string.scenes_dir));
                    assert scenesDir != null;
                    for (DocumentFile sceneDir : scenesDir.listFiles()) {
                        if(Objects.requireNonNull(sceneDir.getName()).equalsIgnoreCase(scene.getName())){
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

    public void enableSceneSpinner(boolean enable){
        sceneSpinner.setEnabled(enable);
    }
    public void setBackButtonText(String text){
        backButton.setText(text);
    }
    public void setBackButtonEnabled(boolean enable){
        backButton.setEnabled(enable);
    }

    private void startStoragePermissionProcess() throws IOException {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            loadScenes();
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(this, R.string.permission_read_script, Toast.LENGTH_LONG).show();
            requestReadLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        } else {
            requestReadLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    public Play getPlay(){
        return play;
    }

}