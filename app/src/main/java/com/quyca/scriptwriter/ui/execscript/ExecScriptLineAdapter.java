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
import com.quyca.scriptwriter.model.Playable;
import com.quyca.scriptwriter.model.QuycaCommandState;
import com.quyca.scriptwriter.model.Macro;
import com.quyca.scriptwriter.model.SoundAction;

import org.w3c.dom.Text;

import java.util.List;

/**
 * The type Exec script line adapter.
 */
public class ExecScriptLineAdapter extends RecyclerView.Adapter<ExecScriptLineAdapter.ExecScriptLineViewHolder>{

    private final List<Playable> lines;

    /**
     * The type Exec script line view holder.
     */
    public static class ExecScriptLineViewHolder extends RecyclerView.ViewHolder
    {
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
        public ExecScriptLineViewHolder(View v )
        {
            super( v );
            scriptCard = v.findViewById( R.id.scriptCard );
            emotionLabel = v.findViewById(R.id.emotion_label);
            action = v.findViewById( R.id.action );
            emotion = v.findViewById( R.id.emotion );
        }

    }

    /**
     * Instantiates a new Exec script line adapter.
     *
     * @param lines the lines
     */
    public ExecScriptLineAdapter(List<Playable> lines)
    {
        this.lines = lines;
    }


    @NonNull
    @Override
    public ExecScriptLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from( parent.getContext() ).inflate( R.layout.execscriptline_cardview, parent, false );
        return new ExecScriptLineViewHolder( v );
    }

    @Override
    public void onBindViewHolder(@NonNull final ExecScriptLineViewHolder holder, int position) {
        Playable play = lines.get(position);
        if(play instanceof Action){
            if(play instanceof SoundAction){
                SoundAction line = (SoundAction) lines.get(position);
                holder.action.setText(line.getNameWithoutPrefix());
                holder.emotionLabel.setVisibility(View.INVISIBLE);
                holder.emotion.setVisibility(View.INVISIBLE);
            }else{
                Action line = (Action) lines.get(position);
                holder.action.setText( line.getAction().getActionName());
                if(line.isExtra()){
                    holder.emotionLabel.setVisibility(View.INVISIBLE);
                    holder.emotion.setVisibility(View.INVISIBLE);
                }else{
                    holder.emotion.setText( line.getEmotion().getEmotionId().name());
                }
            }
        }
        QuycaCommandState state = play.getDone();
        switch(state){
            case IN_EXECUTION:
                ContextCompat.getColor(holder.itemView.getContext(), R.color.teal_200);
                holder.itemView.getBackground().setTint(ContextCompat.getColor(holder.itemView.getContext(), R.color.teal_200));
                break;
            case DONE:
                holder.itemView.getBackground().setTint(ContextCompat.getColor(holder.itemView.getContext(), R.color.teal_700));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return lines.size();
    }

}
