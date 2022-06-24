package com.crudite.apps.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.crudite.apps.utilitaires.Standard;


/**
 * Created by Lincoln on 07/01/16.
 */
public class MyPreferenceManager {

    private String TAG = MyPreferenceManager.class.getSimpleName();

    // Shared Preferences
    public SharedPreferences pref;

    // Editor for Shared preferences
    public SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // All Shared Preferences Keys
    private static final String KEY_USER_ID = "id_utilisateur";
    private static final String KEY_USER_NAME = "nom";
    private static final String KEY_USER_EMAIL = "email";
    private static final String KEY_USER_NUMERO = "numero";
    private static final String KEY_USER_PROFILE = "photo_user";
    private static final String KEY_NOTIFICATIONS = "notifications";

    // Constructor
    public MyPreferenceManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(Standard.MyPREFERENCES, PRIVATE_MODE);
        editor = pref.edit();
    }

    public int getUserId(){
        return pref.getInt("id_utilisateur", 0);
    }

}
