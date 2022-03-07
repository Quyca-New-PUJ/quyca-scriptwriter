package com.quyca.scriptwriter.ui.execplay;

import android.graphics.Color;
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

import java.util.List;

/**
 * The type Exec script line adapter.
 */
public class ExecPlayLineAdapter extends RecyclerView.Adapter<ExecPlayLineAdapter.ExecPlayLineViewHolder>{

    private final List<Macro> lines;

    /**
     * The type Exec script line view holder.
     */
    public static class ExecPlayLineViewHolder extends RecyclerView.ViewHolder
    {
        /**
         * The Script card.
         */
        CardView scriptCard;
        /**
         * The Action.
         */
        TextView characName;
        /**
         * The Emotion.
         */
        TextView macroName;

        /**
         * Instantiates a new Exec script line view holder.
         *
         * @param v the v
         */
        public ExecPlayLineViewHolder(View v )
        {
            super( v );
            scriptCard = v.findViewById( R.id.scriptCard );
            characName=  v.findViewById( R.id.character );
            macroName=  v.findViewById( R.id.macro );
        }

    }

    /**
     * Instantiates a new Exec script line adapter.
     *
     * @param lines the lines
     */
    public ExecPlayLineAdapter(List<Macro> lines)
    {
        this.lines = lines;
    }


    @NonNull
    @Override
    public ExecPlayLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from( parent.getContext() ).inflate( R.layout.execplayline_cardview, parent, false );
        return new ExecPlayLineViewHolder( v );
    }

    @Override
    public void onBindViewHolder(@NonNull final ExecPlayLineViewHolder holder, int position) {
        Macro play = lines.get(position);
        holder.scriptCard.getBackground().setTint(Color.parseColor(play.getCharColor()));
        holder.characName.setText( play.getCharName());
        holder.macroName.setText(play.getMacroName());

        QuycaCommandState state = play.getDone();
        switch(state){
            case IN_EXECUTION:
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
