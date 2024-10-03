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

import java.io.File;

/**
 * SetupActivity is responsible for initializing the app by loading a selected play file from external storage.
 * It manages permission requests, file selection, and navigation to the setup fragment.
 * This activity ensures that the play data is loaded and displayed to the user.
 */
public class SetupActivity extends AppCompatActivity {

    // Launcher for opening a document tree to allow the user to select a file
    private ActivityResultLauncher<Uri> mGetContent;
    // Launcher for requesting permission to read external storage
    private ActivityResultLauncher<String> requestReadLauncher;
    // Selected Play object that contains all the play data
    private Play selectPlay;
    // ViewModel to manage and observe the state of the setup process
    private SetupViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        // Initialize the ViewModel to observe the Play data
        model = new ViewModelProvider(this).get(SetupViewModel.class);
        setupActivity(); // Set up activity components

        // Observe the ViewModel's Play data; if available, load the setup fragment
        model.getPlayObservable().observe(this, play -> {
            if (play != null) {
                selectPlay = play; // Store the selected play
                // Replace the current fragment with the SetupFragment once the play is loaded
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, SetupFragment.newInstance())
                        .commitNow();
            } else {
                // If no play is selected, start the process to load one
                startPlayChargingProcess();
            }
        });
    }

    /**
     * Sets up the activity by registering permission and content launchers. These are used to
     * request storage permissions and open the file picker for selecting a play file.
     */
    private void setupActivity() {
        // Register a launcher for requesting read permission from the user
        requestReadLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                // If permission is granted, proceed to load the play file
                loadPlay();
            } else {
                // Show a message if the permission is denied
                Toast.makeText(this, "Sin permisos es imposible cargar la obra", Toast.LENGTH_SHORT).show();
            }
        });

        // Register a launcher for opening a document tree, allowing the user to select the play file
        mGetContent = registerForActivityResult(new ActivityResultContracts.OpenDocumentTree(), uri -> {
            if (uri != null) {
                // Read the selected play files from the URI and set the ViewModel observable
                selectPlay = FileUtils.readPlayFiles(this, uri);
                model.setPlayObservable(selectPlay);
                // Replace the current fragment with the SetupFragment once the play is loaded
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, SetupFragment.newInstance())
                        .commitNow();
            }
        });
    }

    /**
     * Loads the play file from the external storage directory. The play file is expected to be in the
     * downloads directory of the external storage. After selecting the directory, the document picker is launched.
     */
    private void loadPlay() {
        // Get the directory path where play files are stored (in the downloads folder)
        File f = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        Uri in = Uri.fromFile(f); // Convert the file to a URI
        // Launch the document tree picker, starting from the downloads folder
        mGetContent.launch(in);
    }

    /**
     * Starts the process of checking and requesting the necessary permissions for reading play files
     * from external storage. If the permission is already granted, it loads the play. If not, it requests it.
     */
    private void startPlayChargingProcess() {
        // Check if read permission has already been granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // If permission is granted, load the play
            loadPlay();
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            // If permission was previously denied, show a rationale message
            Toast.makeText(this, R.string.permission_read_play, Toast.LENGTH_LONG).show();
            // Request permission again
            requestReadLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        } else {
            // If permission is not granted, request it directly
            requestReadLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }
}
