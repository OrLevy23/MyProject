package orlevy.com.myproject.Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import orlevy.com.myproject.R;

/**
 * Created by Or.levy on 13/09/2016.
 */

public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final ItemTouchHelperAdapter mAdapter;


    public SimpleItemTouchHelperCallback(ItemTouchHelperAdapter adapter) {
        mAdapter = adapter;
    }




    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }


    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            // Get RecyclerView item from the ViewHolder
            View itemView = viewHolder.itemView;
            float height = (float) itemView.getBottom() - (float) itemView.getTop();
            float width = height / 3;

            Paint p = new Paint();
            if (dX > 0) {
                p.setColor(Color.parseColor("#D32F2F"));
                // Draw Rect with varying right side, equal to displacement dX
                RectF background  = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                c.drawRect(background,p);
                Bitmap icon = BitmapFactory.decodeResource(itemView.getContext().getResources(), R.drawable.ic_delete_sweep_white_48dp);
//                RectF icon_dest = new RectF((float) itemView.getLeft() + 2*width ,(float) itemView.getTop() + width,(float) itemView.getLeft() - width,(float)itemView.getBottom() - width);
                RectF icon_dest = new RectF((float) itemView.getLeft() - 2*width ,(float) itemView.getTop() - width,(float) itemView.getLeft() - width,(float)itemView.getBottom() - width);
                c.drawBitmap(icon,null,icon_dest,p);


            } else {
//                Bitmap icon = BitmapFactory.decodeResource(itemView.getContext().getResources(), R.drawable.ic_delete);
                // Draw Rect with varying left side, equal to the item's right side plus negative displacement dX
                p.setColor(Color.parseColor("#D32F2F"));
                RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                c.drawRect(background,p);
                Bitmap icon = BitmapFactory.decodeResource(itemView.getContext().getResources(), R.drawable.ic_delete_sweep_white_48dp);
                RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                c.drawBitmap(icon,null,icon_dest,p);
            }

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }

}