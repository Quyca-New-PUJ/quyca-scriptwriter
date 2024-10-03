package com.quyca.scriptwriter.utils;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.quyca.scriptwriter.R;
import com.quyca.scriptwriter.model.QuycaCommandState;

/**
 * The UIUtils class provides utility methods related to the user interface.
 * Specifically, it includes methods to change the color of UI components based on certain conditions.
 * In this case, it modifies the background color of a RecyclerView item depending on its execution state.
 */
public class UIUtils {

    /**
     * Changes the background color of a RecyclerView item based on the command state.
     * Different colors represent different states of execution (e.g., To Execute, In Execution, Done, or Error).
     *
     * @param holder The ViewHolder of the RecyclerView item whose background color will be changed.
     * @param state  The state of the Quyca command, which determines the color to be applied.
     */
    public static void changeQuycaHolderColor(@NonNull final RecyclerView.ViewHolder holder, QuycaCommandState state) {
        switch (state) {
            case TO_EXECUTE:
                // Change the background tint to the "to_execute" color when the command is yet to be executed
                holder.itemView.getBackground().setTint(ContextCompat.getColor(holder.itemView.getContext(), R.color.to_execute));
            case IN_EXECUTION:
                // Change the background tint to the "in_execution" color when the command is in progress
                holder.itemView.getBackground().setTint(ContextCompat.getColor(holder.itemView.getContext(), R.color.in_execution));
                break;
            case DONE:
                // Change the background tint to the "done" color when the command has been executed successfully
                holder.itemView.getBackground().setTint(ContextCompat.getColor(holder.itemView.getContext(), R.color.done));
                break;
            case ERROR:
                // Change the background tint to the "error" color when there is an error in executing the command
                holder.itemView.getBackground().setTint(ContextCompat.getColor(holder.itemView.getContext(), R.color.error));
                break;
        }
    }
}
