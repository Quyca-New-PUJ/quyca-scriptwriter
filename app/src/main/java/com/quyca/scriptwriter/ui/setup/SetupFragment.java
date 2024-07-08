package com.quyca.scriptwriter.ui.setup;

import android.content.Intent;
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

import com.quyca.scriptwriter.PlayActivity;
import com.quyca.scriptwriter.R;
import com.quyca.scriptwriter.databinding.SetupFragmentBinding;
import com.quyca.scriptwriter.model.Play;

public class SetupFragment extends Fragment {

    private SetupFragmentBinding binding;
    private Play selPlay;

    public static SetupFragment newInstance() {
        return new SetupFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        SetupViewModel mViewModel = new ViewModelProvider(requireActivity()).get(SetupViewModel.class);
        binding = SetupFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Button startPlay = root.findViewById(R.id.start_play);

        mViewModel.getPlayObservable().observe(getViewLifecycleOwner(), play -> {
            selPlay = play;
            setCharacterAdapter();
        });

        startPlay.setOnClickListener(v -> {
            Intent i = new Intent(requireContext(), PlayActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.putExtra("play", selPlay);
            requireActivity().startActivity(i);
        });

        return root;
    }

    private void setCharacterAdapter() {
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        binding.scriptlineView.setLayoutManager(manager);
        CharacterAdapter slAdapter = new CharacterAdapter(selPlay);
        binding.scriptlineView.setAdapter(slAdapter);
    }
}