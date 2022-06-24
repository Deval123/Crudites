package com.crudite.apps.entite;

import com.crudite.apps.models.BaseModel;
import com.crudite.apps.models.modelAnotationField;

/**
 * Created by root on 26/07/18.
 */
public class HoraireLivraison extends BaseModel{
    @modelAnotationField(tableName = "horaire_livraison")
    public String table_name="";
    @modelAnotationField(columnName = "idhoraire_APP", primaryKey = true)
    public int id_horaire_app;
    @modelAnotationField(columnName = "idhoraire")
    public int id_horaire;
    @modelAnotationField(columnName = "horaire")
    public String horaire;
}
