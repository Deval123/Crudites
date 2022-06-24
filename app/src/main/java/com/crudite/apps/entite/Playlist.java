package com.crudite.apps.entite;

import com.crudite.apps.models.BaseModel;
import com.crudite.apps.models.modelAnotationField;

/**
 * Created by root on 26/07/18.
 */
public class Playlist extends BaseModel{
    @modelAnotationField(columnName = "idson_APP", primaryKey = true)
    public int idson_app;
    @modelAnotationField(columnName = "idson")
    public int idson;
    @modelAnotationField(columnName = "titre")
    public String titre;
    @modelAnotationField(columnName = "key")
    public String key="";
}
