package com.crudite.apps.entite;

import com.crudite.apps.models.BaseModel;
import com.crudite.apps.models.modelAnotationField;

/**
 * Created by root on 26/07/18.
 */
public class ModeReglement extends BaseModel{
    @modelAnotationField(tableName = "mode_reglement")
    public String table_name="";
    @modelAnotationField(columnName = "idmode_APP", primaryKey = true)
    public int idmode_app;
    @modelAnotationField(columnName = "idmode")
    public int idmode;
    @modelAnotationField(columnName = "mode")
    public String mode;
    @modelAnotationField(columnName = "description")
    public String description;
    @modelAnotationField(columnName = "image")
    public String image;
}
