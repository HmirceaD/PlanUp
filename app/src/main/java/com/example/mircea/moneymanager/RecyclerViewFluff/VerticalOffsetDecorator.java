package com.example.mircea.moneymanager.RecyclerViewFluff;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class VerticalOffsetDecorator extends RecyclerView.ItemDecoration{

    /**Delimiteaza space u dintre chestiile din lista**/
    private int verticalSpaceHeight;

    public VerticalOffsetDecorator(int verticalSpaceHeight) {

        this.verticalSpaceHeight = verticalSpaceHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect,
                               View view,
                               RecyclerView parent,
                               RecyclerView.State state) {

        if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {

            outRect.bottom = verticalSpaceHeight;
        }
    }

}
