package com.quyca.scriptwriter.ui.playeditor;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.quyca.scriptwriter.model.Scene;
import com.quyca.scriptwriter.ui.touchhelper.ItemMoveCallback;
import com.quyca.scriptwriter.utils.FileRepository;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * The type Script line adapter.
 */
public class PlayLineAdapter extends RecyclerView.Adapter<PlayLineAdapter.ScriptLineViewHolder>
        implements ItemMoveCallback.ItemTouchHelperContract<PlayLineAdapter.ScriptLineViewHolder> {

    private final Scene scene;
    private final List<Macro> lines;
    private ActivityResultLauncher<String> requestRemoveLauncher;
    private int oldColor;
    private PlayViewerFragment mStartDragListener;
    private int toDelete;

    public PlayLineAdapter(Scene scene, PlayViewerFragment playViewerFragment) {
        this.scene = scene;
        this.lines = scene.getMacros();
        this.mStartDragListener = playViewerFragment;
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
    }

    @Override
    public void onRowClear(ScriptLineViewHolder myViewHolder) {

    }

    @NonNull
    @Override
    public ScriptLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.play_cardview, parent, false);
        ScriptLineViewHolder card = new ScriptLineViewHolder(v);
        oldColor = card.scriptCard.getCardBackgroundColor().getDefaultColor();
        return card;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final ScriptLineViewHolder holder, int position) {
        Macro line = lines.get(position);
        int color = Color.parseColor(line.getCharColor());
        holder.scriptCard.setCardBackgroundColor(color);
        holder.charName.setText(line.getCharName());
        holder.action.setText(line.getMacroName());
        holder.dragHolder.setOnTouchListener((v, event) -> {
            if (event.getAction() ==
                    MotionEvent.ACTION_DOWN) {
                mStartDragListener.requestDrag(holder);
            }
            return false;
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
        TextView charName;
        TextView actionLabel;
        ImageButton dragHolder;

        /**
         * Instantiates a new Script line view holder.
         *
         * @param v the v
         */
        public ScriptLineViewHolder(View v) {
            super(v);
            scriptCard = v.findViewById(R.id.scriptCard);
            action = v.findViewById(R.id.action);
            charName = v.findViewById(R.id.char_name);
            actionLabel = v.findViewById(R.id.action_label);
            dragHolder = v.findViewById(R.id.drag_button);

        }


    }

}
