package no.ntnu.hmsproject;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class MarginDecoration extends RecyclerView.ItemDecoration {
    int spaceHeight;

    public MarginDecoration(int spaceHeight) {
        this.spaceHeight = spaceHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = spaceHeight;
        }
        outRect.left = spaceHeight;
        outRect.right = spaceHeight;
        outRect.bottom = spaceHeight;
    }
}
