package com.quyca.scriptwriter.ui.playeditor;

import androidx.arch.core.internal.SafeIterableMap;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.quyca.scriptwriter.R;
import com.quyca.scriptwriter.databinding.FragmentPlayEditorBinding;
import com.quyca.scriptwriter.model.Play;
import com.quyca.scriptwriter.model.PlayUnit;
import com.quyca.scriptwriter.model.Playable;
import com.quyca.scriptwriter.model.Scene;
import com.quyca.scriptwriter.ui.shared.PlaySharedViewModel;
import com.quyca.scriptwriter.ui.touchhelper.ItemMoveCallback;
import com.quyca.scriptwriter.ui.touchhelper.StartDragListener;
import com.quyca.scriptwriter.utils.PlayableBundle;

import java.util.ArrayList;
import java.util.List;

public class PlayEditorFragment extends Fragment{

    private PlaySharedViewModel model;
    private PlayEditorViewModel localModel;
    private Button playButton;
    private Button backButton;
    private Button selButton;
    private Play selPlay;
    private Spinner sceneSpinner;
    private FragmentPlayEditorBinding binding;
    private LinearLayoutManager manager;
    private PlayLineSelectorAdapter slAdapter;
    private ItemTouchHelper touchHelper;
    private int currentScenePos;
    private Scene currentScene;
    private List<PlayableBundle> currSceneList;
    private List<PlayableBundle> selectedList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        currentScenePos= 0 ;
        model = new ViewModelProvider(requireActivity()).get(PlaySharedViewModel.class);
        localModel  = new ViewModelProvider(requireActivity()).get(PlayEditorViewModel.class);
        binding = FragmentPlayEditorBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        sceneSpinner = root.findViewById(R.id.scene_spinner);
        sceneSpinner.setEnabled(false);
        playButton = root.findViewById(R.id.play_button);
        playButton.setEnabled(false);
        selButton = root.findViewById(R.id.selec_macros);
        backButton = root.findViewById(R.id.back_button);

        playButton.setOnClickListener(v -> {
             if (currentScenePos<selPlay.getPlayables().size()) {
                 sceneSpinner.setSelection(currentScenePos);
                 playButton.setEnabled(false);
            }else{
                model.getPlayObservable().removeObservers(getViewLifecycleOwner());
                model.setPlayObservable(selPlay);
                model.setToDoActionsObservable(selPlay.getPlayGraph());
                Navigation.findNavController(v).navigate(R.id.navigation_execscript_play);
            }

        });
        backButton.setOnClickListener(v -> requireActivity().onBackPressed());
        selButton.setOnClickListener(v->{
                List<PlayableBundle> playBase = selectedList;
                List<Playable> playables = new ArrayList<>();
                playBase.forEach(playableBundle -> playables.add(playableBundle.getPlayable()));
                PlayUnit playUnit = new PlayUnit();
                playUnit.setPlayables(playables);
                currentScene.getPlayUnits().add(playUnit);
                currSceneList.removeAll(playBase);
                currSceneList.forEach(playableBundle -> playableBundle.setSelectable(true));
                localModel.setActive(currSceneList);
                localModel.setSelected(new ArrayList<>());
        });
        model.getPlayObservable().observe(getViewLifecycleOwner(), play -> {
            selPlay = play;
            setUpSceneSpinner();
        });
        localModel.getSelected().observe(getViewLifecycleOwner(),playables -> {
            selectedList = playables;
            selButton.setEnabled(playables.size() > 0);
        });
        localModel.getActive().observe(getViewLifecycleOwner(),playables -> {
            currSceneList = playables;
            if(currSceneList.isEmpty()){
                currentScenePos++;
                playButton.setEnabled(true);
                if (currentScenePos>=selPlay.getPlayables().size()) {
                    playButton.setText(requireContext().getText(R.string.play_play));
                }

                }else{
                startScriptView();
            }
        });

        localModel.setSelected(new ArrayList<>());
        return root;
    }


    private void setUpSceneSpinner() {
        List<String> sceneNames = new ArrayList<>();
        List<Playable> scenes = selPlay.getPlayables();
        scenes.forEach(scene -> sceneNames.add(scene.getName()));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, sceneNames);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sceneSpinner.setAdapter(arrayAdapter);
        sceneSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentScene = (Scene) selPlay.getPlayables().get(position);
                currSceneList= new ArrayList<>();
                currentScene.getPlayables().forEach(playable -> currSceneList.add(new PlayableBundle(playable)));
                localModel.setActive(currSceneList);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void startScriptView(){
        manager = new LinearLayoutManager(getContext());
        binding.playlineView.setLayoutManager(manager);
        slAdapter = new PlayLineSelectorAdapter(currSceneList, localModel);
        binding.playlineView.setAdapter(slAdapter);

    }
}