package com.quyca.scriptwriter.ui.scriptviewer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.quyca.scriptwriter.R;
import com.quyca.scriptwriter.model.Macro;
import com.quyca.scriptwriter.model.Playable;
import com.quyca.scriptwriter.model.Scene;
import com.quyca.scriptwriter.ui.touchhelper.ItemMoveCallback;
import com.quyca.scriptwriter.utils.FileRepository;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * The type Script line adapter.
 */
public class SceneMacroAdapter extends RecyclerView.Adapter<SceneMacroAdapter.ScriptLineViewHolder>
        implements ItemMoveCallback.ItemTouchHelperContract<SceneMacroAdapter.ScriptLineViewHolder> {

    private final Scene scene;
    private final List<Playable> lines;
    private ActivityResultLauncher<String> requestRemoveLauncher;
    private int oldColor;
    private ScriptViewerFragment mStartDragListener;
    private int toDelete;

    public SceneMacroAdapter(Scene scene, ScriptViewerFragment scriptViewerFragment, ActivityResultLauncher<String> requestRemoveLauncher) {
        this.scene = scene;
        this.lines = scene.getPlayables();
        this.mStartDragListener = scriptViewerFragment;
    }

    @Override
    public void onRowMoved(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(lines, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(lines, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onRowSelected(ScriptLineViewHolder myViewHolder) {
        myViewHolder.scriptCard.setBackgroundColor(Color.BLUE);
    }

    @Override
    public void onRowClear(ScriptLineViewHolder myViewHolder) {
        myViewHolder.scriptCard.setBackgroundColor(oldColor);

    }

    public void deleteMacro() throws IOException {
        Macro macro = (Macro) lines.get(toDelete);
        DocumentFile sceneDir = FileRepository.getSceneDir();
        assert sceneDir != null;
        DocumentFile macrosDir = sceneDir.findFile(mStartDragListener.getResources().getString(R.string.macro_dir));
        if (macrosDir != null && macrosDir.exists()) {
            DocumentFile macroDir = macrosDir.findFile(macro.getName());
            assert macroDir != null;
            boolean del = macroDir.delete();
            if (!del) {
                throw new IOException("No se pudo borrar el directorio del macro");
            }
        }
    }

    private void startMacroDeleteRequest(SceneMacroAdapter.ScriptLineViewHolder holder) throws IOException {
        if (ContextCompat.checkSelfPermission(holder.itemView.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            deleteMacro();
        } else if (mStartDragListener.shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(holder.itemView.getContext(), R.string.permission_read_script, Toast.LENGTH_LONG).show();
            requestRemoveLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        } else {
            requestRemoveLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    @NonNull
    @Override
    public SceneMacroAdapter.ScriptLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.scenemacro_cardview, parent, false);
        ScriptLineViewHolder card = new ScriptLineViewHolder(v);
        oldColor = card.scriptCard.getCardBackgroundColor().getDefaultColor();
        return card;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final ScriptLineViewHolder holder, int position) {

        Macro line = (Macro)lines.get(position);
        holder.dragHolder.setOnTouchListener((v, event) -> {
            if (event.getAction() ==
                    MotionEvent.ACTION_DOWN) {
                mStartDragListener.requestDrag(holder);
            }
            return false;
        });

        holder.playButton.setOnClickListener(v -> {
            FileRepository.setCurrentMacroName(line.getName());
            mStartDragListener.getExecViewModel().setToDoActionsObservable(line);
            Navigation.findNavController(v).navigate(R.id.navigation_execscript);
        });

        holder.action.setText(line.getName());
        holder.editButton.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("macroPos", holder.getAdapterPosition());
            Navigation.findNavController(v).navigate(R.id.navigation_script_edit, bundle);
        });
        holder.delete.setOnClickListener(v -> {
            toDelete = holder.getAdapterPosition();
            try {
                startMacroDeleteRequest(holder);
            } catch (IOException e) {
                e.printStackTrace();
            }
            notifyItemRangeChanged(holder.getAdapterPosition(), lines.size());
            lines.remove(holder.getAdapterPosition());
            notifyItemRemoved(holder.getAdapterPosition());
        });

    }

    @Override
    public int getItemCount() {
        return lines.size();
    }

    /**
     * The type Script line view holder.
     */
    public static class ScriptLineViewHolder extends RecyclerView.ViewHolder {

        CardView scriptCard;

        TextView action;

        ImageButton delete;
        ImageButton editButton;
        ImageButton playButton;
        ImageButton dragHolder;
        TextView actionLabel;

        /**
         * Instantiates a new Script line view holder.
         *
         * @param v the v
         */
        public ScriptLineViewHolder(View v) {
            super(v);
            scriptCard = v.findViewById(R.id.scriptCard);
            action = v.findViewById(R.id.action);
            delete = v.findViewById(R.id.delete_button);
            editButton = v.findViewById(R.id.edit_button);
            playButton = v.findViewById(R.id.view_button);
            actionLabel = v.findViewById(R.id.action_label);
            dragHolder = v.findViewById(R.id.drag_button);
        }

    }

}
