package com.quyca.scriptwriter.ui.setup;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quyca.scriptwriter.databinding.SetupFragmentBinding;
import com.quyca.scriptwriter.model.Play;

public class SetupFragment extends Fragment {

    private SetupViewModel mViewModel;
    private SetupFragmentBinding binding;
    private Play selPlay;
    private RecyclerView.LayoutManager manager;

    public static SetupFragment newInstance() {
        return new SetupFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(requireActivity()).get(SetupViewModel.class);
        binding = SetupFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mViewModel.getPlayObservable().observe(getViewLifecycleOwner(),play -> {
            selPlay=play;
            setCharacterAdapter();
        });
        return root;
    }


    private void setCharacterAdapter(){
        manager = new LinearLayoutManager(getContext());
        binding.scriptlineView.setLayoutManager(manager);
        CharacterAdapter slAdapter = new CharacterAdapter(selPlay);
        binding.scriptlineView.setAdapter(slAdapter);

    }

}