package com.quyca.scriptwriter.ui.scripteditor;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
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

import android.os.FileUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.quyca.scriptwriter.MainActivity;
import com.quyca.scriptwriter.R;
import com.quyca.scriptwriter.databinding.FragmentScriptEditorBinding;
import com.quyca.scriptwriter.model.Action;
import com.quyca.scriptwriter.model.Playable;
import com.quyca.scriptwriter.model.Macro;
import com.quyca.scriptwriter.model.Scene;
import com.quyca.scriptwriter.model.Script;
import com.quyca.scriptwriter.model.SoundAction;
import com.quyca.scriptwriter.ui.execscript.ExecScriptViewModel;
import com.quyca.scriptwriter.ui.shared.SharedViewModel;
import com.quyca.scriptwriter.ui.touchhelper.ItemMoveCallback;
import com.quyca.scriptwriter.ui.touchhelper.StartDragListener;
import com.quyca.scriptwriter.utils.AudioRepository;
import com.quyca.scriptwriter.utils.FileRepository;

import java.io.BufferedWriter;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;


public class ScriptEditorFragment extends Fragment implements StartDragListener {
    private List<Action> selectActions;
    private Button saveButton;
    private EditText editName;
    private ActivityResultLauncher<String> requestWriteLauncher;
    private ActivityResultLauncher<String> requestEditLauncher;
    private ActivityResultLauncher<String> requestRemoveLauncher;

    private FragmentScriptEditorBinding binding;
    private SharedViewModel model;
    private Scene actScene;
    private RecyclerView.LayoutManager manager;
    private int macroPos;
    private Script actScript;
    private ItemTouchHelper touchHelper;
    private MacroActionAdapter slAdapter;
    private boolean buttonPressed;
    private boolean isCreate;
    private Button addActionButton;
    private ExecScriptViewModel mViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity main= (MainActivity) requireActivity();
        if (getArguments() != null) {
            macroPos = getArguments().getInt("macroPos");

        } else {
            macroPos = -1;
        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentScriptEditorBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        setupFragment();
        selectActions = new ArrayList<>();
        saveButton = root.findViewById(R.id.new_macro);
        editName = root.findViewById(R.id.editName);
        addActionButton = root.findViewById(R.id.add_actions);
        mViewModel = new ViewModelProvider(requireActivity()).get(ExecScriptViewModel.class);
        model = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        saveButton.setOnClickListener(v -> {
            try {
                createNewMacro();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        addActionButton.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("macroPos", macroPos);
            Navigation.findNavController(v).navigate(R.id.navigation_macro_home,bundle);
        });
        model.getSceneObservable().observe(getViewLifecycleOwner(), scene -> {
            actScene = scene;
        });
        model.getScriptObservable().observe(getViewLifecycleOwner(), script -> {
            if (script != null) {
                actScript = script;
                if(macroPos==-1){
                    saveButton.setEnabled(actScript.getLines().size()>0);
                }
                setScriptEditorAdapter();
            }
        });
        if(macroPos==-1){
            model.getCharacterObservable().observe(getViewLifecycleOwner(),playCharacter -> {
                MainActivity main= (MainActivity) requireActivity();
                main.setBackButtonText(requireContext().getResources().getString(R.string.regresar_macro_home)+" "+playCharacter.getName());
            });
        }
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if(buttonPressed||isCreate){
                    buttonPressed=false;
                    setEnabled(false);
                    requireActivity().onBackPressed();
                }
            }
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity main= (MainActivity) requireActivity();
        if (getArguments() != null) {
            macroPos = getArguments().getInt("macroPos");
            main.setBackButtonEnabled(false);
            main.setBackButtonText(requireContext().getResources().getString(R.string.regresar_macro_home));

        } else {
            macroPos = -1;
            main.setBackButtonText(requireContext().getResources().getString(R.string.regresar_accion));
        }
    }

    private void createNewMacro() throws IOException {
        String name = editName.getText().toString();
        if (!name.isEmpty()) {
            if (macroPos == -1) {
                Macro newMacro = new Macro(selectActions);
                newMacro.setMacroName(name);
                actScene.getMacros().add(newMacro);
                startWritingPermissionProcess();
                model.setScriptObservable(new Script());
            } else {
                startEditingPermissionProcess();
                MainActivity act= (MainActivity) requireActivity();
                act.enableSceneSpinner(true);
            }
            model.setSceneObservable(actScene);
            buttonPressed = true;
            Navigation.findNavController(editName).popBackStack(R.id.navigation_script_viewer,false);
        }else {
            Toast.makeText(requireContext(), "No olvides nombrar la escena", Toast.LENGTH_SHORT).show();
        }
    }

    private void startEditingPermissionProcess() throws IOException {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            editMacro();
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(requireContext(), R.string.permission_read_script, Toast.LENGTH_LONG).show();
            requestEditLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        } else {
            requestEditLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    private void editMacro() throws IOException {
        Macro macro;
        macro = actScene.getMacros().get(macroPos);
        String name = editName.getText().toString();
        macro.setPosition(macroPos);
        DocumentFile sceneDir = FileRepository.getSceneDir();
        DocumentFile macrosDir = sceneDir.findFile(getResources().getString(R.string.macro_dir));
        assert macrosDir != null;
        DocumentFile macroDir = macrosDir.findFile(macro.getMacroName());
        assert macroDir != null;
        if (!name.equalsIgnoreCase(macro.getMacroName())) {
            macroDir.renameTo(name);
            macro.setMacroName(name);
            macroDir = macrosDir.findFile(macro.getMacroName());
            assert macroDir != null;
            DocumentFile soundDir = macroDir.findFile(getResources().getString(R.string.sound_dir));
            assert soundDir!=null;
            macro.getActions().forEach(action -> {
                if(action instanceof SoundAction){
                        DocumentFile soundFile = soundDir.findFile(((SoundAction) action).getName());
                        AudioRepository.replaceSound((SoundAction) action,soundFile);

                }

            });
        }
        DocumentFile macroFile = macroDir.findFile(getResources().getString(R.string.macro_file));
        assert macroFile != null;
        boolean del = macroFile.delete();
        if(del){
            macroFile = macroDir.createFile("*/*",getResources().getString(R.string.macro_file));
        }else{
            throw  new IOException("Archivo de macro no se pudo eliminar");
        }
        assert macroFile != null;
        BufferedWriter jsonWriter = new BufferedWriter(new OutputStreamWriter(requireContext().getContentResolver().openOutputStream(macroFile.getUri())));
        String macroString = Macro.toJSON(macro);
        jsonWriter.write(macroString);
        jsonWriter.flush();
        jsonWriter.close();


}

    private void setScriptEditorAdapter() {
        boolean saved = macroPos != -1;
        if (saved) {
            isCreate=false;
            Macro macro = actScene.getMacros().get(macroPos);
            MainActivity act= (MainActivity) requireActivity();
            act.enableSceneSpinner(false);
            selectActions = macro.getActions();
            editName.setText(macro.getMacroName());
            FileRepository.setCurrentMacroName(macro.getMacroName());
            model.setMacroObservable(macro);
        } else {
            isCreate=true;
            selectActions = actScript.getLines();
            model.setMacroObservable(null);
        }
        manager = new LinearLayoutManager(getContext());
        binding.scriptEditorView.setLayoutManager(manager);
        slAdapter = new MacroActionAdapter(selectActions, this, requestRemoveLauncher,saved);
        ItemTouchHelper.Callback callback =
                new ItemMoveCallback<>(slAdapter);
        touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(binding.scriptEditorView);
        binding.scriptEditorView.setAdapter(slAdapter);
    }


    @Override
    public void requestDrag(RecyclerView.ViewHolder viewHolder) {
        touchHelper.startDrag(viewHolder);
    }


    private void setupFragment() {

        requestWriteLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        try {
                            writeMacro();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(requireContext(), "Sin permisos es imposible grabar audio", Toast.LENGTH_SHORT).show();
                    }
                });

        requestEditLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        try {
                            editMacro();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(requireContext(), "Sin permisos es imposible grabar audio", Toast.LENGTH_SHORT).show();
                    }
                });


        requestRemoveLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        try {
                            slAdapter.deleteSoundAction();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(requireContext(), "Sin permisos es imposible grabar audio", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void startWritingPermissionProcess() throws IOException {

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            writeMacro();
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(requireContext(), R.string.permission_read_script, Toast.LENGTH_LONG).show();
            requestWriteLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        } else {
            requestWriteLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    private void writeMacro() throws IOException {
        Macro macro;
        macro = actScene.getMacros().get(actScene.getMacros().size() - 1);
        macro.setPosition(actScene.getMacros().size() - 1);
        DocumentFile charDir = FileRepository.getCharDir();
        assert charDir != null;
        DocumentFile sceneDir = FileRepository.getSceneDir();
        assert sceneDir != null;
        DocumentFile macrosDir = sceneDir.findFile(getResources().getString(R.string.macro_dir));
        if (macrosDir == null || !macrosDir.exists()) {
            macrosDir = sceneDir.createDirectory(getResources().getString(R.string.macro_dir));
        }
        DocumentFile tempDir = FileRepository.getTempDir();
        assert macrosDir != null;
        DocumentFile macroDir = macrosDir.findFile(macro.getMacroName());
        DocumentFile soundDir;
        DocumentFile macroFile;
        if (macroDir == null || !macroDir.exists()) {
            macroDir = macrosDir.createDirectory(macro.getMacroName());
            assert macroDir != null;
            soundDir = macroDir.createDirectory(getResources().getString(R.string.sound_dir));
            macroFile = macroDir.createFile("*/*", getResources().getString(R.string.macro_file));
        } else {
            soundDir = macroDir.findFile(getResources().getString(R.string.sound_dir));
            macroFile = macroDir.findFile(getResources().getString(R.string.macro_file));
        }

        assert macroFile != null;
        assert soundDir != null;
        BufferedWriter jsonWriter = new BufferedWriter(new OutputStreamWriter(requireContext().getContentResolver().openOutputStream(macroFile.getUri())));
        String macroString = Macro.toJSON(macro);
        jsonWriter.write(macroString);
        jsonWriter.flush();
        jsonWriter.close();
        List<Action> lines = macro.getActions();
        if (tempDir != null && tempDir.exists()) {
            for (Action line : lines) {
                if (line instanceof SoundAction) {
                    SoundAction sa = (SoundAction) line;
                    sa.setSaved(true);
                    DocumentFile soundFile = tempDir.findFile(sa.getName());
                    DocumentFile newSoundFile = soundDir.createFile("*/*", sa.getName());
                    assert soundFile != null;
                    InputStream descrIn = requireContext().getContentResolver().openInputStream(soundFile.getUri());
                    assert newSoundFile != null;
                    OutputStream descrOut = requireContext().getContentResolver().openOutputStream(newSoundFile.getUri());
                    FileUtils.copy(descrIn, descrOut);
                    soundFile.delete();
                    AudioRepository.replaceSound(sa,newSoundFile);
                }
            }
        }


    }

    public ExecScriptViewModel getExecViewModel(){
        return mViewModel;
    }

}