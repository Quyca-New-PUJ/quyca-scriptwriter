package com.quyca.scriptwriter.ui.macros;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.quyca.scriptwriter.R;
import com.quyca.scriptwriter.config.ConfiguredAction;
import com.quyca.scriptwriter.config.QuycaConfiguration;
import com.quyca.scriptwriter.databinding.FragmentMacroExtrasBinding;
import com.quyca.scriptwriter.model.Action;
import com.quyca.scriptwriter.model.Macro;
import com.quyca.scriptwriter.model.PlayCharacter;
import com.quyca.scriptwriter.model.Script;
import com.quyca.scriptwriter.ui.shared.SharedViewModel;

import java.util.ArrayList;
import java.util.List;


public class MacroExtrasFragment extends Fragment {
    private SharedViewModel model;
    private FragmentMacroExtrasBinding binding;
    private Script actScript;
    private RadioButton extra1;
    private RadioButton extra2;
    private RadioButton extra3;
    private RadioButton extra4;
    private RadioButton extra5;
    private RadioButton extra6;
    private RadioButton extra7;
    private RadioButton extra8;
    private RadioButton extra9;
    private List<RadioButton> buttons;
    private PlayCharacter character;
    private QuycaConfiguration conf;
    private Button save;
    private ConfiguredAction currentAction = null;
    private int toEdit;
    private Macro selMacro;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            toEdit= getArguments().getInt("toEdit");
        }else{
            toEdit=-1;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        model = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        binding = FragmentMacroExtrasBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        model.getScriptObservable().observe(getViewLifecycleOwner(), script -> {
            actScript = script;
        });
        model.getMacroObservable().observe(getViewLifecycleOwner(),macro -> {
            selMacro=macro;
        });

        save = root.findViewById(R.id.save_button);

        save.setOnClickListener(v -> {
            if (currentAction == null) {
                Toast.makeText(requireContext(), "Selecciona una accion", Toast.LENGTH_SHORT).show();

            } else {
                Action newAction = new Action(currentAction,true);
                if(toEdit==-1){
                    if(selMacro==null) {
                        actScript.getLines().add(newAction);
                        model.setScriptObservable(actScript);
                    }else{
                        selMacro.getActions().add(newAction);
                        model.setMacroObservable(selMacro);
                    }
                    Toast.makeText(requireContext(), "Accion Creada!", Toast.LENGTH_SHORT).show();
                }else{
                    if(selMacro==null){
                        actScript.getLines().set(toEdit,newAction);
                        model.setScriptObservable(actScript);

                    }else{
                        selMacro.getActions().set(toEdit,newAction);
                        model.setMacroObservable(selMacro);
                    }
                    Toast.makeText(requireContext(), "Accion Modificada!", Toast.LENGTH_SHORT).show();

                    requireActivity().onBackPressed();
                }
                resetButtons();

            }
        });

        extra1 = root.findViewById(R.id.extra1);
        extra2 = root.findViewById(R.id.extra2);
        extra3 = root.findViewById(R.id.extra3);
        extra4 = root.findViewById(R.id.extra4);
        extra5 = root.findViewById(R.id.extra5);
        extra6 = root.findViewById(R.id.extra6);
        extra7 = root.findViewById(R.id.extra7);
        extra8 = root.findViewById(R.id.extra8);
        extra9 = root.findViewById(R.id.extra9);

        extra1.setOnClickListener(this::onClickActions);
        extra2.setOnClickListener(this::onClickActions);
        extra3.setOnClickListener(this::onClickActions);
        extra4.setOnClickListener(this::onClickActions);
        extra5.setOnClickListener(this::onClickActions);
        extra6.setOnClickListener(this::onClickActions);
        extra7.setOnClickListener(this::onClickActions);
        extra8.setOnClickListener(this::onClickActions);
        extra9.setOnClickListener(this::onClickActions);
        model.getCharacterObservable().observe(getViewLifecycleOwner(), playCharacter -> {
            character = playCharacter;
            conf = character.getConf();
            setupClickables();

        });
        return root;
    }

    private void resetButtons() {
        buttons.forEach(radioButton -> {
                radioButton.setChecked(false);
        });
    }

    private void onClickActions(View v) {
        int id = v.getId();
        buttons.forEach(radioButton -> {
            if (radioButton.getId() == id) {
                currentAction = (ConfiguredAction) radioButton.getTag();
                radioButton.setChecked(true);
            } else {
                radioButton.setChecked(false);
            }
        });
    }

    private void setupClickables() {
        extra1.setEnabled(false);
        extra2.setEnabled(false);
        extra3.setEnabled(false);
        extra4.setEnabled(false);
        extra5.setEnabled(false);
        extra6.setEnabled(false);
        extra7.setEnabled(false);
        extra8.setEnabled(false);
        extra9.setEnabled(false);

        buttons = new ArrayList<>();
        buttons.add(extra1);
        buttons.add(extra2);
        buttons.add(extra3);
        buttons.add(extra4);
        buttons.add(extra5);
        buttons.add(extra6);
        buttons.add(extra7);
        buttons.add(extra8);
        buttons.add(extra9);


        List<ConfiguredAction> extras = conf.getExtraActions();
        int size = 0;
        for (ConfiguredAction extra : extras) {
            buttons.get(size).setTag(extra);
            buttons.get(size).setText(extra.getActionId());
            buttons.get(size).setEnabled(true);
            size++;
            if (size > buttons.size()) {
                break;
            }
        }

        if(toEdit!=-1){
           Action act;
            if(selMacro==null){
                act= (Action) actScript.getLines().get(toEdit);
            }else{
                act = (Action) selMacro.getActions().get(toEdit);
            }
            buttons.forEach(radioButton -> {
                ConfiguredAction tag= (ConfiguredAction) radioButton.getTag();
                if (tag!=null && tag.getActionId().equalsIgnoreCase(act.getAction().getActionId())) {
                    radioButton.setChecked(true);
                }
            });
        }

    }


}