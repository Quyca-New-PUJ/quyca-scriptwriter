package com.quyca.scriptwriter.ui.setup;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.quyca.scriptwriter.MainActivity;
import com.quyca.scriptwriter.R;
import com.quyca.scriptwriter.model.Play;
import com.quyca.scriptwriter.model.PlayCharacter;
import com.quyca.scriptwriter.model.Playable;
import com.quyca.scriptwriter.model.Script;

import java.util.List;


public class CharacterAdapter extends RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder> {

    private final List<PlayCharacter> chars;
    private final Play play;

    public static class CharacterViewHolder extends RecyclerView.ViewHolder {

        CardView scriptCard;

        TextView charName;

        ImageButton go;

        ImageView image;

        public CharacterViewHolder(View v) {
            super(v);
            scriptCard = v.findViewById(R.id.scriptCard);
            charName = v.findViewById(R.id.char_name);
            go = v.findViewById(R.id.go_button);
            image = v.findViewById(R.id.char_view);
        }

    }

    public CharacterAdapter(Play play) {
        this.play=play;
        this.chars = play.getCharacters();
    }


    @NonNull
    @Override
    public CharacterAdapter.CharacterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.character_cardview, parent, false);
        return new CharacterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final CharacterViewHolder holder, int position) {
        PlayCharacter charac = chars.get(holder.getAdapterPosition());
        int color = Color.parseColor(charac.getColor());
        holder.scriptCard.setCardBackgroundColor(color);
        holder.charName.setText(String.valueOf(charac.getName()));
            holder.image.setImageURI(charac.getImageUri());
            holder.scriptCard.setOnClickListener(v -> {
                Intent i = new Intent(holder.itemView.getContext(), MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.putExtra("play",play);
                i.putExtra("pos",holder.getAdapterPosition());
                holder.itemView.getContext().startActivity(i);
            });


            holder.go.setOnClickListener(v -> {
                Intent i = new Intent(holder.itemView.getContext(), MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.putExtra("play",play);
                i.putExtra("pos",holder.getAdapterPosition());
                holder.itemView.getContext().startActivity(i);
            });
    }

    @Override
    public int getItemCount() {
        return chars.size();
    }

}
