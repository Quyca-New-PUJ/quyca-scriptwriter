package com.quyca.scriptwriter;

import android.app.Activity;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {

    private SharedViewModel model;
    private Spinner sceneSpinner;
    private Play play;
    private int pos;
    private PlayCharacter character;
    private ActivityResultLauncher<String> requestReadLauncher;
    private ImageView charView;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(this).get(SharedViewModel.class);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        charView = findViewById(R.id.char_view);
        sceneSpinner = findViewById(R.id.scene_spinner);
        Button calibrate = findViewById(R.id.calibrate);
        FileRepository.setUp(this);
        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> onBackPressed());
        calibrate.setOnClickListener(v -> {
            Activity helper = this;
            Runnable mRunnable = () -> {
                try {
                    QuycaMessage actMsg = new QuycaMessage(0);
                    actMsg.setAlias(character.getRobotConf().getAlias());
                    actMsg.setActionId("calibration");
                    actMsg.setAction(new Action(new ConfiguredAction(FixedConfiguredAction.calibration, FixedConfiguredAction.calibration.name(), null), false, character.getName()));
                    int port = getResources().getInteger(R.integer.port_value);
                    RobotExecutioner sender = new QuycaCharacterSender(character);
                    sender.sendMessage(actMsg);
                    sender.closeResources();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                helper.runOnUiThread(() -> Toast.makeText(helper, "Calibrado!", Toast.LENGTH_LONG).show());
            };
            new Thread(mRunnable).start();

        });
        requestReadLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        try {
                            FileUtils.loadScenes(this, character, sceneSpinner, model);
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
                FileUtils.setUpSceneSpinner(this, character, sceneSpinner, model);
            } else {
                if (getIntent().getExtras() != null) {

                    Bundle bundle = getIntent().getExtras();
                    pos = bundle.getInt("pos");
                    play = (Play) bundle.getSerializable("play");
                    model.setPlayObservable(play);
                    character = play.getCharacters().get(pos);
                    try {
                        FileUtils.startStoragePermissionProcess(this, requestReadLauncher, character, sceneSpinner, model);
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

    public void enableSceneSpinner(boolean enable) {
        sceneSpinner.setEnabled(enable);
    }

    public void setBackButtonText(String text) {
        backButton.setText(text);
    }

    public void setBackButtonEnabled(boolean enable) {
        backButton.setEnabled(enable);
    }

    public Play getPlay() {
        return play;
    }

}