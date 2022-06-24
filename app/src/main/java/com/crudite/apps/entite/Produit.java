package com.crudite.apps.entite;

import com.crudite.apps.R;
import com.crudite.apps.models.BaseModel;
import com.crudite.apps.models.modelAnotationField;
import com.crudite.apps.models.modelAnotationType;

import java.util.ArrayList;

/**
 * Created by root on 26/07/18.
 */
public class Produit extends BaseModel{
    @modelAnotationField(columnName = "idproduit_APP", primaryKey = true)
    public int id_produit_app;
    @modelAnotationField(columnName = "idproduit")
    public int id_produit;
    @modelAnotationField(columnName = "idcontenu")
    public int id_contenu;
    @modelAnotationField(columnName = "nomproduit")
    public String nom_produit;
    @modelAnotationField(columnName = "imageproduit")
    public String image="";
}
