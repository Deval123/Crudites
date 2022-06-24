package com.crudite.apps.utilitaires;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by root on 04/09/18.
 */

public class TitleTextView extends TextView {

    public TitleTextView(Context context) {
        super(context);
    }

    public TitleTextView(Context context, AttributeSet attrs, int defStyleAttr)   {
        super(context, attrs, defStyleAttr);
    }

    public TitleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setTypeface(Typeface tf) {
        super.setTypeface(FontCache.getFont(getContext(),"fonts/ITCEDSCR.TTF"));
    }
}
