package com.quyca.scriptwriter.utils;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.quyca.scriptwriter.R;
import com.quyca.scriptwriter.model.QuycaCommandState;

public class UIUtils {

public static void changeQuycaHolderColor(@NonNull final RecyclerView.ViewHolder holder, QuycaCommandState state){
    switch (state) {
        case TO_EXECUTE:
            holder.itemView.getBackground().setTint(ContextCompat.getColor(holder.itemView.getContext(), R.color.to_execute));
        case IN_EXECUTION:
            holder.itemView.getBackground().setTint(ContextCompat.getColor(holder.itemView.getContext(), R.color.in_execution));
            break;
        case DONE:
            holder.itemView.getBackground().setTint(ContextCompat.getColor(holder.itemView.getContext(), R.color.done));
            break;
        case ERROR:
            holder.itemView.getBackground().setTint(ContextCompat.getColor(holder.itemView.getContext(), R.color.error));
            break;
    }
}
}


