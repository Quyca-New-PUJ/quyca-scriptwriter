package com.quyca.scriptwriter.ui.scripteditor;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
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
import com.quyca.scriptwriter.config.FixedConfiguredAction;
import com.quyca.scriptwriter.model.Action;
import com.quyca.scriptwriter.model.Macro;
import com.quyca.scriptwriter.model.Playable;
import com.quyca.scriptwriter.model.SoundAction;
import com.quyca.scriptwriter.ui.touchhelper.ItemMoveCallback;
import com.quyca.scriptwriter.utils.AudioRepository;
import com.quyca.scriptwriter.utils.FileRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The type Script line adapter.
 */
public class MacroActionAdapter extends RecyclerView.Adapter<MacroActionAdapter.ScriptLineViewHolder>
        implements ItemMoveCallback.ItemTouchHelperContract<MacroActionAdapter.ScriptLineViewHolder> {

    private final List<Playable> lines;
    private boolean saved;
    private int oldColor;
    private ScriptEditorFragment mStartDragListener;
    private ActivityResultLauncher<String> requestRemoveLauncher;
    private int toDelete;


    public MacroActionAdapter(List<Playable> lines, ScriptEditorFragment scriptEditorFragment,
                              ActivityResultLauncher<String> requestRemoveLauncher, boolean saved) {
        this.lines = lines;
        this.saved = saved;
        mStartDragListener = scriptEditorFragment;
        this.requestRemoveLauncher = requestRemoveLauncher;
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
    public void onRowSelected(MacroActionAdapter.ScriptLineViewHolder myViewHolder) {
    }

    @Override
    public void onRowClear(MacroActionAdapter.ScriptLineViewHolder myViewHolder) {
        setColor(myViewHolder);
    }

    @NonNull
    @Override
    public MacroActionAdapter.ScriptLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.macroaction_cardview, parent, false);
        return new ScriptLineViewHolder(v);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final ScriptLineViewHolder holder, int position) {
        holder.playable = lines.get(position);
        Action play = (Action) lines.get(position);
        setColor(holder);

        holder.dragHolder.setOnTouchListener((v, event) -> {
            if (event.getAction() ==
                    MotionEvent.ACTION_DOWN) {
                mStartDragListener.requestDrag(holder);
            }
            return false;
        });

        holder.playButton.setOnClickListener(v -> {
            Macro toPlay = new Macro(new ArrayList<>());
            toPlay.getPlayables().add(play);
            mStartDragListener.getExecViewModel().setToDoActionsObservable(toPlay);
            Navigation.findNavController(v).navigate(R.id.navigation_execscript);
        });

        if (lines.size() < 2) {
            holder.delete.setEnabled(false);
        }
        if (play != null) {

            if (play instanceof SoundAction) {
                SoundAction sa = (SoundAction) play;
                holder.editButton.setEnabled(false);
                holder.action.setText(sa.getNameWithoutPrefix());
                holder.emotionLabel.setVisibility(View.INVISIBLE);
                holder.emotion.setVisibility(View.INVISIBLE);
                holder.delete.setOnClickListener(v -> {
                    toDelete = holder.getAdapterPosition();
                    try {
                        startSoundDeleteRequest(holder);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    notifyItemRangeChanged(holder.getAdapterPosition(), lines.size());
                    lines.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());

                });
            } else {
                holder.action.setText(play.getAction().getActionName());
                if (play.isExtra()) {
                    holder.emotionLabel.setVisibility(View.INVISIBLE);
                    holder.emotion.setVisibility(View.INVISIBLE);
                } else {
                    holder.emotion.setText(play.getEmotion().getEmotionId().name());
                }
                holder.editButton.setOnClickListener(v -> {
                    Bundle bundle = new Bundle();
                    bundle.putInt("toEdit", holder.getAdapterPosition());
                    if (play.isExtra()) {
                        Navigation.findNavController(v).navigate(R.id.navigation_macro_extra, bundle);
                    } else {

                        Navigation.findNavController(v).navigate(R.id.navigation_macro_movement, bundle);
                    }
                });

                holder.delete.setOnClickListener(v -> {
                    notifyItemRangeChanged(holder.getAdapterPosition(), lines.size());
                    lines.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());

                });
            }


        }

    }

    private void setColor(ScriptLineViewHolder holder) {
        if (holder.playable instanceof SoundAction) {
            oldColor = ContextCompat.getColor(mStartDragListener.requireContext(), R.color.audio);
        } else if (holder.playable instanceof Action) {
            Action a = (Action) holder.playable;
            if (!a.getAction().getActionId().equalsIgnoreCase(FixedConfiguredAction.emotions.name())) {
                if (a.isExtra()) {
                    oldColor = ContextCompat.getColor(mStartDragListener.requireContext(), R.color.extras);
                } else {

                    oldColor = ContextCompat.getColor(mStartDragListener.requireContext(), R.color.action);
                }
            } else {
                oldColor = ContextCompat.getColor(mStartDragListener.requireContext(), R.color.emotion);
            }

        }
        holder.scriptCard.setCardBackgroundColor(oldColor);
    }

    private void startSoundDeleteRequest(ScriptLineViewHolder holder) throws IOException {
        if (ContextCompat.checkSelfPermission(holder.itemView.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            deleteSoundAction();
        } else if (mStartDragListener.shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(holder.itemView.getContext(), R.string.permission_read_script, Toast.LENGTH_LONG).show();
            requestRemoveLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        } else {
            requestRemoveLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    public void deleteSoundAction() throws IOException {
        DocumentFile audioFile = null;
        SoundAction sa = (SoundAction) lines.get(toDelete);
        if (saved) {
            DocumentFile macrosDir = FileRepository.getMacrosDir();
            if (macrosDir != null && macrosDir.exists()) {
                DocumentFile macroDir = macrosDir.findFile(FileRepository.getCurrentMacroName());
                assert macroDir != null;
                DocumentFile soundDir = macroDir.findFile(mStartDragListener.getResources().getString(R.string.sound_dir));
                assert soundDir != null;
                audioFile = soundDir.findFile(sa.getName());
            }
        } else {
            DocumentFile tempDir = FileRepository.getTempDir();
            assert tempDir != null;
            audioFile = tempDir.findFile(sa.getName());
        }
        AudioRepository.removeSound(sa);
        if (audioFile != null && audioFile.exists()) {
            boolean ret = audioFile.delete();
            if (!ret) {
                throw new IOException("Sound File couldnt be erased.");
            }
        }
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

        TextView emotion;

        ImageButton delete;
        ImageButton editButton;
        ImageButton playButton;
        ImageButton dragHolder;

        TextView actionLabel;
        TextView emotionLabel;
        Playable playable;

        /**
         * Instantiates a new Script line view holder.
         *
         * @param v the v
         */
        public ScriptLineViewHolder(View v) {
            super(v);
            scriptCard = v.findViewById(R.id.scriptCard);
            action = v.findViewById(R.id.action);
            emotion = v.findViewById(R.id.emotion);
            delete = v.findViewById(R.id.delete_button);
            editButton = v.findViewById(R.id.edit_button);
            playButton = v.findViewById(R.id.play_button);
            actionLabel = v.findViewById(R.id.action_label);
            emotionLabel = v.findViewById(R.id.emotion_label);
            dragHolder = v.findViewById(R.id.drag_button);

        }

    }

}
