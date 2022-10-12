package com.quyca.scriptwriter.ui.macros;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.quyca.scriptwriter.R;
import com.quyca.scriptwriter.config.FixedConfiguredAction;
import com.quyca.scriptwriter.databinding.FragmentMacroRecordBinding;
import com.quyca.scriptwriter.model.Macro;
import com.quyca.scriptwriter.model.PlayCharacter;
import com.quyca.scriptwriter.model.SoundAction;
import com.quyca.scriptwriter.ui.shared.SharedViewModel;
import com.quyca.scriptwriter.utils.AudioRepository;
import com.quyca.scriptwriter.utils.FileRepository;

import java.io.IOException;


/**
 * The type Macro record fragment shows the  options available to create sound actions.
 */
public class MacroRecordFragment extends Fragment {
    private SharedViewModel model;
    private FragmentMacroRecordBinding binding;
    private Macro actScript;
    private boolean recording = false;
    private boolean playing = false;
    private ActivityResultLauncher<String> requestRecordLauncher;
    private ImageButton recordButton;
    private ImageButton playButton;
    private Button saveButton;
    private ImageButton discButton;
    private TextView recordInfo;

    private MediaRecorder recorder;
    private SoundAction sAction;
    private ActivityResultLauncher<String> requestRemoveLauncher;
    private MediaPlayer player;
    private DocumentFile audiotempFile;
    private int oldColor;
    private Macro selMacro;
    private PlayCharacter currentChar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        model = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        binding = FragmentMacroRecordBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        recordButton = root.findViewById(R.id.record_button);
        playButton = root.findViewById(R.id.view_button);
        saveButton = root.findViewById(R.id.save_button);
        discButton = root.findViewById(R.id.disc_button);
        recordInfo = root.findViewById(R.id.record_label);
        playButton.setEnabled(false);
        discButton.setEnabled(false);
        recordInfo.setText(requireContext().getResources().getString(R.string.recording_start_label));
        saveButton.setEnabled(false);
        oldColor = 0;
        recordButton.setOnClickListener(v -> {
            try {
                startRecordingPermissionProcess();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        playButton.setOnClickListener(v -> playAudio());

        discButton.setOnClickListener(v -> {
            try {
                startDeletePermissionProcess();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        saveButton.setOnClickListener(v -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(requireContext());

            alert.setTitle(requireContext().getResources().getString(R.string.sound_label));

            final EditText input = new EditText(requireContext());
            alert.setView(input);

            alert.setPositiveButton("Guardar", (dialog, whichButton) -> {
                String name = input.getText().toString();
                if (!name.isEmpty()) {
                    name += requireContext().getResources().getString(R.string.mp4file);
                    audiotempFile.renameTo(name);
                    sAction.setName(name);
                    AudioRepository.addAudio(sAction, audiotempFile);
                    sAction.setSound(null);
                    if (selMacro != null) {
                        selMacro.getPlayables().add(sAction);
                        model.setMacroObservable(selMacro);
                    } else {
                        actScript.getPlayables().add(sAction);
                        model.setScriptObservable(actScript);
                    }

                    Toast.makeText(requireContext(), "¡Sonido Agregado!", Toast.LENGTH_SHORT).show();
                    recordButton.setImageResource(R.drawable.ic_baseline_mic_24);
                    recordInfo.setText(requireContext().getResources().getString(R.string.recording_start_label));
                    playButton.setEnabled(false);
                    saveButton.setEnabled(false);
                    discButton.setEnabled(false);
                    recordButton.setEnabled(true);
                    audiotempFile = null;
                    sAction = null;
                } else {
                    Toast.makeText(requireContext(), "No olvides darle un nombre al sonido", Toast.LENGTH_LONG).show();
                }
            });

            alert.setNegativeButton("Cancelar", (dialog, whichButton) -> {

            });

            alert.show();
        });

        model.getCharacterObservable().observe(getViewLifecycleOwner(),playCharacter -> {
            currentChar = playCharacter;
        });
        model.getScriptObservable().observe(getViewLifecycleOwner(), script -> {
            actScript = script;
        });

        model.getMacroObservable().observe(getViewLifecycleOwner(), macro -> {
            selMacro = macro;
        });

        return root;
    }

    private void resetSoundAction() throws IOException {

        if (audiotempFile != null && audiotempFile.exists()) {
            boolean ret = audiotempFile.delete();
            if (!ret) {
                throw new IOException("Sound File couldnt be erased.");
            }
            recordButton.setImageResource(R.drawable.ic_baseline_mic_24);

        }
        playButton.setEnabled(false);
        discButton.setEnabled(false);
        recordButton.setEnabled(true);
        audiotempFile = null;
        sAction = null;
    }

    private void setupFragment() {

        requestRecordLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        try {
                            recordAudio();
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
                            resetSoundAction();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(requireContext(), "Sin permisos es imposible grabar audio", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void playAudio() {
        if (!playing) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        player = new MediaPlayer();
        playButton.setImageResource(R.drawable.ic_baseline_pause_24);

        player.setOnCompletionListener(mp -> playButton.setImageResource(R.drawable.ic_baseline_play_arrow_24));
        try {
            player.setDataSource(sAction.getSound());
            player.prepare();
            playing = true;
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopPlaying() {
        player.release();
        playing = false;
        player = null;
        playButton.setImageResource(R.drawable.ic_baseline_play_arrow_24);
    }


    private void recordAudio() throws IOException {
        if (!recording) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void stopRecording() {
        recordButton.setBackgroundColor(oldColor);
        recordButton.setImageResource(R.drawable.ic_baseline_mic_off_24);
        recorder.stop();
        recorder.release();
        recording = false;
        playButton.setEnabled(true);
        discButton.setEnabled(true);
        saveButton.setEnabled(true);
        recordInfo.setText(requireContext().getResources().getString(R.string.recording_end_label));
        recordButton.setEnabled(false);
        recorder = null;
    }

    private void startRecording() throws IOException {
        sAction = new SoundAction(currentChar.getConf().getActionsFromId(FixedConfiguredAction.sound.name()));
        String name = requireContext().getResources().getString(R.string.sound_tmp_name);
        if (!name.isEmpty()) {
            ParcelFileDescriptor descr = null;
            if (selMacro != null) {
                DocumentFile macrosDir = FileRepository.getMacrosDir();
                DocumentFile macroDir = macrosDir.findFile(FileRepository.getCurrentMacroName());
                assert macroDir != null;
                DocumentFile soundDir = macroDir.findFile(requireContext().getResources().getString(R.string.sound_dir));
                assert soundDir != null;
                DocumentFile audioFile = soundDir.createFile("*/*", name + requireContext().getResources().getString(R.string.mp4file));
                assert audioFile != null;
                audiotempFile = audioFile;
                descr = requireContext().getContentResolver().openFileDescriptor(audioFile.getUri(), "rwt");
                sAction.setName(name + requireContext().getResources().getString(R.string.mp4file));
                sAction.setSound(descr.getFileDescriptor());
            } else {
                DocumentFile charDir = FileRepository.getCharDir();
                if (charDir != null && charDir.exists()) {
                    DocumentFile tempDir = FileRepository.getTempDir();
                    if (tempDir == null || !tempDir.exists()) {
                        tempDir = charDir.createDirectory(requireContext().getResources().getString(R.string.temp_dir));
                    }
                    assert tempDir != null;
                    DocumentFile audioFile = tempDir.createFile("*/*", name + requireContext().getResources().getString(R.string.mp4file));
                    assert audioFile != null;
                    audiotempFile = audioFile;
                    descr = requireContext().getContentResolver().openFileDescriptor(audioFile.getUri(), "rwt");
                    sAction.setName(name + requireContext().getResources().getString(R.string.mp4file));
                    sAction.setSound(descr.getFileDescriptor());
                }
            }
            if (descr != null) {
                recordButton.setBackgroundColor(Color.RED);
                recordInfo.setText(requireContext().getResources().getString(R.string.recording_prog_label));

                recorder = new MediaRecorder();
                recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                recorder.setOutputFile(descr.getFileDescriptor());
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                recording = true;
                recorder.prepare();
                recorder.start();
            }
        } else {
            Toast.makeText(requireContext(), "Recuerda poner el nombre del audio!", Toast.LENGTH_LONG).show();
        }

    }

    private void startRecordingPermissionProcess() throws IOException {

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            recordAudio();
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)) {
            Toast.makeText(requireContext(), R.string.permission_read_script, Toast.LENGTH_LONG).show();
            requestRecordLauncher.launch(Manifest.permission.RECORD_AUDIO);
        } else {
            requestRecordLauncher.launch(Manifest.permission.RECORD_AUDIO);
        }
    }

    private void startDeletePermissionProcess() throws IOException {

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            resetSoundAction();
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(requireContext(), R.string.permission_read_script, Toast.LENGTH_LONG).show();
            requestRemoveLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        } else {
            requestRemoveLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }
        if (sAction != null) {
            try {
                resetSoundAction();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (player != null) {
            player.release();
            player = null;
        }
    }
}