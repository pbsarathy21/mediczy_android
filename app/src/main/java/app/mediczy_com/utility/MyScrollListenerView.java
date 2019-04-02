package app.mediczy_com.utility;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Prithivi Raj on 15-12-2015.
 */
public abstract class MyScrollListenerView extends RecyclerView.OnScrollListener {

    private int toolbarOffset = 0;
    private int toolbarHeight;

    public MyScrollListenerView(Context context, float viewHeight) {
        int[] actionBarAttr = new int[] { android.R.attr.actionBarSize };
        TypedArray a = context.obtainStyledAttributes(actionBarAttr);
        toolbarHeight = (int) a.getDimension(0, 0) + 10;
     //   toolbarHeight = (int )viewHeight;
        a.recycle();
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        clipToolbarOffset();
        onMoved(toolbarOffset);

        if((toolbarOffset < toolbarHeight && dy>0) || (toolbarOffset > 0 && dy<0)) {
            toolbarOffset += dy;
        }
    }

    private void clipToolbarOffset() {
        if(toolbarOffset > toolbarHeight) {
            toolbarOffset = toolbarHeight;
        } else if(toolbarOffset < 0) {
            toolbarOffset = 0;
        }
    }

    public abstract void onMoved(int distance);
}