package com.quyca.scriptwriter.ui.playeditor;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.quyca.scriptwriter.R;
import com.quyca.scriptwriter.model.Macro;
import com.quyca.scriptwriter.model.Playable;
import com.quyca.scriptwriter.model.Scene;
import com.quyca.scriptwriter.ui.touchhelper.ItemMoveCallback;

import java.util.Collections;
import java.util.List;

/**
 * The type Script line adapter.
 */
public class PlayLineAdapter extends RecyclerView.Adapter<PlayLineAdapter.ScriptLineViewHolder>
        implements ItemMoveCallback.ItemTouchHelperContract<PlayLineAdapter.ScriptLineViewHolder> {

    private final List<Playable> lines;
    private ActivityResultLauncher<String> requestRemoveLauncher;
    private int oldColor;
    private final PlayViewerFragment mStartDragListener;
    private int toDelete;

    /**
     * Instantiates a new Play line adapter.
     *
     * @param scene              the scene
     * @param playViewerFragment the play viewer fragment
     */
    public PlayLineAdapter(Scene scene, PlayViewerFragment playViewerFragment) {
        this.lines = scene.getPlayables();
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
        Macro line = (Macro) lines.get(position);
        int color = Color.parseColor(line.getCharColor());
        holder.scriptCard.setCardBackgroundColor(color);
        holder.charName.setText(line.getCharName());
        holder.action.setText(line.getName());
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

        /**
         * The Script card.
         */
        CardView scriptCard;

        /**
         * The Action.
         */
        TextView action;
        /**
         * The Char name.
         */
        TextView charName;
        /**
         * The Action label.
         */
        TextView actionLabel;
        /**
         * The Drag holder.
         */
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
