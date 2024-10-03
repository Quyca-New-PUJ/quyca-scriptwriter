package com.quyca.scriptwriter;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.quyca.robotmanager.network.RobotExecutioner;
import com.quyca.scriptwriter.config.ConfiguredAction;
import com.quyca.scriptwriter.config.FixedConfiguredAction;
import com.quyca.scriptwriter.databinding.ActivityMainBinding;
import com.quyca.scriptwriter.integ.model.QuycaMessage;
import com.quyca.scriptwriter.integ.network.QuycaCharacterSender;
import com.quyca.scriptwriter.model.Action;
import com.quyca.scriptwriter.model.Play;
import com.quyca.scriptwriter.model.PlayCharacter;
import com.quyca.scriptwriter.ui.shared.SharedViewModel;
import com.quyca.scriptwriter.utils.FileRepository;
import com.quyca.scriptwriter.utils.FileUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Shared ViewModel for data exchange between activities and fragments
    private SharedViewModel model;
    // Spinner to display available scenes for selection
    private Spinner sceneSpinner;
    // Current play being managed in the app
    private Play play;
    // Position of the selected character in the play
    private int pos;
    // The character being selected for actions within the play
    private PlayCharacter character;
    // Activity result launcher to handle read permission requests
    private ActivityResultLauncher<String> requestReadLauncher;
    // ImageView displaying the character's image
    private ImageView charView;
    // Button to navigate back in the UI
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize the ViewModel to handle shared data
        model = new ViewModelProvider(this).get(SharedViewModel.class);
        // Inflate the layout and bind it to the activity
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Bind UI elements
        charView = findViewById(R.id.char_view);
        sceneSpinner = findViewById(R.id.scene_spinner);
        FileRepository.setUp(this); // Set up the file repository
        backButton = findViewById(R.id.back_button);

        // Set up a listener for the back button to trigger onBackPressed
        backButton.setOnClickListener(v -> onBackPressed());

        // Logging to track different phases of setup
        Log.d("test", "calibration");
        setupCalibrationButton();

        Log.d("test", "read launcher");
        setupReadLauncher();

        Log.d("test", "model");
        setupModel();

        Log.d("test", "app bar");
        setupAppBar();
    }

    // Sets up the calibration button with a click listener that triggers a calibration action for the robot
    private void setupCalibrationButton() {
        Button calibrate = findViewById(R.id.calibrate);
        calibrate.setOnClickListener(v -> {
            Activity helper = this;
            // Runnable task to send a calibration command to the robot asynchronously
            Runnable mRunnable = () -> {
                try {
                    // QuycaMessage is used to configure and send the action to the robot
                    QuycaMessage actMsg = new QuycaMessage(0);
                    actMsg.setAlias(character.getRobotConf().getAlias()); // Setting robot alias
                    actMsg.setActionId("calibration"); // Action ID is set as calibration
                    // Creating a new Action object for calibration using FixedConfiguredAction
                    actMsg.setAction(new Action(new ConfiguredAction(FixedConfiguredAction.calibration, FixedConfiguredAction.calibration.name(), null), false, character.getName()));
                    RobotExecutioner sender = new QuycaCharacterSender(character); // Sender sends the message
                    sender.sendMessage(actMsg); // Sends the calibration message to the robot
                    sender.closeResources(); // Close resources after sending the message
                } catch (IOException e) {
                    e.printStackTrace(); // Handle IO exceptions
                }
                // Run a UI task to show a confirmation toast after calibration completes
                helper.runOnUiThread(() -> Toast.makeText(helper, "Calibrado!", Toast.LENGTH_LONG).show());
            };
            // Start a new thread to execute the calibration
            new Thread(mRunnable).start();
        });

        // These buttons are configured to adjust the robot's motor offsets (right/left)
        findViewById(R.id.offset_der_minus).setOnClickListener(v -> {
            cambiar_offset(true, -1); // Decrease right offset
        });
        findViewById(R.id.offset_der_plus).setOnClickListener(v -> {
            cambiar_offset(true, 1); // Increase right offset
        });
        findViewById(R.id.offset_izq_minus).setOnClickListener(v -> {
            cambiar_offset(false, -1); // Decrease left offset
        });
        findViewById(R.id.offset_izq_plus).setOnClickListener(v -> {
            cambiar_offset(false, 1); // Increase left offset
        });
    }

    // Method to change the robot's motor offset based on the direction (left/right) and amount
    private void cambiar_offset(boolean is_der, int cantidad) {
        Activity helper = this;
        // Asynchronous Runnable task to send the offset change command to the robot
        Runnable mRunnable = () -> {
            try {
                QuycaMessage actMsg = new QuycaMessage(0); // New message to send the offset change
                actMsg.setAlias(character.getRobotConf().getAlias()); // Set the alias for the robot

                // Determine which side's offset to change (left or right)
                FixedConfiguredAction lado = is_der ? FixedConfiguredAction.cfg_offset_der : FixedConfiguredAction.cfg_offset_izq;

                // Add the amount (cantidad) as a parameter for the offset adjustment
                List<String> params = new ArrayList<>();
                params.add(String.valueOf(cantidad)); // Convert amount to string

                // Configure action ID and action with the appropriate side and amount
                actMsg.setActionId("cfg_offset_" + (is_der ? "der" : "izq") + " " + String.valueOf(cantidad));
                actMsg.setAction(new Action(new ConfiguredAction(lado, lado.name(), params), false, character.getName()));

                // Send the message to adjust the robot's offset
                RobotExecutioner sender = new QuycaCharacterSender(character);
                sender.sendMessage(actMsg); // Send message
                sender.closeResources(); // Close resources after sending
                helper.runOnUiThread(() -> Toast.makeText(helper, "Offset modificado!", Toast.LENGTH_LONG).show()); // Show success message
            } catch (IOException e) {
                // If an error occurs, display the exception as a Toast message
                helper.runOnUiThread(() -> Toast.makeText(helper, e.toString(), Toast.LENGTH_LONG).show());
            }
        };
        // Execute the offset adjustment on a separate thread
        new Thread(mRunnable).start();
    }

    // Sets up the launcher to request read permissions from the user
    private void setupReadLauncher() {
        requestReadLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                // If permission is granted, load the scenes using FileUtils
                try {
                    FileUtils.loadScenes(this, character, sceneSpinner, model);
                } catch (IOException e) {
                    e.printStackTrace(); // Handle IO exceptions
                }
            } else {
                // If permission is denied, show a message to the user
                Toast.makeText(this, "Sin permisos es imposible cargar la obra", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to configure the ViewModel to observe the character data and update the UI accordingly
    private void setupModel() {
        // Observe the character data from the ViewModel and update the UI when changes occur
        model.getCharacterObservable().observe(this, playCharacter -> {
            if (playCharacter != null) {
                character = playCharacter; // Set the character object
                FileUtils.setUpSceneSpinner(this, character, sceneSpinner, model); // Set up the scene spinner with character data
            } else {
                // If no character is available, extract data from the Intent and initialize the play and character
                if (getIntent().getExtras() != null) {
                    Bundle bundle = getIntent().getExtras();
                    pos = bundle.getInt("pos"); // Extract the position of the character
                    play = (Play) bundle.getSerializable("play"); // Extract the play object
                    model.setPlayObservable(play); // Update ViewModel with the play data
                    character = play.getCharacters().get(pos); // Set the selected character based on position
                    try {
                        // Start the process to request storage permission and load scenes
                        FileUtils.startStoragePermissionProcess(this, requestReadLauncher, character, sceneSpinner, model);
                        getSupportFragmentManager().popBackStack(); // Remove the last fragment from the back stack
                    } catch (IOException e) {
                        e.printStackTrace(); // Handle IO exceptions
                    }
                }
            }
            // Update the character's image in the UI
            charView.setImageURI(character.getImageUri());
        });
    }

    // Sets up the app bar configuration and integrates the navigation component with the UI
    private void setupAppBar() {
        // Configuration for the app bar using a navigation controller
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_script_viewer).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        // Link the app bar with the navigation controller
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }

    // Method to enable or disable the scene spinner based on a boolean flag
    public void enableSceneSpinner(boolean enable) {
        sceneSpinner.setEnabled(enable); // Enable or disable the scene spinner
    }

    // Method to set the text for the back button
    public void setBackButtonText(String text) {
        backButton.setText(text); // Set the text on the back button
    }

    // Method to enable or disable the back button
    public void setBackButtonEnabled(boolean enable) {
        backButton.setEnabled(enable); // Enable or disable the back button
    }

    // Getter for the current play object
    public Play getPlay() {
        return play; // Return the current play object
    }

}
