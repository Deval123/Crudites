package com.crudite.apps.models;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Admin on 21/05/2018.
 */

public class TableLastUpdateDate extends BaseModel {
    @modelAnotationField(tableName = "last_update_date")
    public String table_name;
    @modelAnotationField(primaryKey = true, columnName = "ID_UPDATE")
    public int id_update;
    @modelAnotationField(columnName = "NOM_TABLE")
    public String nom_table;
    @modelAnotationField(columnName = "DATE_UPDATE", columnType = "DATETIME")
    public String DATE_UPDATE;

    @Override
    public String toString() {
        return "TableLastUpdateDate{" +
                "id_update=" + id_update +
                ", nom_table='" + nom_table + '\'' +
                ", DATE_UPDATE='" + DATE_UPDATE + '\'' +
                '}';
    }

    public static HashMap<String, String> getLastId(){
        HashMap<String, String> list = new HashMap<String, String>();
        TableLastUpdateDate type =  new TableLastUpdateDate();

        ArrayList<TableLastUpdateDate> arrayList = (ArrayList<TableLastUpdateDate>) type.query(null, null, null);
        for (int i=0;i<arrayList.size();i++){
            list.put(arrayList.get(i).nom_table, arrayList.get(i).DATE_UPDATE);
        }
        return list;
    }
    
    public static String getTable(String table_name){
        HashMap<String, String> last_id = TableLastUpdateDate.getLastId();
        String last = "";
        if(last_id.get(table_name)==null){
            last = "0000-00-00";
            editLastId(table_name, "0000-00-00 00:00:00");
        }
        else
            last = last_id.get(table_name);
        return last;
    }

    public static void editLastId(String table_name, String date_update){
        TableLastUpdateDate type =  new TableLastUpdateDate();
        type.nom_table = table_name;
        type.DATE_UPDATE = date_update;
        Log.i("sfsdf", type.create()+"");
    }

    public static void updateLastID(String table_name, String date_update){
        TableLastUpdateDate type =  new TableLastUpdateDate();
        type.nom_table = table_name;
        type.DATE_UPDATE = date_update;
        type.update("NOM_TABLE = '"+table_name+"'");
    }
}
