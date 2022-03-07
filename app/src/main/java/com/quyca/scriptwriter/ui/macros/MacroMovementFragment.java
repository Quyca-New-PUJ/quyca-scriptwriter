package com.quyca.scriptwriter.ui.macros;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.quyca.scriptwriter.R;
import com.quyca.scriptwriter.config.ConfiguredAction;
import com.quyca.scriptwriter.config.ConfiguredEmotion;
import com.quyca.scriptwriter.config.FixedConfiguredAction;
import com.quyca.scriptwriter.config.FixedConfiguredEmotion;
import com.quyca.scriptwriter.config.QuycaConfiguration;
import com.quyca.scriptwriter.databinding.FragmentMacroMovementBinding;
import com.quyca.scriptwriter.model.Action;
import com.quyca.scriptwriter.model.Macro;
import com.quyca.scriptwriter.model.PlayCharacter;
import com.quyca.scriptwriter.model.Script;
import com.quyca.scriptwriter.ui.shared.SharedViewModel;

import java.util.ArrayList;
import java.util.List;


public class MacroMovementFragment extends Fragment {
    private SharedViewModel model;
    private FragmentMacroMovementBinding binding;
    private Script actScript;
    private PlayCharacter character;
    private QuycaConfiguration conf;
    private Button save;
    private ImageButton arriba;
    private ImageButton abajo;
    private ImageButton izq;
    private ImageButton der;
    private ImageButton centro;
    private ImageButton muyFeliz;
    private ImageButton feliz;
    private ImageButton triste;
    private ImageButton muyTriste;
    private ImageButton neutro;
    private ImageButton furioso;
    private ImageButton sorprendido;
    private ImageButton enfermo;
    private ConfiguredAction currentAction = null;
    private ConfiguredEmotion currentEmotion = null;
    private List<ImageButton> actionButtons;
    private List<ImageButton> emoButtons;
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
        binding = FragmentMacroMovementBinding.inflate(inflater, container, false);
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

            } else if (currentEmotion == null) {
                Toast.makeText(requireContext(), "Selecciona una emocion", Toast.LENGTH_SHORT).show();

            } else {
                Action newAction = new Action(currentEmotion, currentAction,false);
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

        arriba = root.findViewById(R.id.arriba);
        abajo = root.findViewById(R.id.abajo);
        izq = root.findViewById(R.id.izq);
        der = root.findViewById(R.id.der);
        centro = root.findViewById(R.id.centro);

        arriba.setOnClickListener(this::onClickActions);
        abajo.setOnClickListener(this::onClickActions);
        izq.setOnClickListener(this::onClickActions);
        der.setOnClickListener(this::onClickActions);
        centro.setOnClickListener(this::onClickActions);


        muyFeliz = root.findViewById(R.id.muy_feliz);
        feliz = root.findViewById(R.id.feliz);
        triste = root.findViewById(R.id.triste);
        muyTriste = root.findViewById(R.id.muy_triste);
        neutro = root.findViewById(R.id.neutro);
        furioso = root.findViewById(R.id.furioso);
        sorprendido = root.findViewById(R.id.sorprendido);
        enfermo = root.findViewById(R.id.enfermo);

        muyFeliz.setOnClickListener(this::onClickEmotions);
        feliz.setOnClickListener(this::onClickEmotions);
        triste.setOnClickListener(this::onClickEmotions);
        muyTriste.setOnClickListener(this::onClickEmotions);
        neutro.setOnClickListener(this::onClickEmotions);
        furioso.setOnClickListener(this::onClickEmotions);
        sorprendido.setOnClickListener(this::onClickEmotions);
        enfermo.setOnClickListener(this::onClickEmotions);


        model.getCharacterObservable().observe(getViewLifecycleOwner(), playCharacter -> {
            character = playCharacter;
            conf = character.getConf();
            setUpLayout();
        });

        return root;
    }

    private void resetButtons() {
        /*centro.setChecked(false);
        arriba.setChecked(false);
        abajo.setChecked(false);
        izq.setChecked(false);
        der.setChecked(false);*/
        centro.setSelected(false);
        arriba.setSelected(false);
        abajo.setSelected(false);
        izq.setSelected(false);
        der.setSelected(false);
        sorprendido.setSelected(false);
        muyTriste.setSelected(false);
        muyFeliz.setSelected(false);
        furioso.setSelected(false);
        enfermo.setSelected(false);
        triste.setSelected(false);
        neutro.setSelected(false);
        feliz.setSelected(false);
        currentAction=null;
        currentEmotion=null;
    }

    private void setUpLayout() {

        actionButtons=new ArrayList<>();
        actionButtons.add(centro);
        actionButtons.add(arriba);
        actionButtons.add(abajo);
        actionButtons.add(izq);
        actionButtons.add(der);

        centro.setTag(conf.getActionsFromId(FixedConfiguredAction.CAMBIAR_PANTALLA.name()));
        arriba.setTag(conf.getActionsFromId(FixedConfiguredAction.HACIA_CRUCE.name()));
        abajo.setTag(conf.getActionsFromId(FixedConfiguredAction.REVERSA.name()));
        izq.setTag(conf.getActionsFromId(FixedConfiguredAction.IZQUIERDA.name()));
        der.setTag(conf.getActionsFromId(FixedConfiguredAction.DERECHA.name()));

        emoButtons=new ArrayList<>();

        emoButtons.add(sorprendido);
        emoButtons.add(muyTriste);
        emoButtons.add(muyFeliz);
        emoButtons.add(furioso);
        emoButtons.add(enfermo);
        emoButtons.add(triste);
        emoButtons.add(neutro);
        emoButtons.add(feliz);

        sorprendido.setTag(conf.getEmotionsFromId(FixedConfiguredEmotion.SORPRENDIDO));
        muyTriste.setTag(conf.getEmotionsFromId(FixedConfiguredEmotion.MUY_TRISTE));
        muyFeliz.setTag(conf.getEmotionsFromId(FixedConfiguredEmotion.MUY_FELIZ));
        furioso.setTag(conf.getEmotionsFromId(FixedConfiguredEmotion.FURIOSO));
        enfermo.setTag(conf.getEmotionsFromId(FixedConfiguredEmotion.ENFERMO));
        triste.setTag(conf.getEmotionsFromId(FixedConfiguredEmotion.TRISTE));
        neutro.setTag(conf.getEmotionsFromId(FixedConfiguredEmotion.NEUTRO));
        feliz.setTag(conf.getEmotionsFromId(FixedConfiguredEmotion.FELIZ));


        if(toEdit!=-1){
            Action act;
            if(selMacro==null){
                act= (Action) actScript.getLines().get(toEdit);
            }else{
                act = (Action) selMacro.getActions().get(toEdit);
            }

            actionButtons.forEach(radioButton -> {
                ConfiguredAction tag= (ConfiguredAction) radioButton.getTag();
                if (tag.getActionId().equalsIgnoreCase(act.getAction().getActionId())) {
                    currentAction = tag;
                    radioButton.setSelected(true);
                }
            });

            emoButtons.forEach(radioButton -> {
                ConfiguredEmotion tag= (ConfiguredEmotion) radioButton.getTag();
                if (tag.getEmotionId().equals(act.getEmotion().getEmotionId())) {
                    currentEmotion = tag;
                    radioButton.setSelected(true);
                }
            });
        }
    }

    private void onClickActions(@NonNull View v) {
        int id = v.getId();
        actionButtons.forEach(radioButton -> {
            if (radioButton.getId() == id) {
                currentAction = (ConfiguredAction) radioButton.getTag();
                radioButton.setSelected(true);
            } else {
                radioButton.setSelected(false);
            }
        });
    }


    private void onClickEmotions(@NonNull View v) {
        int id = v.getId();
        emoButtons.forEach(radioButton -> {
            if (radioButton.getId() == id) {
                currentEmotion = (ConfiguredEmotion) radioButton.getTag();
                radioButton.setSelected(true);
            } else {
                radioButton.setSelected(false);
            }
        });
    }
}