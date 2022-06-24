package com.crudite.apps.entite;

import com.crudite.apps.models.BaseModel;
import com.crudite.apps.models.modelAnotationField;

/**
 * Created by root on 26/07/18.
 */
public class UniversCruditeItem extends BaseModel{
    @modelAnotationField(tableName = "unvivers_crudite")
    public String table_name="";
    @modelAnotationField(columnName = "idrubrique_APP", primaryKey = true)
    public int idrubrique_app;
    @modelAnotationField(columnName = "idrubrique")
    public int idrubrique;
    @modelAnotationField(columnName = "titre_rubrique")
    public String titre_rubrique;
    @modelAnotationField(columnName = "detail")
    public String detail;
}
