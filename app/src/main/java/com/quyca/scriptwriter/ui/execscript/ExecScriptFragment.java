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

import com.quyca.scriptwriter.R;
import com.quyca.scriptwriter.databinding.ExecScriptFragmentBinding;
import com.quyca.scriptwriter.integ.network.QuycaExecutionManager;
import com.quyca.scriptwriter.integ.network.QuycaMessageCreator;
import com.quyca.scriptwriter.integ.network.QuycaSender;
import com.quyca.scriptwriter.integ.network.TestQuycaSender;
import com.quyca.scriptwriter.model.PlayCharacter;
import com.quyca.scriptwriter.model.Playable;
import com.quyca.scriptwriter.model.QuycaCommandState;
import com.quyca.scriptwriter.ui.shared.SharedViewModel;

import java.util.List;

/**
 * The type Exec script fragment.
 */
public class ExecScriptFragment extends Fragment {

    private ExecScriptViewModel mViewModel;
    private ExecScriptFragmentBinding binding;
    private RecyclerView.LayoutManager manager;
    private List<Playable> actionList;
    private Button pause;
    private Button resume;
    private Button cancel;
    private QuycaSender sender;
    private SharedViewModel model;
    private PlayCharacter charac;
    private QuycaExecutionManager execManager;
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
                            sendMessages();
                    } else {
                        Toast.makeText(requireContext(), "Sin permisos es imposible grabar audio", Toast.LENGTH_SHORT).show();
                    }
                });
        mViewModel.getToDoActionsObservable().observe(getViewLifecycleOwner(),playables -> {
            actionList=playables;
            actionList.forEach(playable -> playable.setDone(QuycaCommandState.TO_EXECUTE));
            mViewModel.setActionListObservable(actionList);

        });

        model.getCharacterObservable().observe(getViewLifecycleOwner(),playCharacter -> {
            charac=playCharacter;
            startMessageSending();
        });

        mViewModel.getActionListObservable().observe(getViewLifecycleOwner(),playables -> {
            if(playables!=null){
                ExecScriptLineAdapter slAdapter = new ExecScriptLineAdapter( actionList );
                binding.execlineView.setAdapter(slAdapter);
                startScriptView();
            }
        });



        View root = binding.getRoot();

        pause = root.findViewById( R.id.stop_script );
        resume = root.findViewById( R.id.resume_script );
        cancel = root.findViewById( R.id.cancel_script );


        pause.setOnClickListener(v -> execManager.pause());

        resume.setOnClickListener(v -> execManager.resume());

        cancel.setOnClickListener(v -> {
            execManager.stop();
            execThread.interrupt();
            requireActivity().getSupportFragmentManager().popBackStack();
        });


        return root;
    }

    private void startMessageSending() {

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {
            sendMessages();
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.INTERNET)) {
            Toast.makeText(requireContext(), R.string.permission_read_play, Toast.LENGTH_LONG).show();
            requestNetworkLauncher.launch(Manifest.permission.INTERNET);
        } else {
            requestNetworkLauncher.launch(Manifest.permission.INTERNET);
        }

    }

    public void sendMessages(){
        QuycaMessageCreator msgCreator = new QuycaMessageCreator(charac);
        int port = getResources().getInteger(R.integer.port_value);
        String ip = charac.getIp();
        sender = new TestQuycaSender(ip,port);
        execManager = new QuycaExecutionManager(msgCreator,sender,requireContext(),actionList,mViewModel);

        execThread = new Thread(execManager);
        execThread.start();
    }

    private void startScriptView() {
        manager = new LinearLayoutManager(getContext());
        binding.execlineView.setLayoutManager(manager);
    }


}