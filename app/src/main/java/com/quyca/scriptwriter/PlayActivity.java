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