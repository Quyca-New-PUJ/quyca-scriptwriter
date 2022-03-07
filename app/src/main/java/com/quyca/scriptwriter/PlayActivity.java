package com.quyca.scriptwriter;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.quyca.scriptwriter.config.ConfiguredRobot;
import com.quyca.scriptwriter.config.QuycaConfiguration;
import com.quyca.scriptwriter.databinding.ActivityMainBinding;
import com.quyca.scriptwriter.databinding.ActivityPlayBinding;
import com.quyca.scriptwriter.model.Macro;
import com.quyca.scriptwriter.model.Play;
import com.quyca.scriptwriter.model.PlayCharacter;
import com.quyca.scriptwriter.model.Scene;
import com.quyca.scriptwriter.model.Script;
import com.quyca.scriptwriter.ui.shared.PlaySharedViewModel;
import com.quyca.scriptwriter.ui.shared.SharedViewModel;
import com.quyca.scriptwriter.utils.FileRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.TreeMap;

public class PlayActivity extends AppCompatActivity {

    private ActivityPlayBinding binding;
    private PlaySharedViewModel model;
    private Play play;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(this).get(PlaySharedViewModel.class);
        binding = ActivityPlayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getIntent().getExtras() != null) {

            Bundle bundle = getIntent().getExtras();
            play = (Play) bundle.getSerializable("play");
            model.setPlayObservable(play);
        }

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_play_home)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_play);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

    }

}