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
import com.quyca.scriptwriter.databinding.ExecScriptFragmentBinding;
import com.quyca.scriptwriter.integ.exceptions.NonValidPlayableException;
import com.quyca.scriptwriter.integ.network.QuycaPlayExecutionManager;
import com.quyca.scriptwriter.integ.utils.PlayBundle;
import com.quyca.scriptwriter.integ.utils.UIBundle;
import com.quyca.scriptwriter.model.Play;
import com.quyca.scriptwriter.model.Playable;
import com.quyca.scriptwriter.model.QuycaCommandState;
import com.quyca.scriptwriter.ui.shared.PlaySharedViewModel;

import java.io.IOException;
import java.util.List;

/**
 * The type Exec script fragment shows the play to be executed.
 */
public class ExecPlayFragment extends Fragment {

    private PlaySharedViewModel mViewModel;
    private ExecScriptFragmentBinding binding;
    private RecyclerView.LayoutManager manager;
    private List<Playable> actionList;
    private Button pause;
    private Button resume;
    private Button cancel;
    private QuycaPlayExecutionManager execManager;
    private Play selPlay;
    private boolean started = false;
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


        mViewModel.getToDoActionsObservable().observe(getViewLifecycleOwner(), playables -> {
            actionList = playables;
            actionList.forEach(playable -> playable.setDone(QuycaCommandState.TO_EXECUTE));
            mViewModel.setActionListObservable(actionList);

        });
        mViewModel.getPlayObservable().observe(getViewLifecycleOwner(), play -> {
            selPlay = play;
            try {
                startMessageSending();
            } catch (IOException | NonValidPlayableException e) {
                e.printStackTrace();
            }
        });

        mViewModel.getActionListObservable().observe(getViewLifecycleOwner(), playables -> {
            if (playables != null) {
                ExecPlayLineAdapter slAdapter = new ExecPlayLineAdapter(actionList);
                binding.execlineView.setAdapter(slAdapter);
                startScriptView();
            }
        });


        View root = binding.getRoot();

        pause = root.findViewById(R.id.stop_script);
        resume = root.findViewById(R.id.resume_script);
        cancel = root.findViewById(R.id.cancel_script);


        pause.setOnClickListener(v -> {
            execManager.pause();
        });

        resume.setOnClickListener(v -> {
            execManager.resume();
        });

        cancel.setOnClickListener(v -> {
            execManager.stop();
            execThread.interrupt();
            requireActivity().onBackPressed();
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
        if (!started) {
            UIBundle bundle = new UIBundle(requireContext(), mViewModel);
            execManager = new QuycaPlayExecutionManager(new PlayBundle(selPlay), bundle);
            execThread = new Thread(execManager);
            execThread.start();
            started = true;
        }
    }


    private void startScriptView() {
        manager = new LinearLayoutManager(getContext());
        binding.execlineView.setLayoutManager(manager);
    }


}