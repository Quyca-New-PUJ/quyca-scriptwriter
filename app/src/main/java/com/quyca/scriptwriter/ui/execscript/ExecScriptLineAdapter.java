package com.quyca.scriptwriter.ui.execscript;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.quyca.scriptwriter.R;
import com.quyca.scriptwriter.model.Action;
import com.quyca.scriptwriter.model.Macro;
import com.quyca.scriptwriter.model.Playable;
import com.quyca.scriptwriter.model.QuycaCommandState;
import com.quyca.scriptwriter.model.SoundAction;
import com.quyca.scriptwriter.utils.UIUtils;

/**
 * The type Exec script line adapter.
 */
public class ExecScriptLineAdapter extends RecyclerView.Adapter<ExecScriptLineAdapter.ExecScriptLineViewHolder> {

    private final Macro lines;

    /**
     * Instantiates a new Exec script line adapter.
     *
     * @param lines the lines
     */
    public ExecScriptLineAdapter(Macro lines) {
        this.lines = lines;
    }

    @NonNull
    @Override
    public ExecScriptLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.execscriptline_cardview, parent, false);
        return new ExecScriptLineViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ExecScriptLineViewHolder holder, int position) {
        Playable play = lines.getPlayables().get(position);
        if (play instanceof Action) {
            if (play instanceof SoundAction) {
                SoundAction line = (SoundAction) lines.getPlayables().get(position);
                holder.action.setText(line.getNameWithoutPrefix());
                holder.emotionLabel.setVisibility(View.INVISIBLE);
                holder.emotion.setVisibility(View.INVISIBLE);
            } else {
                Action line = (Action) lines.getPlayables().get(position);
                holder.action.setText(line.getAction().getActionName());
                if (line.isExtra()) {
                    holder.emotionLabel.setVisibility(View.INVISIBLE);
                    holder.emotion.setVisibility(View.INVISIBLE);
                } else {
                    holder.emotion.setText(line.getEmotion().getEmotionId().name());
                }
            }
        }
        QuycaCommandState state = play.getDone();
        UIUtils.changeQuycaHolderColor(holder,state);
    }

    @Override
    public int getItemCount() {
        return lines.getPlayables().size();
    }

    /**
     * The type Exec script line view holder.
     */
    public static class ExecScriptLineViewHolder extends RecyclerView.ViewHolder {
        private final TextView emotionLabel;
        /**
         * The Script card.
         */
        CardView scriptCard;
        /**
         * The Action.
         */
        TextView action;
        /**
         * The Emotion.
         */
        TextView emotion;

        /**
         * Instantiates a new Exec script line view holder.
         *
         * @param v the v
         */
        public ExecScriptLineViewHolder(View v) {
            super(v);
            scriptCard = v.findViewById(R.id.scriptCard);
            emotionLabel = v.findViewById(R.id.emotion_label);
            action = v.findViewById(R.id.action);
            emotion = v.findViewById(R.id.emotion);
        }

    }

}
