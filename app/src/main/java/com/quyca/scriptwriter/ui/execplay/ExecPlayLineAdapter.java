package com.quyca.scriptwriter.ui.execplay;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.quyca.scriptwriter.R;
import com.quyca.scriptwriter.model.Macro;
import com.quyca.scriptwriter.model.Playable;
import com.quyca.scriptwriter.model.QuycaCommandState;
import com.quyca.scriptwriter.utils.UIUtils;

import java.util.List;

/**
 * The type Exec script line adapter.
 */
public class ExecPlayLineAdapter extends RecyclerView.Adapter<ExecPlayLineAdapter.ExecPlayLineViewHolder> {

    private final List<Playable> lines;

    /**
     * Instantiates a new Exec script line adapter.
     *
     * @param lines the lines
     */
    public ExecPlayLineAdapter(List<Playable> lines) {
        this.lines = lines;
    }

    @NonNull
    @Override
    public ExecPlayLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.execplayline_cardview, parent, false);
        return new ExecPlayLineViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ExecPlayLineViewHolder holder, int position) {
        Macro play = (Macro) lines.get(position);
        holder.scriptCard.getBackground().setTint(Color.parseColor(play.getCharColor()));
        holder.characName.setText(play.getCharName());
        holder.macroName.setText(play.getName());

        QuycaCommandState state = play.getDone();
        UIUtils.changeQuycaHolderColor(holder,state);
    }

    @Override
    public int getItemCount() {
        return lines.size();
    }

    /**
     * The type Exec script line view holder.
     */
    public static class ExecPlayLineViewHolder extends RecyclerView.ViewHolder {
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
        public ExecPlayLineViewHolder(View v) {
            super(v);
            scriptCard = v.findViewById(R.id.scriptCard);
            characName = v.findViewById(R.id.character);
            macroName = v.findViewById(R.id.macro);
        }

    }

}
