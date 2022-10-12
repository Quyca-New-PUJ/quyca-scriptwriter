package com.quyca.scriptwriter.ui.touchhelper;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * The type Item move callback.
 *
 * @param <T> the type parameter
 */
public class ItemMoveCallback<T extends RecyclerView.ViewHolder> extends ItemTouchHelper.Callback {

    private final ItemTouchHelperContract<T> mAdapter;

    /**
     * Instantiates a new Item move callback.
     *
     * @param adapter the adapter
     */
    public ItemMoveCallback(ItemTouchHelperContract<T> adapter) {
        mAdapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }


    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        return makeMovementFlags(dragFlags, 0);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        mAdapter.onRowMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder,
                                  int actionState) {


        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            mAdapter.onRowSelected((T) viewHolder);
        }

        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView,
                          @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        mAdapter.onRowClear((T) viewHolder);
    }

    /**
     * The interface Item touch helper contract.
     *
     * @param <T> the type parameter
     */
    public interface ItemTouchHelperContract<T extends RecyclerView.ViewHolder> {

        /**
         * On row moved.
         *
         * @param fromPosition the from position
         * @param toPosition   the to position
         */
        void onRowMoved(int fromPosition, int toPosition);

        /**
         * On row selected.
         *
         * @param myViewHolder the my view holder
         */
        void onRowSelected(T myViewHolder);

        /**
         * On row clear.
         *
         * @param myViewHolder the my view holder
         */
        void onRowClear(T myViewHolder);

    }

}

