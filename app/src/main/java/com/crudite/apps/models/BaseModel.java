package com.crudite.apps.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWindow;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.crudite.apps.controller.AppController;
import com.crudite.apps.controller.ImageDownloader;
import com.crudite.apps.controller.RequestManager;
import com.crudite.apps.utilitaires.Standard;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import org.jdeferred.Deferred;
import org.jdeferred.DoneCallback;
import org.jdeferred.Promise;
import org.jdeferred.android.AndroidExecutionScope;
import org.jdeferred.android.AndroidFailCallback;
import org.jdeferred.impl.DeferredObject;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import dalvik.system.DexFile;

/**
 * Created by Chahdro on 07/04/2018.
 */

public class BaseModel implements Serializable{

    public static class Config{
        public static String entitePackageName = "com.crudite.apps.entite";
        public static boolean allowSync = true;
    }
    @modelAnotationField(columnName = "UPDATED_AT")
    public String updated_at="";
    @modelAnotationField(columnName = "SUP_DELETE")
    public int sup_delete;
    @modelAnotationField(columnName = "ROW_ID")
    public String row_id="";

    public static DBHelper dbHelper;
    public HashMap<String, Object> propertiesNoDeclared = new HashMap<String, Object>();
    public void init(Context context){
        dbHelper = new DBHelper(context);
    }

    public int create(){
        dbHelper.Open();
        SQLiteDatabase db = dbHelper.getDB();
        ContentValues cv = new ContentValues();
        String tableName = this.getClass().getSimpleName().toLowerCase();
        String primaryKeyFiedName = "";
        int primaryKeyFiedValue = 0;
        try {
            Field[] declaredFields = this.getClass().getDeclaredFields();
            for (Field field : declaredFields) {
                modelAnotationField anotation = field.getAnnotation(modelAnotationField.class);
                if (!java.lang.reflect.Modifier.isPrivate(field.getModifiers()) && anotation!=null) {
                    String fieldName = anotation.columnName();
                    if (fieldName.length()==0){
                        fieldName = field.getName();
                    }
                    if(anotation.tableName().length()>0){
                        tableName = anotation.tableName();
                    }
                    if(anotation.primaryKey()){
                        primaryKeyFiedName = fieldName;
                    }
                    if(field.get(this)!=null && anotation.tableName().length()==0 && anotation.sqlQuery().length()==0){
                        if(field.get(this).getClass().getSimpleName().equals("String")) {
                            cv.put(fieldName, (String) field.get(this));
                        }
                        else if(field.get(this).getClass().getSimpleName().equals("Integer")){
                            if(!anotation.primaryKey() || ((Integer)field.get(this)!=0)) {
                                cv.put(fieldName, (Integer) field.get(this));
                            }
                        }
                        else if(field.get(this).getClass().getSimpleName().equals("Float")){
                            cv.put(fieldName, (Float)field.get(this));
                        }
                        else if(field.get(this).getClass().getSimpleName().equals("Double")){
                            cv.put(fieldName, (Double)field.get(this));
                        }
                    }
                    else {
                    }
                }
                else{
                }
            }
            if(!tableName.equals("last_update_date")) {
                if (cv.getAsInteger(primaryKeyFiedName.substring(0, primaryKeyFiedName.length() - 4)) != 0) {
                    primaryKeyFiedValue = cv.getAsInteger(primaryKeyFiedName.substring(0, primaryKeyFiedName.length() - 4));
                }
            }
        } catch (IllegalArgumentException
                | IllegalAccessException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        long rowId = db.insert(tableName, null, cv);
        setProperty(this, primaryKeyFiedName, (int)rowId);
        if(!tableName.equals("last_update_date")) {
            try {
                if (primaryKeyFiedValue == 0) {
                    HashMap<String, Object> values = new HashMap<>();
                    values.put("ROW_ID", Standard.randomString(20));
                    String query = "SELECT MAX(" + primaryKeyFiedName.substring(0, primaryKeyFiedName.length() - 4) + ") as m FROM " + tableName + "";
                    Cursor c = db.rawQuery(query, null);
                    int max = 0;
                    while (c.moveToNext()) {
                        max = c.getInt(0) + 1;
                    }
                    if (max == 0) {
                        max = 1;
                    }
                    values.put(primaryKeyFiedName.substring(0, primaryKeyFiedName.length() - 4), max);
                    this.update(values, primaryKeyFiedName + "=?", new String[]{rowId + ""});
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //dbHelper.close();
        return  (int)rowId;
    }

    public void delete(String condition){
        try {
            dbHelper.Open();
        }catch (Exception e){
        }
        SQLiteDatabase db = dbHelper.getDB();

        String tableName = this.getClass().getSimpleName().toLowerCase();
        String primaryKeyFiedName = "";
        int primaryKeyValue = 0;
        try {
            Field[] declaredFields = this.getClass().getDeclaredFields();
            for (Field field : declaredFields) {
                modelAnotationField anotation = field.getAnnotation(modelAnotationField.class);
                if (!java.lang.reflect.Modifier.isPrivate(field.getModifiers()) && anotation!=null) {
                    String fieldName = anotation.columnName();
                    if (fieldName.length()==0){
                        fieldName = field.getName();
                    }
                    if(anotation.primaryKey()){
                        primaryKeyFiedName = fieldName;
                        primaryKeyValue = (Integer)field.get(this);
                    }
                    if(anotation.tableName().length()>0){
                        tableName = anotation.tableName();
                    }
                }
            }
        } catch (IllegalArgumentException
                | IllegalAccessException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        String query = "DELETE FROM "+tableName+" WHERE ";
        if(condition.length()>0){
            query += condition;
        }
        else
            query += primaryKeyFiedName+" = "+primaryKeyValue;
        db.execSQL(query);
        //dbHelper.close();
    }
    public void update(HashMap<String, Object> values, String where, String[] params){

        try {
            dbHelper.Open();
        }catch (Exception e){
        }
        SQLiteDatabase db = dbHelper.getDB();
        ContentValues cv = new ContentValues();
        String tableName = this.getClass().getSimpleName().toLowerCase();
        String primaryKeyFiedName = "";
        int primaryKeyValue = 0;

        try {
            Field[] declaredFields = this.getClass().getDeclaredFields();
            for (Field field : declaredFields) {
                modelAnotationField anotation = field.getAnnotation(modelAnotationField.class);
                if (!java.lang.reflect.Modifier.isPrivate(field.getModifiers()) && anotation!=null) {
                    String fieldName = anotation.columnName();
                    if (fieldName.length()==0){
                        fieldName = field.getName();
                    }
                    if(anotation.tableName().length()>0){
                        tableName = anotation.tableName();
                    }
                }
            }
        } catch (IllegalArgumentException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        Iterator it = values.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();

            //System.out.println(pair.getKey() + " = " + pair.getValue());

            if(pair.getValue().getClass().isAssignableFrom(String.class)) {
                cv.put(pair.getKey().toString(), pair.getValue().toString());
            }
            if(pair.getValue().getClass().isAssignableFrom(Integer.class)) {
                cv.put(pair.getKey().toString(), Integer.parseInt(pair.getValue().toString()));
            }
            if(pair.getValue().getClass().isAssignableFrom(Float.class)) {
                cv.put(pair.getKey().toString(), Float.parseFloat(pair.getValue().toString()));
            }
            if(pair.getValue().getClass().isAssignableFrom(Double.class)) {
                cv.put(pair.getKey().toString(), Double.parseDouble(pair.getValue().toString()));
            }
        }
        if(!tableName.equals("last_update_date")) {
            cv.put("UPDATED_AT", Standard.getCurrentTimeStampComplet());
        }
        db.update(tableName, cv,  where, params);
        //dbHelper.close();
    }
    public void update(String condition){
        try {
            dbHelper.Open();
        }catch (Exception e){
        }
        SQLiteDatabase db = dbHelper.getDB();
        ContentValues cv = new ContentValues();
        String tableName = this.getClass().getSimpleName().toLowerCase();
        String primaryKeyFiedName = "";
        int primaryKeyValue = 0;

        try {
            Field[] declaredFields = this.getClass().getDeclaredFields();
            for (Field field : declaredFields) {
                modelAnotationField anotation = field.getAnnotation(modelAnotationField.class);
                if (!java.lang.reflect.Modifier.isPrivate(field.getModifiers()) && anotation!=null) {
                    String fieldName = anotation.columnName();
                    if (fieldName.length()==0){
                        fieldName = field.getName();
                    }
                    if(anotation.primaryKey()){
                        primaryKeyFiedName = fieldName;
                        primaryKeyValue = (Integer)field.get(this);
                    }
                    if(anotation.tableName().length()>0){
                        tableName = anotation.tableName();
                    }
                    if(field.get(this)!=null && anotation.tableName().length()==0 && anotation.sqlQuery().length()==0){
                        if(field.get(this).getClass().getSimpleName().equals("String"))
                            cv.put(fieldName, (String)field.get(this));
                        else if(field.get(this).getClass().getSimpleName().equals("Integer")){
                            if(!anotation.primaryKey() || ((Integer)field.get(this)!=0))
                                cv.put(fieldName, (Integer) field.get(this));
                        }
                        else if(field.get(this).getClass().getSimpleName().equals("Float")){
                            cv.put(fieldName, (Float)field.get(this));
                        }
                        else if(field.get(this).getClass().getSimpleName().equals("Double")){
                            cv.put(fieldName, (Double)field.get(this));
                        }
                    }
                }
            }
        } catch (IllegalArgumentException
                | IllegalAccessException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        String cond="";
        if(condition.length()>0){
            cond += condition;
        }
        else
            cond += primaryKeyFiedName+" = "+primaryKeyValue;
        if(!tableName.equals("last_update_date")) {
            cv.put("UPDATED_AT", Standard.getCurrentTimeStampComplet());
        }
        db.update(tableName, cv,  cond, new String[]{});
        //dbHelper.close();
    }

    public Object get(int id){


        try {
            dbHelper.Open();
        }catch (Exception e){
        }
        SQLiteDatabase db = dbHelper.getDB();

        String tableName = this.getClass().getSimpleName().toLowerCase();
        String primaryKeyFiedName = "";
        String sqlQuery = "";
        try {
            Field[] declaredFields = this.getClass().getDeclaredFields();
            for (Field field : declaredFields) {
                modelAnotationField anotation = field.getAnnotation(modelAnotationField.class);
                if (!java.lang.reflect.Modifier.isPrivate(field.getModifiers()) && anotation!=null) {
                    String fieldName = anotation.columnName();
                    if (fieldName.length()==0){
                        fieldName = field.getName();
                    }
                    if(anotation.primaryKey()){
                        primaryKeyFiedName = fieldName;
                    }
                    if(anotation.tableName().length()>0){
                        tableName = anotation.tableName();
                    }
                    if(anotation.sqlQuery().length()>0){
                        sqlQuery = anotation.sqlQuery();
                    }
                }
            }
        } catch (IllegalArgumentException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        Cursor c;
        if(sqlQuery.length()>0){
            String requete = sqlQuery+" AND "+primaryKeyFiedName+" = ?";
            c = db.rawQuery(requete, new String[]{""+id});
        }
        else {
            c = db.query(tableName, getAllModelFieldName(), primaryKeyFiedName+" = ?", new String[]{""+id}, null, null, null);
        }

        if (c.moveToNext()) {
            Object object = fetchCursor(c);
            c.close();
            return object;
        }
        //dbHelper.close();
        return null;
    }
    public boolean useDatablaseFieldName = false;
    public ArrayList<?> dataTpoSynch(String requete, String where, String[] condition){
        useDatablaseFieldName = true;
        listRowIdPostion = new HashMap<>();
        return query(requete, where, condition);
    }
    public ArrayList<?> query(String requete, String where, String[] condition){
        ArrayList<Object> arrayList = new ArrayList<>();
        try {

            dbHelper.Open();
        }catch (Exception e){
            e.printStackTrace();
        }
        SQLiteDatabase db = dbHelper.getDB();

        String tableName = this.getClass().getSimpleName().toLowerCase();
        String sqlQuery = "";
        try {
            Field[] declaredFields = this.getClass().getDeclaredFields();
            for (Field field : declaredFields) {
                modelAnotationField anotation = field.getAnnotation(modelAnotationField.class);
                if (!java.lang.reflect.Modifier.isPrivate(field.getModifiers()) && anotation!=null) {
                    if(anotation.tableName().length()>0){
                        tableName = anotation.tableName();
                    }
                    if(anotation.sqlQuery().length()>0){
                        sqlQuery = anotation.sqlQuery();
                    }
                }
            }
        } catch (IllegalArgumentException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        Cursor c;
        if(requete!=null && requete.length()>0){
            c = db.rawQuery(requete, condition);
        }
        else if(sqlQuery.length()>0) {
            String requete2 = sqlQuery;
            if(where!=null && where.length()>0){
                requete2 = requete2+" AND "+where;
            }
            c = db.rawQuery(requete2, condition);
        }
        else{
            if(useDatablaseFieldName){
                c = db.rawQuery("SELECT * FROM "+tableName+" WHERE "+where, condition);
            }
            else {
                c = db.query(tableName, getAllModelFieldName(), where, condition, null, null, null);
            }
        }

        while (c.moveToNext()) {
            Object object = fetchCursor(c);
            arrayList.add(object);
            if(useDatablaseFieldName && ((BaseModel)object).propertiesNoDeclared.get("ROW_ID")!=null){
                listRowIdPostion.put(((BaseModel)object).propertiesNoDeclared.get("ROW_ID").toString(), arrayList.size()-1);
            }
        }
        c.close();
        //dbHelper.close();
        return arrayList;
    }

    public Promise<ArrayList<?>, VolleyError, Void> load(Map<String, String> param_s, boolean cache){
        String tableName = this.getClass().getSimpleName().toLowerCase(),primaryKeyFiedName="";
        Field[] declaredFields = this.getClass().getDeclaredFields();
        final HashMap<String, String> fields = new HashMap<>();
        for (Field field : declaredFields) {
            modelAnotationField anotation = field.getAnnotation(modelAnotationField.class);
            if (!java.lang.reflect.Modifier.isPrivate(field.getModifiers()) && anotation!=null) {
                String fieldName = anotation.columnName();

                if(anotation.tableName().length()>0){
                    tableName = anotation.tableName();
                }

                if(anotation.tableName().length()==0 && anotation.sqlQuery().length()==0) {
                    if(anotation.primaryKey()){
                        primaryKeyFiedName = field.getName();
                    }
                    if (fieldName.length() == 0) {
                        fieldName = field.getName();
                        fields.put(fieldName, fieldName);
                    } else {
                        fields.put(anotation.columnName(), field.getName());
                    }
                }
            }
        }
        final String tableName2 = tableName;
        final String primaryKeyFiedName2 = primaryKeyFiedName;
        try {
            String remoteSqlQuery = ((modelAnotationType) this.getClass().getAnnotation(modelAnotationType.class)).remoteSqlQuery();
            if(remoteSqlQuery!=null && remoteSqlQuery.length()>0){
                String q = "\\?";
                remoteSqlQuery = remoteSqlQuery.replaceAll(q, AppController.getInstance().getPrefManager().getUserId()+"");
                if(param_s==null){
                    param_s = new HashMap<String, String>();
                }
                param_s.put("sqlQuery", remoteSqlQuery);
            }
        }catch (Exception e){
        }
        final Deferred<ArrayList<?>,VolleyError, Void> deferred = new DeferredObject<>();
        RequestManager.get("Apk/"+tableName, param_s, cache)
        .done(new DoneCallback<String>() {
            @Override
            public void onDone(String response) {
                if (response != null) {
                    try {
                        Log.i("res", response+"d");
                        JSONObject myJson = new JSONObject(response);
                        if (!myJson.optString("status").equals("success")) {
                            Log.i("Load Error "+tableName2, response);
                        } else {
                            JSONArray feedArray = new JSONArray(myJson.getString("data"));
                            ArrayList<Object> arrayList = new ArrayList<>();
                            for (int i=0;i<feedArray.length();i++) {
                                JSONObject feedObj = (JSONObject) feedArray.get(i);
                                Constructor<?> cons = BaseModel.this.getClass().getConstructor();
                                BaseModel item = (BaseModel) cons.newInstance();
                                Iterator<?> keys = feedObj.keys();
                                while( keys.hasNext() ) {
                                    String key = (String)keys.next();
                                    if (feedObj.get(key) instanceof JSONObject ) {
                                    }
                                    else {
                                        String fo = fields.get(key);
                                        if(fo==null){
                                            fo = key;
                                        }
                                        item.setProperty2(item, fo, feedObj.get(key));
                                    }
                                }
                                arrayList.add(item);
                            }
                            deferred.resolve(arrayList);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }).fail(new AndroidFailCallback<VolleyError>() {
            @Override
            public void onFail(VolleyError error) {
                deferred.reject(error);
            }

            @Override
            public AndroidExecutionScope getExecutionScope() {
                return null;
            }
        });
        return deferred;
    }
    public Promise<String, String, Void> synch(){
        final Deferred<String,String, Void> deferred = new DeferredObject<>();
        try {
            String tableName = this.getClass().getSimpleName().toLowerCase(),primaryKeyFiedName="",childTableName="";
            Field[] declaredFields = this.getClass().getDeclaredFields();
            modelAnotationType anotation2 = this.getClass().getAnnotation(modelAnotationType.class);
            if(anotation2!=null)
                childTableName = anotation2.childTable();
            final HashMap<String, String> fields = new HashMap<>();
            final HashMap<String, String> downloadFiles = new HashMap<>();
            for (Field field : declaredFields) {
                modelAnotationField anotation = field.getAnnotation(modelAnotationField.class);
                if (!java.lang.reflect.Modifier.isPrivate(field.getModifiers()) && anotation!=null) {
                    String fieldName = anotation.columnName();

                    if(anotation.tableName().length()>0){
                        tableName = anotation.tableName();
                    }

                    if(anotation.tableName().length()==0 && anotation.sqlQuery().length()==0) {
                        if (fieldName.length() == 0) {
                            fieldName = field.getName();
                        }
                        if(anotation.primaryKey()){
                            primaryKeyFiedName = fieldName;
                        }
                        String keyd="";
                        if (fieldName.length() == 0) {
                            fieldName = field.getName();
                            fields.put(fieldName, fieldName);
                            keyd = fieldName;
                        } else {
                            fields.put(anotation.columnName(), field.getName());
                            keyd = anotation.columnName();
                        }
                        if(anotation.downloadRemoteFile()){
                            if(anotation.remoteFileUrl().length()>0) {
                                downloadFiles.put(keyd, anotation.remoteFileUrl());
                            }
                        }
                    }
                }
            }

            final Class cl = this.getClass();

            final String tableName2 = tableName;
            final String primaryKeyFiedName2 = primaryKeyFiedName;
            final String childTableName2 = childTableName;
            Constructor<?> cons = this.getClass().getConstructor();
            BaseModel item = (BaseModel) cons.newInstance();
            final ArrayList<?> listSynch = item.dataTpoSynch("", "SUP_DELETE = 1 OR UPDATED_AT > ?", new String[]{TableLastUpdateDate.getTable(tableName2)});
            boolean uploadData = false;
            HashMap<String, Integer>  listRowIdPosition_tmp = null;
            try {
                if(((modelAnotationType) cl.getAnnotation(modelAnotationType.class)).uploadData()){
                    uploadData = true;
                    listRowIdPosition_tmp = item.listRowIdPostion;
                }
                else {
                    listRowIdPosition_tmp = new HashMap<>();
                }
            }catch (Exception e){
                listRowIdPosition_tmp = new HashMap<>();
            }
            final HashMap<String, Integer> listRowIdPosition = listRowIdPosition_tmp;
            Gson gson = new Gson();
            final String json = gson.toJson(listSynch);
            HashMap<String, String> params = new HashMap<>();
            params.put("last_date", TableLastUpdateDate.getTable(tableName2));

            if(uploadData){
                params.put("data", json);
            }
            else {

            }
            try {
                String remoteSqlQuery = ((modelAnotationType) cl.getAnnotation(modelAnotationType.class)).remoteSqlQuery();
                if(remoteSqlQuery!=null && remoteSqlQuery.length()>0){
                    String q = "\\?";
                    remoteSqlQuery = remoteSqlQuery.replaceAll(q, AppController.getInstance().getPrefManager().getUserId()+"");
                    params.put("sqlQuery", remoteSqlQuery);
                }
            }catch (Exception e){
            }
            boolean ddata = true;
            try {
                if(!((modelAnotationType) cl.getAnnotation(modelAnotationType.class)).downloadData()){
                    ddata = false;
                }
            }catch (Exception e){
            }
            params.put("downloadData", ddata ? "true" : "false");
            final boolean downloadData = ddata;

            Log.i("Synch "+tableName2, TableLastUpdateDate.getTable(tableName2)+" Data : "+json);

            RequestManager.post("synch/"+tableName, params)
                    .done(new DoneCallback<String>() {
                        @Override
                        public void onDone(String response) {
                            if (response != null) {
                                Log.i("res", response+"d");
                                if(downloadData) {
                                    try {
                                        JSONObject myJson = new JSONObject(response);
                                        if (!myJson.optString("status").equals("success")) {
                                            Log.i("Synch Error " + tableName2, response);
                                        } else {
                                            JSONArray feedArray = new JSONArray(myJson.getString(tableName2));
                                            for (int i = 0; i < feedArray.length(); i++) {
                                                JSONObject feedObj = (JSONObject) feedArray.get(i);
                                                Constructor<?> cons = cl.getConstructor();
                                                BaseModel item;
                                                String oldId = "";
                                                if (listRowIdPosition!=null && listRowIdPosition.size()>0 && listRowIdPosition.containsKey(feedObj.getString("ROW_ID"))) {
                                                    item = (BaseModel) listSynch.get(listRowIdPosition.get(feedObj.getString("ROW_ID")));
                                                    oldId = item.getFieldByName(fields.get(primaryKeyFiedName2.substring(0, primaryKeyFiedName2.length() - 4))).toString();
                                                } else {
                                                    ArrayList<BaseModel> search = (ArrayList<BaseModel>) ((BaseModel)cons.newInstance()).query("", primaryKeyFiedName2.substring(0, primaryKeyFiedName2.length() - 4)+"=?", new String[]{fields.get(primaryKeyFiedName2.substring(0, primaryKeyFiedName2.length() - 4)).toString()});
                                                    if(search.size()>0){
                                                        item = search.get(0);
                                                        oldId = item.getFieldByName(fields.get(primaryKeyFiedName2.substring(0, primaryKeyFiedName2.length() - 4))).toString();
                                                    }
                                                    else
                                                        item = (BaseModel) cons.newInstance();
                                                }

                                                Iterator<?> keys = feedObj.keys();
                                                while (keys.hasNext()) {
                                                    String key = (String) keys.next();
                                                    if (feedObj.get(key) instanceof JSONObject) {
                                                    } else {
                                                        String fo = fields.get(key);
                                                        if (fo == null) {
                                                            fo = key;
                                                        }
                                                        try {
                                                            item.setProperty2(item, fo, feedObj.get(key));
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }

                                                if (!oldId.equals("")) {
                                                    if (Integer.parseInt(item.getFieldByName("SUP_DELETE").toString()) == 1) {
                                                        Log.i("Delete " + tableName2, item.toString());
                                                        item.delete("");
                                                    } else {
                                                        if (!oldId.equals(item.getFieldByName(fields.get(primaryKeyFiedName2.substring(0, primaryKeyFiedName2.length() - 4))).toString())) {
                                                            if (childTableName2.length() > 0) {
                                                                String[] classes = childTableName2.split(",");
                                                                for (String c : classes) {
                                                                    Class cl = Class.forName(Config.entitePackageName + "." + c);
                                                                    if (cl.getSuperclass() == BaseModel.class) {
                                                                        Constructor<?> cons2 = cl.getConstructor();
                                                                        BaseModel item2 = (BaseModel) cons2.newInstance();
                                                                        HashMap<String, Object> values = new HashMap<String, Object>();
                                                                        values.put(primaryKeyFiedName2.substring(0, primaryKeyFiedName2.length() - 4), Integer.parseInt(item.getFieldByName(fields.get(primaryKeyFiedName2.substring(0, primaryKeyFiedName2.length() - 4))).toString()));
                                                                        item2.update(values, primaryKeyFiedName2.substring(0, primaryKeyFiedName2.length() - 4) + " = ?", new String[]{oldId});
                                                                    }
                                                                }
                                                            }

                                                            item.update("");
                                                        } else
                                                            item.update("");
                                                    }
                                                } else {
                                                    item.create();
                                                }

                                                if(downloadFiles.size()>0){
                                                    for (Map.Entry<String, String> entry : downloadFiles.entrySet()) {
                                                        String key = entry.getKey();
                                                        String value = entry.getValue();
                                                        if (feedObj.get(key) instanceof JSONObject) {
                                                        } else {
                                                            String fo = fields.get(key);
                                                            if (fo == null) {
                                                                fo = key;
                                                            }
                                                            if(item.getFieldByName(fo)!= null && item.getFieldByName(fo).toString().length()>0){
                                                                final String name = item.getFieldByName(fo).toString();
                                                                ImageDownloader.download(AppController.getInstance().getApplicationContext(), value+name, name);
                                                            }
                                                        }
                                                    }
                                                }
                                            }

                                            Constructor<?> cons = cl.getConstructor();
                                            BaseModel item = (BaseModel) cons.newInstance();
                                            item.delete("SUP_DELETE = 1");
                                            TableLastUpdateDate.updateLastID(tableName2, Standard.getCurrentTimeStampComplet());
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                else {
                                    try {
                                        Constructor<?> cons = cl.getConstructor();
                                        BaseModel item = (BaseModel) cons.newInstance();
                                        item.delete("SUP_DELETE = 1");
                                        TableLastUpdateDate.updateLastID(tableName2, Standard.getCurrentTimeStampComplet());
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                                deferred.resolve("good");
                            }
                            else {
                                deferred.reject("No resp");
                            }
                        }
                    })
                    .fail(new AndroidFailCallback<VolleyError>() {
                        @Override
                        public void onFail(VolleyError result) {
                            VolleyLog.d("dddd", "Error: " + result.getMessage());
                            deferred.reject(result.getMessage());
                        }

                        @Override
                        public AndroidExecutionScope getExecutionScope() {
                            return null;
                        }
                    });

        }catch (Exception e){
            e.printStackTrace();
            deferred.reject(e.getMessage());
        }
        return deferred;
    }
    public Object fetchCursor(Cursor cursor){
        try {
        Constructor<?> cons = this.getClass().getConstructor();
        Object item = cons.newInstance();
            Field[] declaredFields = this.getClass().getDeclaredFields();
            HashMap<String, String> fields = new HashMap<>();
            for (Field field : declaredFields) {
                modelAnotationField anotation = field.getAnnotation(modelAnotationField.class);
                if (!java.lang.reflect.Modifier.isPrivate(field.getModifiers()) && anotation!=null) {
                    String fieldName = anotation.columnName();
                    if(anotation.tableName().length()==0 && anotation.sqlQuery().length()==0) {
                        if (fieldName.length() == 0) {
                            fieldName = field.getName();
                            fields.put(fieldName, fieldName);
                        } else {
                            fields.put(anotation.columnName(), field.getName());
                        }
                    }
                }
            }
            for (int i=0; i<cursor.getColumnCount(); i++){
                int type = getType(cursor, i);
                Object value;
                switch (type){
                    case FIELD_TYPE_INTEGER : {
                        value = cursor.getInt(cursor.getColumnIndex(cursor.getColumnName(i)));
                        break;
                    }
                    case FIELD_TYPE_FLOAT : {
                        value = cursor.getFloat(cursor.getColumnIndex(cursor.getColumnName(i)));
                        break;
                    }
                    case FIELD_TYPE_STRING : {
                        value = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(i)));
                        break;
                    }
                    default: {
                        value = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(i)));
                        break;
                    }
                }
                String fo = fields.get(cursor.getColumnName(i));
                if(fo==null){
                    fo = cursor.getColumnName(i);
                }
                if(useDatablaseFieldName)
                    setProperty(item, cursor.getColumnName(i), value);
                else
                    setProperty(item, fo, value);
            }

            return item;
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return null;
        }
    }
    public HashMap<String, Integer> listRowIdPostion;
    public String[] getAllModelFieldName(){
        ArrayList<String> list = new ArrayList<>();
        try {
            Field[] declaredFields = this.getClass().getDeclaredFields();
            for (Field field : declaredFields) {
                modelAnotationField anotation = field.getAnnotation(modelAnotationField.class);
                if (!java.lang.reflect.Modifier.isPrivate(field.getModifiers()) && anotation!=null) {
                    String fieldName = anotation.columnName();
                    if(anotation.tableName().length()==0 && anotation.sqlQuery().length()==0) {
                        if (fieldName.length() == 0) {
                            fieldName = field.getName();
                        }
                        list.add(fieldName);
                    }
                }
            }
        } catch (IllegalArgumentException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return list.toArray(new String[list.size()]);
    }
    public boolean setProperty(Object object, String fieldName, Object fieldValue) {
        Class<?> clazz = object.getClass();
        //Log.i("SetProposer "+fieldName, fieldValue+"");
        while (clazz != null) {
            try {
                try{
                    ((BaseModel)object).propertiesNoDeclared.put(fieldName, fieldValue);
                }catch (Exception e){
                  e.printStackTrace();
                }

                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(object, fieldValue);
                return true;
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
        return false;
    }
    public boolean setProperty2(Object object, String fieldName, Object fieldValue) {
        Class<?> clazz = object.getClass();
        while (clazz != null) {
            try {
                try{
                    ((BaseModel)object).propertiesNoDeclared.put(fieldName, fieldValue);
                }catch (Exception e){
                    e.printStackTrace();
                }

                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                /*try {
                    field.set(object, fieldValue);
                }catch (Exception e){*/

                if(field.get(this)==null || field.get(this).getClass().getSimpleName().equals("String"))
                    field.set(object, fieldValue.toString());
                else if(field.get(this).getClass().getSimpleName().equals("Integer")){
                    try {
                        int val = Integer.parseInt(fieldValue.toString());
                        field.set(object, val);
                    }catch (Exception e2){
                        e2.printStackTrace();
                    }
                }
                else if(field.get(this).getClass().getSimpleName().equals("Float")){
                    try {
                        float val = Float.parseFloat(fieldValue.toString());
                        field.set(object, val);
                    }catch (Exception e2){
                        e2.printStackTrace();
                    }
                }
                else if(field.get(this).getClass().getSimpleName().equals("Double")){
                    try {
                        double val = Double.parseDouble(fieldValue.toString());
                        field.set(object, val);
                    }catch (Exception e2){
                        e2.printStackTrace();
                    }
                }
                else {
                    field.set(object, fieldValue.toString());
                }
                /*
                if (field.getType().isAssignableFrom(Integer.class)) {
                    try {
                        int val = Integer.parseInt(fieldValue.toString());
                        Log.i("Cast" + fieldName, val + "val");
                        field.set(object, val);
                    }catch (Exception e2){
                        e2.printStackTrace();
                    }
                }
                if (field.getType().isAssignableFrom(String.class)) {
                    field.set(object, fieldValue.toString());
                }
                if (field.getType().isAssignableFrom(Float.class)) {
                    field.set(object, Float.parseFloat(fieldValue.toString()));
                }
                if (field.getType().isAssignableFrom(Double.class)) {
                    field.set(object, Double.parseDouble(fieldValue.toString()));
                }*/
                    //e.printStackTrace();
                //}
                return true;
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
        return false;
    }
    public Object getFieldByName(String fieldName){
        try{
            Field field = this.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            Object value = field.get(this);
            return value==null ? propertiesNoDeclared.get(fieldName) : value;
        }catch (Exception e){
            //e.printStackTrace();
            return propertiesNoDeclared.get(fieldName);
        }
    }

    public void getFiledProperty() throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException{
        Field[] declaredFields = this.getClass().getDeclaredFields();
        List<Field> staticFields = new ArrayList<Field>();
        for (Field field : declaredFields) {
            modelAnotationField anotation = field.getAnnotation(modelAnotationField.class);
            if (!java.lang.reflect.Modifier.isPrivate(field.getModifiers()) && anotation!=null) {
                staticFields.add(field);
                System.out.println(anotation.columnName()+", "+field.get(this)+" "+field.get(this).getClass().getName()+" "+(anotation.primaryKey() ? "Primary key" : ""));
            }
        }
    }

    public String buildSqlQuery(String type){
        String primaryKeyFiedName = "", tableName = this.getClass().getSimpleName().toLowerCase();
        ArrayList<String> fieldWithTypes = new ArrayList<>();
        try {
            Field[] declaredFields = this.getClass().getDeclaredFields();
            for (Field field : declaredFields) {
                modelAnotationField anotation = field.getAnnotation(modelAnotationField.class);
                if (!java.lang.reflect.Modifier.isPrivate(field.getModifiers()) && anotation!=null) {
                    String fieldName = anotation.columnName();
                    if (fieldName.length()==0){
                        fieldName = field.getName();
                    }
                    if(anotation.tableName().length()>0){
                        tableName = anotation.tableName();
                    }
                    if(anotation.primaryKey()){
                        primaryKeyFiedName = fieldName;
                    }
                    if(anotation.tableName().length()==0 && anotation.sqlQuery().length()==0){
                        if(anotation.columnType().length()>0) {
                            fieldWithTypes.add(fieldName+" "+anotation.columnType());
                        }
                        else if(field.getType().isAssignableFrom(String.class)) {
                            fieldWithTypes.add(fieldName+" TEXT");
                        }
                        else if(field.get(this).getClass().getSimpleName().equals("Integer")){
                            if(anotation.primaryKey()){
                                fieldWithTypes.add(fieldName+" INTEGER PRIMARY KEY AUTOINCREMENT");
                            }
                            else {
                                fieldWithTypes.add(fieldName+" INTEGER");
                            }
                        }
                        else if(field.get(this).getClass().getSimpleName().equals("Float")){
                            fieldWithTypes.add(fieldName+" REAL");
                        }
                        else if(field.get(this).getClass().getSimpleName().equals("Double")){
                            fieldWithTypes.add(fieldName+" REAL");
                        }
                    }
                    else {
                    }
                }
                else{
                }
            }
        } catch (IllegalArgumentException
                | IllegalAccessException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        switch (type){
            case "create_table":
                String SQL_TRIGGER_BEFORE_UPDATE="";
                if(Config.allowSync && tableName!="last_update_date"){
                    fieldWithTypes.add("ROW_ID VARCHAR(20)");
                    fieldWithTypes.add("UPDATED_AT VARCHAR(20) DEFAULT (datetime('now','localtime'))");
                    fieldWithTypes.add("SUP_DELETE INTEGER DEFAULT 0");
                    SQL_TRIGGER_BEFORE_UPDATE = "";
                }
                else {
                    SQL_TRIGGER_BEFORE_UPDATE = "";
                }
                return "CREATE TABLE "+tableName+" ("+ TextUtils.join(",", fieldWithTypes.toArray(new String[fieldWithTypes.size()]))+"); "+SQL_TRIGGER_BEFORE_UPDATE;
            case "drop_table":
                return "DROP TABLE IF EXISTS "+tableName+"";
            case "empty_table":
                return "DELETE FROM "+tableName+"";
            default:
                return "";
        }
    }
    public static void initDb(SQLiteDatabase db){
        try {
            if(Config.allowSync){
                Constructor<?> cons = TableLastUpdateDate.class.getConstructor();
                Object item = cons.newInstance();
                String sql = ((BaseModel) item).buildSqlQuery("create_table");
                Log.i("Create", sql + "dfd");
                db.execSQL(sql);
            }
            List<String> classes = getClassesOfPackage(Config.entitePackageName);
            for (String c : classes) {
                Class cl = Class.forName(Config.entitePackageName+"."+c);
                if(cl.getSuperclass() == BaseModel.class) {
                    Constructor<?> cons = cl.getConstructor();
                    Object item = cons.newInstance();
                    String sql = ((BaseModel) item).buildSqlQuery("create_table");
                    Log.i("Create", sql + "dfd");
                    db.execSQL(sql);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void rebuildDb(SQLiteDatabase db){
        try {
            if(Config.allowSync){
                Constructor<?> cons = TableLastUpdateDate.class.getConstructor();
                Object item = cons.newInstance();
                String sql = ((BaseModel) item).buildSqlQuery("drop_table");
                Log.i("Create", sql + "dfd");
                db.execSQL(sql);

                sql = ((BaseModel) item).buildSqlQuery("create_table");
                Log.i("Create", sql + "dfd");
                db.execSQL(sql);
            }
            List<String> classes = getClassesOfPackage(Config.entitePackageName);
            for (String c : classes) {
                Class cl = Class.forName(Config.entitePackageName+"."+c);
                if(cl.getSuperclass() == BaseModel.class) {
                    Constructor<?> cons = cl.getConstructor();
                    Object item = cons.newInstance();
                    String sql = ((BaseModel) item).buildSqlQuery("drop_table");
                    Log.i("Create", sql + "dfd");
                    db.execSQL(sql);


                    sql = ((BaseModel) item).buildSqlQuery("create_table");
                    Log.i("Create", sql + "dfd");
                    db.execSQL(sql);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    protected static final int FIELD_TYPE_BLOB = 4;
    protected static final int FIELD_TYPE_FLOAT = 2;
    protected static final int FIELD_TYPE_INTEGER = 1;
    protected static final int FIELD_TYPE_NULL = 0;
    protected static final int FIELD_TYPE_STRING = 3;
    public static List<String> getClassesOfPackage(String packageName) {
        ArrayList<String> classes = new ArrayList<String>();
        try {
            String packageCodePath = AppController.getInstance().getPackageCodePath();
            DexFile df = new DexFile(packageCodePath);
            for (Enumeration<String> iter = df.entries(); iter.hasMoreElements(); ) {
                String className = iter.nextElement();
                if (className.contains(packageName)) {
                    classes.add(className.substring(className.lastIndexOf(".") + 1, className.length()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return classes;
    }
    static int getType(Cursor cursor, int i) throws Exception {
        SQLiteCursor sqLiteCursor = (SQLiteCursor) cursor;
        CursorWindow cursorWindow = sqLiteCursor.getWindow();
        int pos = cursor.getPosition();
        int type = -1;
        if (cursorWindow.isNull(pos, i)) {
            type = FIELD_TYPE_NULL;
        } else if (cursorWindow.isLong(pos, i)) {
            type = FIELD_TYPE_INTEGER;
        } else if (cursorWindow.isFloat(pos, i)) {
            type = FIELD_TYPE_FLOAT;
        } else if (cursorWindow.isString(pos, i)) {
            type = FIELD_TYPE_STRING;
        } else if (cursorWindow.isBlob(pos, i)) {
            type = FIELD_TYPE_BLOB;
        }

        return type;
    }

}
