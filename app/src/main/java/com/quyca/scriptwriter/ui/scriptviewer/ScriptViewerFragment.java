package com.quyca.scriptwriter.ui.scriptviewer;

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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.quyca.scriptwriter.MainActivity;
import com.quyca.scriptwriter.R;
import com.quyca.scriptwriter.config.QuycaConfiguration;
import com.quyca.scriptwriter.databinding.FragmentScriptViewerBinding;
import com.quyca.scriptwriter.model.Scene;
import com.quyca.scriptwriter.model.Script;
import com.quyca.scriptwriter.ui.execscript.ExecScriptViewModel;
import com.quyca.scriptwriter.ui.shared.SharedViewModel;
import com.quyca.scriptwriter.ui.touchhelper.ItemMoveCallback;
import com.quyca.scriptwriter.ui.touchhelper.StartDragListener;

import java.io.IOException;

/**
 * The type Act fragment.
 */

public class ScriptViewerFragment extends Fragment implements StartDragListener {

    private ScriptViewerViewModel actScriptViewModel;
    private FragmentScriptViewerBinding binding;
    private RecyclerView.LayoutManager manager;
    private Scene actScene;
    private SharedViewModel model;
    private Button createMacro;
    private QuycaConfiguration conf;
    private ItemTouchHelper touchHelper;
    private ActivityResultLauncher<String> requestRemoveLauncher;
    private SceneMacroAdapter slAdapter;
    private ExecScriptViewModel mViewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        actScriptViewModel = new ViewModelProvider(this).get(ScriptViewerViewModel.class);
        model = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        mViewModel = new ViewModelProvider(requireActivity()).get(ExecScriptViewModel.class);
        binding = FragmentScriptViewerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        requestRemoveLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        try {
                            slAdapter.deleteMacro();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(requireContext(), "Sin permisos es imposible grabar audio", Toast.LENGTH_SHORT).show();
                    }
                });
        model.getSceneObservable().observe(getViewLifecycleOwner(),scene -> {
            actScene= scene;
            startScriptView();
        });

        createMacro = root.findViewById(R.id.create_macro);

        createMacro.setOnClickListener(v -> {
            model.setScriptObservable(new Script());
            Navigation.findNavController(v).navigate(R.id.navigation_macro_home);
        });


        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity main= (MainActivity) requireActivity();
        main.setBackButtonText(requireContext().getResources().getString(R.string.regresar_main));
        main.setBackButtonEnabled(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    private void startScriptView() {
        manager = new LinearLayoutManager(getContext());
        binding.scriptlineView.setLayoutManager(manager);
        slAdapter = new SceneMacroAdapter(actScene,this,requestRemoveLauncher);
        ItemTouchHelper.Callback callback =
                new ItemMoveCallback<>(slAdapter);
        touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(binding.scriptlineView);
        binding.scriptlineView.setAdapter(slAdapter);

    }

    @Override
    public void requestDrag(RecyclerView.ViewHolder viewHolder) {
        touchHelper.startDrag(viewHolder);
    }

    public ExecScriptViewModel getExecViewModel(){
        return mViewModel;
    }

}