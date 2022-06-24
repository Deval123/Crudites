package com.crudite.apps.utilitaires;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Admin on 17/04/2018.
 */

public class CustomListView extends ListView {

    private android.view.ViewGroup.LayoutParams params;
    private int prevCount = 0;

    public CustomListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas){
        if (getCount() != prevCount)
        {
            int height = 0;
            for (int i =0;i<getCount();i++){
                height += getChildAt(i).getHeight() + 1;
            }
            //int height = getChildAt(0).getHeight() + 1 ;
            prevCount = getCount();
            params = getLayoutParams();
            params.height = height;
            setLayoutParams(params);
        }
        super.onDraw(canvas);
    }
}