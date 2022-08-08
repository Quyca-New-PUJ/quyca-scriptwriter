package com.quyca.scriptwriter;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.quyca.scriptwriter.model.Play;
import com.quyca.scriptwriter.ui.setup.SetupFragment;
import com.quyca.scriptwriter.ui.setup.SetupViewModel;
import com.quyca.scriptwriter.utils.FileUtils;

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
        model.getPlayObservable().observe(this, play -> {
            if (play != null) {
                selectPlay = play;
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, SetupFragment.newInstance())
                        .commitNow();
            } else {
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
                        selectPlay = FileUtils.readPlayFiles(this, uri);
                        model.setPlayObservable(selectPlay);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, SetupFragment.newInstance())
                                .commitNow();
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