package com.quyca.scriptwriter;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.quyca.scriptwriter.model.Play;
import com.quyca.scriptwriter.model.PlayCharacter;
import com.quyca.scriptwriter.ui.setup.SetupFragment;
import com.quyca.scriptwriter.ui.setup.SetupViewModel;
import com.quyca.scriptwriter.utils.FileRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

public class SetupActivity extends AppCompatActivity {
    private ActivityResultLauncher<Uri> mGetContent;
    private ActivityResultLauncher<String> requestReadLauncher;
    private Play selectPlay;
    private SetupViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        model = new ViewModelProvider(this).get(SetupViewModel.class);
        setupActivity();
      model.getPlayObservable().observe(this,play -> {
          if(play!=null){
              selectPlay=play;
              getSupportFragmentManager().beginTransaction()
                      .replace(R.id.container, SetupFragment.newInstance())
                      .commitNow();
          }else{
              startPlayChargingProcess();
          }
      });

    }

    private void setupActivity() {
        requestReadLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        loadPlay();
                    } else {
                        Toast.makeText(this, "Sin permisos es imposible cargar la obra", Toast.LENGTH_SHORT).show();
                    }
                });

        mGetContent = registerForActivityResult(new ActivityResultContracts.OpenDocumentTree(),
                uri -> {
                    if (uri != null) {
                        try {
                            getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            FileRepository.setStartUri(uri);
                            DocumentFile file = DocumentFile.fromTreeUri(this, uri);
                            if (file != null) {
                                if (file.isDirectory()) {
                                    String filename = getResources().getString(R.string.play_prefix) + getResources().getString(R.string.json_postfix);
                                    DocumentFile playFile = file.findFile(filename);
                                    if (playFile != null && playFile.exists()) {
                                        BufferedReader jsonReader = new BufferedReader(new InputStreamReader(getContentResolver().openInputStream(playFile.getUri())));
                                        StringBuilder jsonBuilder = new StringBuilder();
                                        for (String line; (line = jsonReader.readLine()) != null; ) {
                                            jsonBuilder.append(line).append("\n");
                                        }
                                        selectPlay = Play.parseJson(jsonBuilder.toString());
                                        selectPlay.setCharacters(new ArrayList<>());
                                        selectPlay.setUriString(uri.getPath());
                                        DocumentFile charsDir = file.findFile(getResources().getString(R.string.char_dir));
                                        FileRepository.setCharactersDir(charsDir);
                                        if (charsDir != null && charsDir.exists()) {
                                            for (DocumentFile charDir : charsDir.listFiles()) {
                                                DocumentFile charSpecFile = charDir.findFile(getResources().getString(R.string.char_spec_name));
                                                if (charSpecFile != null && charSpecFile.exists()) {
                                                    jsonReader = new BufferedReader(new InputStreamReader(getContentResolver().openInputStream(charSpecFile.getUri())));
                                                    jsonBuilder = new StringBuilder();
                                                    for (String line; (line = jsonReader.readLine()) != null; ) {
                                                        jsonBuilder.append(line).append("\n");
                                                    }
                                                    PlayCharacter aux = PlayCharacter.parseJson(jsonBuilder.toString());
                                                    FileRepository.addCharDirectory(aux,charDir);
                                                    aux.setBasicUri(charDir.getName());
                                                    aux.setImageUri(Objects.requireNonNull(charDir.findFile(getResources().getString(R.string.char_img_name))).getUri());
                                                    selectPlay.getCharacters().add(aux);
                                                }
                                            }

                                        }
                                    }
                                }
                            }
                            model.setPlayObservable(selectPlay);
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.container, SetupFragment.newInstance())
                                    .commitNow();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
    }


    private void loadPlay() {
        Uri in = Uri.fromFile(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS));
        mGetContent.launch(in);
    }

    private void startPlayChargingProcess() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            loadPlay();
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(this, R.string.permission_read_play, Toast.LENGTH_LONG).show();
            requestReadLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        } else {
            requestReadLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }
}