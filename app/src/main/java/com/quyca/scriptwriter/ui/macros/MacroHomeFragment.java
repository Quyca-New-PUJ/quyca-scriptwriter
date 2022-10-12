package com.quyca.scriptwriter.ui.macros;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.quyca.scriptwriter.MainActivity;
import com.quyca.scriptwriter.R;
import com.quyca.scriptwriter.databinding.FragmentMacroHomeBinding;
import com.quyca.scriptwriter.model.Macro;
import com.quyca.scriptwriter.model.Scene;
import com.quyca.scriptwriter.ui.shared.SharedViewModel;
import com.quyca.scriptwriter.utils.FileRepository;


/**
 * The type Macro home fragmen encapsultes the logic to switch between multiple types of actions.
 */
public class MacroHomeFragment extends Fragment {
    private SharedViewModel model;
    private FragmentMacroHomeBinding binding;
    private RadioButton mvtButton;
    private RadioButton screenButton;
    private RadioButton recordButton;
    private Button prevButton;
    private int lastFragment = -1;
    private Macro actScript;
    private int macroPos;
    private Scene selScene = null;
    private Macro selMacro = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity main = (MainActivity) requireActivity();
        main.setBackButtonText(requireContext().getResources().getString(R.string.regresar_macro_home));

        if (getArguments() != null) {
            macroPos = getArguments().getInt("macroPos");
            main.setBackButtonText(requireContext().getResources().getString(R.string.regresar_lista));

        } else {
            macroPos = -1;
        }
        main.setBackButtonEnabled(true);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        model = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        binding = FragmentMacroHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mvtButton = root.findViewById(R.id.movement_toggle);
        screenButton = root.findViewById(R.id.screen_toggle);
        recordButton = root.findViewById(R.id.record_toggle);
        prevButton = root.findViewById(R.id.prev_button);

        mvtButton.setOnClickListener(this::onClick);
        screenButton.setOnClickListener(this::onClick);
        recordButton.setOnClickListener(this::onClick);
        prevButton.setOnClickListener(v -> {
            if (macroPos != -1) {
                Navigation.findNavController(v).navigate(R.id.navigation_script_edit, getArguments());
            } else {
                Navigation.findNavController(v).navigate(R.id.navigation_script_edit);
            }
        });
        model.getScriptObservable().observe(getViewLifecycleOwner(), script -> {
            actScript = script;
            prevButton.setEnabled(actScript.getPlayables().size() > 0);

        });
        if (macroPos == -1) {
            model.getCharacterObservable().observe(getViewLifecycleOwner(), playCharacter -> {
                MainActivity main = (MainActivity) requireActivity();
                main.setBackButtonText(requireContext().getResources().getString(R.string.regresar_macro_home) + " " + playCharacter.getName());
            });
        }

        if (macroPos != -1) {
            model.getSceneObservable().observe(getViewLifecycleOwner(), scene -> {
                selScene = scene;
                selMacro = (Macro) selScene.getPlayables().get(macroPos);
                model.setMacroObservable(selMacro);
                FileRepository.setCurrentMacroName(selMacro.getName());
                prevButton.setEnabled(true);
            });
        } else {
            model.setMacroObservable(null);
            FileRepository.setCurrentMacroName(null);
        }
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mvtButton.isSelected())
            mvtButton.callOnClick();
    }

    /**
     * On click resets the fragments according to the selection.
     *
     * @param v the view that triggered the onClick.
     */
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.movement_toggle) {
            screenButton.setSelected(false);
            recordButton.setSelected(false);
            mvtButton.setSelected(true);
            changeFragment(id, new MacroMovementFragment());

        } else if (id == R.id.record_toggle) {

            screenButton.setSelected(false);
            mvtButton.setSelected(false);
            recordButton.setSelected(true);
            changeFragment(id, new MacroRecordFragment());

        } else if (id == R.id.screen_toggle) {

            recordButton.setSelected(false);
            mvtButton.setSelected(false);
            screenButton.setSelected(true);
            changeFragment(id, new MacroExtrasFragment());
        }
    }

    private void changeFragment(int screen, Fragment frag) {
        if (lastFragment != screen) {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.actionFragmentContainer, frag)
                    .commit();
            lastFragment = screen;
        }
    }
}