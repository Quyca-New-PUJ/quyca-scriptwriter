package com.quyca.scriptwriter.ui.playeditor;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.quyca.scriptwriter.R;
import com.quyca.scriptwriter.model.Macro;
import com.quyca.scriptwriter.utils.PlayableBundle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.crypto.Mac;

/**
 * The type Script line adapter.
 */
public class PlayLineSelectorAdapter extends RecyclerView.Adapter<PlayLineSelectorAdapter.ScriptLineViewHolder>
{

    private final List<PlayableBundle> lines;
    private final PlayEditorViewModel localModel;

    public PlayLineSelectorAdapter(List<PlayableBundle> scene, PlayEditorViewModel localModel) {
        this.lines = scene;
        this.localModel = localModel;
    }


    @NonNull
    @Override
    public ScriptLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.play_editor_cardview, parent, false);
        return new ScriptLineViewHolder(v);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final ScriptLineViewHolder holder, int position) {
        Macro line = (Macro) lines.get(position).getPlayable();
        holder.playable = lines.get(position);
        int color = Color.parseColor(line.getCharColor());
        holder.scriptCard.setCardBackgroundColor(color);
        holder.charName.setText(line.getCharName());
        holder.action.setText(line.getName());
        holder.selected.setEnabled(holder.playable.isSelectable());
        holder.selected.setOnCheckedChangeListener((buttonView, isChecked) -> {
            lines.get(holder.getAdapterPosition()).setSelected(isChecked);
            setCheckStatusByCondition(isChecked, lines.get(holder.getAdapterPosition()));
            localModel.setSelected(getSelected());
        });
    }

    private void setCheckStatusByCondition(boolean isChecked, PlayableBundle playableBundle) {
        Macro m = (Macro) playableBundle.getPlayable();
        Set<String> setM = m.getResources();
        for (PlayableBundle playableBundle1 : lines) {
            Macro m1 = (Macro) playableBundle1.getPlayable();
            Set<String> setM1 = new HashSet<>(m1.getResources());
            setM1.retainAll(setM);
            if (!playableBundle.equals(playableBundle1) && !setM1.isEmpty()) {
                playableBundle1.setSelectable(!isChecked);
            }
        }
        notifyDataSetChanged();
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
        PlayableBundle playable;
        TextView action;
        TextView charName;
        TextView actionLabel;
        CheckBox selected;

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
            selected = v.findViewById(R.id.selected);
        }


    }

    public List<PlayableBundle> getSelected() {
        List<PlayableBundle> bundles = new ArrayList<>();
        lines.forEach(playableBundle -> {
           if(playableBundle.isSelected()){
               bundles.add(playableBundle);
           }
        });
        return bundles;
    }
}
