package com.quyca.scriptwriter;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.quyca.scriptwriter.databinding.ActivityPlayBinding;
import com.quyca.scriptwriter.model.Play;
import com.quyca.scriptwriter.ui.shared.PlaySharedViewModel;

/**
 * PlayActivity is responsible for managing the main interface when interacting with a play.
 * It initializes the Play object and shares it between fragments through the ViewModel.
 * This activity also sets up navigation and the app bar for managing the play's user interface.
 */
public class PlayActivity extends AppCompatActivity {

    // View binding object to access the layout views
    private ActivityPlayBinding binding;
    // Shared ViewModel to store and observe the Play object across fragments
    private PlaySharedViewModel model;
    // Play object that holds all data related to the current play being worked on
    private Play play;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize the ViewModel to store and observe the play data
        model = new ViewModelProvider(this).get(PlaySharedViewModel.class);

        // Inflate the layout using view binding
        binding = ActivityPlayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Check if the activity was started with an intent that contains a Play object
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            // Retrieve the Play object passed through the intent
            play = (Play) bundle.getSerializable("play");
            // Set the Play object in the ViewModel to make it observable
            model.setPlayObservable(play);
        }

        // Set up the app bar configuration for navigation within the activity
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_play_home) // The home destination in the navigation graph
                .build();

        // Set up the NavController to manage navigation within the PlayActivity
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_play);
        // Link the action bar with the NavController and the app bar configuration
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }
}
