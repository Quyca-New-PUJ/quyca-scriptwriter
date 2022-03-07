package com.quyca.scriptwriter.ui.execplay;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.quyca.scriptwriter.R;
import com.quyca.scriptwriter.config.ConfiguredRobot;
import com.quyca.scriptwriter.config.QuycaConfiguration;
import com.quyca.scriptwriter.databinding.ExecScriptFragmentBinding;
import com.quyca.scriptwriter.integ.network.QuycaExecutionManager;
import com.quyca.scriptwriter.integ.network.QuycaMessageCreator;
import com.quyca.scriptwriter.integ.network.QuycaPlayExecutionManager;
import com.quyca.scriptwriter.integ.network.QuycaSender;
import com.quyca.scriptwriter.integ.network.TestPlayQuycaSender;
import com.quyca.scriptwriter.integ.network.TestQuycaSender;
import com.quyca.scriptwriter.model.Macro;
import com.quyca.scriptwriter.model.Play;
import com.quyca.scriptwriter.model.PlayCharacter;
import com.quyca.scriptwriter.model.Playable;
import com.quyca.scriptwriter.model.QuycaCommandState;
import com.quyca.scriptwriter.model.Script;
import com.quyca.scriptwriter.ui.shared.PlaySharedViewModel;
import com.quyca.scriptwriter.ui.shared.SharedViewModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Exec script fragment.
 */
public class ExecPlayFragment extends Fragment {

    private PlaySharedViewModel mViewModel;
    private ExecScriptFragmentBinding binding;
    private RecyclerView.LayoutManager manager;
    private List<Macro> actionList;
    private Button pause;
    private Button resume;
    private Button cancel;
    private QuycaSender sender;
    private QuycaPlayExecutionManager execManager;
    private Play selPlay;
    private boolean started=false;
    private Thread execThread;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(requireActivity()).get(PlaySharedViewModel.class);
        binding = ExecScriptFragmentBinding.inflate(inflater, container, false);


        mViewModel.getToDoActionsObservable().observe(getViewLifecycleOwner(),playables -> {
            actionList=playables;
            actionList.forEach(playable -> playable.setDone(QuycaCommandState.TO_EXECUTE));
            mViewModel.setActionListObservable(actionList);

        });
        mViewModel.getPlayObservable().observe(getViewLifecycleOwner(),play -> {
            selPlay=play;
            startMessageSending();
        });

        mViewModel.getActionListObservable().observe(getViewLifecycleOwner(),playables -> {
            if(playables!=null){
                ExecPlayLineAdapter slAdapter = new ExecPlayLineAdapter( actionList );
                binding.execlineView.setAdapter(slAdapter);
                startScriptView();
            }
        });


        View root = binding.getRoot();

        pause = root.findViewById( R.id.stop_script );
        resume = root.findViewById( R.id.resume_script );
        cancel = root.findViewById( R.id.cancel_script );


        pause.setOnClickListener(v -> {
            execManager.pause();
        });

        resume.setOnClickListener(v -> {
            execManager.resume();
        });

        cancel.setOnClickListener(v -> {
            execManager.stop();
            execThread.interrupt();
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return root;
    }

    private void startMessageSending() {
        if(!started) {
            Map<String, QuycaMessageCreator> msgCreators = new HashMap<>();
            selPlay.getCharacters().forEach(playCharacter -> {
                msgCreators.put(playCharacter.getName(), new QuycaMessageCreator(playCharacter));
            });
            int port = getResources().getInteger(R.integer.port_value);
            sender = new TestPlayQuycaSender(selPlay.getCharacters(), port);
            execManager = new QuycaPlayExecutionManager(msgCreators, sender, requireContext(), actionList, mViewModel);
            execThread = new Thread(execManager);
            execThread.start();
            started=true;
        }
    }


    private void startScriptView() {
        manager = new LinearLayoutManager(getContext());
        binding.execlineView.setLayoutManager(manager);
    }


}