package com.crudite.apps.entite;

import com.crudite.apps.models.BaseModel;
import com.crudite.apps.models.modelAnotationField;

/**
 * Created by root on 26/07/18.
 */
public class Table extends BaseModel{
    @modelAnotationField(tableName = "table_")
    public String table_name="";
    @modelAnotationField(columnName = "idtable_APP", primaryKey = true)
    public int id_table_app;
    @modelAnotationField(columnName = "idtable")
    public int id_table;
    @modelAnotationField(columnName = "nomtable")
    public String nom_table;
    @modelAnotationField(columnName = "imagetable")
    public String image="";
    @modelAnotationField(columnName = "description")
    public String description="";
}
