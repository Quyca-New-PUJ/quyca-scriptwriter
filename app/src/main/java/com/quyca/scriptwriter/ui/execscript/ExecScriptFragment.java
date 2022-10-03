package com.quyca.scriptwriter.ui.execscript;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.quyca.robotmanager.network.RobotExecutioner;
import com.quyca.scriptwriter.R;
import com.quyca.scriptwriter.databinding.ExecScriptFragmentBinding;
import com.quyca.scriptwriter.integ.exceptions.NonValidPlayableException;
import com.quyca.scriptwriter.integ.network.QuycaMessageCreator;
import com.quyca.scriptwriter.integ.network.QuycaPlayExecutionManager;
import com.quyca.scriptwriter.integ.utils.PlayBundle;
import com.quyca.scriptwriter.integ.utils.UIBundle;
import com.quyca.scriptwriter.model.Macro;
import com.quyca.scriptwriter.model.PlayCharacter;
import com.quyca.scriptwriter.model.QuycaCommandState;
import com.quyca.scriptwriter.ui.shared.ExecScriptViewModel;
import com.quyca.scriptwriter.ui.shared.SharedViewModel;

import java.io.IOException;

/**
 * The type Exec script fragment.
 */
public class ExecScriptFragment extends Fragment {

    private ExecScriptViewModel mViewModel;
    private ExecScriptFragmentBinding binding;
    private RecyclerView.LayoutManager manager;
    private Macro actionList;
    private Button pause;
    private Button resume;
    private Button cancel;
    private RobotExecutioner sender;
    private SharedViewModel model;
    private PlayCharacter charac;
    private QuycaPlayExecutionManager execManager;
    private ActivityResultLauncher<String> requestNetworkLauncher;
    private Thread execThread;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(requireActivity()).get(ExecScriptViewModel.class);
        binding = ExecScriptFragmentBinding.inflate(inflater, container, false);
        model = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        requestNetworkLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        try {
                            sendMessages();
                        } catch (IOException | NonValidPlayableException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(requireContext(), "Sin permisos es imposible grabar audio", Toast.LENGTH_SHORT).show();
                    }
                });
        mViewModel.getToDoActionsObservable().observe(getViewLifecycleOwner(), playables -> {
            actionList = playables;
            actionList.getPlayables().forEach(playable -> playable.setDone(QuycaCommandState.TO_EXECUTE));
            mViewModel.setActionListObservable(actionList.getPlayables());

        });

        model.getCharacterObservable().observe(getViewLifecycleOwner(), playCharacter -> {
            charac = playCharacter;
            try {
                startMessageSending();
            } catch (IOException | NonValidPlayableException e) {
                e.printStackTrace();
            }
        });

        mViewModel.getActionListObservable().observe(getViewLifecycleOwner(), playables -> {
            if (playables != null) {
                ExecScriptLineAdapter slAdapter = new ExecScriptLineAdapter(actionList);
                binding.execlineView.setAdapter(slAdapter);
                startScriptView();
            }
        });


        View root = binding.getRoot();

        pause = root.findViewById(R.id.stop_script);
        resume = root.findViewById(R.id.resume_script);
        cancel = root.findViewById(R.id.cancel_script);


        pause.setOnClickListener(v -> execManager.pause());

        resume.setOnClickListener(v -> execManager.resume());

        cancel.setOnClickListener(v -> {
            execManager.stop();
            execThread.interrupt();
            requireActivity().getSupportFragmentManager().popBackStack();
        });


        return root;
    }
    @Override
    public void onStop() {
        super.onStop();
        execManager.stop();
        execThread.interrupt();
    }

    private void startMessageSending() throws IOException, NonValidPlayableException {

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {
            sendMessages();
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.INTERNET)) {
            Toast.makeText(requireContext(), R.string.permission_read_play, Toast.LENGTH_LONG).show();
            requestNetworkLauncher.launch(Manifest.permission.INTERNET);
        } else {
            requestNetworkLauncher.launch(Manifest.permission.INTERNET);
        }

    }

    public void sendMessages() throws IOException, NonValidPlayableException {
        UIBundle bundle = new UIBundle(requireContext(), mViewModel);
        PlayBundle playBundle = new PlayBundle(actionList, charac);
        execManager = new QuycaPlayExecutionManager(playBundle, bundle);
        execThread = new Thread(execManager);
        execThread.start();
    }

    private void startScriptView() {
        manager = new LinearLayoutManager(getContext());
        binding.execlineView.setLayoutManager(manager);
    }


}