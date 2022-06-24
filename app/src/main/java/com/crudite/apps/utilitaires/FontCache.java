package com.crudite.apps.utilitaires;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 04/09/18.
 */

public class FontCache {

    private static Map<String,Typeface> fontMap = new HashMap<String,Typeface>();

    public static Typeface getFont(Context context, String fontname){
        if(fontMap.containsKey(fontname)){
            return fontMap.get(fontname);
        }
        else{
            Typeface tf = Typeface.createFromAsset(context.getAssets(),fontname);
            fontMap.put(fontname,tf);
            return tf;
        }
    }
}
