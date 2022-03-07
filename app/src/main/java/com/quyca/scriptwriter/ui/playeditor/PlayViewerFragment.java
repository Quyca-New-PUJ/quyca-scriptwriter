package com.quyca.scriptwriter.ui.playeditor;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.quyca.scriptwriter.R;
import com.quyca.scriptwriter.config.ConfiguredRobot;
import com.quyca.scriptwriter.config.QuycaConfiguration;
import com.quyca.scriptwriter.databinding.FragmentPlayViewerBinding;
import com.quyca.scriptwriter.model.Macro;
import com.quyca.scriptwriter.model.Play;
import com.quyca.scriptwriter.model.PlayCharacter;
import com.quyca.scriptwriter.model.Scene;
import com.quyca.scriptwriter.model.SoundAction;
import com.quyca.scriptwriter.ui.shared.PlaySharedViewModel;
import com.quyca.scriptwriter.ui.touchhelper.ItemMoveCallback;
import com.quyca.scriptwriter.ui.touchhelper.StartDragListener;
import com.quyca.scriptwriter.utils.AudioRepository;
import com.quyca.scriptwriter.utils.FileRepository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.TreeMap;

/**
 * The type Act fragment.
 */

public class PlayViewerFragment extends Fragment implements StartDragListener {

    private FragmentPlayViewerBinding binding;
    private RecyclerView.LayoutManager manager;
    private PlaySharedViewModel model;
    private Spinner sceneSpinner;
    private ItemTouchHelper touchHelper;
    private ActivityResultLauncher<String> requestReadLauncher;
    private PlayLineAdapter slAdapter;
    private Play selPlay;
    private Button playButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        model = new ViewModelProvider(requireActivity()).get(PlaySharedViewModel.class);
        binding = FragmentPlayViewerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        sceneSpinner = root.findViewById(R.id.scene_spinner);
        playButton = root.findViewById(R.id.play_button);
        requestReadLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        try {
                            loadPlay();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(requireContext(), "Sin permisos es imposible cargar la obra", Toast.LENGTH_SHORT).show();
                    }
                });

        playButton.setOnClickListener(v -> {
            model.getPlayObservable().removeObservers(getViewLifecycleOwner());
            model.setPlayObservable(selPlay);
            model.setToDoActionsObservable(selPlay.getPlayGraph());
            Navigation.findNavController(v).navigate(R.id.navigation_execscript_play);
        });

        model.getPlayObservable().observe(getViewLifecycleOwner(), play -> {
            if(play!=null){
                Log.i("PLAYSCENE", "OK");
                selPlay = play;
                try {
                    startPlayChargingProcess();
                    setUpSceneSpinner();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void startPlayChargingProcess() throws IOException {

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            loadPlay();
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(requireContext(), R.string.permission_read_play, Toast.LENGTH_LONG).show();
            requestReadLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        } else {
            requestReadLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    private void loadPlay() throws IOException {
        DocumentFile file = DocumentFile.fromTreeUri(requireContext(), FileRepository.getStartUri());
        if (file != null) {
            if (file.isDirectory()) {
                DocumentFile charsDir = file.findFile(getResources().getString(R.string.char_dir));
                assert charsDir != null;
                for (DocumentFile charDir : charsDir.listFiles()) {
                    for (PlayCharacter character : selPlay.getCharacters()) {
                        if (character.getName().equalsIgnoreCase(charDir.getName())) {
                            FileRepository.addCharDirectory(character, charDir);
                        }
                    }
                }
            }
        }
        for (PlayCharacter character : selPlay.getCharacters()) {
            character.setScenes(new ArrayList<>());
            loadScenes(character);
        }
        mergePlay();
    }

    private void mergePlay() {
        TreeMap<Integer, Scene> helpMap = new TreeMap<>();

        for (PlayCharacter character : selPlay.getCharacters()) {

            character.getScenes().forEach(scene -> {
                scene.getMacros().forEach(macro -> {
                    macro.setCharColor(character.getColor());
                });

                if (!helpMap.containsKey(scene.getPosition())) {
                    helpMap.put(scene.getPosition(), new Scene(scene));
                }
                Objects.requireNonNull(helpMap.get(scene.getPosition())).getMacros().addAll(scene.getMacros());
            });
        }
        List<Scene> aux = new ArrayList<>();
        helpMap.keySet().forEach(integer -> {
            Scene scen = helpMap.get(integer);
            assert scen != null;
            scen.orderMacrosPerCharacter();
            aux.add(scen);
        });
        selPlay.setScenes(aux);

    }

    private void setUpSceneSpinner() {
        List<String> sceneNames = new ArrayList<>();
        List<Scene> scenes = selPlay.getScenes();
        scenes.forEach(scene -> {
            sceneNames.add(scene.getName());
        });
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, sceneNames);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sceneSpinner.setAdapter(arrayAdapter);
        sceneSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Scene scene = selPlay.getScenes().get(position);
                startScriptView(scene);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        sceneSpinner.setSelection(0);
        selPlay.getScenes().forEach(scene ->
                Log.i("PLAYSCENE", scene.getName()));
    }

    private void startScriptView(Scene actScene) {
        manager = new LinearLayoutManager(getContext());
        binding.playlineView.setLayoutManager(manager);
        slAdapter = new PlayLineAdapter(actScene, this);
        ItemTouchHelper.Callback callback =
                new ItemMoveCallback<>(slAdapter);
        touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(binding.playlineView);
        binding.playlineView.setAdapter(slAdapter);

    }

    private void loadScenes(PlayCharacter character) throws IOException {
        DocumentFile charDir = FileRepository.getCharDirectory(character);
        DocumentFile confFile = charDir.findFile(getResources().getString(R.string.conf_file) + getResources().getString(R.string.json_postfix));

        if (confFile != null && confFile.exists()) {

            BufferedReader jsonConfReader = new BufferedReader(new InputStreamReader(requireContext().getContentResolver().openInputStream(confFile.getUri())));
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

            BufferedReader jsonConfReader = new BufferedReader(new InputStreamReader(requireContext().getContentResolver().openInputStream(robotFile.getUri())));
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
                        BufferedReader jsonConfReader = new BufferedReader(new InputStreamReader(requireContext().getContentResolver().openInputStream(sceneFile.getUri())));
                        StringBuilder jsonConfBuilder = new StringBuilder();
                        for (String line; (line = jsonConfReader.readLine()) != null; ) {
                            jsonConfBuilder.append(line).append("\n");
                        }
                        Scene scene = Scene.parseJSON(jsonConfBuilder.toString());
                        scene.setCharName(character.getName());
                        scene.setCharColor(character.getColor());
                        scene.setSceneDirName(sceneDir.getName());
                        processScene(sceneDir, scene);
                        character.getScenes().add(scene);
                    }

                }
            }
        }
    }

    private void processScene(DocumentFile sceneDir, Scene scene) throws IOException {
        if (sceneDir != null && sceneDir.exists()) {
            scene.setMacros(new ArrayList<>());
            DocumentFile macrosDir = sceneDir.findFile(getResources().getString(R.string.macro_dir));
            TreeMap<Integer, Macro> macroSet = new TreeMap<>();
            List<Macro> duplicates = new ArrayList<>();
            assert macrosDir != null;
            for (DocumentFile macroDir : macrosDir.listFiles()) {
                if (macroDir != null && macroDir.exists()) {
                    DocumentFile macroFile = macroDir.findFile(getResources().getString(R.string.macro_file));
                    BufferedReader jsonConfReader = new BufferedReader(new InputStreamReader(requireContext().getContentResolver().openInputStream(macroFile.getUri())));
                    StringBuilder jsonConfBuilder = new StringBuilder();
                    for (String line; (line = jsonConfReader.readLine()) != null; ) {
                        jsonConfBuilder.append(line).append("\n");
                    }
                    Macro macro = Macro.parseJSON(jsonConfBuilder.toString());
                    macro.setCharName(scene.getCharName());
                    macro.setCharColor(scene.getCharColor());
                    macro.setFile(macroDir);
                    DocumentFile soundDir = macroDir.findFile(getResources().getString(R.string.sound_dir));
                    assert soundDir!=null;
                    macro.getActions().forEach(action -> {
                        if(action instanceof SoundAction){
                                DocumentFile soundFile = soundDir.findFile(((SoundAction) action).getName());
                                assert soundFile!=null;
                                AudioRepository.addAudioPlay((SoundAction) action,soundFile);

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
                scene.getMacros().add(macroSet.get(key));
            });
            scene.getMacros().addAll(duplicates);
        }

    }


    @Override
    public void requestDrag(RecyclerView.ViewHolder viewHolder) {
        touchHelper.startDrag(viewHolder);
    }


}